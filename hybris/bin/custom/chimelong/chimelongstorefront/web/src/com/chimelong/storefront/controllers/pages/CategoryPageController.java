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
package com.chimelong.storefront.controllers.pages;


import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCategoryPageController;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.ChangeProductPriceBundleRuleModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.ProductService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chimelong.core.enums.ChiemLongChannel;
import com.chimelong.core.model.hotel.DateRangeModel;
import com.chimelong.core.model.hotel.HotelProductModel;
import com.chimelong.core.model.hotel.RoomRatePlanModel;
import com.chimelong.core.model.hotel.RoomRateProductModel;
import com.chimelong.core.model.hotel.TicketRatePlanModel;
import com.chimelong.core.model.hotel.TicketRateProductModel;
import com.chimelong.core.model.ticket.TicketProductModel;
import com.chimelong.storefront.controllers.ControllerConstants;


/**
 * Controller for a category page
 */
@Controller
@RequestMapping(value = "/**/c")
public class CategoryPageController extends AbstractCategoryPageController
{
	@Resource
	private ProductService productService;
	@Resource
	private PriceDataFactory priceDataFactory;
	@Resource
	private CommercePriceService commercePriceService;

	private static String DATE_PATTERN = "yyyy-MM-dd";
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private static final Logger LOG = Logger.getLogger(CategoryPageController.class);
	protected static final String CATEGORY_PARENT_CODE_PATH_VARIABLE_PATTERN = "/{parentCategoryCode}/{categoryCode}";

	@RequestMapping(value = CATEGORY_PARENT_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String category(@PathVariable("parentCategoryCode") final String parentCategoryCode,
			@PathVariable("categoryCode") final String categoryCode, @RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "edate", required = false) String edate,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "net", required = false) Boolean net,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
			throws UnsupportedEncodingException, CMSItemNotFoundException
	{
		final CategoryModel category = getCommerceCategoryService().getCategoryForCode(categoryCode);
		Date dateD = null;
		Date edateD = null;
		ChiemLongChannel cLongChannel = null;
		if (StringUtils.isBlank(channel))
		{
			cLongChannel = ChiemLongChannel.SITE;
		}
		else
		{
			cLongChannel = ChiemLongChannel.valueOf(channel);
		}

		if ("sc".equals(parentCategoryCode))
		{
			try
			{
				if (StringUtils.isNotBlank(date))
				{
					dateD = convertStringDateToDate(date, DATE_PATTERN);
				}
				else
				{
					dateD = new Date();
					date = formatter.format(dateD);
				}
			}
			catch (Exception e)
			{
				LOG.error("convertStringDateToDate ERROR!");
				return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
			}

			List<ProductData> productDatas = new ArrayList<>();
			List<ProductModel> products = category.getProducts();
			for (ProductModel product : products)
			{
				if (product instanceof TicketProductModel)
				{
					TicketProductModel ticketProduct = (TicketProductModel) product;
					ProductData productData = populateTicketProduct(ticketProduct, dateD, cLongChannel);
					productDatas.add(productData);
				}
			}
			model.addAttribute("parentCategoryCode", parentCategoryCode);
			model.addAttribute("date", date);
			model.addAttribute("productDatas", productDatas);
			model.addAttribute("searchUrl", "/chimelongstorefront/c/" + parentCategoryCode + "/" + categoryCode);
			storeCmsPageInModel(model, getContentPageForLabelOrId("chimelongProductListPage"));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId("chimelongProductListPage"));
			return getViewForPage(model);
		}
		else if ("h".equals(parentCategoryCode))
		{
			if (null == net)
			{
				net = true;
			}
			try
			{
				if (StringUtils.isNotBlank(date))
				{
					dateD = convertStringDateToDate(date, DATE_PATTERN);
				}
				else
				{
					dateD = new Date();
					date = formatter.format(dateD);
				}
				if (StringUtils.isNotBlank(edate))
				{
					edateD = convertStringDateToDate(edate, DATE_PATTERN);
				}
				else
				{
					Calendar c = Calendar.getInstance();
					c.setTime(dateD);
					c.add(Calendar.DAY_OF_MONTH, 1);
					edateD = c.getTime();
					edate = formatter.format(edateD);
				}
			}
			catch (Exception e)
			{
				LOG.error("convertStringDateToDate ERROR!");
				return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
			}

			List<ProductData> productDatas = new ArrayList<>();
			List<ProductModel> products = productService.getProductsForCategory(category);
			for (ProductModel product : products)
			{
				if (product instanceof HotelProductModel)
				{
					HotelProductModel ticketProduct = (HotelProductModel) product;
					ProductData productData = populateHotelProduct(ticketProduct, dateD, edateD, net, cLongChannel);
					if (null != productData)
					{
						productDatas.add(productData);
					}
				}
			}
			if (CollectionUtils.isNotEmpty(productDatas))
			{
				model.addAttribute("noresult", true);
			}
			model.addAttribute("parentCategoryCode", parentCategoryCode);
			model.addAttribute("date", date);
			model.addAttribute("edate", edate);
			model.addAttribute("productDatas", productDatas);
			model.addAttribute("net", net);
			model.addAttribute("searchUrl", "/chimelongstorefront/c/" + parentCategoryCode + "/" + categoryCode);
			storeCmsPageInModel(model, getContentPageForLabelOrId("chimelongProductListPage"));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId("chimelongProductListPage"));
			return getViewForPage(model);
		}
		prepareNotFoundPage(model, response);
		return getViewForPage(model);
	}

	@ResponseBody
	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/facets", method = RequestMethod.GET)
	public FacetRefinement<SearchStateData> getFacets(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		return performSearchAndGetFacets(categoryCode, searchQuery, page, showMode, sortCode);
	}

	@ResponseBody
	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/results", method = RequestMethod.GET)
	public SearchResultsData<ProductData> getResults(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		return performSearchAndGetResultsData(categoryCode, searchQuery, page, showMode, sortCode);
	}

	private ProductData populateTicketProduct(TicketProductModel product, Date dateD, ChiemLongChannel cLongChannel)
	{
		ProductData productData = new ProductData();
		productData.setCode(product.getCode());
		productData.setName(product.getName(Locale.CHINESE));
		TicketRatePlanModel ticketRatePlan = product.getTicketRatePlan();
		if (null != ticketRatePlan)
		{
			List<ProductModel> productModels = ticketRatePlan.getProducts();
			for (ProductModel productModel : productModels)
			{
				if (productModel instanceof TicketRateProductModel)
				{
					TicketRateProductModel ticketRateProductModel = (TicketRateProductModel) productModel;
					if (validateRateAgainstDate(dateD, ticketRateProductModel)
							&& cLongChannel.equals(ticketRateProductModel.getChannel()))
					{
						PriceDataType priceType = PriceDataType.BUY;
						final PriceInformation info = commercePriceService.getWebPriceForProduct(ticketRateProductModel);
						final PriceData priceData = priceDataFactory.create(priceType,
								BigDecimal.valueOf(info.getPriceValue().getValue()), info.getPriceValue().getCurrencyIso());
						productData.setPrice(priceData);
						break;
					}
				}
			}
		}
		else
		{
			LOG.info("NO TICKETRATEPLANMODEL FOUND FOR CURRENT PRODUCT!");
			return null;
		}
		return productData;
	}

	private ProductData populateHotelProduct(HotelProductModel product, Date dateD, Date edateD, Boolean net,
			ChiemLongChannel cLongChannel)
	{
		ProductData productData = new ProductData();
		productData.setCode(product.getCode());
		productData.setName(product.getName(Locale.CHINESE));
		RoomRatePlanModel roomRatePlan = product.getRoomRatePlan();
		if (null != roomRatePlan)
		{
			List<ProductModel> productModels = roomRatePlan.getProducts();

			if (net)
			{
				long stayDays = getStayDays(edateD, dateD);
				PriceData priceData = calHotelNetPrice(productModels, dateD, stayDays, cLongChannel);
				if (null == priceData)
				{
					LOG.info("NO ROOMRATEPLANMODEL FOUND FOR CURRENT PRODUCT!");
					return null;
				}
				productData.setPrice(priceData);
			}
			else
			{//only consider 2day1night
				PriceData priceData = calHotelNetPrice(productModels, dateD, 1, cLongChannel);
				if (null == priceData)
				{
					LOG.info("NO ROOMRATEPLANMODEL FOUND FOR CURRENT PRODUCT!");
					return null;
				}
				Collection<BundleTemplateModel> bundleTemplates = product.getBundleTemplates();
				List<BundleTemplateData> bundleTemplateDatas = new ArrayList<>();
				for (BundleTemplateModel bundleTemplateModel : bundleTemplates)
				{
					BundleTemplateData bundleTemplateData = new BundleTemplateData();
					List<ProductData> products = new ArrayList<>();
					bundleTemplateData.setName(bundleTemplateModel.getName(Locale.CHINESE));
					bundleTemplateData.setId(bundleTemplateModel.getId());
					for (ProductModel productModelBT : bundleTemplateModel.getProducts())
					{
						ProductData productDataBT = new ProductData();
						productDataBT.setCode(productModelBT.getCode());
						productDataBT.setName(productModelBT.getName(Locale.CHINESE));
						products.add(productData);
					}
					Collection<ChangeProductPriceBundleRuleModel> bundleRuleModels = bundleTemplateModel
							.getChangeProductPriceBundleRules();
					BigDecimal bundlePrice = new BigDecimal(0);
					if (CollectionUtils.isNotEmpty(bundleRuleModels))
					{
						Iterator it = bundleRuleModels.iterator();
						while (it.hasNext())
						{
							ChangeProductPriceBundleRuleModel changeProductPriceBundleRuleModel = (ChangeProductPriceBundleRuleModel) it
									.next();
							bundlePrice = changeProductPriceBundleRuleModel.getPrice();
						}
					}
					PriceData priceDataTemp = priceDataFactory.create(priceData.getPriceType(), priceData.getValue().add(bundlePrice),
							priceData.getCurrencyIso());
					bundleTemplateData.setPrice(priceDataTemp);
					bundleTemplateData.setProducts(products);
					bundleTemplateDatas.add(bundleTemplateData);
				}
				productData.setBundleTemplates(bundleTemplateDatas);
			}

		}
		else
		{
			LOG.info("NO ROOMRATEPLANMODEL FOUND FOR CURRENT PRODUCT!");
			return null;
		}
		return productData;
	}

	private boolean validateRateAgainstDate(final Date date, final ProductModel productModel)
	{
		if (productModel instanceof TicketRateProductModel)
		{
			TicketRateProductModel ticketRateProduct = (TicketRateProductModel) productModel;
			for (final DateRangeModel dateRange : ticketRateProduct.getDateRanges())
			{
				if (!date.before(dateRange.getStartingDate()) && !date.after(dateRange.getEndingDate()))
				{
					return true;
				}
			}
		}
		else if (productModel instanceof RoomRateProductModel)
		{
			RoomRateProductModel roomRateProduct = (RoomRateProductModel) productModel;
			for (final DateRangeModel dateRange : roomRateProduct.getDateRanges())
			{
				if (!date.before(dateRange.getStartingDate()) && !date.after(dateRange.getEndingDate()))
				{
					return true;
				}
			}
		}
		return false;
	}

	private Date convertStringDateToDate(final String date, final String pattern)
	{
		try
		{
			return Date.from(
					LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern)).atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		catch (final DateTimeParseException e)
		{
			LOG.error("Unable to parse String to Date:", e);
		}
		return null;
	}

	private PriceData calHotelNetPrice(List<ProductModel> productModels, Date dateD, long stayDays, ChiemLongChannel cLongChannel)
	{
		double totalNetPrice = 0;
		PriceDataType priceType = PriceDataType.BUY;
		String currencyIso = null;
		for (int i = 0; i < stayDays; i++)
		{
			boolean findPlan = false;
			for (ProductModel productModel : productModels)
			{
				if (productModel instanceof RoomRateProductModel)
				{
					RoomRateProductModel roomRateProductModel = (RoomRateProductModel) productModel;
					if (validateRateAgainstDate(dateD, roomRateProductModel) && cLongChannel.equals(roomRateProductModel.getChannel()))
					{
						PriceInformation info = commercePriceService.getWebPriceForProduct(roomRateProductModel);
						currencyIso = info.getPriceValue().getCurrencyIso();
						totalNetPrice = totalNetPrice + info.getPriceValue().getValue();
						findPlan = true;
						break;
					}
				}
			}
			if (!findPlan)
			{
				LOG.error("CAN NOT FIND PLAN FOR THIS DATE!");
				return null;
			}
			dateD = plusOneDay(dateD);
		}
		final PriceData priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(totalNetPrice), currencyIso);
		return priceData;
	}

	private Date plusOneDay(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow = c.getTime();
		return tomorrow;
	}

	private long getStayDays(Date edate, Date date)
	{
		long day = (edate.getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}
}