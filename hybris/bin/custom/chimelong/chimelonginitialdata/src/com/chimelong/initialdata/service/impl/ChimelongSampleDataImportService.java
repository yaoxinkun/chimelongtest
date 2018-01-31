/**
 *
 */
package com.chimelong.initialdata.service.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.configurablebundleservices.model.ChangeProductPriceBundleRuleModel;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;



/**
 * @author kyao011
 *
 */
public class ChimelongSampleDataImportService extends SampleDataImportService
{

	private TypeService typeService;
	private ModelService modelService;

	@Override
	protected void importProductCatalog(final String extensionName, final String productCatalogName)
	{

		super.importProductCatalog(extensionName, productCatalogName);

		importBundleProductCatalog(extensionName, productCatalogName);
	}


	protected void importBundleProductCatalog(final String extensionName, final String productCatalogName)
	{
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/bundletemplates.impex",
						extensionName, productCatalogName), false);
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/bundletemplates_en.impex",
						extensionName, productCatalogName), false);
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/bundletemplates_zh.impex",
						extensionName, productCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/bundletemplates-products.impex", extensionName,
						productCatalogName),
				false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/bundletemplates-selectioncriteria.impex",
						extensionName, productCatalogName),
				false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/bundletemplates-pricerules.impex",
						extensionName, productCatalogName),
				false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/bundletemplates-disablerules.impex",
						extensionName, productCatalogName),
				false);

	}

	@Override
	public boolean synchronizeProductCatalog(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final String catalogName, final boolean syncCatalogs)
	{
		systemSetup.logInfo(context, String.format("Begin synchronizing Product Catalog [%s]", catalogName));

		getSetupSyncJobService().createProductCatalogSyncJob(String.format("%sProductCatalog", catalogName));

		final List<SyncItemJobModel> syncItemJobs = getCatalogVersionService()
				.getCatalogVersion(String.format("%sProductCatalog", catalogName), CatalogManager.OFFLINE_VERSION)
				.getSynchronizations();

		if (CollectionUtils.isNotEmpty(syncItemJobs))
		{
			for (final SyncItemJobModel syncItemJob : syncItemJobs)
			{
				final List<ComposedTypeModel> rootTypes = new ArrayList<ComposedTypeModel>(syncItemJob.getRootTypes());
				final ComposedTypeModel bundleTemplateStatus = typeService.getComposedTypeForClass(BundleTemplateStatusModel.class);
				final ComposedTypeModel bundleTemplate = typeService.getComposedTypeForClass(BundleTemplateModel.class);
				final ComposedTypeModel changeProductPriceBundleRule = typeService
						.getComposedTypeForClass(ChangeProductPriceBundleRuleModel.class);

				rootTypes.add(0, bundleTemplateStatus);
				rootTypes.add(0, bundleTemplate);
				rootTypes.add(0, changeProductPriceBundleRule);

				syncItemJob.setRootTypes(rootTypes);
				modelService.save(syncItemJob);
			}
		}

		if (syncCatalogs)
		{
			final PerformResult syncCronJobResult = getSetupSyncJobService()
					.executeCatalogSyncJob(String.format("%sProductCatalog", catalogName));
			if (isSyncRerunNeeded(syncCronJobResult))
			{
				systemSetup.logInfo(context, String.format("Product Catalog [%s] sync has issues.", catalogName));
				return false;
			}
		}

		return true;
	}


	/**
	 * @return the typeService
	 */
	public TypeService getTypeService()
	{
		return typeService;
	}


	/**
	 * @param typeService
	 *           the typeService to set
	 */
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


}
