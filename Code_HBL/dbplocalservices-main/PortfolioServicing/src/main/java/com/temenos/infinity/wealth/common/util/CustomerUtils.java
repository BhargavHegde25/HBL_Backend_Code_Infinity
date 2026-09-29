package com.temenos.infinity.wealth.common.util;


import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
/**
 * @author muthukumarv
 *
 */

public class CustomerUtils {

	public static JSONObject getCustomerDetailsFromTAP(JSONArray customerTAPArr) {
		JSONArray bodyArr = new JSONArray();
		JSONObject resJSON = new JSONObject();
		for (int i = 0; i < customerTAPArr.length(); i++) {
			JSONObject bodyObj = customerTAPArr.getJSONObject(i);
			JSONObject constraints = new JSONObject();
			String customerCode = bodyObj.getString("customerCode");
			String customerName = bodyObj.getString("customerName");
			String totalAssetValue = bodyObj.has("totalAssetValue")?bodyObj.getString("totalAssetValue") : "";
			String currencyCode = bodyObj.getString("currencyCode");
			constraints.put("customerId",customerCode);
			constraints.put("customerName",customerName);
			constraints.put("totalAssetValue",totalAssetValue);
			constraints.put("currencyCode",currencyCode);
			bodyArr.put(constraints);
		}
		resJSON.put("Customers2", bodyArr);

		return resJSON;
	}
	
	public static JSONObject updateCustomerDetailsFromTAP(String existId, String [] existIdarr, String backendId, String operation) {
		JSONObject resultJson = new JSONObject();
 
			if (operation.equalsIgnoreCase("Add")) {

				if (!backendId.equals("") && (!Arrays.asList(existIdarr).contains(backendId))) {
					if (existId.equals("")) {
						existId = backendId;
					} else {
						existId = existId + "," + backendId;
					}
					//JSONObject resultJson = new JSONObject();
					resultJson.put("msg", "Favourite customers updated successfully");
					resultJson.put("existingBackendIds", existId);
					return resultJson;
				} else {
					//JSONObject resultJson = new JSONObject();
					resultJson.put("msg", "Customers already in Favourites");
					return resultJson;
				}

			}
			List<String> existingBackendList = Arrays.asList(existIdarr);
			if (operation.equalsIgnoreCase("Remove")) {
				if (!backendId.equals("") && (existingBackendList.contains(backendId))) {
					existId = existId.contains(backendId + ",") ? existId.replace(backendId + ",", "") : existId.replace(backendId, "");
					existId = existId.replaceAll(",$", "");
					resultJson.put("msg", "Favourite customers updated successfully");
					resultJson.put("existingBackendIds", existId);
					return resultJson;
				} else {
					//JSONObject resultJson = new JSONObject();
					resultJson.put("msg", "Customers was not in Favourites");
					return resultJson;
				}
			}
			return resultJson;
	}
	public static Result finalResult(JSONObject res, String status) {
		Result finalResult = new Result();
		finalResult = Utilities.constructResultFromJSONObject(res);
		finalResult.addOpstatusParam("0");
		finalResult.addHttpStatusCodeParam("200");
		finalResult.addParam(TemenosConstants.STATUS, status);
		finalResult.addParam("msg", res.getString("msg"));
		return finalResult;
	}

}
