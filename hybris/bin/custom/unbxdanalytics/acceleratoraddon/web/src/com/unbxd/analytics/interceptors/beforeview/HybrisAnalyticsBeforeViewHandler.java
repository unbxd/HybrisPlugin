/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.unbxd.analytics.interceptors.beforeview;

import com.unbxd.analytics.constants.UnbxdAnalyticsConstants;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.storefront.data.JavaScriptVariableData;
import de.hybris.platform.addonsupport.config.javascript.BeforeViewJsPropsHandlerAdaptee;
import de.hybris.platform.addonsupport.config.javascript.JavaScriptVariableDataFactory;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;


public class HybrisAnalyticsBeforeViewHandler extends BeforeViewJsPropsHandlerAdaptee
{
	private static final String PIWIK_TRACKER_ENDPOINT_URL = "piwik.tracker.url";
	private static final String PIWIK_TRACKER_ENDPOINT_HTTPS_URL = "piwik.tracker.https.url";
	private static final String PIWIK_TRACKER_SITE_ID = "piwik.tracker.siteid.";
	private static final String PIWIK_TRACKER_DEFAULT_SITE_ID = "piwik.tracker.siteid.default";
	private static final String PIWIK_SITE_ID = "piwikSiteId";
	private static final String UNBXD_ANALYTICS_CATALOG = "unbxdCatalog";
	private static final String UNBXD_ANALYTICS_SITE_KEY = "unbxd.analytics.sitekey.";
	private static final String UNBXD_ANALYTICS_DEFAULT_SITE_KEY = "unbxd.analytics.sitekey.default";
	private static final String UNBXD_ANALYTICS_SITEKEY = "unbxdAnalyticsSiteKey";

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "sessionTokenService")
	private DefaultSessionTokenService sessionTokenService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Override
	public String beforeViewJsProps(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName)
	{

		attachCustomJSVariablesToModel(model, request);
		return viewName;
	}

	/**
	 * Provides a combination of setting attributes as JS variables with base.js.properties or traditional way of setting
	 * the value in model object.
	 *
	 * @param model
	 * @param request
	 */
	protected void attachCustomJSVariablesToModel(final ModelMap model, final HttpServletRequest request) // NOSONAR
	{
		if (model != null)
		{
			final Map<String, String> jsPropsMap = new HashMap<>();
			final List<JavaScriptVariableData> jsPropList = JavaScriptVariableDataFactory.createFromMap(jsPropsMap);
			Map<String, List<JavaScriptVariableData>> jsVariables = (Map<String, List<JavaScriptVariableData>>) model
					.get(detectJsModelName());

			if (jsVariables == null)
			{
				jsVariables = new HashMap<String, List<JavaScriptVariableData>>();
				model.addAttribute(detectJsModelName(), jsVariables);
			}

			//Loads the key-values from base.js.properties and available as JS variables in the storefront
			List<JavaScriptVariableData> jsVariablesList = jsVariables.get(getMessageSource().getAddOnName());
			if (jsVariablesList != null && !jsVariablesList.isEmpty())
			{
				jsVariablesList.addAll(jsPropList);
			}
			else
			{
				jsVariablesList = jsPropList;
			}
			jsVariables.put(getMessageSource().getAddOnName(), jsVariablesList);
			final String piwikSiteId = getCurrentPiwikSiteId();
			model.addAttribute(PIWIK_SITE_ID, piwikSiteId);
			final String unbxdSiteKey = getCurrentUnbxdSiteKey();
			model.addAttribute(UNBXD_ANALYTICS_SITEKEY, unbxdSiteKey);
			final CatalogModel unbxdCatalog = getCurrentUnbxdCatalog();
			model.addAttribute(UNBXD_ANALYTICS_CATALOG, unbxdCatalog);
			//Setting the endpoint url as model attribute rather base.js.properties so that it can be overriden from local.properties
			model.addAttribute("PIWIK_TRACKER_ENDPOINT_URL", siteConfigService.getProperty(PIWIK_TRACKER_ENDPOINT_URL));
			model.addAttribute("PIWIK_TRACKER_ENDPOINT_HTTPS_URL", siteConfigService.getProperty(PIWIK_TRACKER_ENDPOINT_HTTPS_URL));
			model.addAttribute("SESSION_ID", getSessionToken());
		}
	}

	protected String getSessionToken()
	{
		return sessionTokenService.getOrCreateSessionToken();
	}


	protected String getCurrentPiwikSiteId()
	{
		String piwikSiteId = siteConfigService.getProperty(PIWIK_TRACKER_SITE_ID + baseStoreService.getCurrentBaseStore().getUid());
		if (piwikSiteId == null)
		{
			piwikSiteId = siteConfigService.getProperty(PIWIK_TRACKER_DEFAULT_SITE_ID);
		}
		return piwikSiteId;
	}

	protected String getCurrentUnbxdSiteKey()
	{
		String unbxdSiteKey = siteConfigService.getProperty(UNBXD_ANALYTICS_SITE_KEY + baseStoreService.getCurrentBaseStore().getUid());
		if (unbxdSiteKey == null)
		{
			unbxdSiteKey = siteConfigService.getProperty(UNBXD_ANALYTICS_DEFAULT_SITE_KEY);
		}
		return unbxdSiteKey;
	}

	protected CatalogModel getCurrentUnbxdCatalog()
	{
		final CatalogVersionModel unbxdCatalogVersion  = (CatalogVersionModel) sessionService.getAttribute("currentCatalogVersion");
		if (unbxdCatalogVersion != null)
		{
			return unbxdCatalogVersion.getCatalog();
		}
		return baseStoreService.getCurrentBaseStore().getCatalogs().get(0);
	}


}