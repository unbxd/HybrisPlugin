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
package com.unbxd.search.constants;

import com.unbxd.search.model.UnbxdWebConfigComponentModel;


/**
 * Global class for all Unbxdsearch web constants. You can add global constants for your extension into this class.
 */
public final class UnbxdsearchWebConstants // NOSONAR
{
	private UnbxdsearchWebConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

	public interface Views
	{
		public interface Cms // NOSONAR
		{
			String _Prefix = "/view/"; // NOSONAR
			String _Suffix = "Controller"; // NOSONAR

			/**
			 * CMS components that have specific handlers
			 */
			String UnbxdWebConfigComponent = _Prefix + UnbxdWebConfigComponentModel._TYPECODE + _Suffix; // NOSONAR

		}
	}
}
