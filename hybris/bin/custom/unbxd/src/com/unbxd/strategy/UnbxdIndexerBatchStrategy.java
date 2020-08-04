package com.unbxd.strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.unbxd.client.ConfigException;
import com.unbxd.client.Unbxd;
import com.unbxd.client.feed.FeedClient;
import com.unbxd.client.feed.FeedProduct;
import com.unbxd.client.feed.exceptions.FeedUploadException;
import com.unbxd.client.feed.response.FeedResponse;
import com.unbxd.enums.UnbxdDataType;
import com.unbxd.model.UnbxdUploadTaskModel;
import com.unbxd.service.UnbxdIndexer;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.enums.SolrPropertiesTypes;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueriesExecutor;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategy;
import de.hybris.platform.solrfacetsearch.solr.Index;

public class UnbxdIndexerBatchStrategy implements IndexerBatchStrategy {

	private static final Logger LOG = Logger.getLogger("solrIndexThreadLogger");
	private IndexerQueriesExecutor indexerQueriesExecutor;
	private IndexerBatchContextFactory<?> indexerBatchContextFactory;
	private UnbxdIndexer indexer;
	private long indexOperationId;
	private IndexOperation indexOperation;
	private boolean externalIndexOperation;
	private FacetSearchConfig facetSearchConfig;
	private IndexedType indexedType;
	private Collection<IndexedProperty> indexedProperties;
	private Index index;
	private Map<String, String> indexerHints;
	private List<PK> pks;
	@Resource
	private SessionService sessionService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private TenantService tenantService;

	public UnbxdIndexerBatchStrategy() {
	}

	public UnbxdIndexer getIndexer() {
		return this.indexer;
	}

	@Required
	public void setIndexer(UnbxdIndexer indexer) {
		this.indexer = indexer;
	}

	public IndexerBatchContextFactory getIndexerBatchContextFactory() {
		return this.indexerBatchContextFactory;
	}

	@Required
	public void setIndexerBatchContextFactory(IndexerBatchContextFactory<?> indexerBatchContextFactory) {
		this.indexerBatchContextFactory = indexerBatchContextFactory;
	}

	public IndexerQueriesExecutor getIndexerQueriesExecutor() {
		return this.indexerQueriesExecutor;
	}

	@Required
	public void setIndexerQueriesExecutor(IndexerQueriesExecutor indexerQueriesExecutor) {
		this.indexerQueriesExecutor = indexerQueriesExecutor;
	}

	public boolean isExternalIndexOperation() {
		return this.externalIndexOperation;
	}

	public void setExternalIndexOperation(boolean externalIndexOperation) {
		this.externalIndexOperation = externalIndexOperation;
	}

	public FacetSearchConfig getFacetSearchConfig() {
		return this.facetSearchConfig;
	}

	public void setFacetSearchConfig(FacetSearchConfig facetSearchConfig) {
		this.facetSearchConfig = facetSearchConfig;
	}

	public Index getIndex() {
		return this.index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public Collection<IndexedProperty> getIndexedProperties() {
		return this.indexedProperties;
	}

	public void setIndexedProperties(Collection<IndexedProperty> indexedProperties) {
		this.indexedProperties = indexedProperties;
	}

	public IndexedType getIndexedType() {
		return this.indexedType;
	}

	public void setIndexedType(IndexedType indexedType) {
		this.indexedType = indexedType;
	}

	public Map<String, String> getIndexerHints() {
		return this.indexerHints;
	}

	public void setIndexerHints(Map<String, String> indexerHints) {
		this.indexerHints = indexerHints;
	}

	public IndexOperation getIndexOperation() {
		return this.indexOperation;
	}

	public void setIndexOperation(IndexOperation indexOperation) {
		this.indexOperation = indexOperation;
	}

	public long getIndexOperationId() {
		return this.indexOperationId;
	}

	public void setIndexOperationId(long indexOperationId) {
		this.indexOperationId = indexOperationId;
	}

	public List<PK> getPks() {
		return this.pks;
	}

	public void setPks(List<PK> pks) {
		this.pks = pks;
	}

	@Resource
	ModelService modelService;

	public void execute() throws InterruptedException, IndexerException {
		this.validateRequiredFields();
		int batchSize = facetSearchConfig.getIndexConfig().getBatchSize() > 0
				? facetSearchConfig.getIndexConfig().getBatchSize()
				: 1000;
		int numberOfThreads = facetSearchConfig.getIndexConfig().getNumberOfThreads() > 0
				? facetSearchConfig.getIndexConfig().getNumberOfThreads()
				: 1;
		int numberOfWorkers = (int) Math.ceil((double) this.pks.size() / (double) batchSize);

		FeedClient feedClient;
		try {
			feedClient = Unbxd.getFeedClient(facetSearchConfig);
		} catch (ConfigException e1) {
			throw new IndexerException(e1.getMessage(), e1);
		}
		indexedType.getIndexedProperties().entrySet().stream().filter(entry -> entry.getValue().isUnbxd())
				.forEach(entry -> {
					if (entry.getValue().isCurrency()) {
						facetSearchConfig.getIndexConfig().getCurrencies().forEach(currency -> {
							feedClient.addSchema(entry.getKey()+"_"+currency.getIsocode().toLowerCase(),
									entry.getValue().getUnbxdType() != null ? entry.getValue().getUnbxdType()
											: map(entry.getValue().getType()),
									entry.getValue().isMultiValue(), entry.getValue().isAutoSuggest());
							feedClient.addSchema("v" + StringUtils.capitalize(entry.getKey()+"_"+currency.getIsocode().toLowerCase()),
									entry.getValue().getUnbxdType() != null ? entry.getValue().getUnbxdType()
											: map(entry.getValue().getType()),
									entry.getValue().isMultiValue(), entry.getValue().isAutoSuggest());
						});
					}
					feedClient.addSchema(entry.getKey(),
							entry.getValue().getUnbxdType() != null ? entry.getValue().getUnbxdType()
									: map(entry.getValue().getType()),
							entry.getValue().isMultiValue(), entry.getValue().isAutoSuggest());
					feedClient.addSchema("v" + StringUtils.capitalize(entry.getKey()),
							entry.getValue().getUnbxdType() != null ? entry.getValue().getUnbxdType()
									: map(entry.getValue().getType()),
							entry.getValue().isMultiValue(), entry.getValue().isAutoSuggest());
				});
		feedClient.addSchema("catalogId", UnbxdDataType.TEXT.getCode());
		feedClient.addSchema("catalogVersion", UnbxdDataType.TEXT.getCode());
		feedClient.addSchema("vCatalogId", UnbxdDataType.TEXT.getCode());
		feedClient.addSchema("vCatalogVersion", UnbxdDataType.TEXT.getCode());

		if (!(feedClient.get_fields().stream().anyMatch(
				f -> (f.getName().equals("variantId") && f.getDataType().equals(UnbxdDataType.TEXT.getCode()))))) {
			feedClient.addSchema("variantId", UnbxdDataType.TEXT.getCode(), false, false);
		}
		Collection<FeedProduct> addDocuments = Collections.synchronizedCollection(new ArrayList<FeedProduct>());
		Collection<FeedProduct> updateDocuments = Collections.synchronizedCollection(new ArrayList<FeedProduct>());
		LOG.info(String.format("Unbxd Indexing triggered for index %s , process will be executed in %d threads with %d workers of size %d each ", facetSearchConfig.getName()+":"+indexedType.getCode(),numberOfThreads,numberOfWorkers,batchSize));
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		List<PK> itemsList = new ArrayList<PK>(this.pks);
		List<UnbxdWorkerWrapper> workers = new ArrayList<UnbxdWorkerWrapper>();
		int index = 1;
		for (int start = 0; index <= numberOfWorkers; start += batchSize) {
			int end = Math.min(start + batchSize, this.pks.size());

			List<PK> workerObjects = itemsList.subList(start, end);

			workers.add(new UnbxdWorkerWrapper(index, workerObjects,resolveTenantId(),resolveSessionUser(),resolveSessionLanguage(),resolveSessionCurrency()));
			index++;

		}
		executorService.invokeAll(workers, 4l, TimeUnit.HOURS).stream().map(future -> {
			try {
				return future.get();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}).forEach(feedProducts -> {
			if (isFull.test(getIndexOperation())) {
				addDocuments.addAll(feedProducts);
			} else {
				updateDocuments.addAll(updateDocuments);
			}
		});

		try {
			LOG.info(String.format("Serialize Input data for  index %s", facetSearchConfig.getName()+":"+indexedType.getCode()));
			feedClient.addProducts(new ArrayList<>(addDocuments));
			feedClient.updateProducts(new ArrayList<>(updateDocuments));
			FeedResponse response = feedClient.push(isFull.test(getIndexOperation()));
			LOG.info(String.format("Feed for index %s submitted for indexing to unbxd, returned with response %d", facetSearchConfig.getName()+":"+indexedType.getCode(),response.getStatusCode()));
			if (response.getStatusCode() == HttpStatus.SC_OK || response.getStatusCode() == HttpStatus.SC_CREATED) {
				/*
				 * Iterator itemIterator = items.iterator(); List<ProductModel> itemsToBeSaved =
				 * new ArrayList<>(); while (itemIterator.hasNext()) { ItemModel itemModel =
				 * (ItemModel) itemIterator.next(); ProductModel productModel = (ProductModel)
				 * itemModel; if (itemModel instanceof VariantProductModel &&
				 * ((VariantProductModel) itemModel).getBaseProduct() != null) { productModel =
				 * getParentProductModel((ProductModel) itemModel); }
				 * productModel.setUnbxdSyncDate(response.get_timestamp());
				 * itemsToBeSaved.add(productModel); List<VariantProductModel> variants =
				 * getAllProductVariantsWithIntermediatories( productModel); if
				 * (!variants.isEmpty()) { variants.forEach(variantProductModel -> {
				 * variantProductModel.setUnbxdSyncDate(response.get_timestamp());
				 * itemsToBeSaved.add(variantProductModel); }); } }
				 * modelService.saveAll(itemsToBeSaved);
				 */
			}
			createUnbxdUploadTask(response, isFull.test(getIndexOperation()), indexedType.getIndexNameFromConfig());
			LOG.info("Unbxd Feed response" + response.toString());
		} catch (FeedUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createUnbxdUploadTask(FeedResponse response, boolean isFull, String indexName) {
		final UnbxdUploadTaskModel unbxdUploadTask = modelService.create(UnbxdUploadTaskModel.class);
		unbxdUploadTask.setFileName(response.get_fileName() != null ?response.get_fileName():"NA" );
		unbxdUploadTask.setUploadId(response.getUploadID());
		unbxdUploadTask.setIndexName(indexName);
		unbxdUploadTask.setStatus("UPLOADED");
		unbxdUploadTask.setIsDelta(!isFull);
		unbxdUploadTask.setTimeStamp(response.get_timestamp());
		unbxdUploadTask.setCode(response.getStatusCode());
		unbxdUploadTask.setMessage(response.getMessage());
		modelService.save(unbxdUploadTask);
	}

	Predicate<IndexOperation> isFull = (operation) -> operation.ordinal() == 0;

	class UnbxdWorkerWrapper implements Callable<Collection<FeedProduct>> {

		private Integer identifier;

		private List<PK> pks;
		
		String tenantId;
		
		UserModel user;
		
		LanguageModel language;
		
		CurrencyModel currency;

		public Integer getIdentifier() {
			return identifier;
		}

		public UnbxdWorkerWrapper(Integer identifier, List<PK> pks,String tenantId,UserModel user,LanguageModel language,CurrencyModel currency) {

			this.identifier = identifier;
			this.pks = pks;
			this.tenantId=tenantId;
			this.user=user;
			this.language=language;
			this.currency=currency;

		}

		@Override
		public Collection<FeedProduct> call() throws Exception {
			try {
				initializeSession();
				IndexerBatchContext batchContext = indexerBatchContextFactory.createContext(indexOperationId,
						indexOperation, externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);
				batchContext.getIndexerHints().putAll(indexerHints);
				batchContext.setIndex(index);
				indexerBatchContextFactory.prepareContext();
				if (batchContext.getIndexOperation() == IndexOperation.DELETE) {
					batchContext.setPks(this.pks);
					batchContext.setItems(Collections.emptyList());
				} else {
					List<ItemModel> items = executeIndexerQuery(facetSearchConfig, indexedType, pks);
					batchContext.setItems(items);
				}

				indexerBatchContextFactory.initializeContext();
				LOG.info(String.format("Index Worker %d started for  index %s",getIdentifier(),facetSearchConfig.getName()+":"+indexedType.getCode()));
				Collection<FeedProduct> result = executeIndexerOperation(batchContext);
				LOG.info(String.format("Index Worker %d completed for  index %s",getIdentifier(),facetSearchConfig.getName()+":"+indexedType.getCode()));
				indexerBatchContextFactory.destroyContext();
				return result;
			} catch (InterruptedException | RuntimeException | IndexerException ex) {
				indexerBatchContextFactory.destroyContext(ex);
				throw ex;
			}finally {
				destroySession();
			}
		}
		
		protected void initializeSession() {
			Tenant tenant = Registry.getTenantByID(tenantId);
			Registry.setCurrentTenant(tenant);

			sessionService.createNewSession();

			userService.setCurrentUser(user);

			commonI18NService.setCurrentLanguage(language);

			commonI18NService.setCurrentCurrency(currency);
		}

		protected void destroySession() {
			sessionService.closeCurrentSession();
			Registry.unsetCurrentTenant();
		}

		protected Collection<FeedProduct> executeIndexerOperation(IndexerBatchContext batchContext)
				throws IndexerException, InterruptedException {
			switch (batchContext.getIndexOperation().ordinal()) {
			case 0:
				return indexer.indexItems(batchContext.getItems(), batchContext.getFacetSearchConfig(),
						batchContext.getIndexedType(), true);
			case 1:
				return indexer.indexItems(batchContext.getItems(), batchContext.getFacetSearchConfig(),
						batchContext.getIndexedType(), false);
			case 2:
			case 3:
			default:
				throw new IndexerException(batchContext.getIndexOperation().name() + " Operation not supported");
			}

		}

	}

	protected void validateRequiredFields() {
		ServicesUtil.validateParameterNotNull(this.indexOperation, "indexOperation field not set");
		ServicesUtil.validateParameterNotNull(this.facetSearchConfig, "facetSearchConfig field not set");
		ServicesUtil.validateParameterNotNull(this.indexedType, "indexedType field not set");
		ServicesUtil.validateParameterNotNull(this.indexedProperties, "indexedProperties field not set");
		ServicesUtil.validateParameterNotNull(this.index, "index field not set");
		ServicesUtil.validateParameterNotNull(this.indexerHints, "indexerHints field not set");
		ServicesUtil.validateParameterNotNull(this.pks, "pks field not set");
		ServicesUtil.validateIfAnyResult(this.pks, "pks field not set");
	}

	protected List<ItemModel> executeIndexerQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType,
			List<PK> pks) throws IndexerException {
		return this.indexerQueriesExecutor.getItems(facetSearchConfig, indexedType, pks);
	}

	private String map(String type) {
		if (type.equalsIgnoreCase(SolrPropertiesTypes.STRING.getCode())
				|| type.equalsIgnoreCase(SolrPropertiesTypes.TEXT.getCode())) {
			return UnbxdDataType.TEXT.getCode();
		}
		if (type.equalsIgnoreCase(SolrPropertiesTypes.DOUBLE.getCode())
				|| type.equalsIgnoreCase(SolrPropertiesTypes.FLOAT.getCode())) {
			return UnbxdDataType.DECIMAL.getCode();
		}
		if (type.equalsIgnoreCase(SolrPropertiesTypes.BOOLEAN.getCode())) {
			return UnbxdDataType.BOOL.getCode();
		}
		if (type.equalsIgnoreCase(SolrPropertiesTypes.DATE.getCode())) {
			return UnbxdDataType.DATE.getCode();
		}
		if (type.equalsIgnoreCase(SolrPropertiesTypes.INT.getCode())
				|| type.equalsIgnoreCase(SolrPropertiesTypes.LONG.getCode())) {
			return UnbxdDataType.NUMBER.getCode();
		}
		return UnbxdDataType.TEXT.getCode();
	}

	protected String resolveTenantId() {
		return this.tenantService.getCurrentTenantId();
	}

	protected UserModel resolveSessionUser() {
		return this.userService.getCurrentUser();
	}

	protected LanguageModel resolveSessionLanguage() {
		return this.commonI18NService.getCurrentLanguage();
	}

	protected CurrencyModel resolveSessionCurrency() {
		return this.commonI18NService.getCurrentCurrency();
	}

	

}
