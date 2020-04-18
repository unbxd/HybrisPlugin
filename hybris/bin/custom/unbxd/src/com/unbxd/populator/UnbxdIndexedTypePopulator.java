package com.unbxd.populator;

import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.CommerceIndexedTypePopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

public class UnbxdIndexedTypePopulator extends CommerceIndexedTypePopulator
        //implements Populator<SolrIndexedTypeModel, IndexedType>
{
    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final SolrIndexedTypeModel source, final IndexedType target) throws ConversionException
    {
        super.populate(source, target);
        target.setUnbxd(source.getIsUnbxd());
    }
}
