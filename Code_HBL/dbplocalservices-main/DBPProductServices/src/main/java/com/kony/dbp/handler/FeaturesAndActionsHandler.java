package com.kony.dbp.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class FeaturesAndActionsHandler {

	public static JSONArray getCustomerFeatureDetails(String locale, DataControllerRequest dataControllerRequest,
			Result processedResult) throws ApplicationException, Exception {
		Map<String, String> inputParams = new HashMap<>();
		String filter = getFilter(locale, dataControllerRequest,processedResult);
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);

		String response = HelperMethods.callApiAndGetString(dataControllerRequest, inputParams,
				HelperMethods.getHeaders(dataControllerRequest), URLConstants.FEATURE_DETAILS_VIEW_GET);
		if (response == null) {
			throw new ApplicationException(ErrorCodeEnum.ERR_13510);
		}
		JSONObject responseJson = new JSONObject(response);
		if (!responseJson.has("opstatus") || responseJson.getInt("opstatus") != 0 || !responseJson.has("feature_details_view")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(responseJson)));
			throw new ApplicationException(ErrorCodeEnum.ERR_13510);
		}
		
		return responseJson.getJSONArray("feature_details_view");
	}
	
	private static String getFilter(String locale, DataControllerRequest dataControllerRequest,
			Result processedResult) throws HttpCallException, ApplicationException, Exception {
		String filter = "";
		
		JSONObject userFeatures = LegalEntityUtil.getUserCurrentLegalEntityFeaturePermissions(
				dataControllerRequest);
		
		if (userFeatures != null && userFeatures.has("features")) {
			String features = userFeatures.getString("features");
			if(StringUtils.isBlank(features)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_13508);
			}
			JSONArray featuresArray = new JSONArray(features);
			for(int i=0; i<featuresArray.length(); i++) {
				if(StringUtils.isNotBlank(filter)) {
					filter += " or ";
				}else {
					filter += "(";
				}
				filter += "id eq '"+featuresArray.getString(i)+"'";
			}
		}
		
		if(StringUtils.isBlank(filter)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_13508);
		}
		
		filter += ") and Locale_id eq '"+locale+"'";
		return filter;
	}
}
