package com.unbxd.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.ExtendedIndexerListener;
import de.hybris.platform.solrfacetsearch.indexer.IndexerListener;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerContextFactory;

public class UnbxdIndexerContextFactory extends DefaultIndexerContextFactory {

	private static final List<String> excludedListeners = Arrays.asList("CommitModeListener","IndexerOperationListener","OptimizeModeListener");

	
	protected List<ExtendedIndexerListener> getExtendedListeners(DefaultIndexerContext context) {
		List<ExtendedIndexerListener> listeners = (List) context.getAttributes()
				.get("solrfacetsearch.extendedIndexerListener");

		if (listeners == null) {

			FacetSearchConfig contextFacetSearchConfig = context.getFacetSearchConfig();
			IndexedType contextIndexedType = context.getIndexedType();
			listeners = getListenersFactory().getListeners(contextFacetSearchConfig, contextIndexedType,
					ExtendedIndexerListener.class);
			listeners = listeners.stream().filter(listener -> {
				return !excludedListeners.contains(listener.getClass().getSimpleName());
			}).collect(Collectors.toList());

			context.getAttributes().put("solrfacetsearch.extendedIndexerListener", listeners);
		}
		//listeners.stream().map(listener -> listener.getClass().getSimpleName()).forEach(System.out::println);

		return listeners;
	}

	protected List<IndexerListener> getListeners(DefaultIndexerContext context) {
		List<IndexerListener> listeners = (List) context.getAttributes().get("solrfacetsearch.indexerListeners");

		if (listeners == null) {

			FacetSearchConfig facetSearchConfig = context.getFacetSearchConfig();
			IndexedType indexedType = context.getIndexedType();
			listeners = getListenersFactory().getListeners(facetSearchConfig, indexedType, IndexerListener.class);
			listeners = listeners.stream().filter(listener -> {
				return !excludedListeners.contains(listener.getClass().getSimpleName());
			}).collect(Collectors.toList());
			context.getAttributes().put("solrfacetsearch.indexerListeners", listeners);
		}
		//listeners.stream().map(listener -> listener.getClass().getSimpleName()).forEach(System.out::println);

		return listeners;
	}

}
