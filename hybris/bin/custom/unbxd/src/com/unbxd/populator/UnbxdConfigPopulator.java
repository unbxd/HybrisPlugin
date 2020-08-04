package com.unbxd.populator;


import com.unbxd.model.UnbxdSiteConfigModel;
import com.unbxd.web.UnbxdConfig;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class UnbxdConfigPopulator implements Populator<UnbxdSiteConfigModel, UnbxdConfig> {

	@Override
	public void populate(UnbxdSiteConfigModel source, UnbxdConfig target) throws ConversionException {

		target.setApiKey(source.getApiKey());
		target.setSiteName(source.getSiteName());
		target.setAutosuggestEnabled(source.isAutosuggestEnabled());
		target.setCategoryEnabled(source.isCategoryEnabled());
		target.setSearchEnabled(source.isSearchEnabled());
		target.setAutosuggestConfig(source.getAutosuggestConfig());
		target.setCategoryConfig(source.getCategoryConfig());
		target.setSearchConfig(source.getSearchConfig());
		target.setAnalyticsEnabled(source.isAnalyticsEnabled());
		target.setSearchInputSelector(source.getSearchBoxSelector());
		target.setUseDefaultLayout(source.isUseDefaultLayout());
	}

}
