package com.temenos.infinity.api.wealth.preandpostprocessors;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class GetAssetAllocationPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unused")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			String[] portfolioIdArr = null;
			double marketVal = 0, totalMarketValue = 0;
			String portfolioId = "", assetType = "", assetDesc = "", referenceCurrency = "", tempMarketVal = "",
					tempassetGroup = "";
			JSONArray assetArray = new JSONArray();
			LinkedHashSet<String> lh = new LinkedHashSet<String>();
			ArrayList<String> assetGroupList = new ArrayList<String>();
			ArrayList<String> marketValueList = new ArrayList<String>();

			JSONObject responseJSON = new JSONObject();

			portfolioIdArr = request.getParameterValues(TemenosConstants.PORTFOLIOID);
			if (portfolioIdArr != null && portfolioIdArr.length > 0) {
				portfolioId = portfolioIdArr[0].trim();
			}

			Record headerRec = result.getRecordById("header");
			String statusVal = headerRec.getParamValueByName("status");

			if (statusVal != null && statusVal.trim().equalsIgnoreCase("success")) {

				Dataset ds = result.getDatasetById("body");
				if (ds != null) {

					JSONObject resultObj = new JSONObject();
					resultObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
					JSONArray refnitivArray = resultObj.getJSONArray("Field");

					if (refnitivArray != null && refnitivArray.length() > 0) {
						String[] colArray = new String[] { "referenceCurrency", "assetType", "assetTypeDesc",
								"valueInReferenceCurrency" };
						JSONArray assetobjArray = new JSONArray();
						for (int i = 0; i < refnitivArray.length(); i++) {
							JSONObject assetObj = refnitivArray.getJSONObject(i);

							if (assetObj != null && assetObj.length() > 0) {
								for (String key : colArray) {
									if (!assetObj.has(key)) {
										assetObj.put(key, "");
									}
									if (key.trim().equalsIgnoreCase("assetType")) {
										assetType = assetObj.get(key).toString();
										if (assetType.trim().length() > 0) {
											lh.add(assetType);
										}
									}
									if (key.trim().equalsIgnoreCase("referenceCurrency")) {
										if (assetObj.get(key).toString().trim().length() > 0) {
											referenceCurrency = assetObj.get(key).toString();
										}
									}
								}
								assetobjArray.put(assetObj);
							}

						}

						for (String distinctAssetType : lh) {
							marketVal = 0;
							for (int i = 0; i < assetobjArray.length(); i++) {
								JSONObject assetdistinctObj = assetobjArray.getJSONObject(i);
								assetType = assetdistinctObj.get("assetType").toString();
								if (distinctAssetType.trim().equalsIgnoreCase(assetType)) {
									marketVal = marketVal + Double.parseDouble(assetdistinctObj
											.get("valueInReferenceCurrency").toString().trim().length() > 0
													? assetdistinctObj.get("valueInReferenceCurrency").toString()
													: "0");
									assetDesc = assetdistinctObj.get("assetTypeDesc").toString();

								}

							}
							assetGroupList.add(assetDesc);
							marketValueList.add(String.format("%.2f", marketVal));
							totalMarketValue = totalMarketValue + marketVal;
						}

						String assetGroup[] = assetGroupList.toArray(new String[assetGroupList.size()]);
						String marketValue[] = marketValueList.toArray(new String[marketValueList.size()]);

						for (int i = 0; i < marketValue.length; i++) {
							for (int j = i + 1; j < marketValue.length; j++) {
								if (Math.abs(Double.parseDouble(marketValue[i])) < Math
										.abs(Double.parseDouble(marketValue[j]))) {
									tempMarketVal = marketValue[i];
									marketValue[i] = marketValue[j];
									marketValue[j] = tempMarketVal;

									tempassetGroup = assetGroup[i];
									assetGroup[i] = assetGroup[j];
									assetGroup[j] = tempassetGroup;
								}
							}
						}

						for (int i = 0; i < assetGroup.length; i++) {
							JSONObject assetObj = new JSONObject();
							if (Double.parseDouble(marketValue[i]) != 0) {
								assetObj.put(TemenosConstants.ASSETGROUP, assetGroup[i]);
								assetObj.put(TemenosConstants.MARKETVALUE, marketValue[i]);
								assetObj.put(TemenosConstants.REFERENCECURRENCY, referenceCurrency);
								assetArray.put(assetObj);
							}
						}
						responseJSON.put(TemenosConstants.REFERENCECURRENCY, referenceCurrency);
						responseJSON.put(TemenosConstants.TOTALMARKETVALUE, totalMarketValue);
						responseJSON.put(TemenosConstants.ASSETS, assetArray);
						responseJSON.put("opstatus", "0");
						responseJSON.put("httpStatusCode", "200");
						responseJSON.put("status", statusVal);

					}
				}
			} else {
				Record errorRec = result.getRecordById("error");
				if (errorRec != null) {
					Record error = errorRec.getAllRecords().get(0);

					responseJSON.put("errormessage", error.getParamValueByName("message"));
				} else {
					responseJSON.put("errormessage", "");
				}
				responseJSON.put("status", statusVal);
			}

			return Utilities.constructResultFromJSONObject(responseJSON);
		} catch (Exception e) {

			alert.prepareError("Error while invoking GetOrderDetailsPostProcessor - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}

		return result;
	}

}
