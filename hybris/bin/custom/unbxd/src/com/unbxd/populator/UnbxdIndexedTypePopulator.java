package com.unbxd.populator;


import org.apache.commons.lang3.BooleanUtils;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.converters.populator.DefaultIndexedTypePopulator;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

public class UnbxdIndexedTypePopulator extends DefaultIndexedTypePopulator
{
    @Override
    public void populate(final SolrIndexedTypeModel source, final IndexedType target) throws ConversionException
    {
        super.populate(source, target);
        target.setUnbxd(BooleanUtils.isTrue(source.getIsUnbxd()) );
    }
}
