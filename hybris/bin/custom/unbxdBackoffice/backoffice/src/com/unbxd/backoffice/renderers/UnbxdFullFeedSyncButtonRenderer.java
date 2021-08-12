package com.unbxd.backoffice.renderers;

import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;

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
import com.unbxd.backoffice.notificationarea.event.NotificationUtils;
import com.unbxd.model.UnbxdIndexerCronJobModel;
import com.unbxd.model.UnbxdSiteConfigModel;


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
		if (data instanceof UnbxdSiteConfigModel && !((UnbxdSiteConfigModel) data).isFeedPush())
		{
			exportButton.setAttribute("disabled", "true");
		}

		parent.appendChild(exportButton);

		exportButton.addEventListener("onClick", (event) -> {
			if (data instanceof UnbxdSiteConfigModel)
			{
				final UnbxdSiteConfigModel unbxdSiteConfigModel = (UnbxdSiteConfigModel) data;
				final Optional<UnbxdIndexerCronJobModel> runningInstance = unbxdSiteConfigModel.getIndexerJobs().stream()
						.filter(unbxdIndexerCronJobModel -> {
							if (CronJobStatus.RUNNING.equals(unbxdIndexerCronJobModel.getStatus()))
							{
								return true;
							}
							return false;
						}).findFirst();
				if (runningInstance.isPresent())
				{
					notificationService.notifyUser(NotificationUtils.getWidgetNotificationSource(widgetInstanceManager),
							"unbxdFullFeedSyncRunning", NotificationEvent.Level.WARNING);
					return;
				}
				final Optional<UnbxdIndexerCronJobModel> fullIndexJob = unbxdSiteConfigModel.getIndexerJobs().stream()
						.filter(unbxdIndexerCronJobModel -> {
							if (IndexerOperationValues.FULL.equals(unbxdIndexerCronJobModel.getIndexerOperation())
									&& BooleanUtils.isTrue(unbxdIndexerCronJobModel.getActive()))
							{
								return true;
							}
							return false;
						}).findFirst();
				if (fullIndexJob.isPresent())
				{
					cronJobService.performCronJob(fullIndexJob.get());
					notificationService.notifyUser(NotificationUtils.getWidgetNotificationSource(widgetInstanceManager),
							"unbxdFullFeedSyncInitiated", NotificationEvent.Level.SUCCESS);
				}
				else
				{
					notificationService.notifyUser(NotificationUtils.getWidgetNotificationSource(widgetInstanceManager),
							"unbxdFullFeedSyncNotConfigured", NotificationEvent.Level.WARNING);
				}
			}
		});

	}
}
