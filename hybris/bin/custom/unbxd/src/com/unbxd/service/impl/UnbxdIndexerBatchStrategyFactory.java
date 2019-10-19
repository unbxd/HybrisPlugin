package com.unbxd.service.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategy;
import de.hybris.platform.solrfacetsearch.indexer.strategies.impl.DefaultIndexerBatchStrategyFactory;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.solr.impl.DefaultSolrIndexService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.List;

public class UnbxdIndexerBatchStrategyFactory extends DefaultIndexerBatchStrategyFactory {

    private String defaultIndexerBatchStrategyBeanId;
    private String unbxdIndexerBatchStrategyBeanId;
    private ApplicationContext applicationContext;

    @Resource
    private SolrIndexService solrIndexService;

    @Override
    public IndexerBatchStrategy createIndexerBatchStrategy(FacetSearchConfig facetSearchConfig) throws IndexerException {
        boolean isUnbxd = false;
        try {
            IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().get("Product");
            final List<SolrIndexModel> indexes = solrIndexService.getIndexesForConfigAndType(facetSearchConfig.getName(),
                    indexedType.getIdentifier());
            isUnbxd = indexes.get(0).getIndexedType().getIsUnbxd();

            return (IndexerBatchStrategy) this.applicationContext.getBean((isUnbxd ? this.unbxdIndexerBatchStrategyBeanId : this.defaultIndexerBatchStrategyBeanId), IndexerBatchStrategy.class);
        } catch (BeansException var3) {
            throw new IndexerException("Cannot create indexer batch strategy [" + (isUnbxd ? this.unbxdIndexerBatchStrategyBeanId : this.defaultIndexerBatchStrategyBeanId) + "]", var3);
        } catch (SolrServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDefaultIndexerBatchStrategyBeanId() {
        return defaultIndexerBatchStrategyBeanId;
    }

    public void setDefaultIndexerBatchStrategyBeanId(String defaultIndexerBatchStrategyBeanId) {
        this.defaultIndexerBatchStrategyBeanId = defaultIndexerBatchStrategyBeanId;
    }

    public String getUnbxdIndexerBatchStrategyBeanId() {
        return unbxdIndexerBatchStrategyBeanId;
    }

    public void setUnbxdIndexerBatchStrategyBeanId(String unbxdIndexerBatchStrategyBeanId) {
        this.unbxdIndexerBatchStrategyBeanId = unbxdIndexerBatchStrategyBeanId;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
