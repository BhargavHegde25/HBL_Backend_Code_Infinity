package com.kony.adminconsole.service.businessconfiguration;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * Service to Manage Alert Configurations
 *
 * @author Rishi Gupta
 *
 */
public class AlertConfigurationsManageService implements JavaService2 {

	private static final String GET_ALERT_CONFIGURATIONS = "getAlertConfigurations";
	private static final String EDIT_ALERT_CONFIGURATION = "editAlertConfiguration";
	private static final String CONFIGURATION_ID_PARAM = "id";
	private static final String CONFIGURATION_ALERT_VIEW_PREF_PARAM = "alertViewPreference";
	private static final String CONFIGURATION_ENABLE_FREQUENCY_PARAM = "enableFrequency";
	private static final String CONFIGURATION_ENABLE_ALERT_COMMUNICATION_PARAM = "enableAlertCommunication";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		try {

			if (StringUtils.equalsIgnoreCase(methodID, GET_ALERT_CONFIGURATIONS)) {
				return getAlertConfigurations(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, EDIT_ALERT_CONFIGURATION)) {
				return editAlertConfiguration(requestInstance);
			}

		} catch (Exception e) {
			Result operationResult = new Result();
			operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
			alert.prepareError("Exception in Business configuration Manage Service.", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(operationResult);
			return operationResult;
		}

		return new Result();
	}

	/**
	 * Method to get the business configurations
	 * 
	 * @param requestInstance
	 * @return operation Result
	 */
	private Result getAlertConfigurations(DataControllerRequest requestInstance) {

		Result operationResult = new Result();
		try {

			// Edit Eligibility Criteria
			Map<String, String> inputMap = new HashMap<String, String>();
			// Check Operation Response

			String alertConfigurationResponse = Executor
					.invokeService(ServiceURLEnum.CUSTOMERVIEWALERTCONFIGURATION_READ, inputMap, null, requestInstance);
			JSONObject readAlertConfigurationResponseJSON = CommonUtilities
					.getStringAsJSONObject(alertConfigurationResponse);
			if (readAlertConfigurationResponseJSON == null
					|| !readAlertConfigurationResponseJSON.has(FabricConstants.OPSTATUS)
					|| readAlertConfigurationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
					|| !readAlertConfigurationResponseJSON.has("customerviewalertconfiguration")) {
				alert.prepareError("Failed CRUD Operation.Response:" + readAlertConfigurationResponseJSON).log();
				operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED, "Get Alert configurations falied");
				ErrorCodeEnum.ERR_20944.setErrorCode(operationResult);
			}
			JSONArray readAlertConfigurationResponseJSONArray = readAlertConfigurationResponseJSON
					.optJSONArray("customerviewalertconfiguration");

			diagnostic.prepareDebug("Successful CRUD Operation").log();
			JSONObject currQuestionJSONObject;
			Dataset alertConfDataSet = new Dataset();
			alertConfDataSet.setId("AlertConfigurationRecords");
			for (int indexVar = 0; indexVar < readAlertConfigurationResponseJSONArray.length(); indexVar++) {
				currQuestionJSONObject = readAlertConfigurationResponseJSONArray.getJSONObject(indexVar);
				Record currRecord = new Record();
				for (String currKey : currQuestionJSONObject.keySet()) {
					currRecord.addParam(
							new Param(currKey, currQuestionJSONObject.optString(currKey), FabricConstants.STRING));
				}
				alertConfDataSet.addRecord(currRecord);
			}
			operationResult.addDataset(alertConfDataSet);
			operationResult.addParam(new Param("status", "Success", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.SEARCH,
					ActivityStatusEnum.SUCCESSFUL, "Get Alert configurations successful");

		} catch (Exception e) {
			operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
			alert.prepareError("Exception in getting business configuraitons.", e).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Get Alerts configuration failed");
			ErrorCodeEnum.ERR_20944.setErrorCode(operationResult);
		}
		return operationResult;
	}

	/**
	 * Method to edit the business configurations
	 * 
	 * @param requestInstance
	 * @param loggedInUser
	 * @return operation Result
	 */
	private Result editAlertConfiguration(DataControllerRequest requestInstance) throws ApplicationException {

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20908);
		}

		Result operationResult = new Result();

		// Read Inputs
		String id = requestInstance.getParameter(CONFIGURATION_ID_PARAM);
		String alertPreferenceView = requestInstance.getParameter(CONFIGURATION_ALERT_VIEW_PREF_PARAM);
		String enableFrequency = requestInstance.getParameter(CONFIGURATION_ENABLE_FREQUENCY_PARAM);
		String enableSeparateContact = requestInstance.getParameter(CONFIGURATION_ENABLE_ALERT_COMMUNICATION_PARAM);
		String legalEntityId = "";
		// Validate Inputs
		if (StringUtils.isBlank(id) || StringUtils.isBlank(alertPreferenceView) || StringUtils.isBlank(enableFrequency)
				|| StringUtils.isBlank(enableSeparateContact)) {
			alert.prepareError("Missing Mandatory Input: Alert configuraiton parameter").log();
			ErrorCodeEnum.ERR_20920.setErrorCode(operationResult);
			return operationResult;
		}
		
		// Set Input Map
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, "id eq " + id);
		String alertConfigurationResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERVIEWALERTCONFIGURATION_READ,
				inputMap, null, requestInstance);
		JSONObject readAlertConfigurationResponseJSON = CommonUtilities
				.getStringAsJSONObject(alertConfigurationResponse);
		if (readAlertConfigurationResponseJSON == null
				|| !readAlertConfigurationResponseJSON.has(FabricConstants.OPSTATUS)
				|| readAlertConfigurationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readAlertConfigurationResponseJSON.has("customerviewalertconfiguration")) {
			alert.prepareError("Failed CRUD Operation.Response:" + readAlertConfigurationResponseJSON).log();
			operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Get Alerts configurations falied");
			ErrorCodeEnum.ERR_20944.setErrorCode(operationResult);
		}
		String alertconfigurationId = readAlertConfigurationResponseJSON.getJSONArray("customerviewalertconfiguration")
				.getJSONObject(0).getString("id");
		String prevAlertPrefView = readAlertConfigurationResponseJSON.getJSONArray("customerviewalertconfiguration")
				.getJSONObject(0).getString("alertPreferenceView");
		legalEntityId = readAlertConfigurationResponseJSON.getJSONArray("customerviewalertconfiguration").getJSONObject(0)
						.getString("companyLegalUnit");
		if (StringUtils.isBlank(alertconfigurationId)) {
			alert.prepareError("Could not find alert configuration").log();
			ErrorCodeEnum.ERR_20920.setErrorCode(operationResult);
			return operationResult;
		}
		inputMap.clear();
		String timestamp = CommonUtilities.getISOFormattedLocalTimestamp();
		inputMap.put("lastmodifiedts", timestamp);
		inputMap.put("id", alertconfigurationId);
		inputMap.put("alertPreferenceView", alertPreferenceView);
		inputMap.put("enableFrequency", enableFrequency);
		inputMap.put("enableSeparateContact", enableSeparateContact);
		inputMap.put("companyLegalUnit", legalEntityId);
		String updateConfigurationResponse = Executor
				.invokeService(ServiceURLEnum.CUSTOMERVIEWALERTCONFIGURATION_UPDATE, inputMap, null, requestInstance);
		// Check Operation Response
		JSONObject alertConfigurationResponseJSON = CommonUtilities.getStringAsJSONObject(updateConfigurationResponse);
		if (alertConfigurationResponseJSON != null && alertConfigurationResponseJSON.has(FabricConstants.OPSTATUS)
				&& alertConfigurationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
			diagnostic.prepareDebug("Successful CRUD Operation").log();
			operationResult.addParam(new Param("status", "Success", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.UPDATE,
					ActivityStatusEnum.SUCCESSFUL, "Update Alert configurations successful: id" + alertconfigurationId);
		} else {
			alert.prepareError("Failed CRUD Operation.Response:" + alertConfigurationResponseJSON).log();
			operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Update Alerts configurations falied: id" + alertconfigurationId);
			ErrorCodeEnum.ERR_20944.setErrorCode(operationResult);
		}
		if (!prevAlertPrefView.equalsIgnoreCase(alertPreferenceView)) {
			inputMap.clear();
			String resetCustomerAlertPreferencesResponse = Executor.invokeService(
					ServiceURLEnum.CUSTOMER_ALERT_PREFERENCES_RESET_PROC, inputMap, null, requestInstance);
			JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(resetCustomerAlertPreferencesResponse);
			if (responseJSON != null && responseJSON.has(FabricConstants.OPSTATUS)
					&& responseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				// need to add scheduler here as module name
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.DELETE,
						ActivityStatusEnum.SUCCESSFUL,
						"Alert preferences reset successful : alertPreferenceView" + alertPreferenceView);
			} else {
				// need to add scheduler here as module name
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED,
						"Alert preferences reset failed : alertPreferenceView" + alertPreferenceView);
				throw new ApplicationException(ErrorCodeEnum.ERR_20640);
			}
		}
		return operationResult;

	}

}