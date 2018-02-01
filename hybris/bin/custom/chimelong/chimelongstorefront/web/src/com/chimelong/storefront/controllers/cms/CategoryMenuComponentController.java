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
package com.chimelong.storefront.controllers.cms;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.data.CategoryData;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chimelong.core.model.cms2.components.CategoryMenuComponentModel;
import com.chimelong.storefront.controllers.ControllerConstants;


/**
 * Controller for CMS CartSuggestionComponent
 */
@Controller("CategoryMenuComponentController")
@RequestMapping(value = ControllerConstants.Actions.Cms.CategoryMenuComponent)
public class CategoryMenuComponentController
		extends AbstractAcceleratorCMSComponentController<CategoryMenuComponentModel>
{

	@Resource
	private CMSSiteService cmsSiteService;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final CategoryMenuComponentModel component)
	{
		final Collection<CategoryModel> rootCatories = cmsSiteService.getCurrentSite().getProductCatalogs().iterator().next()
				.getRootCategories();
		final List<CategoryData> categoryDatas = new LinkedList<>();
		if (CollectionUtils.isNotEmpty(rootCatories))
		{
			rootCatories.forEach(rootCategory -> {
				addCategory(categoryDatas, rootCategory, null);
				final Collection<CategoryModel> subCategories = rootCategory.getAllSubcategories();
				subCategories.forEach(subCategory -> addCategory(categoryDatas, subCategory, rootCategory));
			});
		}
		model.addAttribute("categoryMenuDatas", categoryDatas);
	}

	private void addCategory(final List<CategoryData> categoryDatas, final CategoryModel categoryModel,
			final CategoryModel parentCategory)
	{
		final CategoryData categoryData = new CategoryData();
		categoryData.setCode(categoryModel.getCode());
		categoryData.setName(categoryModel.getName(Locale.CHINESE));
		categoryData.setParentCategoryName(parentCategory == null ? null : parentCategory.getName(Locale.CHINESE));
		categoryDatas.add(categoryData);
	}
}
