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
package com.unbxd.setup;

import static com.unbxd.constants.UnbxdConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.unbxd.constants.UnbxdConstants;
import com.unbxd.service.UnbxdService;


@SystemSetup(extension = UnbxdConstants.EXTENSIONNAME)
public class UnbxdSystemSetup
{
	private final UnbxdService unbxdService;

	public UnbxdSystemSetup(final UnbxdService unbxdService)
	{
		this.unbxdService = unbxdService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		unbxdService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return UnbxdSystemSetup.class.getResourceAsStream("/unbxd/sap-hybris-platform.png");
	}
}
