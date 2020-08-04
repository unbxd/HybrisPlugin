package com.unbxd.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.impl.DefaultIndexerStrategy;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorker;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.impl.DefaultIndex;

public class UnbxdIndexerStrategy extends DefaultIndexerStrategy {

	protected void doExecute(IndexerContext indexerContext) throws IndexerException {
		IndexConfig indexConfig = indexerContext.getFacetSearchConfig().getIndexConfig();
		List<PK> pks = indexerContext.getPks();
		List<IndexerWorkerWrapper> workers = new ArrayList();

		int numberOfThreads = 1;

		ExecutorService executorService = this.createIndexerWorkersPool(numberOfThreads);
		ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService(executorService);
		int maxRetries = 0;
		int maxBatchRetries = 0;
		try {

			RevertibleUpdate revertibleUpdate = this.markThreadAsSuspendable();
			try {

				IndexerWorker indexerWorker = this.createIndexerWorker(indexerContext, (long) 1, pks);
				IndexerWorkerWrapper indexerWorkerWrapper = new IndexerWorkerWrapper(indexerWorker, 1, maxBatchRetries,
						pks);

				workers.add(indexerWorkerWrapper);

				this.runWorkers(indexerContext, completionService, workers, maxRetries);
			} finally {
				if (revertibleUpdate != null) {
					revertibleUpdate.close();
				}
			}

		} catch (Throwable var37) {

			throw new IndexerException(var37);

		} finally {
			executorService.shutdownNow();
		}
	}

	protected Index resolveIndex() throws IndexerException {
		DefaultIndex index = new DefaultIndex();
		index.setName("unbxd-default");
		index.setFacetSearchConfig(getFacetSearchConfig());
		index.setIndexedType(getIndexedType());
		index.setQualifier("unbxd-default");
		return index;
	}

}
