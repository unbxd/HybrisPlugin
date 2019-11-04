package com.unbxd.backoffice.widgets.actions;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UnbxdProductSyncAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, List> {
    protected static final String SOCKET_OUT_SELECTED_OBJECTS = "currentObjects";
    @Resource
    private ObjectFacade objectFacade;

    public UnbxdProductSyncAction() {
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
            this.sendOutput("currentObjects", this.getData(context));
            result = new ActionResult("success");
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
}
