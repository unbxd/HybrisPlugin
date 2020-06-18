package com.unbxd.recommendations.controllers.cms;

import com.unbxd.recommendations.controllers.UnbxdRecommendationsControllerConstants;
import com.unbxd.recommendations.model.UnbxdProductCarouselComponentModel;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Controller("UnbxdProductCarouselComponentController")
@RequestMapping(value = UnbxdRecommendationsControllerConstants.Actions.Cms.UnbxdProductCarouselComponentController)
public class UnbxdProductCarouselComponentController extends AbstractCMSAddOnComponentController<UnbxdProductCarouselComponentModel>
{
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	private static final String UNBXD_RECOMMENDATION_ACTIVE = "unbxd.recommendation.active";

	private static final String UNBXD_SITE_KEY = "unbxd.sitekey.";
	private static final String UNBXD_API_KEY = "unbxd.apikey.";

	private static final String UNBXD_SITEKEY_MODEL = "unbxdSiteKey";
	private static final String UNBXD_APIKEY_MODEL = "unbxdApiKey";

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final UnbxdProductCarouselComponentModel component)
	{
		if (Config.getBoolean(UNBXD_RECOMMENDATION_ACTIVE, false)) {

			final List<ProductData> products = new ArrayList<>();

			products.addAll(collectRecommendedProducts(component));

			model.addAttribute("title", component.getTitle());
			model.addAttribute("productData", products);
			model.addAttribute("UnbxdPageType", component.getPageType().getCode());
			model.addAttribute("widgetType", component.getWidget().getCode());

			if (getCartFacade().hasEntries()) {
				CartData cartData = getCartFacade().getSessionCart();
				model.addAttribute("cartentries", cartData.getEntries());
				List<String> productcodes = new ArrayList<>();
				cartData.getEntries().forEach(entry -> productcodes.add(entry.getProduct().getCode()));
				model.addAttribute("productcodes", productcodes);
			}

			model.addAttribute(UNBXD_SITEKEY_MODEL, getCurrentUnbxdAutoSuggestSiteKey());
			model.addAttribute(UNBXD_APIKEY_MODEL, getCurrentUnbxdAutoSuggestAPIKey());
		}
	}

	protected String getCurrentUnbxdAutoSuggestSiteKey()
	{
		return siteConfigService.getProperty(UNBXD_SITE_KEY + baseStoreService.getCurrentBaseStore().getUid());
	}

	protected String getCurrentUnbxdAutoSuggestAPIKey()
	{
		return siteConfigService.getProperty(UNBXD_API_KEY + baseStoreService.getCurrentBaseStore().getUid());
	}

	protected List<ProductData> collectRecommendedProducts(final UnbxdProductCarouselComponentModel component)
	{
		return Collections.emptyList();
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}
}
