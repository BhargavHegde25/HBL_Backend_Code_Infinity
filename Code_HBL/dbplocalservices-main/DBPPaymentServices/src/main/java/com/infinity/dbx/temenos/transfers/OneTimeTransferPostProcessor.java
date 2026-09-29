package com.infinity.dbx.temenos.transfers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

import net.minidev.json.parser.JSONParser;

public class OneTimeTransferPostProcessor extends TemenosBasePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("null")
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response) {
		try {
			result = super.execute(result, request, response);
			String status = result.getParamValueByName(TransferConstants.PARAM_STATUS);
			alert.prepareError("Status : " + status).log();
			alert.prepareError("Transaction Id : " + CommonUtils.getParamValue(result, "referenceId")).log();
			alert.prepareError("Body : " + CommonUtils.getParamValue(result, "body")).log();
			String errmsg = result.getParamValueByName(Constants.PARAM_ERR_MSG);
			if ((StringUtils.isNotEmpty(status) && TransferConstants.STATUS_FAILED.equalsIgnoreCase(status))
					|| StringUtils.isNotBlank(errmsg)) {
				Result res = TransferUtils.setErrorDetails(result);
				return res;
			}
			
			String serviceResponse = ResultToJSON.convert(result);
			String overrideValue;
			JSONObject serviceResponseJSON = new JSONObject(serviceResponse);
			JSONObject currentPaymentOrderJSONs , currentOverrideJSON, currentChargesJSON ;
			JSONArray currentOverridesArray, chargesArray;
			JSONObject currentPaymentOrderJSON = serviceResponseJSON;
			JsonArray finalArray = new JsonArray();
			JSONObject OVERRIDES_MAP = new JSONObject();
			
			// Convert Result to JSON
            // Load Override Values from C360 Bundle Configurations
            JSONObject bundleConfig = TemenosUtils.getBundleConfigurations(TransactionConstants.DBP_BUNDLE,
                    TransactionConstants.DBP_OVERRIDE_CONFIG_KEY, request);
            String overrides = "";
            if (bundleConfig != null) {
                JSONArray configurations = bundleConfig.optJSONArray(CONFIGURATIONS);
                if (configurations != null && configurations.length() > 0) {
                    JSONObject paymentStatus = configurations.optJSONObject(0);
                    if (paymentStatus.has(TransactionConstants.DBP_CONFIG_TABLE_VALUE)) {
                    	overrides = paymentStatus.getString(TransactionConstants.DBP_CONFIG_TABLE_VALUE);
                    }
                }
            }
            if (StringUtils.isBlank(overrides)) {
            	alert.prepareError("Unable to get override values from configurations").log();
            }
            if(overrides != "") {
            	OVERRIDES_MAP = new JSONObject(overrides);
            }
			
			
			//String overrides ="{\"AC-OVERDRAFT.ON.ACCOUNT\":\"overdraft\",\"PI-UNAUTH.OVERDRAFT\":\"overdraft\",\"PI-CUT.OFF.TIME.BREACHED\":\"cutOfTimeBreached\",\"PI-CHNG.CUT.OFF.PRODUCT\":\"changeProduct\"}";
			//JSONObject OVERRIDES_MAP = new JSONObject(overrides);
			JsonArray mylist = new JsonArray();
			currentOverridesArray = currentPaymentOrderJSON
					.optJSONArray(TransactionConstants.OVERRIDES);
			if (currentOverridesArray != null && currentOverridesArray.length() > 0) {
				for(Object currentOverride : currentOverridesArray) {
					JsonObject overridesJSON = new JsonObject();
					if (currentOverride instanceof JSONObject) {
						currentOverrideJSON = (JSONObject) currentOverride;
						    int index=currentOverrideJSON
									.optString("override").indexOf("}");
							if (StringUtils.isNotEmpty("override")) {
								String override = currentOverrideJSON
										.optString("override").substring(0, index);
								overridesJSON.addProperty(TransactionConstants.ID,currentOverrideJSON
										.optString("override").substring(0, index));
								overridesJSON.addProperty(TransactionConstants.MESSAGE,currentOverrideJSON
										.optString("override").substring(index+1, currentOverrideJSON
												.optString("override").length()));
								if(OVERRIDES_MAP != null && OVERRIDES_MAP.has(override)) {
									mylist.add(OVERRIDES_MAP.get(override).toString());
								}
								else {
									mylist.add(override);
								}
			                }						
					}
					finalArray.add(overridesJSON);
				}
				currentPaymentOrderJSON.put(TransactionConstants.OVERRIDES, finalArray);
			}
			
			
			
			// Handle Overrides
						/*currentOverridesArray = currentPaymentOrderJSON
								.optJSONArray(TransactionConstants.OVERRIDES);
						if (currentOverridesArray != null && currentOverridesArray.length() > 0) {
							for(Object currentOverride : currentOverridesArray) {
								JsonObject overridesJSON = new JsonObject();
								if (currentOverride instanceof JSONObject) {
									currentOverrideJSON = (JSONObject) currentOverride;
									    int index=currentOverrideJSON
												.optString("override").indexOf("}");
										if (StringUtils.isNotEmpty("override")) {
											overridesJSON.addProperty(TransactionConstants.ID,currentOverrideJSON
													.optString("override").substring(0, index));
											overridesJSON.addProperty(TransactionConstants.MESSAGE,currentOverrideJSON
													.optString("override").substring(index+1, currentOverrideJSON
															.optString("override").length()));
						                }						
								}
								finalArray.add(overridesJSON);
							}
							currentPaymentOrderJSON.put(TransactionConstants.OVERRIDES, finalArray);
						}*/
			
			
			
			serviceResponseJSON.put(TransactionConstants.CHARGES,currentPaymentOrderJSON.optString(TransactionConstants.CHARGES_ARRAY));
			serviceResponseJSON.remove(TransactionConstants.CHARGES_ARRAY);
			serviceResponseJSON.remove(TransactionConstants.OVERRIDES);
			serviceResponseJSON.put(TransactionConstants.OVERRIDES, finalArray);
			serviceResponseJSON.put(TransactionConstants.OVERRIDE_LIST, mylist);
			// Convert JSON to Result
			result = JSONToResult.convert(serviceResponseJSON.toString());
			
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception Occured while creating one time transfer post processor:" + e).log();
			CommonUtils.setErrMsg(errorResult, e.getMessage());
			return errorResult;
		}
		return result;
	}
}
