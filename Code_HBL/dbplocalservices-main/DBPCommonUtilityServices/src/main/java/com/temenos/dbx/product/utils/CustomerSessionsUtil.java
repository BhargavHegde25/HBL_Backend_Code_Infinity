package com.temenos.dbx.product.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerSessionsUtil {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public static Result deleteActiveUserSessionsIfAny(String customerId, String userName, DataControllerRequest dcRequest) {
		String quantumAuthToken = AdminUtil.getQuantumAuthToken(dcRequest);
		Result result = new Result();
		if (StringUtils.isBlank(quantumAuthToken) || StringUtils.isBlank(customerId)) {
			result.addStringParam("errmsg", "Mandatory input empty for delete active sessions");
			return result;
		}
		Map<String, Object> inputParams = new HashMap<String, Object>();
		Map<String, Object> headerMap = dcRequest.getHeaderMap();
		headerMap.put("claims_token", quantumAuthToken);
        String userIds = "[\"" + customerId + "\",\"" + userName +"\"]";
		inputParams.put("userIds", userIds);
		inputParams.put("providerName", "DbxUserLogin");
		inputParams.put("providerType", "custom");
		try {
			JsonObject response = ServiceCallHelper.invokePassThroughServiceAndGetJson(dcRequest, inputParams,
					headerMap, URLConstants.DELETE_ACTIVE_USER_SESSIONS);
			if (response != null && !response.isJsonNull() && response.has("userSessionsCount")) {
				return result;
			} else {
				result.addStringParam("errmsg", "Failed to delete active user sessions");
				return result;
			}
		} catch (Exception e) {
			result.addStringParam("errmsg", "Failed to delete active user sessions");
			return result;
		}
	}
	
	/*
	 * 1) All projects which depends on user attributes will call this method 2)
	 * This method will call java integrationservice to get user attibutes 3) this
	 * is for dcrequest
	 */
	public static Map<String, Object> getLoggedInUserAttributesMap(DataControllerRequest dcRequest) throws Exception {
		{
			Map<String, Object> resultMap = new HashMap<>();
			try {
				Result result = ServiceCallHelper.invokeServiceAndGetResult(new HashMap<>(),
						HelperMethods.getHeaders(dcRequest), URLConstants.GET_USER_ATTRIBUTES,
						dcRequest.getHeader("x-kony-authorization"));
				resultMap = formatUserAttributeRecordtoMap(result);
			} catch (HttpCallException e) {
				diagnostic.prepareDebug("Error occured while fetching user attributes.. ").log();
			}

			if (resultMap.isEmpty()) {
				return dcRequest.getServicesManager().getIdentityHandler().getUserAttributes();
			}

			return resultMap;
		}
	}

	/*
	 * 1) All projects which depends on user attributes will call this method 2)
	 * This method will call java integrationservice to get user attibutes 3) this
	 * is for request manager
	 */

	public static Map<String, Object> getLoggedInUserAttributesMap(FabricRequestManager requestManager)
			throws Exception {
		Map<String, Object> params = new HashMap<>();
		Result result = ServiceCallHelper.invokeServiceAndGetResult(params,  HelperMethods.getHeaders(requestManager), URLConstants.GET_USER_ATTRIBUTES,
				requestManager.getHeadersHandler().getHeader("x-kony-authorization"));
		return formatUserAttributeRecordtoMap(result);
	}

	public static Map<String, Object> formatUserAttributeRecordtoMap(Result result) throws Exception {

		Record userAttributeRecord = result.getRecordById(DBPUtilitiesConstants.USR_ATTR);
		Map<String, Object> userAttributesMap = new HashMap<String, Object>();
		if (userAttributeRecord != null) {
			for (Param param : userAttributeRecord.getAllParams()) {
				userAttributesMap.put(param.getName(), param.getValue());
			}
		}
		return userAttributesMap;
	}
}
