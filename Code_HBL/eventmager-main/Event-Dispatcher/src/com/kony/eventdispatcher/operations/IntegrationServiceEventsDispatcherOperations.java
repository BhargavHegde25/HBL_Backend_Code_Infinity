package com.kony.eventdispatcher.operations;

import java.util.Iterator;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonObject;
import com.kony.utils.HelperMethods;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class IntegrationServiceEventsDispatcherOperations {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
	
	public static void convertDcRequestToJson(DataControllerRequest request, JsonObject requestObj) {
		try {
			if (request != null) {
				Iterator<String> parameterNames = request.getParameterNames();
				while (parameterNames.hasNext()) {
					String parameterName = parameterNames.next();
					requestObj.addProperty(parameterName, request.getParameter(parameterName));
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug(e.toString()).log();
		}
	}
	
	public static void convertDcResponseToJson(DataControllerResponse response, JsonObject responseObj) {
		try {
			if (response != null) {
				Iterator<String> parameterNames = response.getAttributeNames();
				while (parameterNames.hasNext()) {
					String parameterName = parameterNames.next();
					responseObj.addProperty(parameterName, response.getAttribute(parameterName).toString());
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug(e.toString()).log();
		}

	}
	
	public static void setAppSessionId(DataControllerRequest request, JsonObject customParams) {
		String authkey = request.getHeader(URLConstants.XKONYAUTHORIZATION);
		String appsessionid = HelperMethods.getParamFromIToken(authkey, URLConstants.SESSIONID);
		customParams.addProperty(URLConstants.APPSESSIONID, appsessionid);

	}
}
