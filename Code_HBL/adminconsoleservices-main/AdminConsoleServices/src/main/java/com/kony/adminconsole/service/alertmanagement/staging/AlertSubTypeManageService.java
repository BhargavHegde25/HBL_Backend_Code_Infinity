package com.kony.adminconsole.service.alertmanagement.staging;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.alertmanagement.staging.util.Channel;
import com.kony.adminconsole.service.alertmanagement.utils.Constants;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.ServiceUtil;
import com.kony.adminconsole.utilities.StatusEnum;
import com.kony.adminconsole.utilities.TABLE_OPERATION_MAPPING;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to create/edit the Alert Sub Type Details
 *
 * @author Aditya Mankal
 */
public class AlertSubTypeManageService implements JavaService2 {

	private static final String SUB_ALERT_NAME_PARAM = "name";
	private static final String SUB_ALERT_CODE_PARAM = "code";
	private static final String ALERT_TYPE_CODE = "alertTypeCode";
	private static final String STATUS_ID_PARAM = "statusId";

	private static final String ADDED_TEMPLATES_PARAM = "addedTemplates";
	private static final String REMOVED_TEMPLATES_PARAM = "removedTemplates";

	private static final int SUB_ALERT_NAME_MIN_LENGTH = 5;
	private static final int SUB_ALERT_NAME_MAX_LENGTH = 50;

	private static final String CREATE_ALERT_SUB_TYPE_METHOD_ID = "createAlertSubType";
	private static final String EDIT_ALERT_SUB_TYPE_METHOD_ID = "editAlertSubType";
	private static final String UPDATE_ALERT_SUB_TYPE_STATUS = "updateAlertSubTypeStatus";
	private static final String IS_DEFAULT_SUBSCRIBED_PARAM = "isAutoSubscribeEnabled";
	private static final String EXTERNAL_SYSTEM_PARAM ="externalsystem";
	
	private static final String IS_GLOBAL_ALERT_PARAM = "isGlobalAlert";
	

	private static final String IS_ACCOUNT_LEVEL_PARAM = "isAccountLevel";
	private static final String CHANNELS_PARAM = "channels";
	private static final String FREQUENCY_PARAM = "frequency";

	private static final String ATTRIBUTE_ID_PARAM = "attributeId";
	private static final String CONDITION_ID_PARAM = "conditionId";
	private static final String VALUE_1_PARAM = "value1";
	private static final String VALUE_2_PARAM = "value2";
	private static final String APP_PREFERENCES_PARAM = "appPreferences";
	private static final String RECIPIENT_TYPE_PREFERENCE_PARAM = "recipientTypePreference";
	private static final String USER_TYPE_PREFERENCES_PARAM = "userTypePreferences";
	private static final String ACCOUNT_TYPE_PREFERENCES_PARAM = "accountTypePreference";
	private static final String ADDED_DISPLAY_PREFERENCE_PARAM = "addedDisplayPreferences";
	private static final String REMOVED_DISPLAY_PREFERENCE_PARAM = "removedDisplayPreferences";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		Result processedResult = new Result();

		EventEnum eventEnum = EventEnum.UPDATE;
		String activityMessage = StringUtils.EMPTY;

		try {

			diagnostic.prepareDebug("Method Id:" + methodID).log();
			activityMessage = "Alert SubType Id:" + requestInstance.getParameter(SUB_ALERT_CODE_PARAM);

			// Fetch Logged In User Info
			String loggedInUser = StringUtils.EMPTY;
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);

			if (userDetailsBeanInstance != null) {
				loggedInUser = userDetailsBeanInstance.getId();
			}

			if (StringUtils.equals(methodID, CREATE_ALERT_SUB_TYPE_METHOD_ID)) {
				processedResult = setAlertSubTypeDefintion(requestInstance, true, loggedInUser);
				eventEnum = EventEnum.CREATE;
			} else if (StringUtils.equals(methodID, EDIT_ALERT_SUB_TYPE_METHOD_ID)) {
				processedResult = setAlertSubTypeDefintion(requestInstance, false, loggedInUser);
				eventEnum = EventEnum.UPDATE;

			} else if (StringUtils.equals(methodID, UPDATE_ALERT_SUB_TYPE_STATUS)) {
				processedResult = setAlertSubTypeStatus(requestInstance, false, loggedInUser);
				eventEnum = EventEnum.UPDATE;

			} else {
				// Unsupported Method Id. Return Empty Result
				diagnostic.prepareDebug("Unsupported Method Id. Returning Empty Result").log();
				return new Result();
			}

			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, eventEnum,
					ActivityStatusEnum.SUCCESSFUL, activityMessage);

			return processedResult;

		} catch (ApplicationException e) {
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();

			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, eventEnum,
					ActivityStatusEnum.FAILED, activityMessage);

			Result errorResult = new Result();
			// errorResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			errorResult.addParam(new Param(ACConstants.DBP_ERROR_CODE, e.getErrorCodeEnum().getErrorCodeAsString(),
					FabricConstants.INT));
			errorResult.addParam(new Param(ACConstants.DBP_ERROR_MESSAGE,
					e.getErrorCodeEnum().getMessage() + "" + e.getMessage(), FabricConstants.STRING));
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			alert.prepareError("Unexpected Exception.Exception Trace:", e).log();

			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, eventEnum,
					ActivityStatusEnum.FAILED, activityMessage);

			Result errorResult = new Result();
			errorResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20926.setErrorCode(errorResult);
			return errorResult;
		}
	}

	private Result setAlertSubTypeStatus(DataControllerRequest requestInstance, boolean b, String loggedInUser) throws Exception {
		Result operationResult = new Result();
		String code = requestInstance.getParameter(SUB_ALERT_CODE_PARAM);
		diagnostic.prepareDebug("Recieved Sub Alert Code:" + code).log();
		
		if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
			alert.prepareError("legalEntityId cannot be empty").log();
			ErrorCodeEnum.ERR_22232.setErrorCode(operationResult);
			return operationResult;
		}
		
		String[] reqPermissions = {""};
		reqPermissions[0] = PermissionName.UPDATE_ALERTS;

		if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
        {
			operationResult.addParam(new Param("Status", "Update alert sub type status operation failed", FabricConstants.STRING));
            ErrorCodeEnum.ERR_22231.setErrorCode(operationResult);
            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
            return operationResult;        
        }
		
		// Alert Code is a mandatory input for both create/update requests. Alert Code
		// is chosen as a dropdown from the client
		if (StringUtils.isBlank(code)) {
			alert.prepareError("Invalid Input: SubAlertCode").log();
			operationResult.addParam(new Param("message", "Sub Alert Code cannot be empty"));
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20900.setErrorCode(operationResult);
			return operationResult;
		}

		String alertTypeCode = requestInstance.getParameter(ALERT_TYPE_CODE);
		diagnostic.prepareDebug("Recieved Alert Type Code:" + alertTypeCode).log();
		if (StringUtils.isBlank(alertTypeCode)) {
			alert.prepareError("Invalid Input: AlertTypeCode").log();
			operationResult.addParam(new Param("message", "Alert Type Code cannot be empty"));
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20900.setErrorCode(operationResult);
			return operationResult;
		}

		String statusId = requestInstance.getParameter(STATUS_ID_PARAM);
		diagnostic.prepareDebug("Recieved Status Id:" + statusId).log();
		// Status Id
		if (StringUtils.isBlank(statusId)
				|| (StringUtils.isNotBlank(statusId) && !StatusEnum.isValidStatusCode(statusId))) {
			alert.prepareError("Invalid Input: statusId").log();
			operationResult.addParam(new Param("message", "Invalid Status Id"));
			ErrorCodeEnum.ERR_20900.setErrorCode(operationResult);
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			return operationResult;
		}

		diagnostic.prepareDebug("Recieved Status Id:" + statusId).log();
		
		// Legal Entity Id
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
		
		// Prepare Input Map
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("id", code);
		inputMap.put("AlertTypeId", alertTypeCode);
		inputMap.put("Status_id", statusId);
		inputMap.put("modifiedby", loggedInUser);
		inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
		inputMap.put("companyLegalUnit", legalEntityId);
		

		String operationResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPE_UPDATE, inputMap, null,
				requestInstance);
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
				|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation").log();
			operationResult.addParam(new Param("message", operationResponse, FabricConstants.STRING));
			ErrorCodeEnum.ERR_20900.setErrorCode(operationResult);
			return operationResult;
		}

		AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.UPDATE,
				ActivityStatusEnum.SUCCESSFUL, "AlertSubType Update Success");
		operationResult.addParam(new Param("status", "success", FabricConstants.STRING));
		return operationResult;
	}

	/**
	 * Method to create/update an Alert Sub Type
	 * 
	 * @param requestInstance
	 * @param isCreateMode
	 * @param loggedInUser
	 * @return operation Result
	 * @throws ApplicationException
	 * @throws UnsupportedEncodingException
	 */
	private Result setAlertSubTypeDefintion(DataControllerRequest requestInstance, boolean isCreateMode,
			String loggedInUser) throws ApplicationException, UnsupportedEncodingException, Exception {
		Result operationResult = new Result();
		
		if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
			alert.prepareError("legalEntityId cannot be empty").log();
			ErrorCodeEnum.ERR_22232.setErrorCode(operationResult);
			return operationResult;
		}
		
		String[] reqPermissions = {""};
		
		if(isCreateMode == true)
			reqPermissions[0] = PermissionName.CREATE_ALERTS;
		else
			reqPermissions[0] = PermissionName.UPDATE_ALERTS;

		if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
        {
			operationResult.addParam(new Param("Status", "Create/Update alert sub type operation failed", FabricConstants.STRING));
            ErrorCodeEnum.ERR_22231.setErrorCode(operationResult);
            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
            return operationResult;        
        }

		String code = requestInstance.getParameter(SUB_ALERT_CODE_PARAM);
		diagnostic.prepareDebug("Recieved Sub Alert Code:" + code).log();
		// Alert Code is a mandatory input for both create/update requests. Alert Code
		// is chosen as a dropdown from the client
		if (StringUtils.isBlank(code)) {
			alert.prepareError("Invalid Input: SubAlertCode").log();
			operationResult.addParam(new Param("message", "Sub Alert Code cannot be empty"));
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20900.setErrorCode(operationResult);
			return operationResult;
		}
		
		if (isCreateMode == true) {
			boolean isAlertSubTypeCodeAvaialble = AlertManagementHandler.isAlertSubTypeCodeAvaialable(code, requestInstance);
			if (isAlertSubTypeCodeAvaialble == false) {
				alert.prepareError("Alert Sub Type with the Code :" + code + " has already been defined").log();
				ErrorCodeEnum.ERR_22215.setErrorCode(operationResult);
				operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
				return operationResult;
			}
		}

		String alertTypeCode = requestInstance.getParameter(ALERT_TYPE_CODE);
		diagnostic.prepareDebug("Recieved Alert Type Code:" + alertTypeCode).log();
		if (StringUtils.isBlank(alertTypeCode)) {
			alert.prepareError("Invalid Input: AlertTypeCode").log();
			operationResult.addParam(new Param("message", "Alert Type Code cannot be empty"));
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20900.setErrorCode(operationResult);
			return operationResult;
		}
		
		// Checking if the alert group is present for the legal entity
		if (AlertManagementHandler.isAlertCodeAvaialable(alertTypeCode, requestInstance)) {
			alert.prepareError("No Alert Group present with this name for the legal entity").log();
			ErrorCodeEnum.ERR_22235.setErrorCode(operationResult);
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			return operationResult;
		}

		String name = requestInstance.getParameter(SUB_ALERT_NAME_PARAM);
		name = StringEscapeUtils.escapeHtml(name);
		diagnostic.prepareDebug("Recieved Sub Alert Name:" + name).log();
		if (isCreateMode == true || StringUtils.isNotBlank(name)) {
			if (StringUtils.length(name) < SUB_ALERT_NAME_MIN_LENGTH
					|| StringUtils.length(name) > SUB_ALERT_NAME_MAX_LENGTH) {
				alert.prepareError("Invalid Input: SubAlertName").log();
				operationResult.addParam(new Param("message",
						"Sub Alert Name should have a minimum of " + SUB_ALERT_NAME_MIN_LENGTH
						+ " characters and a maximum of " + SUB_ALERT_NAME_MAX_LENGTH + " characters",
						FabricConstants.STRING));
				operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20900.setErrorCode(operationResult);
				return operationResult;
			}
		}

		String statusId = requestInstance.getParameter(STATUS_ID_PARAM);
		diagnostic.prepareDebug("Recieved Status Id:" + statusId).log();
		// Status Id
		if (isCreateMode == true || (StringUtils.isNotBlank(statusId))) {
			if (!StatusEnum.isValidStatusCode(statusId)) {
				alert.prepareError("Invalid Input: statusId").log();
				operationResult.addParam(new Param("message", "Invalid Status Id"));
				ErrorCodeEnum.ERR_20900.setErrorCode(operationResult);
				operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
				return operationResult;
			}
		}
		// Legal Entity Id
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);

		// Condition Id
		String conditionId = requestInstance.getParameter(CONDITION_ID_PARAM);

		// Attribute Id
		String attributeId = requestInstance.getParameter(ATTRIBUTE_ID_PARAM);

		// Value 1
		String value1 = requestInstance.getParameter(VALUE_1_PARAM);

		// Value 2
		String value2 = requestInstance.getParameter(VALUE_2_PARAM);
		String freqId = StringUtils.EMPTY, freqValue = StringUtils.EMPTY, freqTime = StringUtils.EMPTY;
		String frequency = requestInstance.getParameter(FREQUENCY_PARAM);
		// Validate Inputs
		JSONObject frequencyJSON = CommonUtilities.getStringAsJSONObject(frequency);
		if (frequencyJSON != null) {
			if (frequencyJSON.has("id")) {
				freqId = frequencyJSON.optString("id");
			}
			if (frequencyJSON.has("value")) {
				freqValue = frequencyJSON.optString("value");
			}
			if (frequencyJSON.has("time")) {
				freqTime = frequencyJSON.optString("time");
			}
			if (!AlertManagementHandler.validateFrequencyFormat(freqId, freqValue, freqTime)) {
				alert.prepareError("Frequency format is not proper").log();
				ErrorCodeEnum.ERR_21867.setErrorCode(operationResult);
				return operationResult;
			}
		}
		String defaultSubscribedStr =  requestInstance.getParameter(IS_DEFAULT_SUBSCRIBED_PARAM);
		String externalSystem =   requestInstance.getParameter(EXTERNAL_SYSTEM_PARAM);
		String isGlobalAlertStr = requestInstance.getParameter(IS_GLOBAL_ALERT_PARAM);
		// Is Global Alert Flag
		if (isCreateMode == true || (StringUtils.isNotBlank(isGlobalAlertStr))) {
			if (!(StringUtils.equalsIgnoreCase(isGlobalAlertStr, "TRUE")
					|| StringUtils.equalsIgnoreCase(isGlobalAlertStr, "FALSE"))) {
				alert.prepareError("Invalid Input: isGlobalAlert").log();
				operationResult.addParam(new Param("message", "Invalid IsGlobalAlert Flag"));
				ErrorCodeEnum.ERR_20901.setErrorCode(operationResult);
				operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
				return operationResult;
			}
		}
		// Is Account Level alert
		String isAccountLevelAlertStr = requestInstance.getParameter(IS_ACCOUNT_LEVEL_PARAM);
		if (isCreateMode == true || (StringUtils.isNotBlank(isAccountLevelAlertStr))) {
			if (!(StringUtils.equalsIgnoreCase(isAccountLevelAlertStr, "TRUE")
					|| StringUtils.equalsIgnoreCase(isAccountLevelAlertStr, "FALSE"))) {
				alert.prepareError("Invalid Input: isAccountLevel").log();
				operationResult.addParam(new Param("message", "Invalid IsAccountLevel Flag"));
				ErrorCodeEnum.ERR_20901.setErrorCode(operationResult);
				operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
				return operationResult;
			}
		}
		// Is Account Level alert
		String recipientPrefStr = requestInstance.getParameter(RECIPIENT_TYPE_PREFERENCE_PARAM);
		if (isCreateMode == true || (StringUtils.isNotBlank(recipientPrefStr))) {

			Result readalertRecipientRsp = getAlertRecipientTypes(requestInstance,
					StringUtils.equalsIgnoreCase(isAccountLevelAlertStr, "TRUE") ? "1" : "0");
			Dataset readDataset = readalertRecipientRsp.getDatasetById("alertrecipienttype");
			if (readDataset != null && !readDataset.getAllRecords().isEmpty()) {
				boolean isValidRecipientPref = readalertRecipientRsp.getDatasetById("alertrecipienttype")
						.getAllRecords().stream()
						.anyMatch(rec -> rec.getParamValueByName("id").equals(recipientPrefStr));
				if (!isValidRecipientPref) {
					alert.prepareError("Invalid recipient preference").log();
					operationResult.addParam(new Param("message", "Invalid recipient preference"));
					ErrorCodeEnum.ERR_20976.setErrorCode(operationResult);
					operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
					return operationResult;
				}
			}

		}
		

		// Prepare Input Map
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("id", code);
		inputMap.put("AlertTypeId", alertTypeCode);
		inputMap.put("companyLegalUnit", legalEntityId);
		if (StringUtils.isNotBlank(recipientPrefStr)) {
			inputMap.put("recipienttype", recipientPrefStr);
		}
		if (StringUtils.isNotBlank(name)) {
			inputMap.put("Name", name);
		}
		if (StringUtils.isNotBlank(statusId)) {
			inputMap.put("Status_id", statusId);
		}
		if (StringUtils.equalsIgnoreCase(isGlobalAlertStr, "true")) {
			inputMap.put("isGlobal", "1");
		} else if (StringUtils.equalsIgnoreCase(isGlobalAlertStr, "false")) {
			inputMap.put("isGlobal", "0");
		}
		
		inputMap.put("isAccountLevel" , StringUtils.equalsIgnoreCase(isAccountLevelAlertStr, "TRUE") ? "1" : "0");
		diagnostic.prepareDebug("defaultSubscribedStr ::"+defaultSubscribedStr).log();
		

		if (StringUtils.isNotBlank(externalSystem)) {
			inputMap.put("externalSystem", externalSystem);
		}
		if (StringUtils.equalsIgnoreCase(defaultSubscribedStr, "true")) {
			inputMap.put("isAutoSubscribeEnabled", "1");
		} else if (StringUtils.equalsIgnoreCase(defaultSubscribedStr, "false")) {
			inputMap.put("isAutoSubscribeEnabled", "0");
		}

		if (StringUtils.isNotBlank(attributeId)) {
			inputMap.put("attributeId", attributeId);
		} else {
			inputMap.put("attributeId", "NULL");
		}

		if (StringUtils.isNotBlank(conditionId)) {
			inputMap.put("alertConditionId", conditionId);
		} else {
			inputMap.put("alertConditionId", "NULL");
		}

		if (StringUtils.isNotBlank(value1)) {
			inputMap.put("value1", value1);
		} else {
			inputMap.put("value1", "NULL");
		}

		if (StringUtils.isNotBlank(value2)) {
			inputMap.put("value2", value2);
		} else {
			inputMap.put("value2", "NULL");
		}

		if (StringUtils.isNotBlank(freqId)) {
			inputMap.put("defaultFrequencyId", freqId);
		} else {
			inputMap.put("defaultFrequencyId", "NULL");
		}

		if (StringUtils.isNotBlank(freqValue)) {
			inputMap.put("defaultFrequencyValue", freqValue);
		} else {
			inputMap.put("defaultFrequencyValue", "NULL");
		}
		if (StringUtils.isNotBlank(freqTime)) {
			inputMap.put("defaultFrequencyTime", freqTime);
		} else {
			inputMap.put("defaultFrequencyTime", "NULL");
		}

		// Create/Update Alert Sub Type
		String operationResponse;
		if (isCreateMode == true) {
			inputMap.put("createdby", loggedInUser);
			inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
			operationResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPE_CREATE, inputMap, null,
					requestInstance);
		} else {
			inputMap.put("modifiedby", loggedInUser);
			inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

			Result readalertRes = getAlertRecord(requestInstance, code, legalEntityId);

			operationResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPE_UPDATE, inputMap, null,
					requestInstance);

			Dataset readDataset = readalertRes.getDatasetById("alertsubtype");
			if (readDataset != null && !readDataset.getAllRecords().isEmpty()) {
				if (readDataset.getRecord(0).getParamValueByName("defaultFrequencyId") != null
						&& inputMap.get("defaultFrequencyId").equalsIgnoreCase("NULL")) {
					AlertManagementHandler.syncCustomerAlertFrequencies(requestInstance, "edit", code, legalEntityId);
				}

				if (readDataset.getRecord(0).getParamValueByName("alertConditionId") != null
						&& !inputMap.get("alertConditionId").equals(readDataset.getRecord(0).getParamValueByName("alertConditionId"))) {
					AlertManagementHandler.syncCustomerAlertEntitlements(requestInstance, "edit", code, legalEntityId);
				}

			}
		}

		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
				|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation").log();
			operationResult.addParam(new Param("message", operationResponse, FabricConstants.STRING));
			ErrorCodeEnum.ERR_20900.setErrorCode(operationResult);
			return operationResult;
		}

		String addedTemplatesStr = requestInstance.getParameter(ADDED_TEMPLATES_PARAM);
		String removedTemplatesStr = requestInstance.getParameter(REMOVED_TEMPLATES_PARAM);

		if (StringUtils.isNotBlank(removedTemplatesStr) || StringUtils.isNotBlank(addedTemplatesStr)) {
			JSONArray addedTemplates = CommonUtilities.getStringAsJSONArray(addedTemplatesStr);
			JSONArray removedTemplates = CommonUtilities.getStringAsJSONArray(removedTemplatesStr);
			// Set Alert Sub Type Communication Templates
			setAlertSubTypeCommunicationTemplates(requestInstance, code, addedTemplates, removedTemplates,
					loggedInUser);
		}
		// Edit Alert Type App Preferences
		if (StringUtils.isNotBlank(requestInstance.getParameter(APP_PREFERENCES_PARAM))) {
			setAlertAssociations(APP_PREFERENCES_PARAM, requestInstance, operationResult, code, loggedInUser,
					"appTypeSettings", TABLE_OPERATION_MAPPING.ALERTSUBTYPEAPP, legalEntityId);
		}

		if (StringUtils.isNotBlank(requestInstance.getParameter(ACCOUNT_TYPE_PREFERENCES_PARAM))) {
			setAlertAssociations(ACCOUNT_TYPE_PREFERENCES_PARAM, requestInstance, operationResult, code, loggedInUser,
					"accountSettings", TABLE_OPERATION_MAPPING.ALERTSUBTYPEACCOUNTTYPE, legalEntityId);
		}

		if (StringUtils.isNotBlank(requestInstance.getParameter(USER_TYPE_PREFERENCES_PARAM))) {
			setAlertAssociations(USER_TYPE_PREFERENCES_PARAM, requestInstance, operationResult, code, loggedInUser,
					"userTypes", TABLE_OPERATION_MAPPING.ALERTSUBTYPECUSTOMERTYPE, legalEntityId);
		}

		if (StringUtils.isNotBlank(CHANNELS_PARAM)) {
			setAlertAssociations(CHANNELS_PARAM, requestInstance, operationResult, code, loggedInUser, "channelList",
					TABLE_OPERATION_MAPPING.ALERTSUBTYPECHANNEL, legalEntityId);
		}

		// Edit Alert Type Display Preferences
		String addedDisplayPreferencesStr = requestInstance.getParameter(ADDED_DISPLAY_PREFERENCE_PARAM);
		String removedDisplayPreferencesStr = requestInstance.getParameter(REMOVED_DISPLAY_PREFERENCE_PARAM);
		if (StringUtils.isNotBlank(addedDisplayPreferencesStr)
				|| StringUtils.isNotBlank(removedDisplayPreferencesStr)) {
			Record displayPreferencesRecord = setAlertTypeDisplayPreferences(code, addedDisplayPreferencesStr,
					removedDisplayPreferencesStr, loggedInUser, requestInstance, legalEntityId);
			operationResult.addRecord(displayPreferencesRecord);
		}

		AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.UPDATE,
				ActivityStatusEnum.SUCCESSFUL, "AlertSubType Update Success");
		operationResult.addParam(new Param("status", "success", FabricConstants.STRING));
		operationResult.addParam(new Param("legalEntityId", legalEntityId, FabricConstants.STRING));

		return operationResult;
	}

	public Result getAlertRecord(DataControllerRequest requestInstance, String code, String legalEntityId) throws ApplicationException {
		Map<String, Object> readInputMap = new HashMap<>();
		String filterQuery = "id eq '" + code + "' and companyLegalUnit eq '" + legalEntityId + "'";
		readInputMap.put(ODataQueryConstants.FILTER, filterQuery);
		Result readalertRes = ServiceUtil.invokeService(ServiceURLEnum.ALERTSUBTYPE_READ, readInputMap, null,
				requestInstance);
		AlertManagementHandler.isOperationSuccessful(readalertRes, ServiceURLEnum.ALERTSUBTYPE_READ,
				ErrorCodeEnum.ERR_20946);
		return readalertRes;
	}

	public Result getAlertRecipientTypes(DataControllerRequest requestInstance, String isAccountLevel)
			throws ApplicationException {
		Map<String, Object> readInputMap = new HashMap<>();
		if (StringUtils.equals(isAccountLevel, "0")) {
			readInputMap.put(ODataQueryConstants.FILTER, "isaccountlevel eq '0'");
		}
		readInputMap.put(ODataQueryConstants.SELECT, "id");
		Result readalertRes = ServiceUtil.invokeService(ServiceURLEnum.ALERTRECIPIENTTYPE_READ, readInputMap, null,
				requestInstance);
		AlertManagementHandler.isOperationSuccessful(readalertRes, ServiceURLEnum.ALERTRECIPIENTTYPE_READ,
				ErrorCodeEnum.ERR_20975);
		return readalertRes;
	}

	private Record setAlertAssociations(String inputName, DataControllerRequest requestInstance, Result operationResult,
			String alertSubTypeId, String loggedInUserId, String responsename, TABLE_OPERATION_MAPPING tableOpMapping, String legalEntityId)
					throws ApplicationException {
		diagnostic.prepareDebug("input apppreference is " + requestInstance.getParameter(inputName)).log();
		List<String> supportedAppsList = new ArrayList<>();
		List<String> unSupportedAppsList = new ArrayList<>();
		getSuppAndUnSuppList(requestInstance.getParameter(inputName), supportedAppsList, unSupportedAppsList);
		diagnostic.prepareDebug("Setting " + inputName + " Preferences...").log();
		Record respRecord = excuteAlertAssociations(alertSubTypeId, supportedAppsList, unSupportedAppsList,
				loggedInUserId, requestInstance, responsename, tableOpMapping, legalEntityId);
		operationResult.addRecord(respRecord);
		diagnostic.prepareDebug("AlertSubType " + inputName + " Set").log();
		return respRecord;
	}

	private Result invokeGetService(DataControllerRequest requestInstance, ServiceURLEnum serviceName, String filterStr,
			String selectStr, String tableName, ErrorCodeEnum errorcode) throws ApplicationException {
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, filterStr);
		if (selectStr != null) {
			inputMap.put(ODataQueryConstants.SELECT, selectStr);
		}
		Result alertsubTypeResult = ServiceUtil.invokeService(serviceName, inputMap, null, requestInstance);
		AlertManagementHandler.isOperationSuccessful(alertsubTypeResult, serviceName, tableName, errorcode);
		return alertsubTypeResult;
	}

	private Record excuteAlertAssociations(String alertId, List<String> supportedList, List<String> unSupportedList,
			String loggedInUserId, DataControllerRequest requestInstance, String resDatasetId,
			TABLE_OPERATION_MAPPING tableMapping, String legalEntityId) throws ApplicationException {
		Record setAlertTypeAppRecord = new Record();
		setAlertTypeAppRecord.setId(resDatasetId);

		// Fetch Existing Association
		Result getResult = invokeGetService(requestInstance, tableMapping.getReadServiceName(),
				"alertSubTypeId eq '" + alertId + "' and companyLegalUnit eq '" + legalEntityId + "'", tableMapping.getCompositePKid(), tableMapping.getTableName(),
				ErrorCodeEnum.ERR_20929);

		Set<String> associatedSet = getResult.getDatasetById(tableMapping.getTableName()).getAllRecords().stream()
				.filter(rec -> rec.getParamValueByName(tableMapping.getCompositePKid()) != null)
				.map(rec -> rec.getParamValueByName(tableMapping.getCompositePKid())).collect(Collectors.toSet());

		// Handle Supported associations
		if (supportedList != null && !supportedList.isEmpty()) {
			upsertAlertAssociation(supportedList, alertId, requestInstance, loggedInUserId, setAlertTypeAppRecord,
					associatedSet, "create", tableMapping, legalEntityId);
		}
		// Handle Removed associations
		if (unSupportedList != null && !unSupportedList.isEmpty()) {
			List<Channel> channelList = null;
			if (TABLE_OPERATION_MAPPING.ALERTSUBTYPECHANNEL.equals(tableMapping)) {
				channelList = unSupportedList.stream().filter(item -> associatedSet.contains(item))
						.map(item2 -> new Channel(item2, false)).collect(Collectors.toList());
			}
			upsertAlertAssociation(unSupportedList, alertId, requestInstance, loggedInUserId, setAlertTypeAppRecord,
					associatedSet, "delete", tableMapping, legalEntityId);
			if (TABLE_OPERATION_MAPPING.ALERTSUBTYPECHANNEL.equals(tableMapping) && channelList != null
					&& !channelList.isEmpty()) {
				AlertManagementHandler.syncCustomerAlertChannels(requestInstance, "edit", alertId, null,
						channelList.stream().map(Channel::getId).collect(Collectors.joining(",")),
						ACConstants.ALERTPREFERNCES.ALERT.name());
			}
		}
		return setAlertTypeAppRecord;
	}

	private void upsertAlertAssociation(List<String> associatedList, String alertId,
			DataControllerRequest requestInstance, String loggedInUserId, Record responseRecord,
			Set<String> associatedSet, String operationType, TABLE_OPERATION_MAPPING tableMapping, String legalEntityId)
					throws ApplicationException {
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(tableMapping.getAlertpkId1(), alertId);
		if (operationType.equals("create")) {
			inputMap.put("createdby", loggedInUserId);
		}
		inputMap.put("companyLegalUnit", legalEntityId);
		ServiceURLEnum sname = operationType.equals("create") ? tableMapping.getCreateServiceName()
				: tableMapping.getDeleteServiceName();
		for (String alertAssociatedId : associatedList) {
			if ((operationType.equals("create") && !associatedSet.contains(alertAssociatedId))
					|| (operationType.equals("delete") && associatedSet.contains(alertAssociatedId))) {
				diagnostic.prepareDebug("Adding Support for " + tableMapping.getCompositePKid() + alertAssociatedId).log();
				inputMap.put(tableMapping.getCompositePKid(), alertAssociatedId);
				if (operationType.equals("create")) {
					inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
				}
				Result responseResult = ServiceUtil.invokeService(sname, inputMap, null, requestInstance);
				AlertManagementHandler.isOperationSuccessful(responseResult, sname, tableMapping.getTableName(),
						ErrorCodeEnum.ERR_20966);
				if (operationType.equals("create")) {
					associatedSet.add(alertAssociatedId);
				} else {
					associatedSet.remove(alertAssociatedId);
				}
			}
			responseRecord.addParam(new Param(alertAssociatedId, String.valueOf(operationType.equals("create")),
					FabricConstants.STRING));
		}
	}

	private void getSuppAndUnSuppList(String jsonStr, List<String> supportedList, List<String> unSuppportedList) {
		JSONObject jsonObject = CommonUtilities.getStringAsJSONObject(jsonStr);
		if (jsonObject != null)
			for (String key : jsonObject.keySet()) {
				if (StringUtils.equalsIgnoreCase(jsonObject.optString(key), "TRUE")) {
					supportedList.add(key);
				} else if (StringUtils.equalsIgnoreCase(jsonObject.optString(key), "FALSE")) {
					unSuppportedList.add(key);
				}
			}
	}

	/**
	 * Method to create/update/delete communication templates of an Alert Sub Type
	 * 
	 * @param requestInstance
	 * @param alertTypeCode
	 * @param addedTemplates
	 * @param removedTemplates
	 * @param loggedInUser
	 * @throws ApplicationException
	 * @throws UnsupportedEncodingException
	 */
	private void setAlertSubTypeCommunicationTemplates(DataControllerRequest requestInstance, String alertTypeCode,
			JSONArray addedTemplates, JSONArray removedTemplates, String loggedInUser)
					throws ApplicationException, UnsupportedEncodingException {

		Record operationRecord = new Record();
		operationRecord.setId("communicationTemplates");
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
		String operationResponse;
		JSONObject currJSON, operationResponseJSON;

		Map<String, String> inputMap = new HashMap<>();

		// Fetch Associated Templates
		// <Key, CommunicationTemplateId>
		Map<String, String> associatedTemplates = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, "AlertSubTypeId eq '" + alertTypeCode +
				"' and companyLegalUnit eq '" + legalEntityId + "'");
		operationResponse = Executor.invokeService(ServiceURLEnum.COMMUNICATIONTEMPLATE_READ, inputMap, null,
				requestInstance);
		operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
				|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !operationResponseJSON.has("communicationtemplate")) {
			alert.prepareError("Failed CRUD Operation").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20900);
		}
		diagnostic.prepareDebug("Successful CRUD Operation").log();
		JSONArray communicationTemplateJSONArray = operationResponseJSON.optJSONArray("communicationtemplate");

		String currTemplateId;
		StringBuffer currKeyBuffer = null;
		for (Object currObject : communicationTemplateJSONArray) {
			if (currObject instanceof JSONObject) {
				currJSON = (JSONObject) currObject;
				currKeyBuffer = new StringBuffer();

				if (StringUtils.isNotBlank(currJSON.optString("LanguageCode"))) {
					currKeyBuffer.append(currJSON.optString("LanguageCode") + "$");
				}
				if (StringUtils.isNotBlank(currJSON.optString("ChannelID"))) {
					currKeyBuffer.append(currJSON.optString("ChannelID") + "$");
				}
				if (StringUtils.isNotBlank(currJSON.optString("Status_id"))) {
					currKeyBuffer.append(currJSON.optString("Status_id"));
				}
				currTemplateId = currJSON.optString("Id");
				associatedTemplates.put(currKeyBuffer.toString(), currTemplateId);
			}
		}

		// Handle Added Templates
		if (addedTemplates != null && addedTemplates.length() > 0) {
			inputMap.clear();
			String currLocale, currStatusId, currChannelId, currContent, currName, currSubject, currSenderName,
			currSenderEmail;
			for (Object currObject : addedTemplates) {
				if (currObject instanceof JSONObject) {
					currJSON = (JSONObject) currObject;
					currKeyBuffer = new StringBuffer();

					currLocale = currJSON.optString("locale");
					currChannelId = currJSON.optString("channelId");
					currStatusId = currJSON.optString("statusId");
					currContent = currJSON.optString("content");

					if (StringUtils.isNotBlank(currLocale) && StatusEnum.isValidStatusCode(currStatusId)
							&& StringUtils.isNotBlank(currChannelId)) {

						currName = currJSON.optString("name");
						currSubject = currJSON.optString("subject");
						currSenderName = currJSON.optString("senderName");
						currSenderEmail = currJSON.optString("senderEmail");

						inputMap.clear();
						inputMap.put("AlertSubTypeId", alertTypeCode);

						if (StringUtils.isNotBlank(currName)) {
							inputMap.put("Name", currName);
						}
						if (StringUtils.isNotBlank(currLocale)) {
							inputMap.put("LanguageCode", currLocale);
						}
						if (StringUtils.isNotBlank(currChannelId)) {
							inputMap.put("ChannelID", currChannelId);
						}
						if (StringUtils.isNotBlank(currStatusId)) {
							inputMap.put("Status_id", currStatusId);
						}
						if (StringUtils.isNotBlank(currContent)) {
							inputMap.put("Text",
									CommonUtilities.encodeToBase64(CommonUtilities.encodeURI(currContent)));
							inputMap.put("rtx", "Text");
						}
						if (StringUtils.isNotBlank(currSubject)) {
							inputMap.put("Subject", currSubject);
						}
						if (StringUtils.isNotBlank(currSenderName)) {
							inputMap.put("SenderName", currSenderName);
						}
						if (StringUtils.isNotBlank(currSenderEmail)) {
							inputMap.put("SenderEmail", currSenderEmail);
						}
						inputMap.put("companyLegalUnit", legalEntityId);
						currKeyBuffer.append(currLocale + "$" + currChannelId + "$" + currStatusId);
						if (associatedTemplates.containsKey(currKeyBuffer.toString())) {
							// Existing Template. Update record
							currTemplateId = associatedTemplates.get(currKeyBuffer.toString());
							inputMap.put("Id", currTemplateId);
							inputMap.put("modifiedby", loggedInUser);
							inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
							operationResponse = Executor.invokeService(ServiceURLEnum.COMMUNICATIONTEMPLATE_UPDATE,
									inputMap, null, requestInstance);
						} else {
							// Non Existing Template. Create record
							currTemplateId = String.valueOf(CommonUtilities.getNumericId());
							inputMap.put("Id", currTemplateId);
							inputMap.put("createdby", loggedInUser);
							inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
							operationResponse = Executor.invokeService(ServiceURLEnum.COMMUNICATIONTEMPLATE_CREATE,
									inputMap, null, requestInstance);
							associatedTemplates.put(currKeyBuffer.toString(), currTemplateId);
						}
						operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
						if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
								|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
							alert.prepareError("Failed CRUD Operation. Attempted Template Record:" + currKeyBuffer.toString()).log();
							throw new ApplicationException(ErrorCodeEnum.ERR_20900);
						}
						diagnostic.prepareDebug("Successful CRUD Operation. Attempted Template Record:" + currKeyBuffer.toString()).log();
					}
				}
			}
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.UPDATE,
					ActivityStatusEnum.SUCCESSFUL, "AlertSubType Update Success");
		}

		if (removedTemplates != null && removedTemplates.length() > 0) {
			inputMap.clear();
			for (int index = 0; index < removedTemplates.length(); index++) {
				if (removedTemplates.opt(index) instanceof String) {

					currTemplateId = removedTemplates.optString(index);
					if (associatedTemplates.values().contains(currTemplateId)) {

						inputMap.put("Id", currTemplateId);
						inputMap.put("companyLegalUnit", legalEntityId);
						operationResponse = Executor.invokeService(ServiceURLEnum.COMMUNICATIONTEMPLATE_DELETE,
								inputMap, null, requestInstance);

						operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
						if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
								|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
							alert.prepareError("Failed CRUD Operation. Attempted Template Id:" + currTemplateId).log();
							throw new ApplicationException(ErrorCodeEnum.ERR_20900);
						}
						diagnostic.prepareDebug("Successful CRUD Operation. Attempted Template Id:" + currTemplateId).log();
					}
				}

			}
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.UPDATE,
					ActivityStatusEnum.SUCCESSFUL, "AlertSubType Update Success");
		}
	}

	private Record setAlertTypeDisplayPreferences(String alertCode, String addedDisplayPreference,
			String removedDisplayPreference, String loggedInUserId, DataControllerRequest requestInstance, String legalEntityId)
					throws ApplicationException {

		Record displayPreference = new Record();
		displayPreference.setId("displayPreference");

		String currOperationResponse = StringUtils.EMPTY, currLocale = StringUtils.EMPTY;
		JSONObject currOperationResponseJSON;

		JSONObject currJSON = null;
		Map<String, String> inputMap = new HashMap<>();

		TABLE_OPERATION_MAPPING tableMapping = TABLE_OPERATION_MAPPING.ALERTSUBTYPETEXT;
		Result getResult = invokeGetService(requestInstance, tableMapping.getReadServiceName(),
				tableMapping.getAlertpkId1() + " eq '" + alertCode + "' and companyLegalUnit eq '" + legalEntityId +"'", 
				tableMapping.getCompositePKid(),
				tableMapping.getTableName(), ErrorCodeEnum.ERR_20929);

		Set<String> associatedDisplayPreferencesSet = getResult.getDatasetById(tableMapping.getTableName())
				.getAllRecords().stream()
				.filter(rec -> rec.getParamValueByName(tableMapping.getCompositePKid()) != null)
				.map(rec -> rec.getParamValueByName(tableMapping.getCompositePKid())).collect(Collectors.toSet());

		diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DBXALERTTYPETEXT_READ.name()).log();

		// Handle Removed Display Preferences
		inputMap.clear();
		inputMap.put(tableMapping.getAlertpkId1(), alertCode);
		inputMap.put("companyLegalUnit", legalEntityId);
		JSONArray removedDisplayPreferencesJSONArray = CommonUtilities.getStringAsJSONArray(removedDisplayPreference);
		if (removedDisplayPreferencesJSONArray != null && removedDisplayPreferencesJSONArray.length() > 0) {
			for (Object currObject : removedDisplayPreferencesJSONArray) {
				if (currObject instanceof String) {
					currLocale = (String) currObject;
					if (associatedDisplayPreferencesSet.contains(currLocale)) {
						diagnostic.prepareDebug("Removing Display Preference for Locale:" + currLocale).log();
						inputMap.put("languageCode", currLocale);
						currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPETEXT_DELETE, inputMap,
								null, requestInstance);
						currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
						if (currOperationResponseJSON == null
								|| !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
								|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
							alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.ALERTSUBTYPETEXT_DELETE.name()).log();
							throw new ApplicationException(ErrorCodeEnum.ERR_20904);
						}
						diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.ALERTSUBTYPETEXT_DELETE.name()).log();
						associatedDisplayPreferencesSet.remove(currLocale);
					}
					displayPreference.addParam(new Param(currLocale, "Text Removed", FabricConstants.STRING));
				}
			}
		}

		// Handle Added Display Preferences
		JSONArray addedDisplayPreferencesJSONArray = CommonUtilities.getStringAsJSONArray(addedDisplayPreference);
		if (addedDisplayPreferencesJSONArray != null && addedDisplayPreferencesJSONArray.length() > 0) {

			for (Object currObject : addedDisplayPreferencesJSONArray) {
				if (currObject instanceof JSONObject) {
					currJSON = (JSONObject) currObject;
					if (currJSON.has("languageCode") && currJSON.has("displayName") && currJSON.has("description")) {

						inputMap.clear();
						currLocale = currJSON.optString("languageCode");
						inputMap.put(tableMapping.getAlertpkId1(), alertCode);
						inputMap.put(tableMapping.getCompositePKid(), currLocale);
						inputMap.put("displayName", StringEscapeUtils.escapeHtml(currJSON.optString("displayName")));
						inputMap.put("description", StringEscapeUtils.escapeHtml(currJSON.optString("description")));
						inputMap.put("companyLegalUnit", legalEntityId);
						if (associatedDisplayPreferencesSet.contains(currLocale)) {
							diagnostic.prepareDebug("Updating Display Preference for Locale:" + currLocale).log();
							inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
							inputMap.put("updatedby", loggedInUserId);
							currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPETEXT_UPDATE,
									inputMap, null, requestInstance);
							currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
						} else {
							diagnostic.prepareDebug("Creating Display Preference for Locale:" + currLocale).log();
							inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
							inputMap.put("createdby", loggedInUserId);
							currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPETEXT_CREATE,
									inputMap, null, requestInstance);
							currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
						}
						associatedDisplayPreferencesSet.add(currJSON.optString("languageCode"));
						if (currOperationResponseJSON == null
								|| !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
								|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
							throw new ApplicationException(ErrorCodeEnum.ERR_20904);
						}
						diagnostic.prepareDebug("Successful CRUD Operation").log();
						displayPreference.addParam(new Param(currLocale, "Text Added", FabricConstants.STRING));
					}
				}
			}
		}

		return displayPreference;
	}

}