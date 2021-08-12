/**
 *
 */
package com.unbxd.backoffice.widgets.actions;

import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.unbxd.model.UnbxdSiteConfigModel;


/**
 * @author jack8603
 *
 */
public class IndexerAction extends AbstractComponentWidgetAdapterAware
		implements CockpitAction<UnbxdSiteConfigModel, UnbxdSiteConfigModel>
{
	protected static final String SOCKET_OUT_CONTEXT = "unbxdIndexerTypeContext";

	public boolean canPerform(final ActionContext<UnbxdSiteConfigModel> actionContext)
	{
		final Object data = actionContext.getData();
		final boolean decision = false;
		if (data instanceof UnbxdSiteConfigModel)
		{
			final UnbxdSiteConfigModel siteConfig = ((UnbxdSiteConfigModel) data);
			return !(!siteConfig.isFeedPush() || !Optional.ofNullable(siteConfig.getSecretKey()).isPresent()
					|| !Optional.ofNullable(siteConfig.getSiteName()).isPresent()
					|| CollectionUtils.isEmpty(siteConfig.getIndexerJobs()));

		}

		return decision;
	}

	public String getConfirmationMessage(final ActionContext<UnbxdSiteConfigModel> actionContext)
	{
		return null;
	}



	public boolean needsConfirmation(final ActionContext<UnbxdSiteConfigModel> actionContext)
	{
		return false;
	}



	public ActionResult<UnbxdSiteConfigModel> perform(final ActionContext<UnbxdSiteConfigModel> actionContext)
	{
		this.sendOutput(SOCKET_OUT_CONTEXT, actionContext.getData());
		final ActionResult actionResult = new ActionResult("success");
		return actionResult;
	}


}
