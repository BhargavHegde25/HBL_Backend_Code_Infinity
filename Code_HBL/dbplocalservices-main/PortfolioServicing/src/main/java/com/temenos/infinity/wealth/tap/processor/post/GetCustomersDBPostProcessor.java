package com.temenos.infinity.wealth.tap.processor.post;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;

import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author sarah
 *
 */

public class GetCustomersDBPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
				diagnostic.prepareDebug("==========> GetCustomersDBPostProcessor - Entered").log();
				String backendIdType = request.getParameter("coreCustomerId").toString();
				JSONObject resultJson = new JSONObject();
				Result finalResult = null;
				JSONArray assetArray = new JSONArray();
				String customerId = HelperMethods.getCustomerIdFromSession(request);
				//String customerId = "5771239668";
				Map<String, String> inputParams = new HashMap<>();
				String filter = "customerId" + DBPUtilitiesConstants.EQUAL + customerId;
				inputParams.put(DBPUtilitiesConstants.FILTER, filter);
				try {
					result = HelperMethods.callGetApi(request, inputParams.get(DBPUtilitiesConstants.FILTER),
							HelperMethods.getHeaders(request), URLConstants.INF_WLTH_FAVORITE_CUSTOMER_GET);
				if (HelperMethods.hasRecords(result)) {
					List<Dataset> dataset = result.getAllDatasets();
					List<Record> drecords = dataset.get(0).getAllRecords();
					resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
					String backendIds = resultJson.get("favoriteCustomerId").toString();
					int index;
					String value;
					// Create a map to store index-value pairs from backendIdType
					/*Map<String, Integer> indexMap = new HashMap<>();
					for (int i = 0; i < backendIdType.size(); i++) {
					    indexMap.put(backendIdType.get(i), i);
					}*/
					// Iterate over the keys (strings) in indexMap


					if (backendIds != null && backendIds.length() > 0) {
						String backendIdArr[] = backendIds.trim().split(",");
						String backendCoreCustomerId[] = backendIdType.trim().split(",");
						for (String s : backendCoreCustomerId) {
						    if (Arrays.asList(backendIdArr).contains(s)) {
						        //index = indexMap.get(s);
						        //value = backendIdType.get(index);
						        JSONObject jsonObj1 = new JSONObject();
						        String favoriteStatus = "true";
						        jsonObj1.put("isFavourite", favoriteStatus);
						        jsonObj1.put("customerId", s);
						        assetArray.put(jsonObj1);
						    } else {
						    	//index = indexMap.get(s);
						    	//value = backendIdType.get(index);
						    	JSONObject jsonObj2 = new JSONObject();
						        String favoriteStatus = "false";
						        jsonObj2.put("isFavourite", favoriteStatus);
						        jsonObj2.put("customerId", s);
						        assetArray.put(jsonObj2);
						    }
						}
						resultJson.put("Customers1", assetArray);
						resultJson.put("opstatus", result.getOpstatusParamValue());
						resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
					    resultJson.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					}else {
					resultJson.put("Customers1", assetArray);
					resultJson.put("isFavourite", "false");
					resultJson.put("opstatus", result.getOpstatusParamValue());
					resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
				    resultJson.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				}
				} else {
					resultJson.put("Customers1", assetArray);
					resultJson.put("isFavourite", "false");
					resultJson.put("opstatus", result.getOpstatusParamValue());
					resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
					resultJson.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				}
				finalResult = Utilities.constructResultFromJSONObject(resultJson);
	} catch (Exception e) {
		alert.prepareError("==========> GetCustomersDBPostProcessor - Error: " + e.getMessage()).log();

	}
	diagnostic.prepareDebug("==========> GetCustomersDBPostProcessor - Exiting with success").log();
	return finalResult;
	}

}

