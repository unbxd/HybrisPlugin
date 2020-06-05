/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.unbxd.populator;


import com.unbxd.client.ConfigException;
import com.unbxd.client.Unbxd;
import com.unbxd.client.feed.FeedClient;
import com.unbxd.client.search.SearchClient;
import com.unbxd.client.search.exceptions.SearchException;
import com.unbxd.client.search.response.SearchResponse;
import com.unbxd.client.search.response.SearchResults;
import com.unbxd.constants.UnbxdConstants;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.*;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrException;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;


/**
 * Convert the SolrSearchRequest into a SolrSearchResponse by executing the SOLR search.
 */
public class UnbxdSolrSearchRequestResponsePopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>, SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult>>
{
	private static final Logger LOG = Logger.getLogger(UnbxdSolrSearchRequestResponsePopulator.class);

	private FacetSearchService solrFacetSearchService;
	private SolrKeywordRedirectService solrKeywordRedirectService;

	protected FacetSearchService getSolrFacetSearchService()
	{
		return solrFacetSearchService;
	}

	@Required
	public void setSolrFacetSearchService(final FacetSearchService solrFacetSearchService)
	{
		this.solrFacetSearchService = solrFacetSearchService;
	}

	public SolrKeywordRedirectService getSolrKeywordRedirectService()
	{
		return solrKeywordRedirectService;
	}

	@Required
	public void setSolrKeywordRedirectService(final SolrKeywordRedirectService solrKeywordRedirectService)
	{
		this.solrKeywordRedirectService = solrKeywordRedirectService;
	}

	@Override
	public void populate(
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> source,
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult> target)
	{
		try
		{
			target.setRequest(source);
			final SearchResult searchResult = getSolrFacetSearchService().search(source.getSearchQuery());

			String indexName = ((IndexedType) source.getIndexedType()).getIndexNameFromConfig();

			Unbxd.configure(Config.getParameter(UnbxdConstants.SITE_KEY + indexName),
					Config.getParameter(UnbxdConstants.API_KEY + indexName),
					Config.getParameter(UnbxdConstants.SECRET_KEY + indexName));

			SearchClient searchClient = Unbxd.getSearchClient();
			String query = "jacket";
			//String queryParameters =
			searchClient.search(query, new HashMap<>());
			try {
				final SearchResponse unbxdSearchResult = searchClient.execute();
				SearchResults searchResults = unbxdSearchResult.getResults();
			} catch (SearchException e) {
				e.printStackTrace();
			}
			if (searchResult instanceof SolrSearchResult)
			{
				getSolrKeywordRedirectService().attachKeywordRedirect((SolrSearchResult) searchResult);
			}
			target.setSearchResult(searchResult);
		}
		catch (final FacetSearchException | SolrException | NullPointerException | ConfigException ex)
		{
			LOG.error("Exception while executing SOLR search", ex);
		}
	}
}
