package com.kony.utilityproductservices;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

import eu.bitwalker.useragentutils.UserAgent;

public class LogBrowser implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String eventType = "APPLICATION_LAUNCH";
		String reportingParams = (String) HelperMethods.getHeadersWithReportingParams(dcRequest)
				.get(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
		String StatusId = "SID_EVENT_SUCCESS";
		String devicename = null;
		UserAgent userAgent = UserAgent.parseUserAgentString(dcRequest.getHeader("User-Agent"));

		if (StringUtils.isNotBlank(reportingParams)) {
			JSONObject reportingParamsJson = new JSONObject(
					URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));

			String channel_id = reportingParamsJson.optString("chnl");

			if (channel_id.equalsIgnoreCase("desktop")) {
				StringBuilder sb = new StringBuilder();
				sb.append(userAgent.getBrowser().getName());
				devicename = sb.toString();
			} else {
				devicename = reportingParamsJson.optString("dm");
			}
		}

		if (StringUtils.isBlank(devicename)) {
			ErrorCodeEnum.ERR_10163.setErrorCode(result);
			return result;
		}

		switch (devicename) {
		case "Chrome Browser":
			devicename = "BROWSER_CHROME";
			break;
		case "Microsoft Edge":
			devicename = "BROWSER_MICROSOFT_EDGE";
			break;
		case "Mozilla Firefox":
			devicename = "BROWSER_MOZILLA";
			break;
		case "Apple Safari":
			devicename = "BROWSER_APPLE_SAFARI";
			break;
		case "Internet Explorer":
			devicename = "BROWSER_INTERNET_EXPLORER";
			break;
		default:
			devicename = "NON_SUPPORTED_BROWSER";
		}

		JsonObject customParams = new JsonObject();
//		customParams.addProperty("version", userAgent.getBrowserVersion().getVersion());
		for (String key : inputParams.keySet()) {
			customParams.addProperty(key, inputParams.get(key));
		}
		
		  EventsDispatcher.dispatch(dcRequest, dcResponse, eventType, devicename,
		  "utility/operations/Browser/logBrowser", StatusId, null, "", customParams);
		  
		  
		 return result;
		 
	}

}
