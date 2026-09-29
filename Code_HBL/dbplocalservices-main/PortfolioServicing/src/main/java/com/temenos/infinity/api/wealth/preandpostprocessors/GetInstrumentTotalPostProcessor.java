package com.temenos.infinity.api.wealth.preandpostprocessors;

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

public class GetInstrumentTotalPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {

			String[] portfolioIdArr = null, graphDurationArr = null;
			String portfolioId = "", unRealizedPLAmount = "", unRealizedPLPercentage = "", unRealizedPL = "",
					marketValue = "";
			String graphDuration = "";
			portfolioIdArr = request.getParameterValues(TemenosConstants.PORTFOLIOID);
			graphDurationArr = request.getParameterValues(TemenosConstants.GRAPHDURATION);
			if (portfolioIdArr != null && portfolioIdArr.length > 0) {
				portfolioId = portfolioIdArr[0].trim();
			}
			if (graphDurationArr != null && graphDurationArr.length > 0) {
				graphDuration = graphDurationArr[0].trim();
			}
			Record headerRec = result.getRecordById("header");
			JSONObject instTotalJSON = new JSONObject();
			JSONObject responseJSON = new JSONObject();
			JSONObject responseObj = new JSONObject();
			String statusVal = headerRec.getParamValueByName("status");
			if (statusVal != null && statusVal.trim().equalsIgnoreCase("success")) {

				Dataset ds = result.getDatasetById("body");
				if (ds != null) {
					JSONObject resultObj = new JSONObject();
					resultObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
					JSONArray refnitivArray = resultObj.getJSONArray("Field");

					if (refnitivArray != null && refnitivArray.length() > 0) {

						String[] colArray = new String[] { "referenceCurrency", "marketValue", "unRealizedPLAmount",
								"unRealizedPLPercentage", "accountName", "accountNumber" };

						for (int i = 0; i < refnitivArray.length(); i++) {
							instTotalJSON = refnitivArray.getJSONObject(i);
							if (instTotalJSON != null && instTotalJSON.length() > 0) {
								responseJSON = refnitivArray.getJSONObject(i);
								for (String key : colArray) {

									if (!responseJSON.has(key)) {

										responseJSON.put(key, "");
									}
									if (key.trim().equalsIgnoreCase("unRealizedPLAmount")) {

										unRealizedPLAmount = responseJSON.get(key).toString().trim().length() > 0
												? (responseJSON.get(key).toString())
												: "0";

										unRealizedPL = Double.parseDouble(unRealizedPLAmount) >= 0 ? "P" : "L";
										responseJSON.put("unRealizedPL", unRealizedPL);
										responseJSON.put(key, unRealizedPLAmount);
									}
									if (key.trim().equalsIgnoreCase("marketValue")) {

										marketValue = responseJSON.get(key).toString().trim().length() > 0
												? (responseJSON.get(key).toString())
												: "0";

										responseJSON.put(key, marketValue);
									}
									if (key.trim().equalsIgnoreCase("unRealizedPLPercentage")) {
										unRealizedPLPercentage = String.format("%.2f",
												Double.parseDouble(responseJSON.get(key).toString()));
										responseJSON.put("unRealizedPLPercentage", unRealizedPLPercentage);
									}

								}
							}
						}
						/*
						 * for (int i = 0; i < refnitivArray.length(); i++) { JSONObject refnitivObj =
						 * refnitivArray.getJSONObject(i);
						 * 
						 * referenceCurrency = refnitivObj.has("referenceCurrency") ?
						 * (refnitivObj.get("referenceCurrency").toString().trim().length() > 0 ?
						 * refnitivObj.get("referenceCurrency").toString() : "") : "";
						 * 
						 * marketValue = refnitivObj.has("valuationAmt") ?
						 * (refnitivObj.get("valuationAmt").toString().trim().length() > 0 ?
						 * refnitivObj.get("valuationAmt").toString() : "0") : "0";
						 * 
						 * unRealizedPLPercentage = refnitivObj.has("unrealizedProfitLossPct") ?
						 * (refnitivObj.get("unrealizedProfitLossPct").toString().trim().length() > 0 ?
						 * refnitivObj.get("unrealizedProfitLossPct").toString() : "0") : "0";
						 * unRealizedPLAmount = refnitivObj.has("unrealProfitLoss") ?
						 * (refnitivObj.get("unrealProfitLoss").toString().trim().length() > 0 ?
						 * refnitivObj.get("unrealProfitLoss").toString() : "0") : "0";
						 * 
						 * unRealizedPL = Double.parseDouble(unRealizedPLAmount) >= 0 ? "P" : "L";
						 * 
						 * }
						 */

					}
					JSONArray objGraph = new JSONArray();

					responseJSON.put("portfolioID", portfolioId);
					// responseJSON.put("referenceCurrency", referenceCurrency);
					// responseJSON.put("marketValue", marketValue);
					// responseJSON.put("unRealizedPLAmount", unRealizedPLAmount);
					// responseJSON.put("unRealizedPLPercentage", unRealizedPLPercentage);
					// responseJSON.put("unRealizedPL", unRealizedPL);
					responseJSON.put("todayPL", "");
					responseJSON.put("todayPLAmount", "");
					// responseJSON.put("accountName", "");
					// responseJSON.put("accountNumber", "");
					responseJSON.put("todayPLPercentage", "");
					responseJSON.put(graphDuration, objGraph);
					responseJSON.put("graphDuration", graphDuration);
					responseJSON.put("opstatus", "0");
					responseJSON.put("httpStatusCode", "200");
					responseJSON.put("status", statusVal);
					JSONArray responseArr = new JSONArray();
					responseArr.put(responseJSON);
					responseObj.put("instrumentTotal",responseArr);

				}
			} else {
				Record errorRec = result.getRecordById("error");
				if (errorRec != null) {
					Record error = errorRec.getAllRecords().get(0);

					responseObj.put("errormessage", error.getParamValueByName("message"));
				} else {
					responseObj.put("errormessage", "");
				}
				responseObj.put("status", statusVal);
			}

			Result res = new Result();
			res = Utilities.constructResultFromJSONObject(responseObj);
			res.addOpstatusParam("0");
			res.addHttpStatusCodeParam("200");
			res.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return res;
		} catch (Exception e) {

			alert.prepareError("Error while invoking GetInstrumentTotalPostProcessor - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}

		return result;
	}

}
