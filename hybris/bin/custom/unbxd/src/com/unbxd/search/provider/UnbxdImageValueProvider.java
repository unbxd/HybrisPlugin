package com.unbxd.search.provider;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.VariantProductModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * This ValueProvider will provide the product's image url for the first gallery image that supports the requested media
 * format.
 */
public class UnbxdImageValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private static final Logger LOG = Logger.getLogger(UnbxdImageValueProvider.class);
	private static final String UNBXD_MEDIA_USE_ABSOLUTE_PATH =  "unbxd.media.url.use.absolute.path";
	private static final String UNBXD_MEDIA__PATH_HOST =  "unbxd.media.url.path.host";
	private static final String STOREFRONT_CONTEXT_ROOT = "storefrontContextRoot";

	private String mediaFormat;
	private MediaService mediaService;
	private MediaContainerService mediaContainerService;
	private FieldNameProvider fieldNameProvider;


	protected String getMediaFormat()
	{
		return mediaFormat;
	}

	@Required
	public void setMediaFormat(final String mediaFormat)
	{
		this.mediaFormat = mediaFormat;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	protected MediaContainerService getMediaContainerService()
	{
		return mediaContainerService;
	}

	@Required
	public void setMediaContainerService(final MediaContainerService mediaContainerService)
	{
		this.mediaContainerService = mediaContainerService;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
												 final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final MediaFormatModel mediaFormatModel = getMediaService().getFormat(getMediaFormat());
			if (mediaFormatModel != null)
			{
				final MediaModel media = findMedia((ProductModel) model, mediaFormatModel);
				if (media != null)
				{
					return createFieldValues(indexedProperty, media);
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug("No [" + mediaFormatModel.getQualifier() + "] image found for product ["
							+ ((ProductModel) model).getCode() + "]");
				}
			}
		}
		return Collections.emptyList();
	}

	protected MediaModel findMedia(final ProductModel product, final MediaFormatModel mediaFormat)
	{
		if (product != null && mediaFormat != null)
		{
			final List<MediaContainerModel> galleryImages = product.getGalleryImages();
			if (galleryImages != null && !galleryImages.isEmpty())
			{
				// Search each media container in the gallery for an image of the right format
				for (final MediaContainerModel container : galleryImages)
				{
					try
					{
						final MediaModel media = getMediaContainerService().getMediaForFormat(container, mediaFormat);
						if (media != null)
						{
							return media;
						}
					}
					catch (final ModelNotFoundException ignore)
					{
						// ignore
					}
				}
			}

			// Failed to find media in product
			if (product instanceof VariantProductModel)
			{
				// Look in the base product
				return findMedia(((VariantProductModel) product).getBaseProduct(), mediaFormat);
			}
		}
		return null;
	}

	protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, final MediaModel media)
	{
		return createFieldValues(indexedProperty, media.getURL());
	}

	protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, String value)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (indexedProperty.isUnbxd() && Config.getBoolean(UNBXD_MEDIA_USE_ABSOLUTE_PATH, false)){
			value = Config.getString(UNBXD_MEDIA__PATH_HOST, StringUtils.EMPTY)
					.concat(Config.getString(STOREFRONT_CONTEXT_ROOT, StringUtils.EMPTY)
							.concat(value));
		}

		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}

		return fieldValues;
	}


//	@Override
//	protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, String value)
//	{
//		if (indexedProperty.isUnbxd() && Config.getBoolean(UNBXD_MEDIA_USE_ABSOLUTE_PATH, false)){
//			value = Config.getString(UNBXD_MEDIA__PATH_HOST, StringUtils.EMPTY)
//					.concat(Config.getString(STOREFRONT_CONTEXT_ROOT, StringUtils.EMPTY)
//							.concat(value));
//		}
//		return super.createFieldValues(indexedProperty, value);
//	}
}

