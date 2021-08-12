package com.unbxd.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.unbxd.model.UnbxdIndexerCronJobModel;
import com.unbxd.model.UnbxdSiteConfigModel;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

public class UnbxdIndexerJobListAttribute implements DynamicAttributeHandler<List<UnbxdIndexerCronJobModel>, UnbxdSiteConfigModel> {
    @Override
    public List<UnbxdIndexerCronJobModel> get(UnbxdSiteConfigModel unbxdSiteConfig) {
    	final List<UnbxdIndexerCronJobModel> unbxdIndexerCronJobModels=new ArrayList<>();
        if(null != unbxdSiteConfig.getFacetSearchConfig())
        {
        	SolrFacetSearchConfigModel facetSearchConfigModel = unbxdSiteConfig.getFacetSearchConfig();
            facetSearchConfigModel.getSolrIndexerCronJob().stream().forEach(solrIndexerCronJobModel ->
            {
                if(solrIndexerCronJobModel instanceof  UnbxdIndexerCronJobModel)
                    unbxdIndexerCronJobModels.add((UnbxdIndexerCronJobModel) solrIndexerCronJobModel);
            });
            return  unbxdIndexerCronJobModels;
        }
        return unbxdIndexerCronJobModels;
    }

    @Override
    public void set(UnbxdSiteConfigModel model, List<UnbxdIndexerCronJobModel> unbxdIndexerCronJobModels) {
    	throw new UnsupportedOperationException();
    }	
}
