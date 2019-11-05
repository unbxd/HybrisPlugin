package com.unbxd.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

public class UnbxdIndexedPropertyPopulator implements Populator<SolrIndexedPropertyModel, IndexedProperty>
{
    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final SolrIndexedPropertyModel property, final IndexedProperty indexedProperty) throws ConversionException {
        indexedProperty.setUnbxd(Boolean.TRUE.equals(property.getIsUnbxd()));
    }
}
