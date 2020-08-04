/**
 *
 */
package com.unbxd.search.controllers.cms;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unbxd.helper.UnbxdHelperFunction;
import com.unbxd.search.constants.UnbxdsearchWebConstants;
import com.unbxd.search.controllers.UnbxdsearchControllerConstants;
import com.unbxd.search.model.UnbxdWebConfigComponentModel;
import com.unbxd.web.PageConfig;
import com.unbxd.web.UnbxdConfig;




/**
 * @author jack8603
 *
 */
@Controller("UnbxdWebConfigComponentController")
@RequestMapping(value = UnbxdsearchWebConstants.Views.Cms.UnbxdWebConfigComponent)
public class UnbxdWebConfigComponentController extends AbstractCMSAddOnComponentController<UnbxdWebConfigComponentModel>
{


	@Resource
	UnbxdHelperFunction unbxdHelperFunction;


	private static final Logger LOG = Logger.getLogger(UnbxdWebConfigComponentController.class);


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController#fillModel(javax.servlet.http
	 * .HttpServletRequest, org.springframework.ui.Model,
	 * de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final UnbxdWebConfigComponentModel component)
	{

		final UnbxdConfig config = unbxdHelperFunction.getCurrentUnbxdConfig.get();

		if (config != null)
		{
			model.addAttribute(UnbxdsearchControllerConstants.UNBXD_CONFIG, config);
		}

		try
		{
			final Map<String, Object> modelMap = model.asMap();
			final PageConfig pageConfig = new PageConfig();
			if (modelMap.containsKey("pageType"))
			{
				final String pageType = (String) modelMap.get("pageType");
				pageConfig.setPageType(pageType);
				if (pageType == PageType.PRODUCT.name())
				{
					if (modelMap.containsKey("product") && modelMap.get("product") instanceof ProductModel)
					{
						pageConfig.setProductID(((ProductModel) modelMap.get("product")).getCode());
					}
				}
				else if (pageType == PageType.CATEGORY.name())
				{
					if (modelMap.containsKey("breadcrumbs"))
					{
						try
						{
							final List<Breadcrumb> breadCrumb = (List<Breadcrumb>) modelMap.get("breadcrumbs");
							final String categoryPath = breadCrumb.stream().map(Breadcrumb::getName).collect(Collectors.joining(">"));
							pageConfig.setCategoryPath(categoryPath);
						}
						catch (final Exception e)
						{
							LOG.error("Error while parsing breadcrumb", e);
						}
					}
				}
			}
			model.addAttribute(UnbxdsearchControllerConstants.UNBXD_PAGE_CONFIG, pageConfig);
		}
		catch (final Exception e)
		{
			LOG.error("Error while parsing page information", e);
		}


	}
}
