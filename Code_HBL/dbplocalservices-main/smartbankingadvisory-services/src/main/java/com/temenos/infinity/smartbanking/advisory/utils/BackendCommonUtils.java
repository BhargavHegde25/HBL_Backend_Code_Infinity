package com.temenos.infinity.smartbanking.advisory.utils;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.konylabs.middleware.dataobject.Result;

public class BackendCommonUtils {

	public static boolean isBackendResponseSuccess(Result result) {

		if (result != null && result.getParamValueByName("status") != null
				&& result.getParamValueByName("status").equalsIgnoreCase("Success"))
			return true;
		return false;
	}
	
	public static boolean isBackendResponseSuccess(Result result, String paramName) {
		boolean isSuccess = false;
		String paramValue = (result != null) ? result.getParamValueByName(paramName) : null;
		if (paramValue != null) {
			switch (paramName) {
			case "status":
				isSuccess = paramValue.equals("Success");
				break;
			case "httpStatusCode":
				isSuccess = paramValue.equals("200") || paramValue.equals("201");
				break;
			case "opstatus":
				isSuccess = paramValue.equals("0");
				break;
			}
		}
		return isSuccess;
	}

	public static boolean isBackendResponseSuccess(JSONObject result, String paramName) {
		boolean isSuccess = false;
		String paramValue = result.optString(paramName);
		if (StringUtils.isNotBlank(paramValue)) {
			switch (paramName) {
			case "status":
				isSuccess = paramValue.equals("Success");
				break;
			case "httpStatusCode":
				isSuccess = paramValue.equals("200") || paramValue.equals("201");
				break;
			}
		}
		return isSuccess;
	}
	
	public double roundDoubleValueTo2Digits(double value) {

		return (double) Math.round(value * 100.0) / 100.0;
	}
	
}
