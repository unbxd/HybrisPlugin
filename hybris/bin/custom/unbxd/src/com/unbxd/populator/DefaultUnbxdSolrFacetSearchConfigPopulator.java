package com.unbxd.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

/**
 * DefaultUnbxdSolrFacetSearchConfigPopulator is used to populate Unbxd site, secreat & Api key into FacetSearchConfig
 */
public class DefaultUnbxdSolrFacetSearchConfigPopulator implements Populator<SolrFacetSearchConfigModel, FacetSearchConfig> {

    @Override
    public void populate(SolrFacetSearchConfigModel solrFacetSearchConfigModel, FacetSearchConfig facetSearchConfig)
            throws ConversionException {
        facetSearchConfig.setUnbxdSiteKey(solrFacetSearchConfigModel.getUnbxdSiteKey());
        facetSearchConfig.setUnbxdSecretKey(solrFacetSearchConfigModel.getUnbxdSecretKey());
        facetSearchConfig.setUnbxdApiKey(solrFacetSearchConfigModel.getUnbxdApiKey());
    }
}
