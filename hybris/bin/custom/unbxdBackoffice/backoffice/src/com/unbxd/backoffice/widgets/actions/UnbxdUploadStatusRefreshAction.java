package com.unbxd.backoffice.widgets.actions;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.unbxd.backoffice.notificationarea.event.NotificationEvent;
import com.unbxd.backoffice.notificationarea.event.NotificationUtils;
import com.unbxd.model.UnbxdUploadTaskModel;
import com.unbxd.service.impl.CatalogSyncService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class UnbxdUploadStatusRefreshAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, List> {
    protected static final String SOCKET_OUT_SELECTED_OBJECTS = "currentObjects";
    @Resource
    private ObjectFacade objectFacade;
    @Autowired
    private CatalogSyncService catalogSyncService;


    public UnbxdUploadStatusRefreshAction() {
    }

    public boolean canPerform(ActionContext<Object> ctx) {
        if (ctx.getData() != null) {
            List<Object> data = this.getData(ctx);
            return CollectionUtils.isNotEmpty(data) && data.stream().noneMatch(this.objectFacade::isModified);
        } else {
            return false;
        }
    }

    public ActionResult<List> perform(ActionContext<Object> context) {
        ActionResult<List> result = new ActionResult("error");
        if (context.getData() != null) {
            //TODO call UnbxdSyncService
            try {
                List<Object> currentObjects = this.getData(context);
                for(Object currentObject : currentObjects) {
                    catalogSyncService.refreshUploadStatus((UnbxdUploadTaskModel)currentObject);
                }
                result = new ActionResult("success");
                result.setStatusFlags(EnumSet.of(ActionResult.StatusFlag.OBJECT_MODIFIED));
                NotificationUtils.notifyUser(NotificationUtils.getWidgetNotificationSource(context), "unbxdSyncInitiated", NotificationEvent.Level.SUCCESS, new Object[0]);
                return result;
            } catch (Exception e){
                NotificationUtils.notifyUser(NotificationUtils.getWidgetNotificationSource(context), "unbxdSyncInitFailed", NotificationEvent.Level.FAILURE, e);
            }
        }
        return result;
    }

    protected List<Object> getData(ActionContext<Object> context) {
        if (context.getData() instanceof Collection) {
            Collection<Object> data = (Collection)context.getData();
            return (List)data.stream().filter((o) -> {
                return !Objects.isNull(o);
            }).collect(Collectors.toList());
        } else {
            return (List)(context.getData() != null ? Lists.newArrayList(new Object[]{context.getData()}) : Collections.emptyList());
        }
    }

    public ObjectFacade getObjectFacade() {
        return this.objectFacade;
    }
    public CatalogSyncService getCatalogSyncService() {
        return this.catalogSyncService;
    }
}
