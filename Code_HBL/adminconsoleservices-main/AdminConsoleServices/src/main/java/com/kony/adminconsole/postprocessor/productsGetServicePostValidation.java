package com.kony.adminconsole.postprocessor;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class productsGetServicePostValidation implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		try {
			Dataset catalog = result.getDatasetById("marketingCatalogue");
			List<Record> Records = null;
			if (catalog != null)
				Records = catalog.getAllRecords();
			if (Records != null)
				for (int i = 0; i < Records.size(); i++) {
					Record record = Records.get(i);
					if (!record.hasParamByName("externalIndicator")) {
						record.addParam("externalIndicator", "");
					}
					if (!record.hasParamByName("productLineId")) {
						record.addParam("productLineId", "");
					}
					if (!record.hasParamByName("productLineName")) {
						record.addParam("productLineName", "");
					}
					if (!record.hasParamByName("productLineRef")) {
						record.addParam("productLineRef", "");
					}
					Dataset productGroups = record.getDatasetById("productGroups");
					List<Record> productGroupRecords = null;
					if (productGroups != null)
						productGroupRecords = productGroups.getAllRecords();
					if (productGroupRecords != null && productGroupRecords.size() > 0) {
						for (Record productGroupRecord : productGroupRecords) {
							if (!productGroupRecord.hasParamByName("branchRef")) {
								productGroupRecord.addParam("branchRef", "");
							}
							if (!productGroupRecord.hasParamByName("description")) {
								productGroupRecord.addParam("description", "");
							}
							if (!productGroupRecord.hasParamByName("detailedDesc")) {
								productGroupRecord.addParam("detailedDesc", "");
							}
							if (!productGroupRecord.hasParamByName("externalIndicator")) {
								productGroupRecord.addParam("externalIndicator", "");
							}
							if (!productGroupRecord.hasParamByName("productGroupId")) {
								productGroupRecord.addParam("productGroupId", "");
							}
							if (!productGroupRecord.hasParamByName("productGroupName")) {
								productGroupRecord.addParam("productGroupName", "");
							}
							if (!productGroupRecord.hasParamByName("productGroupRef")) {
								productGroupRecord.addParam("productGroupRef", "");
							}
							// products
							Dataset products = productGroupRecord.getDatasetById("products");
							List<Record> productsRecords = null;
							if (products != null)
								productsRecords = products.getAllRecords();
							if (productsRecords != null && productsRecords.size() > 0) {
								for (Record productsRecord : productsRecords) {
									if (!productsRecord.hasParamByName("apr")) {
										productsRecord.addParam("apr", "");
									}
									if (!productsRecord.hasParamByName("availableFrom")) {
										productsRecord.addParam("availableFrom", "");
									}
									if (!productsRecord.hasParamByName("availableTo")) {
										productsRecord.addParam("availableTo", "");
									}
									if (!productsRecord.hasParamByName("branchRef")) {
										productsRecord.addParam("branchRef", "");
									}
									if (!productsRecord.hasParamByName("externalIndicator")) {
										productsRecord.addParam("externalIndicator", "");
									}
									if (!productsRecord.hasParamByName("productId")) {
										productsRecord.addParam("productId", "");
									}
									if (!productsRecord.hasParamByName("productName")) {
										productsRecord.addParam("productName", "");
									}
									if (!productsRecord.hasParamByName("productRef")) {
										productsRecord.addParam("productRef", "");
									}
									if (!productsRecord.hasParamByName("status")) {
										productsRecord.addParam("status", "");
									}
									if (!productsRecord.hasParamByName("purposes")) {
										productsRecord.addParam("purposes", "");
									}
									// product Description
									Record descRecord = productsRecord.getRecordById("productDescription");
									if (!descRecord.hasParamByName("description")) {
										descRecord.addParam("description", "");
									}
									if (!descRecord.hasParamByName("detailedDesc")) {
										descRecord.addParam("detailedDesc", "");
									}
									if (!descRecord.hasParamByName("disclosure")) {
										descRecord.addParam("disclosure", "");
									}
									if (!descRecord.hasParamByName("notes")) {
										descRecord.addParam("notes", "");
									}
									if (!descRecord.hasParamByName("termsConditions")) {
										descRecord.addParam("termsConditions", "");
									}
									// imageDetails
									Dataset imageDetails = productsRecord.getDatasetById("imageDetails");
									List<Record> imageRecords = null;
									if (imageDetails != null)
										imageRecords = imageDetails.getAllRecords();
									if (imageRecords != null && imageRecords.size() > 0) {
										for (Record imageRecord : imageRecords) {
											if (!imageRecord.hasParamByName("height")) {
												imageRecord.addParam("height", "");
											}
											if (!imageRecord.hasParamByName("width")) {
												imageRecord.addParam("width", "");
											}
											if (!imageRecord.hasParamByName("imageType")) {
												imageRecord.addParam("imageType", "");
											}
											if (!imageRecord.hasParamByName("imageUrl")) {
												imageRecord.addParam("imageUrl", "");
											}
										}
									}

									// productFeatures
									Dataset productFeatures = productsRecord.getDatasetById("productFeatures");
									List<Record> fetauresRecords = null;
									if (productFeatures != null)
										fetauresRecords = productFeatures.getAllRecords();
									if (fetauresRecords != null && fetauresRecords.size() > 0) {
										for (Record fetauresRecord : fetauresRecords) {
											if (!fetauresRecord.hasParamByName("defaultValue")) {
												fetauresRecord.addParam("defaultValue", "");
											}
											if (!fetauresRecord.hasParamByName("description")) {
												fetauresRecord.addParam("description", "");
											}
											if (!fetauresRecord.hasParamByName("featureGroup")) {
												fetauresRecord.addParam("featureGroup", "");
											}
											if (!fetauresRecord.hasParamByName("featureName")) {
												fetauresRecord.addParam("featureName", "");
											}
											if (!fetauresRecord.hasParamByName("isMandatory")) {
												fetauresRecord.addParam("isMandatory", "");
											}
											if (!fetauresRecord.hasParamByName("option")) {
												fetauresRecord.addParam("option", "");
											}
											if (!fetauresRecord.hasParamByName("optionDispType")) {
												fetauresRecord.addParam("optionDispType", "");
											}
											if (!fetauresRecord.hasParamByName("sequenceNo")) {
												fetauresRecord.addParam("sequenceNo", "");
											}
											if (!fetauresRecord.hasParamByName("type")) {
												fetauresRecord.addParam("type", "");
											}
											// Option Values
											Dataset OptionValues = fetauresRecord.getDatasetById("optionValues");
											List<Record> optionValuesRecords = null;
											if (OptionValues != null)
												optionValuesRecords = OptionValues.getAllRecords();
											if (optionValuesRecords != null && optionValuesRecords.size() > 0) {
												for (Record optionValuesRecord : optionValuesRecords) {
													if (!optionValuesRecord.hasParamByName("desc")) {
														optionValuesRecord.addParam("desc", "");
													}
													if (!optionValuesRecord.hasParamByName("value")) {
														optionValuesRecord.addParam("value", "");
													}
												}
											}
										}
									}

								}
							}
							// productGroup Image details
							Dataset productGroupimageDetails = productGroupRecord.getDatasetById("imageDetails");
							List<Record> productGroupimageDetailsRecords = null;
							if (productGroupimageDetails != null)
								productGroupimageDetailsRecords = productGroupimageDetails.getAllRecords();
							if (productGroupimageDetailsRecords != null && productGroupimageDetailsRecords.size() > 0) {
								for (Record imageRecord : productGroupimageDetailsRecords) {
									if (!imageRecord.hasParamByName("height")) {
										imageRecord.addParam("height", "");
									}
									if (!imageRecord.hasParamByName("width")) {
										imageRecord.addParam("width", "");
									}
									if (!imageRecord.hasParamByName("imageType")) {
										imageRecord.addParam("imageType", "");
									}
									if (!imageRecord.hasParamByName("imageUrl")) {
										imageRecord.addParam("imageUrl", "");
									}
								}
							}

						}
					}
				}
			return result;
		} catch (Exception e) {
			result.addParam("Exception", e.toString());
			return result;
		}
	}
}
