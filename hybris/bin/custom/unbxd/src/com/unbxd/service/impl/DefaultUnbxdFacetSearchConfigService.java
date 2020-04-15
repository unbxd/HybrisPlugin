package com.unbxd.service.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.config.impl.DefaultFacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultUnbxdFacetSearchConfigService extends DefaultFacetSearchConfigService {

    @Resource
    private SolrIndexService solrIndexService;

    @Override
    public List<IndexedProperty> resolveIndexedProperties(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<String> indexedPropertiesIds) throws FacetConfigServiceException {

        List<IndexedProperty> indexedProperties = super.resolveIndexedProperties(facetSearchConfig, indexedType, indexedPropertiesIds);

        //indexedProperties = indexedProperties.stream().filter(indexedProperty -> indexedProperty.isUnbxd()).collect(Collectors.toList());
        if (indexedType.isUnbxd()) {
            indexedProperties.removeIf(indexedProperty -> !indexedProperty.isUnbxd());
        }
        return indexedProperties;
    }
}
