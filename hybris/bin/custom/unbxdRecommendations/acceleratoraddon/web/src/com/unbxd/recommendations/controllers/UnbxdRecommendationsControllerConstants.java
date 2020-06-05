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
package com.unbxd.recommendations.controllers;

import com.unbxd.recommendations.model.UnbxdProductCarouselComponentModel;

/**
 */
public interface UnbxdRecommendationsControllerConstants
{
	// implement here controller constants used by this extension
    interface Actions
    {
        interface Cms
        {
            String _Prefix = "/view/"; // NOSONAR
            String _Suffix = "Controller"; // NOSONAR
            String UnbxdProductCarouselComponentController = _Prefix + UnbxdProductCarouselComponentModel._TYPECODE + _Suffix; // NOSONAR
        }
    }
}
