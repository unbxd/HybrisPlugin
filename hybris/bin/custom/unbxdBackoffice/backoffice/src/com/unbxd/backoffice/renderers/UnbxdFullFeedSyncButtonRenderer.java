package com.unbxd.backoffice.renderers;

import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerCronJobModel;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaPanelRenderer;
import com.unbxd.model.UnbxdIndexerCronJobModel;


public class UnbxdFullFeedSyncButtonRenderer extends AbstractEditorAreaPanelRenderer<Object>
{

	@Resource
	private CronJobService cronJobService;

	@Resource
	private NotificationService notificationService;

	@Override
	public void render(final Component parent, final AbstractPanel panel, final Object data, final DataType type,
			final WidgetInstanceManager widgetInstanceManager)
	{
		final Label label = new Label(Labels.getLabel("unbxd.full.feed.sync.label", "Full feed synchronisation"));
		label.setSclass("unbxd-label");
		parent.appendChild(label);
		final Button exportButton = new Button(Labels.getLabel("unbxd.full.feed.sync.button", "Index"));
		exportButton.setSclass("export-solr-configuration-btn");
		parent.appendChild(exportButton);
		exportButton.addEventListener("onClick", (event) -> {
			if (data instanceof SolrFacetSearchConfigModel)
			{
				final SolrFacetSearchConfigModel solrFacetSearchConfig = (SolrFacetSearchConfigModel) data;
				final Optional<SolrIndexerCronJobModel> runningInstance = solrFacetSearchConfig.getSolrIndexerCronJob().stream()
						.filter(solrIndexerCronJobModel -> {
							if (solrIndexerCronJobModel instanceof UnbxdIndexerCronJobModel
									&& CronJobStatus.RUNNING.equals(solrIndexerCronJobModel.getStatus()))
							{
								return true;
							}
							return false;
						}).findFirst();
				if (runningInstance.isPresent())
				{
					notificationService.notifyUser("Unbxd full sync already running [" + runningInstance.get().getCode() + "].", null,
							NotificationEvent.Level.WARNING);
					return;
				}
				final Optional<SolrIndexerCronJobModel> fullIndexJob = solrFacetSearchConfig.getSolrIndexerCronJob().stream()
						.filter(solrIndexerCronJobModel -> {
							if (solrIndexerCronJobModel instanceof UnbxdIndexerCronJobModel
									&& IndexerOperationValues.FULL.equals(solrIndexerCronJobModel.getIndexerOperation())
									&& BooleanUtils.isTrue(solrIndexerCronJobModel.getActive()))
							{
								return true;
							}
							return false;
						}).findFirst();
				if (fullIndexJob.isPresent())
				{
					cronJobService.performCronJob(fullIndexJob.get());
					notificationService.notifyUser("Unbxd full sync triggered successfully.", null, NotificationEvent.Level.SUCCESS);
				}
				else
				{
					notificationService.notifyUser("Unbxd full sync is not configured/enabled. ", null,
							NotificationEvent.Level.WARNING);
				}
			}
		});

	}
}
