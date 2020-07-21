package com.unbxd.model;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class UnbxdIndexerJobListAttribute implements DynamicAttributeHandler<List<UnbxdIndexerCronJobModel>, SolrFacetSearchConfigModel> {
    @Override
    public List<UnbxdIndexerCronJobModel> get(SolrFacetSearchConfigModel facetSearchConfigModel) {
        if(null != facetSearchConfigModel)
        {
            final List<UnbxdIndexerCronJobModel> unbxdIndexerCronJobModels=new ArrayList<>();
            facetSearchConfigModel.getSolrIndexerCronJob().stream().forEach(solrIndexerCronJobModel ->
            {
                if(solrIndexerCronJobModel instanceof  UnbxdIndexerCronJobModel)
                    unbxdIndexerCronJobModels.add((UnbxdIndexerCronJobModel) solrIndexerCronJobModel);
            });
            return CollectionUtils.isNotEmpty(unbxdIndexerCronJobModels)?
                    unbxdIndexerCronJobModels:null;
        }
        return null;
    }

    @Override
    public void set(SolrFacetSearchConfigModel model, List<UnbxdIndexerCronJobModel> unbxdIndexerCronJobModels) {

    }
}
