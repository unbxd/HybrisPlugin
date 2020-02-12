package com.unbxd.populator;

import de.hybris.platform.commerceservices.model.solrsearch.config.SolrSortModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class IndexedTypeSortPopulator implements Populator<SolrSortModel, IndexedTypeSort>
{
    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final SolrSortModel source, final IndexedTypeSort target) throws ConversionException
    {
        target.setSort(source);
        target.setCode(source.getCode());
        target.setName(source.getName());
    }

}
