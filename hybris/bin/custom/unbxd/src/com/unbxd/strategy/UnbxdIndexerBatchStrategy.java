package com.unbxd.strategy;

import com.unbxd.service.UnbxdIndexer;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueriesExecutor;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategy;
import de.hybris.platform.solrfacetsearch.solr.Index;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UnbxdIndexerBatchStrategy implements IndexerBatchStrategy {
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

    public void execute() throws InterruptedException, IndexerException {
        this.validateRequiredFields();

        try {
            IndexerBatchContext batchContext = this.indexerBatchContextFactory.createContext(this.indexOperationId, this.indexOperation, this.externalIndexOperation, this.facetSearchConfig, this.indexedType, this.indexedProperties);
            batchContext.getIndexerHints().putAll(this.indexerHints);
            batchContext.setIndex(this.index);
            this.indexerBatchContextFactory.prepareContext();
            if (batchContext.getIndexOperation() == IndexOperation.DELETE) {
                batchContext.setPks(this.pks);
                batchContext.setItems(Collections.emptyList());
            } else {
                List<ItemModel> items = this.executeIndexerQuery(this.facetSearchConfig, this.indexedType, this.pks);
                batchContext.setItems(items);
            }

            this.indexerBatchContextFactory.initializeContext();
            this.executeIndexerOperation(batchContext);
            this.indexerBatchContextFactory.destroyContext();
        } catch (InterruptedException | RuntimeException | IndexerException var3) {
            this.indexerBatchContextFactory.destroyContext(var3);
            throw var3;
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

    protected List<ItemModel> executeIndexerQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType, List<PK> pks) throws IndexerException {
        return this.indexerQueriesExecutor.getItems(facetSearchConfig, indexedType, pks);
    }

    protected void executeIndexerOperation(IndexerBatchContext batchContext) throws IndexerException, InterruptedException {
        switch(batchContext.getIndexOperation().ordinal()) {
            case 0:
            case 1:
            case 2:
                this.indexer.indexItems(batchContext.getItems(), batchContext.getFacetSearchConfig(), batchContext.getIndexedType());
                break;
            case 3:
                this.indexer.removeItemsByPk(batchContext.getPks(), batchContext.getFacetSearchConfig(), batchContext.getIndexedType(), batchContext.getIndex());
                break;
            case 4:
                this.indexer.indexItems(batchContext.getItems(), batchContext.getFacetSearchConfig(), batchContext.getIndexedType(), batchContext.getIndexedProperties());
                break;
            default:
                throw new IndexerException("Unsupported index operation: " + batchContext.getIndexOperation());
        }

    }
}
