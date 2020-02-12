package com.unbxd.service;

import com.unbxd.client.feed.FeedProduct;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;

import java.util.Collection;

public interface UnbxdDocumentFactory {
    FeedProduct createInputDocument(ItemModel var1, IndexConfig var2, IndexedType var3) throws FieldValueProviderException;

    FeedProduct createInputDocument(ItemModel var1, IndexConfig var2, IndexedType var3, Collection<IndexedProperty> var4) throws FieldValueProviderException;
}
