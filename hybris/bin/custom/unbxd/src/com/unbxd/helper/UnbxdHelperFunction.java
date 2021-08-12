package com.unbxd.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.unbxd.dao.impl.UnbxdSiteConfigDaoImpl;
import com.unbxd.jalo.UnbxdSiteConfig;
import com.unbxd.model.UnbxdSiteConfigModel;
import com.unbxd.web.UnbxdConfig;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

public class UnbxdHelperFunction {

	@Resource
	UnbxdSiteConfigDaoImpl unbxdSiteConfigDao;

	public Function<SolrFacetSearchConfigModel, UnbxdSiteConfigModel> getUnbxdSiteConfigBySearchConfig;

	public Supplier<UnbxdConfig> getCurrentUnbxdConfig;

	public Supplier<UnbxdSiteConfigModel> getCurrentUnbxdConfigModel;

	@Resource
	public BaseSiteService baseSiteService;

	@Resource
	private Converter<UnbxdSiteConfigModel, UnbxdConfig> unbxdConfigConverter;

	@PostConstruct
	public void init() {

		getUnbxdSiteConfigBySearchConfig = (facetSearchConfig) -> {

			Map<String, SolrFacetSearchConfigModel> params = new HashMap<String, SolrFacetSearchConfigModel>();
			params.put(UnbxdSiteConfigModel.FACETSEARCHCONFIG, facetSearchConfig);
			List<UnbxdSiteConfigModel> results = unbxdSiteConfigDao.find(params);
			if (CollectionUtils.isNotEmpty(results)) {
				return results.get(0);
			}
			return null;
		};

		getCurrentUnbxdConfig = () -> {

			UnbxdSiteConfigModel siteConfigModel = getCurrentUnbxdConfigModel.get();
			if (siteConfigModel != null) {
				return unbxdConfigConverter.convert(siteConfigModel);
			}
			return null;
		};

		getCurrentUnbxdConfigModel = () -> {
			BaseSiteModel baseSite = baseSiteService.getCurrentBaseSite();
			if (baseSite != null && baseSite.getSolrFacetSearchConfiguration() != null) {
				return baseSite.getSolrFacetSearchConfiguration().getUnbxdSiteConfig();
			}
			return null;
		};
	}

}
