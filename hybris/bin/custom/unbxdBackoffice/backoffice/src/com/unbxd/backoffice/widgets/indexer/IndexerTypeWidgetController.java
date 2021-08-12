package com.unbxd.backoffice.widgets.indexer;




import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;

import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.unbxd.model.UnbxdIndexerCronJobModel;
import com.unbxd.model.UnbxdSiteConfigModel;


public class IndexerTypeWidgetController extends DefaultWidgetController
{

	private static final long serialVersionUID = 1L;
	protected static final String IN_SOCKET = "unbxdSiteConfigInput";
	protected static final String OUT_CONFIRM = "confirmOutput";
	protected static final String COMPLETED = "completed";



	private UnbxdSiteConfigModel unbxdSiteConfig;

	@Wire
	private Combobox indexerType;


	@Resource
	private CronJobService cronJobService;



	@SocketEvent(socketId = "unbxdSiteConfigInput")
	public void initReallocationConsignmentForm(final UnbxdSiteConfigModel inputObject)
	{
		unbxdSiteConfig = inputObject;
		this.indexerType.setModel(
				new ListModelArray(Arrays.asList(IndexerOperationValues.FULL.getCode(), IndexerOperationValues.UPDATE.getCode())));
	}

	@ViewEvent(componentID = "triggerIndex", eventName = "onClick")
	public void submitIndex() throws InterruptedException
	{
		try
		{

			final Optional<String> selectedValueOptional = Optional.ofNullable(indexerType.getValue());
			if (!selectedValueOptional.isPresent())
			{
				Messagebox.show("Select an index type to trigger indexing");
				return;
			}
			/*
			 * final Optional<UnbxdIndexerCronJobModel> runningInstance = unbxdSiteConfig.getUnbxdIndexerCronJob().stream()
			 * .filter(unbxdIndexerCronJobModel -> { if
			 * (CronJobStatus.RUNNING.equals(unbxdIndexerCronJobModel.getStatus())) { return true; } return false;
			 * }).findFirst(); if (runningInstance.isPresent()) {
			 * Messagebox.show("An existing instance of index is in progress, try after sometime"); return; }
			 */
			final Optional<UnbxdIndexerCronJobModel> indexJob = unbxdSiteConfig.getIndexerJobs().stream()
					.filter(unbxdIndexerCronJobModel -> {
						if (selectedValueOptional.get().equals(unbxdIndexerCronJobModel.getIndexerOperation().getCode())
								&& BooleanUtils.isTrue(unbxdIndexerCronJobModel.getActive()))
						{
							return true;
						}
						return false;
					}).findFirst();
			if (indexJob.isPresent())
			{
				cronJobService.performCronJob(indexJob.get());
				Messagebox.show(this.getLabel("unbxd.indexer.triggered.ok.message"), getLabel("unbxd.indexer.triggered.ok.title"),
						Messagebox.OK, Messagebox.ON_OK, (obj) -> {
							this.sendOutput("confirmOutput", "COMPLETED");
						});
			}
			else
			{
				Messagebox.show(
						"A " + selectedValueOptional.get()
								+ " index job has to be configured in the system, for indexing request to be submitted.",
						getLabel("unbxd.indexer.triggered.nok.title"), Messagebox.OK, Messagebox.ERROR, (obj) -> {
							this.sendOutput("confirmOutput", "COMPLETED");
						});
			}
		}

		catch (final Exception wve)
		{
			Messagebox.show(wve.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}

	}

}
