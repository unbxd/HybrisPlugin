package com.unbxd.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.unbxd.event.CacheClearEvent;
import com.unbxd.model.UnbxdIndexerCronJobModel;
import com.unbxd.model.UnbxdSiteConfigModel;

import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.cronjob.impl.DefaultJobDao;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

public class UnbxdSiteConfigPrepareInterceptor implements PrepareInterceptor<UnbxdSiteConfigModel> {

	@Resource
	ModelService modelService;
	
	@Resource
	EventService eventService;

	@Resource
	DefaultJobDao jobDao;
	
	final List<String> cacheRegions=Arrays.asList("facetSearchConfigCacheRegion");

	private static final Logger LOG = Logger.getLogger(UnbxdSiteConfigPrepareInterceptor.class);

	@Override
	public void onPrepare(UnbxdSiteConfigModel siteConfig, InterceptorContext ctx) throws InterceptorException {

		if (ctx.isNew(siteConfig)) {
			if (siteConfig.getFacetSearchConfig() != null) {
				try {
					SolrFacetSearchConfigModel searchConfigModel = siteConfig.getFacetSearchConfig();
					List<JobModel> jobs = jobDao.findJobs("unbxdIndexerJob");
					if (CollectionUtils.isNotEmpty(jobs)) {
						createCronJob(searchConfigModel, jobs.get(0), IndexerOperationValues.FULL);
						createCronJob(searchConfigModel, jobs.get(0), IndexerOperationValues.UPDATE);
					}
				} catch (Exception e) {
					LOG.error("Error while creating cronjobs", e);
				}

			} else {
				LOG.warn("Cronjobs not created as facetsearch config is missing");
			}
		}
		CacheClearEvent event = new CacheClearEvent(cacheRegions);
		eventService.publishEvent(event);

	}

	private void createCronJob(SolrFacetSearchConfigModel searchConfigModel, JobModel job,
			IndexerOperationValues operation) {
		UnbxdIndexerCronJobModel cronJob = modelService.create(UnbxdIndexerCronJobModel.class);
		cronJob.setActive(true);
		cronJob.setCode("unbxd_" + searchConfigModel.getName() + "_" + operation.name() + "_indexjob");
		cronJob.setIndexerOperation(operation);
		cronJob.setJob(job);
		cronJob.setFacetSearchConfig(searchConfigModel);
		modelService.save(cronJob);
	}

}
