package com.unbxd.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

import com.unbxd.client.feed.FeedProduct;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.variants.model.VariantProductModel;

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

	public Collection<FeedProduct> indexItems(Collection<ItemModel> items, FacetSearchConfig facetSearchConfig,
			IndexedType indexedType, boolean isFull) throws IndexerException, InterruptedException {
		
		if (items == null) {
			return Collections.emptyList();
		} else {
			Collection<FeedProduct> feedProducts = new HashSet<FeedProduct>();
			Iterator<ItemModel> var8 = items.iterator();
			IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
			while (var8.hasNext()) {
				ItemModel itemModel = (ItemModel) var8.next();
				try {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Indexing item with PK " + itemModel.getPk());
					}

					if (itemModel instanceof ProductModel) {
						ProductModel productModel = (ProductModel) itemModel;
						if (itemModel instanceof VariantProductModel) {
							if (((VariantProductModel) itemModel).getBaseProduct() == null) {
								LOG.warn("A variant product without base product identified "
										+ ((VariantProductModel) itemModel).getCode());
							}
							productModel = getParentProductModel((ProductModel) itemModel);
						}
						FeedProduct unbxdDocument = unbxdDocumentFactory.createInputDocument(productModel, indexConfig,
								indexedType);
						List<VariantProductModel> variants = getAllProductVariants(productModel);
						if (!variants.isEmpty()) {
							variants.forEach(variantProductModel -> {
								FeedProduct variantDocument = null;
								try {
									variantDocument = unbxdDocumentFactory.createInputDocument(variantProductModel,
											indexConfig, indexedType);
									Map<String, Object> variant = new HashMap<>();
									variant.put("variantId", variantDocument.getUniqueId());
									variantDocument.get_attributes().remove("uniqueId");
									variantDocument.get_attributes().entrySet().stream().forEach(entry -> {
										variant.put("v" + StringUtils.capitalize(entry.getKey()), entry.getValue());
									});
									unbxdDocument.addVariant(variant);
								} catch (FieldValueProviderException e) {
									e.printStackTrace();
								}
							});
						}
						feedProducts.add(unbxdDocument);
					} else {
						feedProducts.add(unbxdDocumentFactory.createInputDocument(itemModel, indexConfig, indexedType));
					}

				} catch (RuntimeException | FieldValueProviderException var11) {
					String message = "Failed to index item with PK " + itemModel.getPk() + ": " + var11.getMessage();
					handleError(indexConfig, indexedType, message, var11);
				}
			}

			return feedProducts;
		}
	}

	
	protected Object lookupVariantAttributeName(final VariantProductModel productModel, final String attribute) {
		final Object value = variantsService.getVariantAttributeValue(productModel, attribute);
		if (value == null) {
			final ProductModel baseProduct = productModel.getBaseProduct();
			if (baseProduct instanceof VariantProductModel) {
				return lookupVariantAttributeName((VariantProductModel) baseProduct, attribute);
			}
		}
		return value;
	}

	protected void handleError(IndexConfig indexConfig, IndexedType indexedType, String message, Exception error)
			throws IndexerException {
		if (indexConfig.isIgnoreErrors()) {
			LOG.warn(message);
		} else {
			throw new IndexerException(message, error);
		}
	}

	ProductModel getParentProductModel(ProductModel productModel) {
		if (productModel instanceof VariantProductModel
				&& ((VariantProductModel) productModel).getBaseProduct() != null)
			return getParentProductModel(((VariantProductModel) productModel).getBaseProduct());
		return productModel;
	}

	List<VariantProductModel> getAllProductVariants(ProductModel productModel) {
		List<VariantProductModel> productVariants = new ArrayList<>();
		if (productModel.getVariantType() != null && CollectionUtils.isNotEmpty(productModel.getVariants())) {
			productModel.getVariants().forEach(variantProductModel -> {
				productVariants.addAll(getAllProductVariants(variantProductModel));
			});
		} else if (productModel instanceof VariantProductModel) {
			productVariants.add((VariantProductModel) productModel);
		}

		return productVariants;
	}

	List<VariantProductModel> getAllProductVariantsWithIntermediatories(ProductModel productModel) {
		List<VariantProductModel> productVariants = new ArrayList<>();
		if (productModel.getVariantType() != null && CollectionUtils.isNotEmpty(productModel.getVariants())) {
			productModel.getVariants().forEach(variantProductModel -> {
				productVariants.add(variantProductModel);
				productVariants.addAll(getAllProductVariants(variantProductModel));
			});
		} else if (productModel instanceof VariantProductModel) {
			productVariants.add((VariantProductModel) productModel);
		}

		return productVariants;
	}
}
