/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar
 *
 */
public class GetInstrumentDetailsPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			JSONObject instrumentMin = new JSONObject();
			JSONObject instrumentAssets = new JSONObject();
			JSONArray instrumentDet = new JSONArray();

			/*
			 * Record headerRec = result.getRecordById("header"); Record messagesRec =
			 * result.getRecordById("messages"); if (messagesRec != null) { String message =
			 * messagesRec.getParamValueByName("message"); Result errorRes = new Result();
			 * errorRes.addParam("errormessage", message); return errorRes; }
			 */

			String[] colArray = new String[] { TemenosConstants.STOCKEXCHANGE, TemenosConstants.INSTRUMENTNAME,
					"referenceCurrency", "ISINCode", TemenosConstants.INSTRUMENTID, "id", "marketPrice",
					"instrumentCurrencyId" };

			Record bodyRec = result.getRecordById("body");
			JSONObject instrumentDetails = new JSONObject();
			JSONObject assetTypes = new JSONObject();
			JSONObject assetValues = new JSONObject();
			String[] typeArr = null;
			if (bodyRec != null) {
				instrumentDetails = ResultToJSON.convertRecord(bodyRec);
				for (String key : colArray)
					if (!instrumentDetails.has(key)) {
						instrumentDetails.put(key, "");
					}

				instrumentDetails.put("dateReceived", "");
				instrumentDetails.put("quantity", "");
				instrumentDetails.put("RICCode", "");

				if (instrumentDetails.getString("assetType").equalsIgnoreCase("Stock")) {
					typeArr = new String[] { TemenosConstants.ASSETTYPE, TemenosConstants.REGION, "country",
							TemenosConstants.SECTOR };
					assetValues = ResultToJSON.convertRecord(bodyRec);
					for (String key : typeArr)
						if (!assetValues.has(key)) {
							assetTypes.put(key, "");
						} else if (assetValues.has(key)) {
							assetTypes.put(key, assetValues.getString(key));
						} else {
						}

				}
				if (instrumentDetails.getString("assetType").equalsIgnoreCase("Fixed Income")) {
					typeArr = new String[] { "interestRate", TemenosConstants.DURATION, "rating", "endD",
							"nxtCoupnDate", "modifiedDuration", TemenosConstants.ASSETTYPE, "yieldToMaturity"};
					assetValues = ResultToJSON.convertRecord(bodyRec);
					for (String key : typeArr) {
						if (key.equals("endD") && !assetValues.has(key)) {
							assetTypes.put("maturityDate", "");
						} else if (key.equals("endD") && assetValues.has(key)) {
							assetTypes.put("maturityDate", assetValues.getString(key));
						} else if (!assetValues.has(key)) {
							assetTypes.put(key, "");
						} else if (key.equals("yieldToMaturity") && assetValues.has(key)){
							//String yieldToMaturity = String.format("%.2f", (Double.parseDouble(instrumentDetails.getString("yieldToMaturity"))*100));
							String yieldToMaturity = ((Double.parseDouble(instrumentDetails.getString("yieldToMaturity"))*100) >= 0 || (Double.parseDouble(instrumentDetails.getString("yieldToMaturity"))*100) >= 0.00 )? ( "+" + String.format("%.2f", (Double.parseDouble(instrumentDetails.getString("yieldToMaturity"))*100))) : (String.format("%.2f", (Double.parseDouble(instrumentDetails.getString("yieldToMaturity"))*100))); 
							assetTypes.put("yield", yieldToMaturity);
						}else if (assetValues.has(key)) {
							assetTypes.put(key, assetValues.getString(key));
						} else {
						}

					}
				}
					if (instrumentDetails.getString("assetType").equalsIgnoreCase("Fund Share")) {
						typeArr = new String[] { TemenosConstants.ASSETTYPE, TemenosConstants.REGION, "country",
								TemenosConstants.SECTOR, "type", "issuer" };
						assetValues = ResultToJSON.convertRecord(bodyRec);
						for (String key : typeArr)
							if (key.equals("endD") && !assetValues.has(key)) {
								assetTypes.put("maturityDate", "");
							} else if (key.equals("endD") && assetValues.has(key)) {
								assetTypes.put("maturityDate", assetValues.getString(key));
							} else if (!assetValues.has(key)) {
								assetTypes.put(key, "");
							} else if (assetValues.has(key)) {
								assetTypes.put(key, assetValues.getString(key));
							} else {
							}

					}
					if (instrumentDetails.getString("assetType").equalsIgnoreCase("Money Market")) {
						typeArr = new String[] { TemenosConstants.ASSETTYPE, "interestRate", "contractType", "endD" };
						assetValues = ResultToJSON.convertRecord(bodyRec);
						for (String key : typeArr)
							if (key.equals("endD") && !assetValues.has(key)) {
								assetTypes.put("maturityDate", "");
							} else if (key.equals("endD") && assetValues.has(key)) {
								assetTypes.put("maturityDate", assetValues.getString(key));
							} else if (!assetValues.has(key)) {
								assetTypes.put(key, "");
							} else if (assetValues.has(key)) {
								assetTypes.put(key, assetValues.getString(key));
							} else {
							}

					}
					if (instrumentDetails.getString("assetType").equalsIgnoreCase("Future")) {
						typeArr = new String[] { TemenosConstants.ASSETTYPE, "contractSize", "underlying", "endD" };
						assetValues = ResultToJSON.convertRecord(bodyRec);
						for (String key : typeArr)
							if (key.equals("endD") && !assetValues.has(key)) {
								assetTypes.put("expiryDate", "");
							} else if (key.equals("endD") && assetValues.has(key)) {
								assetTypes.put("expiryDate", assetValues.getString(key));
							} else if (!assetValues.has(key)) {
								assetTypes.put(key, "");
							} else if (assetValues.has(key)) {
								assetTypes.put(key, assetValues.getString(key));
							} else {
							}

					}
					if (instrumentDetails.getString("assetType").equalsIgnoreCase("Option")) {
						typeArr = new String[] { TemenosConstants.ASSETTYPE, "contractSize", "underlying",
								"strikePrice", "endD", "optionStyle", "optionClass" };
						assetValues = ResultToJSON.convertRecord(bodyRec);
						for (String key : typeArr)
							if (key.equals("endD") && !assetValues.has(key)) {
								assetTypes.put("expiryDate", "");
							} else if (key.equals("endD") && assetValues.has(key)) {
								assetTypes.put("expiryDate", assetValues.getString(key));
							} else if (!assetValues.has(key)) {
								assetTypes.put(key, "");
							} else if (assetValues.has(key)) {
								assetTypes.put(key, assetValues.getString(key));
							} else {
							}

					}
					if (instrumentDetails.getString("assetType").equalsIgnoreCase("Forward")) {
						typeArr = new String[] { TemenosConstants.ASSETTYPE, "endD" };
						assetValues = ResultToJSON.convertRecord(bodyRec);
						for (String key : typeArr) {
							if (key.equals("endD") && !assetValues.has(key)) {
								assetTypes.put("maturityDate", "");
							} else if (key.equals("endD") && assetValues.has(key)) {
								assetTypes.put("maturityDate", assetValues.getString(key));
							}else if (!assetValues.has(key)) {
								assetTypes.put(key, "");
							} else if (assetValues.has(key)) {
								assetTypes.put(key, assetValues.getString(key));
							} else {
							}

					}
					}
					String[] assetArray = new String[] { TemenosConstants.REGION, "nxtCoupnDate",
							TemenosConstants.SECTOR, "country", "interestRate", "rating", "nxtCoupnDate",
							"modifiedDuration", "type", "expiryDate", "contractSize", "underlying", "strikePrice",
							"contractType", "endD", TemenosConstants.SECTOR, "country", TemenosConstants.DURATION,
							"optionStyle", "optionClass" };
					for (String val : assetArray) {
						instrumentDetails.remove(val);
					}
				
				if (instrumentDetails.getString("assetType").equalsIgnoreCase("Stock")
						|| instrumentDetails.getString("assetType").equalsIgnoreCase("Fund Share")
						|| instrumentDetails.getString("assetType").equalsIgnoreCase("Fixed Income")) {
					instrumentAssets.put("isSecurityAsset", true);
				} else {
					instrumentAssets.put("isSecurityAsset", false);
				}

				instrumentDetails.remove("assetType");
			} else {
				instrumentAssets.put("isSecurityAsset", "");
				instrumentDetails.remove("RICCode");
			}

			instrumentDet.put(instrumentDetails);
			instrumentMin.put("instrumentMinimal", instrumentDet);
			instrumentAssets.put("assetTypes", assetTypes);

			instrumentMin.put("instrumentAssets", instrumentAssets);

			Result instrumentRes = Utilities.constructResultFromJSONObject(instrumentMin);
			instrumentRes.addOpstatusParam("0");
			instrumentRes.addHttpStatusCodeParam("200");
			instrumentRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return instrumentRes;

		} catch (Exception e) {
			alert.prepareError("Error while invoking GetInstrumentDetailsPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}
		return null;
	}

}
