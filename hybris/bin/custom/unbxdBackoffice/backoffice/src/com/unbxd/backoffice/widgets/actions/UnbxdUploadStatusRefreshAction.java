package com.unbxd.backoffice.widgets.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.unbxd.model.UnbxdUploadTaskModel;
import com.unbxd.service.impl.UnbxdUploadTaskService;


public class UnbxdUploadStatusRefreshAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, List>
{
	protected static final String SOCKET_OUT_SELECTED_OBJECTS = "currentObjects";
	@Resource
	private ObjectFacade objectFacade;
	@Autowired
	private UnbxdUploadTaskService unbxdUploadTaskService;


	public UnbxdUploadStatusRefreshAction()
	{
	}

	public boolean canPerform(final ActionContext<Object> ctx)
	{
		if (ctx.getData() != null)
		{
			final List<Object> data = this.getData(ctx);
			return CollectionUtils.isNotEmpty(data) && data.stream().noneMatch(this.objectFacade::isModified);
		}
		else
		{
			return false;
		}
	}

	public ActionResult<List> perform(final ActionContext<Object> context)
	{
		ActionResult<List> result = new ActionResult("error");
		if (context.getData() != null)
		{
			try
			{
				final List<Object> currentObjects = this.getData(context);
				for (final Object currentObject : currentObjects)
				{
					unbxdUploadTaskService.refreshUploadStatus((UnbxdUploadTaskModel) currentObject);
				}
				result = new ActionResult("success");
				result.setStatusFlags(EnumSet.of(ActionResult.StatusFlag.OBJECT_MODIFIED));
				return result;
			}
			catch (final Exception e)
			{
				//
			}
		}
		return result;
	}

	protected List<Object> getData(final ActionContext<Object> context)
	{
		if (context.getData() instanceof Collection)
		{
			final Collection<Object> data = (Collection) context.getData();
			return data.stream().filter((o) -> {
				return !Objects.isNull(o);
			}).collect(Collectors.toList());
		}
		else
		{
			return context.getData() != null ? Lists.newArrayList(new Object[]
			{ context.getData() }) : Collections.emptyList();
		}
	}

	public ObjectFacade getObjectFacade()
	{
		return this.objectFacade;
	}


}
