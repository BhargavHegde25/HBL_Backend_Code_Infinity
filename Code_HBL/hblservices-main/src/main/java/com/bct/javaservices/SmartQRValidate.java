package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.bct.utilities.HttpConnector;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SmartQRValidate implements JavaService2 {
	private static LoggerUtil logger = new LoggerUtil(SmartQRValidate.class);
	
	
	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		
		String URL = paramHelper.getServerProperty("SMARTQR_TOKEN_URL");
		String username = paramHelper.getServerProperty("SMARTQR_TOKEN_USERNAME");
		String password = paramHelper.getServerProperty("SMARTQR_TOKEN_PASSWORD");
		
		logger.debug("Server Prop SMARTQR_TOKEN_URL : " + URL);
		logger.debug("Server Prop SMARTQR_TOKEN_USERNAME : " + username);
		logger.debug("Server Prop SMARTQR_TOKEN_PASSWORD : " + password);
		
		Map<String, String> inputParams = new HashMap<>();
		/*inputParams.put("grant_type", "password");
		inputParams.put("username", "HBLUAT");
		inputParams.put("password", "Smart@123");*/
		
		inputParams.put("grant_type", "password");
		inputParams.put("username", username);
		inputParams.put("password", password);
		
		getAccessToken(URL, inputParams);
		Result result = new Result();
		result.setParam(new Param("opstatus", "200"));

		return result;
	} 
	
	public String getAccessToken(String URL, Map<String, String> inputParams) {
		HttpConnector conn = new HttpConnector();
		JsonObject response = new JsonObject();
		Map<String, String> headerParams = new HashMap<>();
		headerParams.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
		String token = null;
		try {
			response = conn.invokeHttpPost(URL, inputParams, headerParams);
			token = response.getAsJsonObject().get("access_token").getAsString();
			logger.debug("getAccessToken response : " + response.getAsString());
		} catch (Exception e) {
			logger.error("Exception in getAccessToken", e);
		}
		return token;
	}
}
