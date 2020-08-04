/**
 *
 */
package com.unbxd.search.restriction;

import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;

import java.util.function.Predicate;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.unbxd.helper.UnbxdHelperFunction;
import com.unbxd.model.UnbxdSiteConfigModel;
import com.unbxd.search.model.UnbxdWebRestrictionModel;



/**
 * Evaluates to true if the Unbxd Site Config is enabled for the basesite and has keys configured
 */
public class UnbxdWebRestrictionEvaluator implements CMSRestrictionEvaluator<UnbxdWebRestrictionModel>
{
	private static final Logger LOG = Logger.getLogger(UnbxdWebRestrictionEvaluator.class);


	@Resource
	UnbxdHelperFunction unbxdHelperFunction;

	@Override
	public boolean evaluate(final UnbxdWebRestrictionModel unbxdWebRestrictionModel, final RestrictionData context)
	{

		final UnbxdSiteConfigModel siteConfigModel = unbxdHelperFunction.getCurrentUnbxdConfigModel.get();

		if (siteConfigModel != null && isKeysConfigured.test(siteConfigModel) && isAlteastOneModuleEnabled.test(siteConfigModel))
		{
			return true;
		}
		return false;
	}

	Predicate<UnbxdSiteConfigModel> isKeysConfigured = (siteConfig) -> {
		return siteConfig.getSiteName() != null && siteConfig.getApiKey() != null;
	};


	Predicate<UnbxdSiteConfigModel> isAlteastOneModuleEnabled = (siteConfig) -> {
		return siteConfig.isSearchEnabled() || siteConfig.isAutosuggestEnabled() || siteConfig.isAnalyticsEnabled()
				|| siteConfig.isCategoryEnabled();
	};


}
