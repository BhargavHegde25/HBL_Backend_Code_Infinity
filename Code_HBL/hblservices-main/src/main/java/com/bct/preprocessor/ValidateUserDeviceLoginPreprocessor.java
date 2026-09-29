package com.bct.preprocessor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ValidateUserDeviceLoginPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(ValidateUserDeviceLoginPreprocessor.class);
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		String deviceBindProp = "";
		boolean serviceExecStatus = false;
		deviceBindProp = EnvironmentConfigurationsHandler.getServerProperty("MOCK_MOBILE_DEVICE_BINDING");
		LOG.debug("deviceBindProp : " + deviceBindProp);
		
		if (StringUtils.isNotBlank(deviceBindProp) && deviceBindProp.equalsIgnoreCase("1")) {
			// Mock response
			serviceExecStatus = false;
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			Dataset dataset = new Dataset();
			Record record = new Record();
			record.addStringParam("loginStatus", "Mock Response New Customer or Device Login");
			record.addStringParam("statusCd", "0");
			dataset.addRecord(record);
			dataset.setId("records");	
			result.addDataset(dataset);			
			
		} else {
			String bypassUsersList = EnvironmentConfigurationsHandler.getServerProperty("MOBILE_DEVICE_BINDING_BYPASS_USERS").toUpperCase().trim();
			LOG.debug("bypassUsersList : " + bypassUsersList);
			
			String reqUsername = request.getParameter("_username");
			LOG.debug("reqUsername : " + reqUsername);
			
			String cleanUsername = reqUsername.toUpperCase().trim();
			LOG.debug("cleanUsername : " + cleanUsername);
			
			List<String> listArr = new ArrayList<String>();
			if(bypassUsersList != null) {
				listArr = Arrays.asList(bypassUsersList.split(","));
			}
			
			if(!StringUtils.isBlank(cleanUsername) && cleanUsername.length() > 4 &&  listArr.contains(cleanUsername)) {
				serviceExecStatus = false;
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				Dataset dataset = new Dataset();
				Record record = new Record();
				record.addStringParam("loginStatus", "Username present in Device Binding Bypass list");
				record.addStringParam("statusCd", "0");
				dataset.addRecord(record);
				dataset.setId("records");	
				result.addDataset(dataset);	
			} else {
				String reqDeviceId = getCurrentDeviceID(request);
				LOG.debug("reqDeviceId : " + reqDeviceId);
				request.addRequestParam_("_deviceId", reqDeviceId);
				params.put("_deviceId", reqDeviceId);
				serviceExecStatus = true;
			}
		}
		return serviceExecStatus;
	}
	
	private String getCurrentDeviceID(DataControllerRequest dcRequest) {
		String deviceId = "";
		String reportingParams = dcRequest.getHeader("X-Kony-ReportingParams");
		if (StringUtils.isNotBlank(reportingParams)) {
			JSONObject reportingParamsJson;
			try {
				reportingParamsJson = new JSONObject(URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
				String channelId = reportingParamsJson.optString("chnl");
				String archType = reportingParamsJson.optString("atype");
				if (channelId.equalsIgnoreCase("mobile") && archType.equalsIgnoreCase("native")) {
					deviceId = reportingParamsJson.optString("did");
				}
			} catch (Exception e) {
				LOG.error("Exception getCurrentDeviceID ", e);
			}

		}
		return deviceId;
	}

}
