package com.unbxd.service;

import com.unbxd.client.ConfigException;
import com.unbxd.client.Unbxd;
import com.unbxd.client.feed.FeedClient;
import com.unbxd.client.feed.FeedField;
import com.unbxd.client.feed.FeedProduct;
import com.unbxd.client.feed.exceptions.FeedUploadException;
import com.unbxd.client.feed.response.FeedResponse;
import com.unbxd.constants.UnbxdConstants;
import com.unbxd.enums.UnbxdDataType;
import com.unbxd.model.UnbxdUploadTaskModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.*;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.enums.SolrPropertiesTypes;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.spi.Exporter;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantProductModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;



public class UnbxdIndexer implements BeanFactoryAware {
    private static final Logger LOG = Logger.getLogger("solrIndexThreadLogger");
    private SolrSearchProviderFactory solrSearchProviderFactory;
    private UnbxdDocumentFactory unbxdDocumentFactory;
    private BeanFactory beanFactory;

    @Resource(name = "modelService")
    private ModelService modelService;

    @Resource
    private SolrIndexService solrIndexService;
    @Resource(name = "variantsService")
    private VariantsService variantsService;


    public UnbxdIndexer() {
    }

    public SolrSearchProviderFactory getSolrSearchProviderFactory() {
        return this.solrSearchProviderFactory;
    }

    @Required
    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory) {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }

    public UnbxdDocumentFactory getSolrDocumentFactory() {
        return this.unbxdDocumentFactory;
    }

    @Required
    public void setUnbxdDocumentFactory(UnbxdDocumentFactory unbxdDocumentFactory) {
        this.unbxdDocumentFactory = unbxdDocumentFactory;
    }

    protected BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Collection<FeedProduct> indexItems(Collection<ItemModel> items, FacetSearchConfig facetSearchConfig, IndexedType indexedType, boolean isFull) throws IndexerException, InterruptedException {
        if (items == null) {
            return Collections.emptyList();
        } else {
            try {
                Unbxd.configure(Config.getParameter(UnbxdConstants.SITE_KEY + indexedType.getIndexNameFromConfig()),
                        Config.getParameter(UnbxdConstants.API_KEY + indexedType.getIndexNameFromConfig()),
                        Config.getParameter(UnbxdConstants.SECRET_KEY + indexedType.getIndexNameFromConfig()));

                FeedClient feedClient = Unbxd.getFeedClient();
                indexedType.getIndexedProperties().entrySet().stream().filter(entry -> entry.getValue().isUnbxd()).forEach(entry -> {
                    feedClient.addSchema(entry.getKey(), entry.getValue().getUnbxdType() != null ? entry.getValue().getUnbxdType() : map(entry.getValue().getType()), entry.getValue().isMultiValue(), entry.getValue().isAutoSuggest());
                    feedClient.addSchema("v" + StringUtils.capitalize(entry.getKey()), entry.getValue().getUnbxdType() != null ? entry.getValue().getUnbxdType() : map(entry.getValue().getType()), entry.getValue().isMultiValue(), entry.getValue().isAutoSuggest());
                });
                if(!(feedClient.get_fields().stream().anyMatch(f->( f.getName().equals("variantId") && f.getDataType().equals(UnbxdDataType.TEXT.getCode()))))) {
                    feedClient.addSchema("variantId", UnbxdDataType.TEXT.getCode(), false, false);
                }
                IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
                SolrConfig solrConfig = facetSearchConfig.getSolrConfig();
                Collection<FeedProduct> documents = new ArrayList();
                Collection<FeedProduct> updateDocuments = new ArrayList();
                Iterator var8 = items.iterator();

                while (var8.hasNext()) {
                    ItemModel itemModel = (ItemModel) var8.next();
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }

                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Indexing item with PK " + itemModel.getPk());
                        }

                        FeedProduct solrDocument = this.unbxdDocumentFactory.createInputDocument(itemModel, indexConfig, indexedType);
                        if (itemModel instanceof ProductModel) {
                            ProductModel productModel = (ProductModel) itemModel;
                            if(productModel instanceof VariantProductModel && ((VariantProductModel)productModel).getBaseProduct() != null) {
                                productModel = getParentProductModel(productModel);
                                solrDocument = this.unbxdDocumentFactory.createInputDocument(productModel, indexConfig, indexedType);
                            }
                            FeedProduct finalSolrDocument = solrDocument;
                            List<VariantProductModel> variants = getAllProductVariants(productModel);
                            if(!variants.isEmpty()) {
                                variants.forEach(variantProductModel -> {
                                    FeedProduct variantSolrDocument = null;
                                    try {
                                        variantSolrDocument = unbxdDocumentFactory.createInputDocument(variantProductModel, indexConfig, indexedType);
                                        Map<String, Object> variant = new HashMap<>();
                                        variant.put("variantId", variantSolrDocument.getUniqueId());
                                        variantSolrDocument.get_attributes().remove("uniqueId");
                                        variantSolrDocument.get_attributes().remove("catalogId");
                                        variantSolrDocument.get_attributes().remove("catalogVersion");
                                        variantSolrDocument.get_attributes().entrySet().stream().forEach(entry -> {
                                            variant.put("v" + StringUtils.capitalize(entry.getKey()), entry.getValue());
                                        });
                                        final List<VariantAttributeDescriptorModel> descriptorModels = variantsService.getVariantAttributesForVariantType(
                                                variantProductModel.getBaseProduct().getVariantType());
                                        for (final VariantAttributeDescriptorModel descriptorModel : descriptorModels) {
                                            final String qualifier = descriptorModel.getQualifier();
                                            String vQualifier = "v" + StringUtils.capitalize(qualifier);
                                            if(!(feedClient.get_fields().stream().anyMatch(f->( f.getName().equals(vQualifier) && f.getDataType().equals(UnbxdDataType.TEXT.getCode()))))) {
                                                if(feedClient.get_fields().stream().anyMatch(f->( f.getName().equals(qualifier)))) {
                                                    Optional<FeedField> field = feedClient.get_fields().stream().filter(f->( f.getName().equals(qualifier))).findFirst();
                                                    if(field.isPresent()) {
                                                        feedClient.addSchema(vQualifier,field.get().getDataType(),field.get().isMultiValued(),field.get().isAutoSuggest());
                                                    } else {
                                                        feedClient.addSchema(vQualifier, UnbxdDataType.TEXT.getCode(), true, false);
                                                    }
                                                } else {
                                                    feedClient.addSchema(vQualifier, UnbxdDataType.TEXT.getCode(), true, false);
                                                }
                                            }
                                            final Object variantAttributeValue = lookupVariantAttributeName(variantProductModel, qualifier);
                                            final String qualifierValue = variantAttributeValue == null ? "" : variantAttributeValue.toString();
                                            variant.put("v" + StringUtils.capitalize(qualifier), qualifierValue);
                                        }
                                        finalSolrDocument.addVariant(variant);
                                    } catch (FieldValueProviderException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                            if (!isFull && productModel.getUnbxdSyncDate() != null) {
                                updateDocuments.add(solrDocument);
                            } else {
                                documents.add(solrDocument);
                            }
                            //documents.add(solrDocument);
                        } else {
                            documents.add(solrDocument);
                        }
                    } catch (RuntimeException | FieldValueProviderException var11) {
                        String message = "Failed to index item with PK " + itemModel.getPk() + ": " + var11.getMessage();
                        this.handleError(indexConfig, indexedType, message, var11);
                    }
                }


                try {

                /*//Add schema with Index properties
                final List<SolrIndexModel> indexes = solrIndexService.getIndexesForConfigAndType(facetSearchConfig.getName(),
                        indexedType.getIdentifier());
                SolrIndexedTypeModel unbxdIndex = indexes.get(0).getIndexedType();

                unbxdIndex.getSolrIndexedProperties().stream().
                        filter(solrIndexedPropertyModel -> solrIndexedPropertyModel.getIsUnbxd() != null ? solrIndexedPropertyModel.getIsUnbxd() : false).
                        forEach(solrIndexedPropertyModel -> feedClient.addSchema(solrIndexedPropertyModel.getName(), map(solrIndexedPropertyModel.getType()), solrIndexedPropertyModel.isMultiValue(), solrIndexedPropertyModel.getUseForAutocomplete()));
*/
                    feedClient.addProducts(new ArrayList<>(documents));
                    feedClient.updateProducts(new ArrayList<>(updateDocuments));
                    FeedResponse response = feedClient.push(isFull);
                    if (response.getStatusCode() == HttpStatus.SC_OK || response.getStatusCode() == HttpStatus.SC_CREATED) {
                        Iterator itemIterator = items.iterator();
                        List<ProductModel> itemsToBeSaved = new ArrayList<>();
                        while (itemIterator.hasNext()) {
                            ItemModel itemModel = (ItemModel) itemIterator.next();
                            /*if (itemModel instanceof VariantProductModel && ((VariantProductModel)itemModel).getBaseProduct() != null) {
                                VariantProductModel variantProductModel = ((VariantProductModel) itemModel);
                                ProductModel parentProduct = variantProductModel.getBaseProduct();
                                parentProduct.setUnbxdSyncDate(response.get_timestamp());
                                itemsToBeSaved.add(variantProductModel.getBaseProduct());
                                if (parentProduct.getVariantType() != null && CollectionUtils.isNotEmpty(parentProduct.getVariants())) {
                                    parentProduct.getVariants().forEach(variant -> {
                                        variant.setUnbxdSyncDate(response.get_timestamp());
                                        itemsToBeSaved.add(variant);
                                    });
                                }
                            }
                            else if (itemModel instanceof ProductModel) {
                                ProductModel productModel = ((ProductModel) itemModel);
                                productModel.setUnbxdSyncDate(response.get_timestamp());
                                itemsToBeSaved.add(productModel);
                            }*/
                            ProductModel productModel = (ProductModel) itemModel;
                            if(itemModel instanceof VariantProductModel && ((VariantProductModel)itemModel).getBaseProduct() != null) {
                                productModel = getParentProductModel((ProductModel) itemModel);
                            }
                            productModel.setUnbxdSyncDate(response.get_timestamp());
                            itemsToBeSaved.add(productModel);
                            List<VariantProductModel> variants = getAllProductVariantsWithIntermediatories(productModel);
                            if(!variants.isEmpty()) {
                                variants.forEach(variantProductModel -> {
                                    variantProductModel.setUnbxdSyncDate(response.get_timestamp());
                                    itemsToBeSaved.add(variantProductModel);
                                });
                            }
                        }
                        modelService.saveAll(itemsToBeSaved);
                    }
                    createUnbxdUploadTask(response, isFull, indexedType.getIndexNameFromConfig());
                    System.out.println(response.toString());
                } catch (FeedUploadException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            /*SolrServerMode serverMode = solrConfig.getMode();
            Exporter exporter = this.getExporter(serverMode);
            exporter.exportToUpdateIndex(documents, facetSearchConfig, indexedType);*/
                documents.addAll(updateDocuments);
                return documents;
            } catch (ConfigException e) {
                    e.printStackTrace();
            }
            return Collections.emptyList();
        }
    }

    private String map(String type){
        if (type.equalsIgnoreCase(SolrPropertiesTypes.STRING.getCode()) || type.equalsIgnoreCase(SolrPropertiesTypes.TEXT.getCode()))
        { return UnbxdDataType.TEXT.getCode();}
        if (type.equalsIgnoreCase(SolrPropertiesTypes.DOUBLE.getCode()) || type.equalsIgnoreCase(SolrPropertiesTypes.FLOAT.getCode()))
        { return UnbxdDataType.DECIMAL.getCode();}
        if (type.equalsIgnoreCase(SolrPropertiesTypes.BOOLEAN.getCode()))
        { return UnbxdDataType.BOOL.getCode();}
        if (type.equalsIgnoreCase(SolrPropertiesTypes.DATE.getCode()))
        { return UnbxdDataType.DATE.getCode();}
        if (type.equalsIgnoreCase(SolrPropertiesTypes.INT.getCode()) || type.equalsIgnoreCase(SolrPropertiesTypes.LONG.getCode()))
        { return UnbxdDataType.NUMBER.getCode();}
        return UnbxdDataType.TEXT.getCode();
    }

    private void createUnbxdUploadTask(FeedResponse response, boolean isFull, String indexName) {
        final UnbxdUploadTaskModel unbxdUploadTask = modelService.create(UnbxdUploadTaskModel.class);
        unbxdUploadTask.setFileName(response.get_fileName());
        unbxdUploadTask.setUploadId(response.getUploadID());
        unbxdUploadTask.setIndexName(indexName);
        unbxdUploadTask.setStatus("UPLOADED");
        unbxdUploadTask.setIsDelta(!isFull);
        unbxdUploadTask.setTimeStamp(response.get_timestamp());
        unbxdUploadTask.setCode(response.getStatusCode());
        unbxdUploadTask.setMessage(response.getMessage());
        modelService.save(unbxdUploadTask);
    }

    public Collection<FeedProduct> indexItems(Collection<ItemModel> items, FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<IndexedProperty> indexedProperties) throws IndexerException, InterruptedException {
        if (items == null) {
            return Collections.emptyList();
        } else {
            IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
            SolrConfig solrConfig = facetSearchConfig.getSolrConfig();
            Collection<FeedProduct> documents = new ArrayList();
            Collection<FeedProduct> updateDocuments = new ArrayList();
            Iterator var9 = items.iterator();

            while(var9.hasNext()) {
                ItemModel itemModel = (ItemModel)var9.next();
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }

                try {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Indexing item with PK " + itemModel.getPk());
                    }

                    FeedProduct solrDocument = this.unbxdDocumentFactory.createInputDocument(itemModel, indexConfig, indexedType, indexedProperties);
                    if(itemModel instanceof ProductModel){
                        ProductModel productModel =  (ProductModel) itemModel;

                        if(productModel.getUnbxdSyncDate() != null){
                            updateDocuments.add(solrDocument);
                        } else {
                            documents.add(solrDocument);
                        }
                    } else {
                        documents.add(solrDocument);
                    }

                } catch (RuntimeException | FieldValueProviderException var12) {
                    String message = "Failed to index item with PK " + itemModel.getPk() + ": " + var12.getMessage();
                    this.handleError(indexConfig, indexedType, message, var12);
                }
            }

            try {
                FeedClient feedClient = Unbxd.getFeedClient();
                feedClient.addProducts(new ArrayList<>(documents));
                feedClient.updateProducts(new ArrayList<>(updateDocuments));
            } catch (ConfigException e) {
                e.printStackTrace();
            }

            /*SolrServerMode serverMode = solrConfig.getMode();
            Exporter exporter = this.getExporter(serverMode);
            exporter.exportToUpdateIndex(documents, facetSearchConfig, indexedType);*/
            documents.addAll(updateDocuments);
            return documents;
        }
    }

    protected Object lookupVariantAttributeName(final VariantProductModel productModel, final String attribute)
    {
        final Object value = variantsService.getVariantAttributeValue(productModel, attribute);
        if (value == null)
        {
            final ProductModel baseProduct = productModel.getBaseProduct();
            if (baseProduct instanceof VariantProductModel)
            {
                return lookupVariantAttributeName((VariantProductModel) baseProduct, attribute);
            }
        }
        return value;
    }

    public void removeItemsByPk(Collection<PK> pks, FacetSearchConfig facetSearchConfig, IndexedType indexedType, Index index) throws IndexerException, InterruptedException {
        if (!CollectionUtils.isEmpty(pks)) {
            SolrServerMode serverMode = facetSearchConfig.getSolrConfig().getMode();
            if (serverMode == SolrServerMode.XML_EXPORT) {
                Exporter exporter = this.getExporter(serverMode);
                List<String> pkValues = (List)pks.stream().map(PK::getLongValueAsString).collect(Collectors.toList());
                exporter.exportToDeleteFromIndex(pkValues, facetSearchConfig, indexedType);
            } else {
                try {
                    SolrSearchProvider searchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
                    searchProvider.deleteDocumentsByPk(index, pks);
                } catch (SolrServiceException var8) {
                    throw new IndexerException(var8.getMessage(), var8);
                }
            }

        }
    }

    public Collection<String> removeItems(Collection<ItemModel> items, FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws IndexerException, InterruptedException {
        if (items == null) {
            return Collections.emptyList();
        } else {
            IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
            SolrConfig solrConfig = facetSearchConfig.getSolrConfig();
            Collection<String> delIds = new ArrayList(items.size());
            Iterator var8 = items.iterator();

            while(var8.hasNext()) {
                ItemModel itemModel = (ItemModel)var8.next();
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }

                try {
                    String solrId = this.getIdentityProvider(indexedType).getIdentifier(indexConfig, itemModel);
                    delIds.add(solrId);
                } catch (RuntimeException var11) {
                    String message = "Failed to remove item with PK " + itemModel.getPk() + ": " + var11.getMessage();
                    this.handleError(indexConfig, indexedType, message, var11);
                }
            }

            try {
                FeedClient feedClient = Unbxd.getFeedClient();
                feedClient.deleteProducts(new ArrayList<>(delIds));
            } catch (ConfigException e) {
                e.printStackTrace();
            }

            /*SolrServerMode serverMode = solrConfig.getMode();
            Exporter exporter = this.getExporter(serverMode);
            exporter.exportToDeleteFromIndex(delIds, facetSearchConfig, indexedType);*/

            return delIds;
        }
    }

    protected void handleError(IndexConfig indexConfig, IndexedType indexedType, String message, Exception error) throws IndexerException {
        if (indexConfig.isIgnoreErrors()) {
            LOG.warn(message);
        } else {
            throw new IndexerException(message, error);
        }
    }

    protected IdentityProvider<ItemModel> getIdentityProvider(IndexedType indexedType) {
        return (IdentityProvider)this.beanFactory.getBean(indexedType.getIdentityProvider(), IdentityProvider.class);
    }

    protected Exporter getExporter(SolrServerMode serverMode) throws IndexerException {
        String beanName = "solr.exporter." + serverMode.toString().toLowerCase();
        return (Exporter)this.beanFactory.getBean(beanName, Exporter.class);
    }

    ProductModel getParentProductModel(ProductModel productModel) {
        if(productModel instanceof VariantProductModel && ((VariantProductModel)productModel).getBaseProduct() != null)
            return getParentProductModel(((VariantProductModel)productModel).getBaseProduct());
        return productModel;
    }

    List<VariantProductModel> getAllProductVariants(ProductModel productModel){
        List<VariantProductModel> productVariants = new ArrayList<>();
        if (productModel.getVariantType() != null && CollectionUtils.isNotEmpty(productModel.getVariants())) {
            productModel.getVariants().forEach(variantProductModel -> {
                productVariants.addAll(getAllProductVariants(variantProductModel));
            });
        } else if(productModel instanceof VariantProductModel){
            productVariants.add((VariantProductModel)productModel);
        }

        return productVariants;
    }

    List<VariantProductModel> getAllProductVariantsWithIntermediatories(ProductModel productModel){
        List<VariantProductModel> productVariants = new ArrayList<>();
        if (productModel.getVariantType() != null && CollectionUtils.isNotEmpty(productModel.getVariants())) {
            productModel.getVariants().forEach(variantProductModel -> {
                productVariants.add(variantProductModel);
                productVariants.addAll(getAllProductVariants(variantProductModel));
            });
        } else if(productModel instanceof VariantProductModel){
            productVariants.add((VariantProductModel)productModel);
        }

        return productVariants;
    }
}
