package com.kony.dbp.fileprocessingengine.httputils;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.kony.dbp.fileprocessingengine.FileProcessingEngineConstants;
import com.kony.dbp.fileprocessingengine.HelperPackage.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.registry.AppRegistryException;

public class HttpHelperMethods {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	private HttpHelperMethods() {

	}

	public static String getBaseURL(DataControllerRequest dataControllerRequest) {
		if (dataControllerRequest == null)
			return null;
		StringBuilder builder = new StringBuilder();
		builder.append("https");
		builder.append("://");
		String host = dataControllerRequest.getHeader("host");
		if (host == null || host.equals("")) {
			host = dataControllerRequest.getHeader("Host");
		}
		builder.append(host);
		return builder.toString();
	}

	public static JsonObject callhttpDeleteApi(Map inputparams, Map headerparams, String url) throws HttpCallException {
		HttpConnector httpConn = new HttpConnector();
		JsonObject response = httpConn.invokeHttpDelete(url, headerparams);
		return (null == response) ? new JsonObject() : response;
	}

	public static Map<String, String> getHeaderParamsForAuth(DataControllerRequest dcrequest)
			throws AppRegistryException {
		Map<String, String> inputparams = new HashMap<>();
		inputparams.put("X-Kony-App-Key", EnvironmentConfigurationsHandler
				.getValue(FileProcessingEngineConstants.DBPEVENTMANAGER_APPKEY, dcrequest.getServicesManager()));
		inputparams.put("X-Kony-App-Secret", EnvironmentConfigurationsHandler
				.getValue(FileProcessingEngineConstants.DBPEVENTMANAGER_APPSECRET, dcrequest.getServicesManager()));
		return inputparams;
	}

	public static Map<String, String> getInputParamsForAuth(DataControllerRequest dcrequest)
			throws AppRegistryException {
		Map<String, String> inputparams = new HashMap<>();
		inputparams.put("userid", EnvironmentConfigurationsHandler.getValue(
				FileProcessingEngineConstants.FIELPROCESSINGENGINE_IDENTITY_USER, dcrequest.getServicesManager()));
		inputparams.put("password", EnvironmentConfigurationsHandler.getValue(
				FileProcessingEngineConstants.FIELPROCESSINGENGINE_IDENTITY_PSWD, dcrequest.getServicesManager()));
		return inputparams;
	}

	public static synchronized String getAuthenticationKey(DataControllerRequest dcrequest) throws HttpCallException {

		String key = null;
		try {
			String url = EnvironmentConfigurationsHandler.getValue(
					FileProcessingEngineConstants.FIELPROCESSINGENGINE_IDENTITY_URL, dcrequest.getServicesManager());

			HttpConnector httpConnector = new HttpConnector();
			JsonObject response = httpConnector.invokeHttpPost(url, getInputParamsForAuth(dcrequest),
					getHeaderParamsForAuth(dcrequest));
			key = response.getAsJsonObject("claims_token").get("value").getAsString();
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
		return key;
	}

}
