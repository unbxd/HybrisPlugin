package com.unbxd.client;

import com.unbxd.client.feed.FeedClient;
import com.unbxd.client.feed.FeedClientFactory;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import org.apache.commons.lang.StringUtils;

public class Unbxd {
    /**
     * Should return a new Feed Client
     * @param  facetSearchConfig
     * @return {@link FeedClient}
     * @throws ConfigException
     */
    public static FeedClient getFeedClient(FacetSearchConfig facetSearchConfig) throws ConfigException {
        if(null == facetSearchConfig)
            throw new ConfigException("Please configure UnBxd properties in FacetSearchConfig.");
        if(StringUtils.isEmpty(facetSearchConfig.getUnbxdSiteKey())
                || StringUtils.isEmpty(facetSearchConfig.getUnbxdSecretKey())
                || StringUtils.isEmpty(facetSearchConfig.getUnbxdApiKey()))
        {
            throw new ConfigException("Please configure UnBxd properties in FacetSearchConfig.");
        }
        return FeedClientFactory.getFeedClient(facetSearchConfig.getUnbxdSiteKey(),
                facetSearchConfig.getUnbxdSecretKey(), Boolean.FALSE);
    }
}
