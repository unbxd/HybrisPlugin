package com.unbxd.backoffice.widgets.syncpopup;

import com.google.common.collect.Lists;
import com.hybris.backoffice.sync.SyncTask;
import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.backoffice.widgets.syncpopup.SyncJobType;
import com.hybris.backoffice.widgets.syncpopup.SyncJobsLoader;
import com.hybris.backoffice.widgets.syncpopup.SyncPopupViewModel;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.daos.impl.DefaultSolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerHotUpdateCronJobModel;

import org.apache.commons.collections4.CollectionUtils;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;

import javax.annotation.Resource;

public class SyncPopupController extends DefaultWidgetController {
    protected static final String SOCKET_IN_INPUT_OBJECT = "inputObject";
    protected static final String SOCKET_IN_INPUT_OBJECTS = "inputObjects";
    protected static final String SOCKET_OUTPUT_CANCEL = "cancel";
    protected static final String SETTING_SEARCH_VISIBILITY_THRESHOLD = "searchVisibilityThreshold";
    protected static final String SETTING_LISTITEM_RENDERER = "listitemRenderer";
    protected static final String LABEL_TITLE_SINGLE = "title.single";
    protected static final String LABEL_TITLE_WINDOW = "title.window";
    protected static final String LABEL_TITLE_MANY = "title.many";
    protected static final String CANCEL_BUTTON_ID = "cancel";
    protected static final String SYNC_BUTTON_ID = "sync";
    protected static final String SEARCHBOX_ID = "searchbox";
    protected static final String PUSHLIST_ID = "pushList";
    protected static final String PULLLIST_ID = "pullList";
    protected static final String MODEL_SYNC_ITEMS = "modelSyncItems";
    public static final String SOCKET_STARTED_SYNC_CRON_JOB = "startedSyncCronJob";
    private ListModelList<SyncPopupViewModel> pullListModel;
    private ListModelList<SyncPopupViewModel> pushListModel;
    @Wire
    private Listbox pullList;
    @Wire
    private Listbox pushList;
    @Wire
    private Tabbox tabbox;
    @Wire
    private Tab pushTab;
    @Wire
    private Tab pullTab;
    @Wire
    private Label title;
    @Wire
    private Button sync;
    @Wire
    private Textbox searchbox;
    @WireVariable
    protected transient LabelService labelService;
    @WireVariable
    protected transient SynchronizationFacade synchronizationFacade;
    @WireVariable
    protected transient CronJobService cronJobService;
    @WireVariable
    protected transient ModelService modelService;

    @Resource
    private DefaultSolrFacetSearchConfigDao defaultSolrFacetSearchConfigDao;


    public SyncPopupController() {
    }

    public void initialize(Component comp) {
        super.initialize(comp);
        this.setWidgetTitle(this.getLabel("title.window"));
        this.setupRenderers();
        this.initializeLists();
        List<ItemModel> syncItems = this.getSyncItems();
        if (CollectionUtils.isNotEmpty(syncItems)) {
            this.prepareView(syncItems);
        }

    }

    protected void initializeLists() {
        this.pullListModel = new ListModelList();
        this.pushListModel = new ListModelList();
        this.pullList.setModel(this.pullListModel);
        this.pushList.setModel(this.pushListModel);
    }

    protected void setupRenderers() {
        String listItemRenderer = this.getWidgetSettings().getString("listitemRenderer");
        if (StringUtils.isNotBlank(listItemRenderer)) {
            this.pullList.setItemRenderer((ListitemRenderer)BackofficeSpringUtil.getBean(listItemRenderer, ListitemRenderer.class));
            this.pushList.setItemRenderer((ListitemRenderer)BackofficeSpringUtil.getBean(listItemRenderer, ListitemRenderer.class));
        }

    }

    protected List<ItemModel> getSyncItems() {
        List<ItemModel> items = (List)this.getValue("modelSyncItems", List.class);
        if (items == null) {
            items = new ArrayList();
            this.setValue("modelSyncItems", items);
        }

        return (List)items;
    }

    protected void prepareView(List<ItemModel> itemsToSync) {
        SyncJobsLoader syncJobsLoader = this.createSyncDataLoader(itemsToSync);
        if (syncJobsLoader.getLoadingStatus().isOK()) {
            this.loadPushJobs(syncJobsLoader.getPushJobs());
            this.loadPullJobs(syncJobsLoader.getPullJobs());
            this.adjustTitle(itemsToSync);
            this.setSearchboxVisibility();
            this.hideListIfEmpty();
        } else {
            this.clearLoadedJobs();
            this.closeSyncPopup();
            this.showWarningMessageBox("sync.cannot.perform", syncJobsLoader.getLoadingStatus().getMsgLabelKey());
        }

    }

    protected SyncJobsLoader createSyncDataLoader(List<ItemModel> itemsToSync) {
        return new SyncJobsLoader(itemsToSync);
    }

    protected void adjustTitle(List<ItemModel> itemsToSync) {
        String titleText = null;
        String toolTip = null;
        if (CollectionUtils.isNotEmpty(itemsToSync)) {
            if (itemsToSync.size() == 1) {
                titleText = this.getLabel("title.single", new Object[]{this.labelService.getObjectLabel(itemsToSync.get(0))});
                toolTip = titleText;
            } else {
                titleText = this.getLabel("title.many", new Object[]{itemsToSync.size()});
            }
        }

        this.title.setValue(titleText);
        this.title.setTooltiptext(toolTip);
    }

    protected void setSearchboxVisibility() {
        boolean showSearchbox = this.pullListModel.getSize() + this.pushListModel.getSize() >= this.getWidgetSettings().getInt("searchVisibilityThreshold");
        this.searchbox.setVisible(showSearchbox);
    }

    protected void showWarningMessageBox(String titleKey, String msgKey) {
        Messagebox.show(this.getLabel(msgKey), this.getLabel(titleKey), 1, "z-messagebox-icon z-messagebox-exclamation");
    }

    protected void hideListIfEmpty() {
        boolean pullListEmpty = this.pullListModel.isEmpty();
        boolean pushListEmpty = this.pushListModel.isEmpty();
        this.pullTab.setVisible(!pullListEmpty);
        this.pushTab.setVisible(!pushListEmpty);
        this.tabbox.setSelectedTab(pullListEmpty ? this.pushTab : this.pullTab);
    }

    @ViewEvent(
            eventName = "onSelect",
            componentID = "pushList"
    )
    public void onPushListSelect() {
        this.pullListModel.clearSelection();
        this.sync.setDisabled(this.pushListModel.getSelection().isEmpty());
    }

    @ViewEvent(
            eventName = "onSelect",
            componentID = "pullList"
    )
    public void onPullListSelect() {
        this.pushListModel.clearSelection();
        this.sync.setDisabled(this.pullListModel.getSelection().isEmpty());
    }

    @ViewEvent(
            eventName = "onClick",
            componentID = "cancel"
    )
    public void closeSyncPopup() {
        this.sendOutput("cancel", (Object)null);
    }

    @ViewEvent(
            eventName = "onClick",
            componentID = "sync"
    )
    public void onSyncButtonClick() {
        Optional<SyncItemJobModel> selectedSyncJob = this.getSelectedSyncJob();
        /*if (selectedSyncJob.isPresent()) {
            Optional<String> cronJobCode = this.synchronizationFacade.performSynchronization(this.createSyncTask(this.getSyncItems(), (SyncItemJobModel)selectedSyncJob.get()));
            if (cronJobCode.isPresent()) {
                this.sendOutput("startedSyncCronJob", cronJobCode.get());
            } else {
                NotificationUtils.notifyUser("syncPopup", "syncCannotRun", Level.FAILURE, new Object[0]);
            }
        }

        //SolrIndexerCronJobModel cronJob = (SolrIndexerCronJobModel)SolrHotUpdateIndexerStepTwo.this.getCurrentObject(component, SolrIndexerCronJobModel.class);
        SolrIndexerCronJobModel solrIndexerCronJobModel;
        SolrIndexerHotUpdateCronJobModel solrIndexerHotUpdateCronJobModel;*/

        /*final String indexerCronJobName = buildSolrCronJobCode(solrFacetSearchConfig, indexerOperation);
        final CronJobModel cronJob = this.cronJobService.getCronJob(indexerCronJobName);

        if (cronJob instanceof SolrIndexerCronJobModel)
        {
            solrIndexerCronJobModel = (SolrIndexerCronJobModel) modelService.getSource(cronJob);
        } else {
            solrIndexerCronJobModel = SolrfacetsearchManager.getInstance().createSolrIndexerCronJob(indexerCronJobName, solrFacetSearchConfig,
                    this.modelService.getSource(indexerOperation));;
        }*/

        /*JaloSession jaloSession = JaloSession.getCurrentSession();
        ComposedType solrIndexerCronJob = jaloSession.getTypeManager().getComposedType(SolrIndexerHotUpdateCronJob.class);
        Map<String, Object> values = new HashMap();
        values.put("code", code);
        values.put("indexerOperation", solrIndexerOperation);
        values.put("facetSearchConfig", solrFacetSearchConfig);
        values.put("indexTypeName", indexedType);
        values.put("items", items);
        values.put("logToDatabase", Boolean.TRUE);
        Job job = this.getSolrIndexerJob(true);
        values.put("job", job);
        return (SolrIndexerHotUpdateCronJob)solrIndexerCronJob.newInstance(values);
        */

        IndexerOperationValues indexerOperation = IndexerOperationValues.UPDATE;
        SolrIndexerHotUpdateCronJobModel solrIndexerHotUpdateCronJobModel;
        Collection<CronJobModel> cronJobs = this.cronJobService.getJob("solrIndexerHotUpdateJob").getCronJobs();

        if (false && CollectionUtils.isNotEmpty(cronJobs))
        {
            CronJobModel cronJob = cronJobs.iterator().next();
            if(cronJob instanceof SolrIndexerHotUpdateCronJobModel)
                solrIndexerHotUpdateCronJobModel = (SolrIndexerHotUpdateCronJobModel)cronJob;
            else
                solrIndexerHotUpdateCronJobModel = new SolrIndexerHotUpdateCronJobModel();
        } else {
            solrIndexerHotUpdateCronJobModel = new SolrIndexerHotUpdateCronJobModel();
        }

        SolrFacetSearchConfigModel solrFacetSearConfig;
        List<SolrFacetSearchConfigModel> solrFacetSearchConfigs = defaultSolrFacetSearchConfigDao.findAllFacetSearchConfigs();
        Optional<SolrFacetSearchConfigModel> solrFacetSearConfigResult = solrFacetSearchConfigs.stream().filter( s -> {
            if(s.getSolrIndexedTypes().stream().anyMatch( i -> i.getIsUnbxd()))
                return true;
            return false;
        }).findFirst();
        if(solrFacetSearConfigResult.isPresent()) {
            solrFacetSearConfig = solrFacetSearConfigResult.get();
            Optional<SolrIndexedTypeModel> indexedTypeResult = solrFacetSearConfig.getSolrIndexedTypes().stream().filter(s -> s.getIsUnbxd()).findFirst();
            //final List<SolrIndexModel> indexes = solrIndexService.getIndexesForConfigAndType(facetSearchConfig.getName(),
            //        indexedType.getIdentifier());
            //isUnbxd = indexes.get(0).getIndexedType().getIsUnbxd();
            if(indexedTypeResult.isPresent()) {
                SolrIndexedTypeModel indexedType = indexedTypeResult.get();
                solrIndexerHotUpdateCronJobModel.setIndexTypeName("Product");
                solrIndexerHotUpdateCronJobModel.setFacetSearchConfig(solrFacetSearConfig);
                solrIndexerHotUpdateCronJobModel.setIndexerOperation(IndexerOperationValues.UPDATE);
                solrIndexerHotUpdateCronJobModel.setItems(this.getSyncItems());
                JobModel jobModel = this.cronJobService.getJob("solrIndexerHotUpdateJob");
                solrIndexerHotUpdateCronJobModel.setJob(jobModel);
                solrIndexerHotUpdateCronJobModel.setLogToDatabase(Boolean.TRUE);
                if (BooleanUtils.isNotTrue(solrIndexerHotUpdateCronJobModel.getActive())) {
                    solrIndexerHotUpdateCronJobModel.setActive(true);
                }
                this.modelService.save(solrIndexerHotUpdateCronJobModel);

                cronJobService.performCronJob(solrIndexerHotUpdateCronJobModel);

                Optional<String> cronJobCode = solrIndexerHotUpdateCronJobModel != null ? Optional.ofNullable(solrIndexerHotUpdateCronJobModel.getCode()) : Optional.empty();
                if (cronJobCode.isPresent()) {
                    this.sendOutput("startedSyncCronJob", cronJobCode.get());
                } else {
                    NotificationUtils.notifyUser("syncPopup", "syncCannotRun", Level.FAILURE, new Object[0]);
                }
            } else {
                NotificationUtils.notifyUser("syncPopup", "syncCannotRun", Level.FAILURE, new Object[0]);
            }
        } else {
            NotificationUtils.notifyUser("syncPopup", "syncCannotRun", Level.FAILURE, new Object[0]);
        }
        //SolrHotUpdateIndexerStepTwo.this.startCronJob(SolrHotUpdateIndexerStepTwo.this.getWidgetController(), cronJob);

    }

    @ViewEvent(
            eventName = "onChanging",
            componentID = "searchbox"
    )
    public void filterLists(InputEvent event) {
        Consumer<Listitem> listItemFilter = (item) -> {
            item.setVisible(this.itemMatches(event.getValue(), item));
        };
        this.pullList.getItems().forEach(listItemFilter);
        this.pushList.getItems().forEach(listItemFilter);
    }

    /*protected String buildSolrCronJobCode(final SolrFacetSearchConfig solrFacetSearchConfig,
                                          final IndexerOperationValues indexerOperation)
    {
        return indexerOperation.getCode() + "-" + solrFacetSearchConfig.getName() + "-cronJob";
    }*/

    protected boolean itemMatches(String value, Listitem item) {
        Iterator var4 = item.queryAll("label").iterator();

        while(var4.hasNext()) {
            Component label = (Component)var4.next();
            if (StringUtils.containsIgnoreCase(((Label)label).getValue(), value)) {
                return true;
            }
        }

        return false;
    }

    @SocketEvent(
            socketId = "inputObject"
    )
    public void showSyncJobsForInputObject(ItemModel data) {
        List<ItemModel> items = data != null ? Lists.newArrayList(new ItemModel[]{data}) : Collections.emptyList();
        this.setValue("modelSyncItems", items);
        this.prepareView((List)items);
    }

    @SocketEvent(
            socketId = "inputObjects"
    )
    public void showSyncJobsForInputObjects(List<ItemModel> items) {
        this.setValue("modelSyncItems", items);
        this.prepareView(items);
    }

    protected void loadPushJobs(List<SyncItemJobModel> jobs) {
        this.pushListModel.clear();
        this.pushListModel.addAll(this.packModels(jobs, SyncJobType.PUSH));
    }

    protected void loadPullJobs(List<SyncItemJobModel> jobs) {
        this.pullListModel.clear();
        this.pullListModel.addAll(this.packModels(jobs, SyncJobType.PULL));
    }

    protected void clearLoadedJobs() {
        this.pullListModel.clear();
        this.pushListModel.clear();
    }

    protected SyncTask createSyncTask(Collection<ItemModel> item, SyncItemJobModel syncJob) {
        SyncTask syncTask = new SyncTask(Lists.newArrayList(item), syncJob);
        syncTask.setParameters(this.createSyncCtx());
        return syncTask;
    }

    protected List<SyncPopupViewModel> packModels(List<SyncItemJobModel> itemJobModels, SyncJobType type) {
        List<ItemModel> syncItems = this.getSyncItems();
        Map<String, Object> ctx = this.createSyncCtx();
        return (List)itemJobModels.stream().map((item) -> {
            Optional<Boolean> inSync = this.synchronizationFacade.isInSync(syncItems, item, ctx);
            return new SyncPopupViewModel(item, type, inSync.isPresent() ? (Boolean)inSync.get() : null);
        }).collect(Collectors.toList());
    }

    protected Map<String, Object> createSyncCtx() {
        return new HashMap();
    }

    protected List<SyncItemJobModel> unpackModels(Collection<SyncPopupViewModel> viewModels) {
        return (List)viewModels.stream().map(SyncPopupViewModel::getJobModel).collect(Collectors.toList());
    }

    protected Optional<SyncItemJobModel> getSelectedSyncJob() {
        Set<SyncPopupViewModel> pullSelection = this.pullListModel.getSelection();
        if (CollectionUtils.isNotEmpty(pullSelection)) {
            return Optional.ofNullable(((SyncPopupViewModel)pullSelection.iterator().next()).getJobModel());
        } else {
            Set<SyncPopupViewModel> pushSelection = this.pushListModel.getSelection();
            return CollectionUtils.isNotEmpty(pushSelection) ? Optional.ofNullable(((SyncPopupViewModel)pushSelection.iterator().next()).getJobModel()) : Optional.empty();
        }
    }

    protected LabelService getLabelService() {
        return this.labelService;
    }

    public Listbox getPullList() {
        return this.pullList;
    }

    public Listbox getPushList() {
        return this.pushList;
    }

    public Tabbox getTabbox() {
        return this.tabbox;
    }

    public Tab getPullTab() {
        return this.pullTab;
    }

    public Tab getPushTab() {
        return this.pushTab;
    }

    public Button getSync() {
        return this.sync;
    }

    public Textbox getSearchbox() {
        return this.searchbox;
    }

    public Label getTitle() {
        return this.title;
    }

    public ListModelList<SyncPopupViewModel> getPullListModel() {
        return this.pullListModel;
    }

    public ListModelList<SyncPopupViewModel> getPushListModel() {
        return this.pushListModel;
    }
}
