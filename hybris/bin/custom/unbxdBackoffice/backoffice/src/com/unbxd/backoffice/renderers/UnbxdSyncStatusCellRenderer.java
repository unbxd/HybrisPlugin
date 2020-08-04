package com.unbxd.backoffice.renderers;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Span;
import org.zkoss.zul.impl.XulElement;

import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.lazyloading.DefaultLazyTaskResult;
import com.hybris.cockpitng.lazyloading.LazyTaskResult;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractLazyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;


public class UnbxdSyncStatusCellRenderer extends AbstractLazyRenderer<Component, Object, ItemModel, Optional<Boolean>>
{
	private static final Logger LOG = LoggerFactory.getLogger(UnbxdSyncStatusCellRenderer.class);
	protected SynchronizationFacade synchronizationFacade;
	protected WidgetComponentRenderer<XulElement, Object, ItemModel> partialSyncInfoRenderer;
	protected Boolean lazyRender;

	public UnbxdSyncStatusCellRenderer()
	{
		this.lazyRender = Boolean.FALSE;
	}

	@Override
	public void render(final Component parent, final Object configuration, final ItemModel data, final DataType dataType,
			final WidgetInstanceManager widgetInstanceManager)
	{
		if (BooleanUtils.isTrue(this.lazyRender))
		{
			super.render(parent, configuration, data, dataType, widgetInstanceManager);
		}
		else
		{
			try
			{
				final Optional<Boolean> synced = this.loadData(configuration, data, dataType);
				this.renderAfterLoad(parent, configuration, data, dataType, widgetInstanceManager,
						DefaultLazyTaskResult.success(synced));
			}
			catch (final Exception var7)
			{
				this.renderAfterLoad(parent, configuration, data, dataType, widgetInstanceManager, DefaultLazyTaskResult.failure());
				LOG.error("Unable to calculate sync status", var7);
			}
		}

		this.fireComponentRendered(parent, configuration, data);
	}

	@Override
	protected void renderBeforeLoad(final Component parent, final Object configuration, final ItemModel data,
			final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
	{
		final Span icon = new Span();
		UITools.modifySClass(icon, "yw-image-attribute-sync-status-loading", true);
		parent.appendChild(icon);
		this.fireComponentRendered(icon, parent, configuration, data);
	}

	@Override
	protected Optional<Boolean> loadData(final Object configuration, final ItemModel data, final DataType dataType)
	{
		if (data instanceof ProductModel)
		{
			/*
			 * Date syncDate = ((ProductModel)data).getUnbxdSyncDate(); Date modifiedTime =
			 * ((ProductModel)data).getModifiedtime();
			 * 
			 * if(syncDate == null) { return Optional.of(Boolean.FALSE); } Date syncDateAvg = new
			 * Date(((ProductModel)data).getUnbxdSyncDate().getTime() + 60000); if(syncDateAvg.compareTo(modifiedTime) < 0)
			 * { return Optional.of(Boolean.FALSE); } return Optional.of(Boolean.TRUE);
			 */
		}
		return Optional.empty();
	}

	@Override
	protected void renderAfterLoad(final Component parent, final Object configuration, final ItemModel data,
			final DataType dataType, final WidgetInstanceManager wim, final LazyTaskResult<Optional<Boolean>> lazyLoadedData)
	{
		parent.getChildren().clear();
		final Span icon = new Span();
		if (!lazyLoadedData.isSuccess())
		{
			UITools.modifySClass(icon, "yw-image-attribute-sync-status-error", true);
			icon.setTooltiptext(Labels.getLabel("sync.status.error.tooltip"));
		}
		else if (((Optional) lazyLoadedData.get()).isPresent())
		{
			if ((Boolean) ((Optional) lazyLoadedData.get()).get())
			{
				UITools.modifySClass(icon, "yw-image-attribute-sync-status-in-sync", true);
			}
			else
			{
				UITools.modifySClass(icon, "yw-image-attribute-sync-status-out-of-sync", true);
			}

			this.partialSyncInfoRenderer.render(icon, configuration, data, dataType, wim);
		}
		else
		{
			UITools.modifySClass(icon, "yw-image-attribute-sync-status-undefined", true);
			icon.setTooltiptext(Labels.getLabel("sync.status.undefined.tooltip"));
		}

		parent.appendChild(icon);
		this.fireComponentRendered(icon, parent, configuration, data);
	}

	@Required
	public void setSynchronizationFacade(final SynchronizationFacade synchronizationFacade)
	{
		this.synchronizationFacade = synchronizationFacade;
	}

	public SynchronizationFacade getSynchronizationFacade()
	{
		return this.synchronizationFacade;
	}

	@Required
	public void setPartialSyncInfoRenderer(final WidgetComponentRenderer<XulElement, Object, ItemModel> partialSyncInfoRenderer)
	{
		this.partialSyncInfoRenderer = partialSyncInfoRenderer;
	}

	public WidgetComponentRenderer<XulElement, Object, ItemModel> getPartialSyncInfoRenderer()
	{
		return this.partialSyncInfoRenderer;
	}

	public void setLazyRender(final Boolean lazyRender)
	{
		this.lazyRender = lazyRender;
	}

	public Boolean getLazyRender()
	{
		return this.lazyRender;
	}
}
