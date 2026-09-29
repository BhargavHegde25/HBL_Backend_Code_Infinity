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
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
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
 * Service to Manage Business Configuration
 *
 * @author Rishi Gupta
 *
 */
public class BusinessConfigurationManageService implements JavaService2 {

	private static final String GET_CORE_TYPE_INFORMATION = "getCoreTypeInformation";
	private static final String GET_BUSINESS_CONFIGURATIONS = "getBusinessConfigurations";
	private static final String EDIT_BUSINESS_CONFIGURATION = "editBusinessConfiguration";
	private static final String APPLICATION_ATTRIBUTE = "isAccountCentricCore";
	private static final String CONFIGURATION_ID_PARAM = "id";
	private static final String CONFIGURATION_VALUE_PARAM = "value";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		try {
			String loggedInUser = StringUtils.EMPTY;
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			if (userDetailsBeanInstance != null) {
				loggedInUser = userDetailsBeanInstance.getId();
			}
			if (StringUtils.equalsIgnoreCase(methodID, GET_CORE_TYPE_INFORMATION)) {
				return getCoreTypeInformation(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_BUSINESS_CONFIGURATIONS)) {
				return getBusinessConfigurations(requestInstance, loggedInUser);
			} else if (StringUtils.equalsIgnoreCase(methodID, EDIT_BUSINESS_CONFIGURATION)) {
				return editBusinessConfiguration(requestInstance, loggedInUser);
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

	private String getAttributeFromApplicationTable(String attribute, DataControllerRequest requestInstance)
			throws ApplicationException {

		Map<String, String> inputMap = new HashMap<>();

		String readResponse = Executor.invokeService(ServiceURLEnum.APPLICATION_READ, inputMap, null, requestInstance);
		JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readResponse);

		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0 || !responseJSON.has("application")
				|| responseJSON.getJSONArray("application").length() == 0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20890);
		}
		String attributeValue = responseJSON.getJSONArray("application").getJSONObject(0).getString(attribute);
		if (StringUtils.isBlank(attributeValue)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20890);
		}
		return attributeValue;
	}

	/**
	 * Method to read core type information
	 * 
	 * @param requestInstance
	 * @return operation Result
	 */
	private Result getCoreTypeInformation(DataControllerRequest requestInstance) {

		Result operationResult = new Result();
		try {

			// Validate Inputs
			operationResult.addParam(new Param("isAccountCentricCore",
					getAttributeFromApplicationTable(APPLICATION_ATTRIBUTE, requestInstance), FabricConstants.BOOLEAN));
			operationResult.addParam(new Param("status", "Success", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSCONFIGURATION, EventEnum.SEARCH,
					ActivityStatusEnum.SUCCESSFUL, "Get core type information passed");
		} catch (Exception e) {
			operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
			alert.prepareError("Exception in editing eligibility criteria.", e).log();
			ErrorCodeEnum.ERR_20944.setErrorCode(operationResult);
		}
		return operationResult;
	}

	/**
	 * Method to get the business configurations
	 * 
	 * @param requestInstance
	 * @return operation Result
	 */
	private Result getBusinessConfigurations(DataControllerRequest requestInstance, String loggedInUser) {

		Result operationResult = new Result();
		try {

			// Edit Eligibility Criteria
			Map<String, String> inputMap = new HashMap<String, String>();
			// Check Operation Response

			String businessConfigurationResponse = Executor.invokeService(ServiceURLEnum.BUSINESSCONFIGURATION_READ,
					inputMap, null, requestInstance);
			JSONObject readBusinessConfigurationResponseJSON = CommonUtilities
					.getStringAsJSONObject(businessConfigurationResponse);
			if (readBusinessConfigurationResponseJSON == null
					|| !readBusinessConfigurationResponseJSON.has(FabricConstants.OPSTATUS)
					|| readBusinessConfigurationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
					|| !readBusinessConfigurationResponseJSON.has("businessconfiguration")) {
				alert.prepareError("Failed CRUD Operation.Response:" + readBusinessConfigurationResponseJSON).log();
				operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSCONFIGURATION, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED, "Get Business configurations falied");
				ErrorCodeEnum.ERR_20944.setErrorCode(operationResult);
			}
			JSONArray readBusinessConfigurationResponseJSONArray = readBusinessConfigurationResponseJSON
					.optJSONArray("businessconfiguration");

			diagnostic.prepareDebug("Successful CRUD Operation").log();
			JSONObject currQuestionJSONObject;
			Dataset businessConfDataSet = new Dataset();
			businessConfDataSet.setId("BusinessConfigurationRecords");
			for (int indexVar = 0; indexVar < readBusinessConfigurationResponseJSONArray.length(); indexVar++) {
				currQuestionJSONObject = readBusinessConfigurationResponseJSONArray.getJSONObject(indexVar);
				Record currRecord = new Record();
				for (String currKey : currQuestionJSONObject.keySet()) {
					currRecord.addParam(
							new Param(currKey, currQuestionJSONObject.optString(currKey), FabricConstants.STRING));
				}
				businessConfDataSet.addRecord(currRecord);
			}
			operationResult.addDataset(businessConfDataSet);
			operationResult.addParam(new Param("status", "Success", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSCONFIGURATION, EventEnum.SEARCH,
					ActivityStatusEnum.SUCCESSFUL, "Get Business configurations successful");

		} catch (Exception e) {
			operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
			alert.prepareError("Exception in getting business configuraitons.", e).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSCONFIGURATION, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Get Business configuration failed");
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
	private Result editBusinessConfiguration(DataControllerRequest requestInstance, String loggedInUser)
			throws ApplicationException {

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20908);
		}

		Result operationResult = new Result();

		// Read Inputs
		String configurationKey = requestInstance.getParameter(CONFIGURATION_ID_PARAM);
		String configurationValue = requestInstance.getParameter(CONFIGURATION_VALUE_PARAM);

		// Validate Inputs
		if (StringUtils.isBlank(configurationKey)) {
			alert.prepareError("Missing Mandatory Input: Configuration key").log();
			ErrorCodeEnum.ERR_20920.setErrorCode(operationResult);
			return operationResult;
		}
		if (StringUtils.isBlank(configurationValue)) {
			alert.prepareError("Missing Mandatory Input: Configuration value").log();
			ErrorCodeEnum.ERR_20920.setErrorCode(operationResult);
			return operationResult;
		}
		// Set Input Map
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, "key eq " + configurationKey);
		String businessConfigurationResponse = Executor.invokeService(ServiceURLEnum.BUSINESSCONFIGURATION_READ, inputMap, null, requestInstance);
		JSONObject readBusinessConfigurationResponseJSON = CommonUtilities.getStringAsJSONObject(businessConfigurationResponse);
		if (readBusinessConfigurationResponseJSON == null
				|| !readBusinessConfigurationResponseJSON.has(FabricConstants.OPSTATUS)
				|| readBusinessConfigurationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readBusinessConfigurationResponseJSON.has("businessconfiguration")) {
			alert.prepareError("Failed CRUD Operation.Response:" + readBusinessConfigurationResponseJSON).log();
			operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSCONFIGURATION, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Get Business configurations falied");
			ErrorCodeEnum.ERR_20944.setErrorCode(operationResult);
		}
		String businessconfigurationId = readBusinessConfigurationResponseJSON.getJSONArray("businessconfiguration").getJSONObject(0).getString("id");
		
		if(StringUtils.isBlank(businessconfigurationId))
		{			
			alert.prepareError("Could not find business configuration").log();
			ErrorCodeEnum.ERR_20920.setErrorCode(operationResult);
			return operationResult;
		}
		inputMap.clear();
		String timestamp = CommonUtilities.getISOFormattedLocalTimestamp();
		inputMap.put("lastmodifiedts", timestamp);
		inputMap.put("modifiedby", loggedInUser);
		inputMap.put("id", businessconfigurationId);
		inputMap.put("key", configurationKey);
		inputMap.put("value", configurationValue);
		String updateConfigurationResponse = Executor.invokeService(ServiceURLEnum.BUSINESSCONFIGURATION_UPDATE,
				inputMap, null, requestInstance);
		// Check Operation Response
		JSONObject businessConfigurationResponseJSON = CommonUtilities
				.getStringAsJSONObject(updateConfigurationResponse);
		if (businessConfigurationResponseJSON != null && businessConfigurationResponseJSON.has(FabricConstants.OPSTATUS)
				&& businessConfigurationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
			diagnostic.prepareDebug("Successful CRUD Operation").log();
			operationResult.addParam(new Param("status", "Success", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSCONFIGURATION, EventEnum.UPDATE,
					ActivityStatusEnum.SUCCESSFUL, "Update Business configurations successful: key" + configurationKey);
		} else {
			alert.prepareError("Failed CRUD Operation.Response:" + businessConfigurationResponseJSON).log();
			operationResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.BUSINESSCONFIGURATION, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Update Business configurations falied: key" + configurationKey);
			ErrorCodeEnum.ERR_20944.setErrorCode(operationResult);
		}

		return operationResult;

	}

}