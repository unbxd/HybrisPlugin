package com.unbxd.cron;

import com.unbxd.model.UnbxdIndexerCronJobModel;
import com.unbxd.model.UnbxdSiteConfigModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.indexer.cron.SolrIndexerJob;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerCronJobModel;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

public class UnbxdIndexerJob  extends SolrIndexerJob {
    private static final Logger LOG = Logger.getLogger(UnbxdIndexerJob.class);
    @Override
    public PerformResult performIndexingJob(final CronJobModel cronJob) {
        LOG.info("Started unbxd indexer cronjob.");
        if (!(cronJob instanceof UnbxdIndexerCronJobModel)) {
            LOG.warn("Unexpected cronjob type: " + cronJob);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }else {
            final UnbxdIndexerCronJobModel unbxdIndexerCronJob = (UnbxdIndexerCronJobModel)cronJob;
            if(null == unbxdIndexerCronJob.getFacetSearchConfig().getUnbxdSiteConfig())
            {
                LOG.warn("Unbxd Site config for solr facet search "+unbxdIndexerCronJob.getFacetSearchConfig().getName()+" is not mapped");
                return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
            }
            if(BooleanUtils.isNotTrue(unbxdIndexerCronJob.getFacetSearchConfig().getUnbxdSiteConfig().isFeedPush()))
            {
                LOG.warn("Enable Unbxd feed push in Unbxd site config.");
                return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
            }
            final SolrFacetSearchConfigModel facetSearchConfigModel = unbxdIndexerCronJob.getFacetSearchConfig();
            final FacetSearchConfig facetSearchConfig = this.getFacetSearchConfig(facetSearchConfigModel);
            if (facetSearchConfig == null) {
                return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
            }
            else {
                try {
                    super.indexItems(unbxdIndexerCronJob, facetSearchConfig);
                } catch (IndexerException var6) {
                    LOG.warn("Error during indexing: " + facetSearchConfigModel.getName(), var6);
                    return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
                }

                LOG.info("Finished indexer cronjob.");
                return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
            }
        }
    }
}
