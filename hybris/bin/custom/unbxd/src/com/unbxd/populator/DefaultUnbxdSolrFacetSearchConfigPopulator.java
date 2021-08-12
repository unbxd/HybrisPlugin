package com.unbxd.populator;

import com.unbxd.model.UnbxdSiteConfigModel;

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
    	
    	UnbxdSiteConfigModel unbxdSiteConfig = solrFacetSearchConfigModel.getUnbxdSiteConfig();
    	if (unbxdSiteConfig != null) {
        facetSearchConfig.setUnbxdSiteKey(unbxdSiteConfig.getSiteName());
        facetSearchConfig.setUnbxdSecretKey(unbxdSiteConfig.getSecretKey());
        facetSearchConfig.setUnbxdApiKey(unbxdSiteConfig.getApiKey());
        facetSearchConfig.setUnbxdDomain(unbxdSiteConfig.getDomain());
    	}
    }
}
