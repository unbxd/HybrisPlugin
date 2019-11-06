package com.unbxd.service.impl;

import com.unbxd.client.feed.FeedProduct;
import com.unbxd.service.UnbxdDocumentFactory;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.provider.*;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;



public class UnbxdDefaultDocumentFactory implements UnbxdDocumentFactory, BeanFactoryAware {
    private static final Logger LOG = Logger.getLogger("solrIndexThreadLogger");
    protected static final String VALUE_PROVIDERS_KEY = "solrfacetsearch.valueProviders";
    protected static final String INDEXED_FIELDS_KEY = "solrfacetsearch.indexedFields";
    private ModelService modelService;
    private TypeService typeService;
    private SolrSearchProviderFactory solrSearchProviderFactory;
    private IndexerBatchContextFactory<?> indexerBatchContextFactory;
    private FieldNameProvider fieldNameProvider;
    private RangeNameProvider rangeNameProvider;
    private ValueProviderSelectionStrategy valueProviderSelectionStrategy;
    private BeanFactory beanFactory;

    public UnbxdDefaultDocumentFactory() {
    }

    public ModelService getModelService() {
        return this.modelService;
    }

    @Required
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public TypeService getTypeService() {
        return this.typeService;
    }

    @Required
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    public SolrSearchProviderFactory getSolrSearchProviderFactory() {
        return this.solrSearchProviderFactory;
    }

    @Required
    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory) {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }

    public IndexerBatchContextFactory getIndexerBatchContextFactory() {
        return this.indexerBatchContextFactory;
    }

    @Required
    public void setIndexerBatchContextFactory(IndexerBatchContextFactory<?> indexerBatchContextFactory) {
        this.indexerBatchContextFactory = indexerBatchContextFactory;
    }

    public FieldNameProvider getFieldNameProvider() {
        return this.fieldNameProvider;
    }

    @Required
    public void setFieldNameProvider(FieldNameProvider fieldNameProvider) {
        this.fieldNameProvider = fieldNameProvider;
    }

    public RangeNameProvider getRangeNameProvider() {
        return this.rangeNameProvider;
    }

    @Required
    public void setRangeNameProvider(RangeNameProvider rangeNameProvider) {
        this.rangeNameProvider = rangeNameProvider;
    }

    public ValueProviderSelectionStrategy getValueProviderSelectionStrategy() {
        return this.valueProviderSelectionStrategy;
    }

    @Required
    public void setValueProviderSelectionStrategy(ValueProviderSelectionStrategy valueProviderSelectionStrategy) {
        this.valueProviderSelectionStrategy = valueProviderSelectionStrategy;
    }

    public BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public FeedProduct createInputDocument(ItemModel model, IndexConfig indexConfig, IndexedType indexedType) throws FieldValueProviderException {
        this.validateCommonRequiredParameters(model, indexConfig, indexedType);
        IndexerBatchContext batchContext = this.indexerBatchContextFactory.getContext();
        FeedProduct doc = new FeedProduct();
        //doc.addField("indexOperationId", "" + batchContext.getIndexOperationId());
        this.addCommonFields(doc, batchContext, model);
        this.addIndexedPropertyFields(doc, batchContext, model);
        this.addIndexedTypeFields(doc, batchContext, model);
        return doc;
    }

    public FeedProduct createInputDocument(ItemModel model, IndexConfig indexConfig, IndexedType indexedType, Collection<IndexedProperty> indexedProperties) throws FieldValueProviderException {
        this.validateCommonRequiredParameters(model, indexConfig, indexedType);
        IndexerBatchContext batchContext = this.indexerBatchContextFactory.getContext();
        Set<String> indexedFields = this.getIndexedFields(batchContext);
        FeedProduct doc = new FeedProduct();
        this.addCommonFields(doc, batchContext, model);
        this.addIndexedPropertyFields(doc, batchContext, model);
        return doc;
    }

    protected void validateCommonRequiredParameters(ItemModel item, IndexConfig indexConfig, IndexedType indexedType) {
        if (item == null) {
            throw new IllegalArgumentException("item must not be null");
        } else if (indexConfig == null) {
            throw new IllegalArgumentException("indexConfig must not be null");
        } else if (indexedType == null) {
            throw new IllegalArgumentException("indexedType must not be null");
        }
    }

    /*protected DefaultSolrInputDocument createWrappedDocument(IndexerBatchContext batchContext, FeedProduct delegate) {
        return new DefaultSolrInputDocument(delegate, batchContext, this.fieldNameProvider, this.rangeNameProvider);
    }

    protected DefaultSolrInputDocument createWrappedDocumentForPartialUpdates(IndexerBatchContext batchContext, FeedProduct delegate, Set<String> indexedPropertiesFields) {
        return new DefaultSolrPartialUpdateInputDocument(delegate, batchContext, this.fieldNameProvider, this.rangeNameProvider, indexedPropertiesFields);
    }*/

    protected void addCommonFields(FeedProduct document, IndexerBatchContext batchContext, ItemModel model) {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
        IndexedType indexedType = batchContext.getIndexedType();
        IdentityProvider<ItemModel> identityProvider = this.getIdentityProvider(indexedType);
        String id = identityProvider.getIdentifier(facetSearchConfig.getIndexConfig(), model);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Using FeedProduct id [" + id + "]");
        }
        document.setUniqueId(id);
        //document.addField("id", id);
        document.addField("uniqueId", id);
        //document.addField("pk", model.getPk().getLongValueAsString());
        ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(model.getClass());
        if (Objects.equals(composedType.getCatalogItemType(), Boolean.TRUE)) {
            AttributeDescriptorModel catalogAttDesc = composedType.getCatalogVersionAttribute();
            CatalogVersionModel catalogVersion = (CatalogVersionModel)this.modelService.getAttributeValue(model, catalogAttDesc.getQualifier());
            //document.addField("catalogId", catalogVersion.getCatalog().getId());
            //document.addField("catalogVersion", catalogVersion.getVersion());
        }

    }

    protected void addIndexedPropertyFields(FeedProduct document, IndexerBatchContext batchContext, ItemModel model) throws FieldValueProviderException {
        Map<String, Collection<IndexedProperty>> valueProviders = this.resolveValueProviders(batchContext);
        Iterator var6 = valueProviders.entrySet().iterator();

        while(var6.hasNext()) {
            Map.Entry<String, Collection<IndexedProperty>> entry = (Map.Entry)var6.next();
            String valueProviderId = (String)entry.getKey();
            Collection<IndexedProperty> indexedProperties = (Collection)entry.getValue();
            Object valueProvider = this.valueProviderSelectionStrategy.getValueProvider(valueProviderId);
            if (valueProvider instanceof FieldValueProvider) {
                this.addIndexedPropertyFieldsForOldApi(document, batchContext, model, indexedProperties, valueProviderId, (FieldValueProvider)valueProvider);
            } else {
                if (!(valueProvider instanceof ValueResolver)) {
                    throw new FieldValueProviderException("Value provider is not of an expected type: " + valueProviderId);
                }

                this.addIndexedPropertyFieldsForNewApi(document, batchContext, model, indexedProperties, valueProviderId, (ValueResolver)valueProvider);
            }
        }

    }

    protected void addIndexedPropertyFieldsForOldApi(FeedProduct document, IndexerBatchContext batchContext, ItemModel model, Collection<IndexedProperty> indexedProperties, String valueProviderId, FieldValueProvider valueProvider) throws FieldValueProviderException {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
        Iterator var9 = indexedProperties.iterator();

        while(var9.hasNext()) {
            IndexedProperty indexedProperty = (IndexedProperty)var9.next();

            try {
                Collection<FieldValue> fieldValues = valueProvider.getFieldValues(facetSearchConfig.getIndexConfig(), indexedProperty, model);
                Iterator var12 = fieldValues.iterator();

                while(var12.hasNext()) {
                    FieldValue fieldValue = (FieldValue)var12.next();
                    document.addField(fieldValue.getFieldName(), fieldValue.getValue());
                }
            } catch (RuntimeException | FieldValueProviderException var13) {
                String message = "Failed to resolve values for item with PK: " + model.getPk() + ", by resolver: " + valueProviderId + ", for property: " + indexedProperty.getName() + ", reason: " + var13.getMessage();
                this.handleError(facetSearchConfig.getIndexConfig(), message, var13);
            }
        }

    }

    protected void addIndexedPropertyFieldsForNewApi(FeedProduct document, IndexerBatchContext batchContext, ItemModel model, Collection<IndexedProperty> indexedProperties, String valueProviderId, ValueResolver<ItemModel> valueProvider) throws FieldValueProviderException {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();

        try {
            valueProvider.resolve(document, batchContext, indexedProperties, model);
        } catch (RuntimeException | FieldValueProviderException var12) {
            ArrayList<String> indexedPropertiesNames = new ArrayList();
            Iterator var11 = indexedProperties.iterator();

            while(var11.hasNext()) {
                IndexedProperty indexedProperty = (IndexedProperty)var11.next();
                indexedPropertiesNames.add(indexedProperty.getName());
            }

            String message = "Failed to resolve values for item with PK: " + model.getPk() + ", by resolver: " + valueProviderId + ", for properties: " + indexedPropertiesNames + ", reason: " + var12.getMessage();
            this.handleError(facetSearchConfig.getIndexConfig(), message, var12);
        }

    }

    protected void addIndexedTypeFields(FeedProduct document, IndexerBatchContext batchContext, ItemModel model) throws FieldValueProviderException {
        IndexedType indexedType = batchContext.getIndexedType();
        String typeValueProviderBeanId = indexedType.getFieldsValuesProvider();
        if (typeValueProviderBeanId != null) {
            Object typeValueProvider = this.getTypeValueProvider(typeValueProviderBeanId);
            if (typeValueProvider instanceof IndexedTypeFieldsValuesProvider) {
                this.addIndexedTypeFieldsForOldApi(document, batchContext, model, typeValueProviderBeanId, (IndexedTypeFieldsValuesProvider)typeValueProvider);
            } else {
                if (!(typeValueProvider instanceof TypeValueResolver)) {
                    throw new FieldValueProviderException("Type value provider is not of an expected type: " + typeValueProviderBeanId);
                }

                this.addIndexedTypeFieldsForNewApi(document, batchContext, model, typeValueProviderBeanId, (TypeValueResolver)typeValueProvider);
            }
        }

    }

    protected void addIndexedTypeFieldsForOldApi(FeedProduct document, IndexerBatchContext batchContext, ItemModel model, String typeValueProviderBeanId, IndexedTypeFieldsValuesProvider typeValueProvider) throws FieldValueProviderException {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();

        try {
            Collection<FieldValue> fieldValues = typeValueProvider.getFieldValues(facetSearchConfig.getIndexConfig(), model);
            Iterator var9 = fieldValues.iterator();

            while(var9.hasNext()) {
                FieldValue fieldValue = (FieldValue)var9.next();
                document.addField(fieldValue.getFieldName(), fieldValue.getValue());
            }
        } catch (RuntimeException | FieldValueProviderException var10) {
            String message = "Failed to resolve values for item with PK: " + model.getPk() + ", by resolver: " + typeValueProviderBeanId + ", reason: " + var10.getMessage();
            this.handleError(facetSearchConfig.getIndexConfig(), message, var10);
        }

    }

    protected void addIndexedTypeFieldsForNewApi(FeedProduct document, IndexerBatchContext batchContext, ItemModel model, String typeValueProviderBeanId, TypeValueResolver<ItemModel> typeValueProvider) throws FieldValueProviderException {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();

        try {
            typeValueProvider.resolve(document, batchContext, model);
        } catch (RuntimeException | FieldValueProviderException var9) {
            String message = "Failed to resolve values for item with PK: " + model.getPk() + ", by resolver: " + typeValueProviderBeanId + ", reason: " + var9.getMessage();
            this.handleError(facetSearchConfig.getIndexConfig(), message, var9);
        }

    }

    protected Map<String, Collection<IndexedProperty>> resolveValueProviders(IndexerBatchContext batchContext) {
        Map<String, Collection<IndexedProperty>> valueProviders = (Map)batchContext.getAttributes().get("solrfacetsearch.valueProviders");
        if (valueProviders == null) {
            valueProviders = this.valueProviderSelectionStrategy.resolveValueProviders(batchContext.getIndexedType(), batchContext.getIndexedProperties());
            batchContext.getAttributes().put("solrfacetsearch.valueProviders", valueProviders);
        }

        return valueProviders;
    }

    protected Set<String> getIndexedFields(IndexerBatchContext batchContext) throws FieldValueProviderException {
        Set<String> indexedPropertiesFields = (Set)batchContext.getAttributes().get("solrfacetsearch.indexedFields");
        if (CollectionUtils.isNotEmpty(indexedPropertiesFields)) {
            return indexedPropertiesFields;
        } else {
            indexedPropertiesFields = new HashSet();
            Set<String> indexedPropertiesNames = new HashSet();
            Iterator var5 = batchContext.getIndexedProperties().iterator();

            while(var5.hasNext()) {
                IndexedProperty indexedProperty = (IndexedProperty)var5.next();
                indexedPropertiesNames.add(indexedProperty.getName());
            }

            try {
                Index index = batchContext.getIndex();
                FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
                IndexedType indexedType = batchContext.getIndexedType();
                SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
                Set<String> fields = this.getIndexedFields(index);
                //Set<String> fields = this.getIndexedFields(facetSearchConfig, index);
                Iterator var11 = fields.iterator();

                while(var11.hasNext()) {
                    String field = (String)var11.next();
                    String indexedPropertyName = this.fieldNameProvider.getPropertyName(field);
                    if (indexedPropertiesNames.contains(indexedPropertyName)) {
                        indexedPropertiesFields.add(field);
                    }
                }
            } catch (SolrServiceException var16) {
                throw new FieldValueProviderException("Could not fetch fields from solr server", var16);
            }

            batchContext.getAttributes().put("solrfacetsearch.indexedFields", indexedPropertiesFields);
            return indexedPropertiesFields;
        }
    }

    protected Set<String> getIndexedFields(Index index) /*throws SolrServerException, IOException */{
        /*LukeRequest request = new LukeRequest();
        request.setNumTerms(0);
        LukeResponse response = (LukeResponse)request.process(solrClient, index.getName());
        Map<String, FieldInfo> fields = response.getFieldInfo();*/
        Map<String, IndexedProperty> fields = index.getIndexedType().getIndexedProperties();
        return fields != null ? fields.keySet() : Collections.emptySet();
    }

/*    protected Set<String> getIndexedFields(FacetSearchConfig facetSearchConfig, Index index) *//*throws SolrServerException, IOException *//*{
        try {
            final List<SolrIndexModel> indexes = solrIndexService.getIndexesForConfigAndType(facetSearchConfig.getName(),
                    index.getIndexedType().getIdentifier());

            SolrIndexedTypeModel unbxdIndex = indexes.get(0).getIndexedType();

            Set<String> fields = unbxdIndex.getSolrIndexedProperties().stream().
                    filter(solrIndexedPropertyModel -> solrIndexedPropertyModel.getIsUnbxd() != null ? solrIndexedPropertyModel.getIsUnbxd() : false).
                    map(solrIndexedPropertyModel -> solrIndexedPropertyModel.getName()).collect(Collectors.toSet());
            return fields != null ? fields : Collections.emptySet();

        } catch (SolrServiceException e) {
            LOG.error(e.getMessage());
            return Collections.emptySet();
        }
    }*/

    protected void handleError(IndexConfig indexConfig, String message, Exception error) throws FieldValueProviderException {
        if (indexConfig.isIgnoreErrors()) {
            LOG.warn(message);
        } else {
            throw new FieldValueProviderException(message, error);
        }
    }

    protected IdentityProvider<ItemModel> getIdentityProvider(IndexedType indexedType) {
        return (IdentityProvider)this.beanFactory.getBean(indexedType.getIdentityProvider(), IdentityProvider.class);
    }

    protected Object getTypeValueProvider(String beanName) {
        return this.beanFactory.getBean(beanName);
    }
}

