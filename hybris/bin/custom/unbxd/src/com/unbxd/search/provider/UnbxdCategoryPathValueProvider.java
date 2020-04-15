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
package com.unbxd.search.provider;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.CategorySource;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;


/**
 * Category path value provider. Value provider that generates field values for category paths and parent paths.
 */
public class UnbxdCategoryPathValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private CategorySource categorySource;
	private FieldNameProvider fieldNameProvider;
	private CategoryService categoryService;

	private static String CATEGORY_PATH_SEPARATOR = "unbxd.category.path.separator";
	private String categoryPathSeparator = StringUtils.defaultIfEmpty(Config.getParameter(CATEGORY_PATH_SEPARATOR),">");

	protected CategorySource getCategorySource()
	{
		return categorySource;
	}

	@Required
	public void setCategorySource(final CategorySource categorySource)
	{
		this.categorySource = categorySource;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final Collection<CategoryModel> categories = getCategorySource().getCategoriesForConfigAndProperty(indexConfig,
				indexedProperty, model);
		if (categories != null && !categories.isEmpty())
		{
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			final Set<String> categoryPaths = getCategoryPaths(categories);
			fieldValues.addAll(createFieldValue(categoryPaths, indexedProperty));

			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	protected List<FieldValue> createFieldValue(final Collection<String> categoryPaths, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			for (final String path : categoryPaths)
			{
				fieldValues.add(new FieldValue(fieldName, path));
			}
		}

		return fieldValues;
	}

	protected Set<String> getCategoryPaths(final Collection<CategoryModel> categories)
	{
		final Set<String> allPaths = new HashSet<String>();

		for (final CategoryModel category : categories)
		{
			if (!(category instanceof ClassificationClassModel))
			{
				final Collection<List<CategoryModel>> pathsForCategory = getCategoryService().getPathsForCategory(category);
				if (pathsForCategory != null)
				{
					for (final List<CategoryModel> categoryPath : pathsForCategory)
					{
						accumulateCategoryPaths(categoryPath, allPaths);
					}
				}
			}
		}

		return allPaths;
	}

	protected void accumulateCategoryPaths(final List<CategoryModel> categoryPath, final Set<String> output)
	{
		final StringBuilder accumulator = new StringBuilder();
		for (final CategoryModel category : categoryPath)
		{
			if (category instanceof ClassificationClassModel)
			{
				break;
			}
            if (StringUtils.isNotEmpty(accumulator.toString())){
                accumulator.append(categoryPathSeparator);
            }
			accumulator.append(category.getCode());
			output.add(accumulator.toString());
		}
	}
}
