/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.unbxd.search.interceptors;


import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.unbxd.helper.UnbxdHelperFunction;
import com.unbxd.model.UnbxdSiteConfigModel;
import com.unbxd.search.constants.UnbxdsearchConstants;


public class UnbxdAddonBeforeViewHandler implements BeforeViewHandler
{

	public static final String VIEW_NAME_MAP_KEY = "viewName";

	private Map<String, String> viewMap;
	private Map<String, String> componentViewMap;



	@Resource
	UnbxdHelperFunction unbxdHelperFunction;


	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		final String viewName = modelAndView.getViewName();
		final UnbxdSiteConfigModel siteConfig = unbxdHelperFunction.getCurrentUnbxdConfigModel.get();
		if (siteConfig.isUseDefaultLayout())
		{
			if (viewMap.containsKey(viewName))
			{
				if ((viewName.indexOf("search") > 0 && siteConfig.isSearchEnabled())
						|| (viewName.indexOf("category") > 0 && siteConfig.isCategoryEnabled()))
				{
					modelAndView.setViewName(UnbxdsearchConstants.ADDON_PREFIX + viewMap.get(viewName));
				}
			}
		}
		else
		{
			if (componentViewMap.containsKey(viewName))
			{
				if ((viewName.indexOf("search") > 0 && siteConfig.isSearchEnabled())
						|| (viewName.indexOf("category") > 0 && siteConfig.isCategoryEnabled()))
				{
					modelAndView.setViewName(UnbxdsearchConstants.ADDON_PREFIX + componentViewMap.get(viewName));
				}
			}
		}
	}

	public Map<String, String> getViewMap()
	{
		return viewMap;
	}

	public void setViewMap(final Map<String, String> viewMap)
	{
		this.viewMap = viewMap;
	}

	/**
	 * @return the componentViewMap
	 */
	public Map<String, String> getComponentViewMap()
	{
		return componentViewMap;
	}

	/**
	 * @param componentViewMap
	 *           the componentViewMap to set
	 */
	public void setComponentViewMap(final Map<String, String> componentViewMap)
	{
		this.componentViewMap = componentViewMap;
	}
}
