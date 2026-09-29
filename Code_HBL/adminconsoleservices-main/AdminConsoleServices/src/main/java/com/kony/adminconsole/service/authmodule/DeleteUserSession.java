package com.kony.adminconsole.service.authmodule;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.alertmanagement.utils.Constants;
import com.kony.adminconsole.service.customermanagement.CustomerSearch;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class DeleteUserSession implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	public static final String USERNAME = "DBX_FABRIC_USERNAME";
	public static final String PASSWORD = "DBX_FABRIC_PASSWORD";
	public static final String PROVIDER_NAME = "DbxKeyCloakLogin";
	public static final String PROVIDER_TYPE = "oauth2";
	public static final String AUTH_TOKEN = "x-kony-authorization";
	public static final String CLAIMS_TOKEN = "claims_token";
	
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		try {
			
			String username = EnvironmentConfigurationsHandler.getServerAppPropertyValue(USERNAME, request);
			String password = EnvironmentConfigurationsHandler.getServerAppPropertyValue(PASSWORD, request);

			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				alert.prepareError("Unable to fetch username or password for quantum login").log();
				return JSONToResult.convert(new JSONObject().put(Constants.SUCCESS, Constants.FALSE).toString());
			}
			
			String fabricToken = "";
			String result = "";
			String api_token = request.getHeader(AUTH_TOKEN);
			String userId = request.getParameter("userId");
			
			if (StringUtils.isBlank(userId) || StringUtils.isBlank(api_token)) {
				alert.prepareError("Unable to fetch user id or fabric auth token").log();
				return JSONToResult.convert(new JSONObject().put(Constants.SUCCESS, Constants.FALSE).toString());
			}
			
			Map<String, Object> headerMap = new HashMap<String, Object>();
			Map<String, Object> inputParams = new HashMap<String, Object>();
			
			headerMap.put(AUTH_TOKEN, api_token);
			
			String userIds = "[\"" + userId + "\"]";

			inputParams.put("userid", username);
			inputParams.put("password", password);
			try {
				result = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.QUANTUM_LOGIN)
						.withOperationId(OperationName.LOGIN).withRequestHeaders(headerMap)
						.withRequestParameters(inputParams).build().getResponse().toString();
				JSONObject resultJsonObject = new JSONObject(result);
				if (resultJsonObject != null && !resultJsonObject.isEmpty() && !resultJsonObject.has("errmsg")
						&& resultJsonObject.has("claimsToken")) {
					fabricToken = resultJsonObject.getString("claimsToken");
				} else {
					alert.prepareError("Failed to fetch Quantum Auth Token, Invalid UserId or Password").log();
					return JSONToResult.convert(new JSONObject().put(Constants.SUCCESS, Constants.FALSE).toString());
				}
			} catch (Exception e) {
				alert.prepareError("Exception occured in calling Quantum login").log();
				return JSONToResult.convert(new JSONObject().put(Constants.SUCCESS, Constants.FALSE).toString());
			}
			headerMap.put(CLAIMS_TOKEN, fabricToken);

			inputParams = new HashMap<String, Object>();
			inputParams.put("userIds", userIds);
			inputParams.put("providerName", PROVIDER_NAME);
			inputParams.put("providerType", PROVIDER_TYPE);
			try {
				result = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DELETE_USER_SESSION)
						.withOperationId(OperationName.DELETE_USER_SESSION).withRequestHeaders(headerMap)
						.withRequestParameters(inputParams).withPassThroughOutput(true).build().getResponse()
						.toString();
				JSONObject resultJsonObject = new JSONObject(result);
				if (resultJsonObject != null && !resultJsonObject.isEmpty() && resultJsonObject.has("userSessionsCount")) {
					
					return JSONToResult.convert(new JSONObject().put(Constants.SUCCESS, Constants.TRUE).toString());
					
				} else {
					alert.prepareError("Failed to delete active user sessions").log();
					return JSONToResult.convert(new JSONObject().put(Constants.SUCCESS, Constants.FALSE).toString());
				}
			} catch (Exception e) {
				alert.prepareError("Exception occured in calling delete user session service").log();
				return JSONToResult.convert(new JSONObject().put(Constants.SUCCESS, Constants.FALSE).toString());
			}

		} catch (Exception e) {
			alert.prepareError("Runtime Exception.Exception Trace:", e).log();
			return JSONToResult.convert(new JSONObject().put(Constants.SUCCESS, Constants.FALSE).toString());
		}
	}
}
