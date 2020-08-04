package com.unbxd.dynamic;

import javax.annotation.Resource;

import com.unbxd.helper.UnbxdHelperFunction;
import com.unbxd.model.UnbxdSiteConfigModel;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

public class UnbxdSiteConfigAttributeHandler implements DynamicAttributeHandler<UnbxdSiteConfigModel, SolrFacetSearchConfigModel> {

	@Resource
	UnbxdHelperFunction unbxdHelperFunction;
	
	@Override
	public UnbxdSiteConfigModel get(SolrFacetSearchConfigModel configModel) {
		
		
		return unbxdHelperFunction.getUnbxdSiteConfigBySearchConfig.apply(configModel);
	}

	@Override
	public void set(SolrFacetSearchConfigModel arg0, UnbxdSiteConfigModel arg1) {
		throw new UnsupportedOperationException();
		
	}

}
