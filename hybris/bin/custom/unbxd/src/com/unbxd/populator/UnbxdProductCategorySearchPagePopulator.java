package com.unbxd.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UnbxdProductCategorySearchPagePopulator<QUERY, STATE, RESULT, ITEM extends ProductData, SCAT, CATEGORY>
		implements Populator<ProductCategorySearchPageData<QUERY, RESULT, SCAT>, ProductCategorySearchPageData<STATE, ITEM, CATEGORY>>
{
	private CommerceCategoryService commerceCategoryService;
	private static String CATEGORY_PATH_SEPARATOR = "unbxd.category.path.separator";

	@Override
	public void populate(final ProductCategorySearchPageData<QUERY, RESULT, SCAT> source,
			final ProductCategorySearchPageData<STATE, ITEM, CATEGORY> target)
	{
		if (source.getCategoryCode() != null) {
			target.setUnbxdCategoryPath(buildCategoryPath(source.getCategoryCode()));
		}
	}

	protected String buildCategoryPath(String categoryCode){
		List<String> categories = new ArrayList<>();
		final CategoryModel lastCategoryModel = getCommerceCategoryService().getCategoryForCode(categoryCode);

		Collection<List<CategoryModel>> pathsForCategory = getCommerceCategoryService().getPathsForCategory(lastCategoryModel);
		List<CategoryModel> categoryModels = (List<CategoryModel>) CollectionUtils.get(pathsForCategory, 0);
		categoryModels.forEach(categoryModel -> {categories.add(categoryModel.getCode());});

		String categoryPathSeparator = StringUtils.defaultIfEmpty(Config.getParameter(CATEGORY_PATH_SEPARATOR),">");
		return String.join(categoryPathSeparator, categories);
	}

	public CommerceCategoryService getCommerceCategoryService() {
		return commerceCategoryService;
	}

	public void setCommerceCategoryService(CommerceCategoryService commerceCategoryService) {
		this.commerceCategoryService = commerceCategoryService;
	}
}
