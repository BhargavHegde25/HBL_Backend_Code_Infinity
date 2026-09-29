package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.jwt.auth.utils.LegalEntityUtil;
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
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to manage the Alert Type Settings
 * 
 * @author Aditya Mankal
 */
public class AlertTypeManageService implements JavaService2 {
	private static final String REORDER_ALERT_TYPE_METHOD_ID = "reorderAlertType";
	private static final String CREATE_ALERT_TYPE_METHOD_ID = "createAlertType";
	private static final String EDIT_ALERT_TYPE_METHOD_ID   = "editAlertType";
	private static final String REASSIGN_ALERT_TYPE_METHOD_ID = "reassignAlertType";															
	private static final String GET_ALERT_CATERGORIES_METHOD_ID =  "getAlertCategoriesByAccountType";

	private static final String ALERT_NAME_PARAM = "alertName";
	private static final String ALERT_CODE_PARAM = "alertCode";
	private static final String STATUS_ID_PARAM = "statusId";

	private static final String ADDED_DISPLAY_PREFERENCE_PARAM = "addedDisplayPreferences";
	private static final String REMOVED_DISPLAY_PREFERENCE_PARAM = "removedDisplayPreferences";

	private static final String TYPE_ORDER_PARAM = "typeOrder";
	private static final String CATEGORY_CODE_PARAM = "alertCategoryCode";

	private static final String FROM_ALERT_CATEGORY_PARAM = "fromAlertyCategory";
	private static final String TO_ALERT_CATEGORY_PARAM = "toAlertCategory";

	private static final int ALERT_NAME_MIN_LENGTH = 5;
	private static final int ALERT_NAME_MAX_LENGTH = 50;

	private static final String IS_ACCOUNT_LEVEL_PARAM = "isAccountLevel";
	private static final String CHANNELS_PARAM = "channels";
	private static final String FREQUENCY_PARAM = "frequency";	

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
			activityMessage = "Alert Type Id:" + requestInstance.getParameter(ALERT_CODE_PARAM);

			// Fetch Logged In User Info
			String loggedInUser = StringUtils.EMPTY;
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			if (userDetailsBeanInstance != null) {
				loggedInUser = userDetailsBeanInstance.getId();
			}

			if (StringUtils.equalsIgnoreCase(methodID, REORDER_ALERT_TYPE_METHOD_ID)) {
				processedResult = reorderAlertType(requestInstance, loggedInUser);
				eventEnum = EventEnum.UPDATE;
			}

			else if (StringUtils.equalsIgnoreCase(methodID, CREATE_ALERT_TYPE_METHOD_ID)) {
				processedResult = setAlertType(requestInstance, true, loggedInUser);
				eventEnum = EventEnum.CREATE;
			} 
			else if (StringUtils.equalsIgnoreCase(methodID, EDIT_ALERT_TYPE_METHOD_ID)) {
				processedResult = setAlertType(requestInstance, false, loggedInUser);
				eventEnum = EventEnum.UPDATE;
			}else if (StringUtils.equalsIgnoreCase(methodID, REASSIGN_ALERT_TYPE_METHOD_ID)) {
				processedResult = reAssignAlertType(requestInstance, loggedInUser);
				eventEnum = EventEnum.UPDATE;
			}else if (StringUtils.equalsIgnoreCase(methodID, GET_ALERT_CATERGORIES_METHOD_ID)) {
				processedResult = getCategoriesOfSameAccountLevel(requestInstance, loggedInUser);
				eventEnum = EventEnum.UPDATE;
			}

			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, eventEnum,
					ActivityStatusEnum.SUCCESSFUL, activityMessage);
			
			if(processedResult.getParamByName(ACConstants.DBP_ERROR_MESSAGE) == null) {
			     processedResult.addParam(new Param("status", "success", FabricConstants.STRING));
			}

		} catch (ApplicationException e) {
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();

			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, eventEnum,
					ActivityStatusEnum.FAILED, activityMessage);

			Result errorResult = new Result();
			errorResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();

			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, eventEnum,
					ActivityStatusEnum.FAILED, activityMessage);

			Result errorResult = new Result();
			errorResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20921.setErrorCode(errorResult);
			return errorResult;
		}
		return processedResult;
	}

	/**
	 * Method to Create/Edit Alert Type
	 * 
	 * @param requestInstance
	 * @param isCreateMode
	 * @param loggedInUserId
	 * @return operation Result
	 * @throws ApplicationException
	 */
	private Result setAlertType(DataControllerRequest requestInstance, boolean isCreateMode, String loggedInUserId)
			throws ApplicationException {
		
		try {
			Result operationResult = new Result();
			
			if (requestInstance == null) {
				alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_20908);
			}
			
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
				operationResult.addParam(new Param("Status", "Create Alert Group operation failed", FabricConstants.STRING));
	            ErrorCodeEnum.ERR_22231.setErrorCode(operationResult);
	            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	            return operationResult;        
	        }

		diagnostic.prepareDebug("isCreateMode" + isCreateMode).log();
		String channels = requestInstance.getParameter(CHANNELS_PARAM);
		// Alert Category Code
		String categoryCode = requestInstance.getParameter(CATEGORY_CODE_PARAM);
		if (StringUtils.isBlank(categoryCode)) {
			alert.prepareError("Missing Mandatory Input: AlertCategoryCode").log();
			ErrorCodeEnum.ERR_20920.setErrorCode(operationResult);
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			return operationResult;
		}
		
		// Checking if the alert category is present for the legal entity
		if (AlertManagementHandler.isAlertCategoryAvaialable(categoryCode, requestInstance)) {
			alert.prepareError("No Alert Category present with this name for the legal entity").log();
			ErrorCodeEnum.ERR_22234.setErrorCode(operationResult);
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			return operationResult;
		}

		// Alert Name
		String alertName = requestInstance.getParameter(ALERT_NAME_PARAM);
		alertName = StringEscapeUtils.escapeHtml(alertName);
		if (isCreateMode == true || (StringUtils.isNotBlank(alertName))) {
			if (StringUtils.length(alertName) < ALERT_NAME_MIN_LENGTH
					|| StringUtils.length(alertName) > ALERT_NAME_MAX_LENGTH) {
				alert.prepareError("Invalid Input: AlertName").log();
				operationResult
				.addParam(new Param("message", "Alert Name should have a minimum of " + ALERT_NAME_MIN_LENGTH
						+ " characters and a maximum of " + ALERT_NAME_MAX_LENGTH + " characters"));
				operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20901.setErrorCode(operationResult);
				return operationResult;
			}
		}

		// Alert Code - Mandatory Input from Client. Code to be chosen from a pre-populated list
		String alertCode = requestInstance.getParameter(ALERT_CODE_PARAM);
		if (StringUtils.isBlank(alertCode)) {
			alert.prepareError("Invalid Input: AlertCode").log();
			operationResult.addParam(new Param("message", "Alert Code cannot be empty"));
			ErrorCodeEnum.ERR_20901.setErrorCode(operationResult);
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			return operationResult;
		}
		// Check if an Alert with the given alert code already exists
		if (isCreateMode == true) {
			boolean isAlertCodeAvaialble = AlertManagementHandler.isAlertCodeAvaialable(alertCode, requestInstance);
			if (isAlertCodeAvaialble == false) {
				alert.prepareError("Alert with the Code :" + alertCode + " has already been defined").log();
				ErrorCodeEnum.ERR_20892.setErrorCode(operationResult);
				operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
				return operationResult;
			}
		}

		// Status Id
		String statusId = requestInstance.getParameter(STATUS_ID_PARAM);
		if (isCreateMode == true || (StringUtils.isNotBlank(statusId))) {
			if (!StatusEnum.isValidStatusCode(statusId)) {
				alert.prepareError("Invalid Input: statusId").log();
				operationResult.addParam(new Param("message", "Invalid Status Id"));
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
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
		
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("id", alertCode);
		inputMap.put("AlertCategoryId", categoryCode);
		inputMap.put("companyLegalUnit", legalEntityId);

		if (StringUtils.isNotBlank(alertName)) {
			inputMap.put("Name", alertName);
		}

		if (StringUtils.isNotBlank(statusId)) {
			inputMap.put("Status_id", statusId);
		}

		if(isCreateMode) {
			if (StringUtils.equalsIgnoreCase(isAccountLevelAlertStr, String.valueOf(true))) {
				inputMap.put("isAccountLevel", "1");
			} else if (StringUtils.equalsIgnoreCase(isAccountLevelAlertStr, String.valueOf(false))) {
				inputMap.put("isAccountLevel", "0");
			}
		}

		if (isCreateMode == true) {
			inputMap.put("DisplaySequence", "0");
		}

		if(StringUtils.isNotBlank(freqId)) {
			inputMap.put("defaultFrequencyId", freqId);
		}
		if(StringUtils.isNotBlank(freqValue)) {
			inputMap.put("defaultFrequencyValue", freqValue);
		}
		if(StringUtils.isNotBlank(freqTime)) {
			inputMap.put("defaultFrequencyTime", freqTime);
		}
	
		String operationResponse;
		if (isCreateMode == true) {
			inputMap.put("createdby", loggedInUserId);
			inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
			operationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTTYPE_CREATE, inputMap, null,
					requestInstance);
		} else {
			inputMap.put("modifiedby", loggedInUserId);
			inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
			operationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTTYPE_UPDATE, inputMap, null,
					requestInstance);
		}
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
		if (operationResponseJSON == null || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			operationResult.addParam(new Param("alertTypeDefinition", String.valueOf(false), FabricConstants.STRING));
			if (isCreateMode == true) {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.CREATE,
						ActivityStatusEnum.FAILED, "AlertType Create Failed");
				ErrorCodeEnum.ERR_20893.setErrorCode(operationResult);
			} else {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED, "AlertType Update Failed");
				ErrorCodeEnum.ERR_20902.setErrorCode(operationResult);
			}

			operationResult.addParam(new Param("message", operationResponse, FabricConstants.STRING));
			return operationResult;
		}
		operationResult.addParam(new Param("alertTypeDefinition", String.valueOf(true), FabricConstants.STRING));
		operationResult.addParam(new Param("legalEntityId", String.valueOf(legalEntityId), FabricConstants.STRING));

		// Create AlertType or AlertGroup Channels
		upsertAlertTypeChannels(requestInstance, legalEntityId, isCreateMode, loggedInUserId, operationResult, channels,
				alertCode);

		// Edit Alert Type Display Preferences
		String addedDisplayPreferencesStr = requestInstance.getParameter(ADDED_DISPLAY_PREFERENCE_PARAM);
		String removedDisplayPreferencesStr = requestInstance.getParameter(REMOVED_DISPLAY_PREFERENCE_PARAM);
		Record displayPreferencesRecord = setAlertTypeDisplayPreferences(alertCode, legalEntityId, addedDisplayPreferencesStr,
				removedDisplayPreferencesStr, loggedInUserId, requestInstance);
		operationResult.addRecord(displayPreferencesRecord);		
		return operationResult;
		}
		catch(Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20893.setErrorCode(errorResult);
			return errorResult;
		}
	}

	private void upsertAlertTypeChannels(DataControllerRequest requestInstance, String legalEntityId, boolean isCreateMode,
			String loggedInUserId, Result operationResult, String channels, String alertCode)
					throws ApplicationException {
		JSONObject channelsJSON = CommonUtilities.getStringAsJSONObject(channels);
		if (channelsJSON != null) {
			List<String> supportedChannelsList = new ArrayList<>();
			List<String> unSupportedChannelsList = new ArrayList<>();
			for (String key : channelsJSON.keySet()) {
				if (StringUtils.equalsIgnoreCase(channelsJSON.optString(key), "TRUE")) {
					supportedChannelsList.add(key);
				} else if (StringUtils.equalsIgnoreCase(channelsJSON.optString(key), "FALSE")) {
					unSupportedChannelsList.add(key);
				}
			}

			diagnostic.prepareDebug("Setting AlertType Channels...").log();
			Record alertTypeChannels = null;
			if (isCreateMode == true) {
				alertTypeChannels = createAlertTypeChannels(alertCode, legalEntityId, supportedChannelsList,
						loggedInUserId, requestInstance);					
			} else {
				alertTypeChannels = setAlertTypeChannels(alertCode, legalEntityId, supportedChannelsList,
						unSupportedChannelsList, loggedInUserId, requestInstance);
			}
			operationResult.addRecord(alertTypeChannels);
			diagnostic.prepareDebug("AlertType Channels Set").log();
		}
	}

	/**
	 * Method to create AlertType Channels
	 * 
	 * @param alertTypeId 
	 * @param channelsList
	 * @param loggedInUserId
	 * @param requestInstance
	 * @return operation Record
	 * @throws ApplicationException
	 */
	private Record createAlertTypeChannels(String alertTypeId, String legalEntityId, List<String> channelsList, String loggedInUserId,
			DataControllerRequest requestInstance) throws ApplicationException {

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20960);
		}

		Record createtAlertCategoryChannelsRecord = new Record();
		createtAlertCategoryChannelsRecord.setId("alertGroupChannels");

		Map<String, String> inputMap = new HashMap<>();

		JSONObject currOperationResponseJSON;
		String currOperationResponse = StringUtils.EMPTY, timestamp = StringUtils.EMPTY;

		// Handle Supported Channels
		if (channelsList != null && !channelsList.isEmpty()) {
			inputMap.clear();
			inputMap.put("alertTypeId", alertTypeId);
			inputMap.put("createdby", loggedInUserId);
			inputMap.put("companyLegalUnit", legalEntityId);
			
			for (String channelId : channelsList) {
				diagnostic.prepareDebug("Adding Support for Channel:" + channelId).log();
				timestamp = CommonUtilities.getISOFormattedLocalTimestamp();
				inputMap.put("createdts", timestamp);
				inputMap.put("channelId", channelId);
				currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTTYPECHANNEL_CREATE, inputMap,
						null, requestInstance);
				currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
				if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
						|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
					alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.ALERTTYPECHANNEL_CREATE.name()).log();
					throw new ApplicationException(ErrorCodeEnum.ERR_20960);
				}
				diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.ALERTTYPECHANNEL_CREATE.name()).log();

				createtAlertCategoryChannelsRecord
				.addParam(new Param(channelId, String.valueOf(true), FabricConstants.STRING));
			}
		}
		return createtAlertCategoryChannelsRecord;
	}

	/**
	 * Method to Set AlertType Channels
	 * 
	 * @param alertTypeId
	 * @param supportedChannelsList
	 * @param unSupportedChannelsList
	 * @param loggedInUserId
	 * @param requestInstance
	 * @return operation Record
	 * @throws ApplicationException
	 */
	private Record setAlertTypeChannels(String alertTypeId, String legalEntityId, List<String> supportedChannelsList,
			List<String> unSupportedChannelsList, String loggedInUserId, DataControllerRequest requestInstance)
					throws ApplicationException {

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20960);
		}

		Record setAlertTypeChannelsRecord = new Record();
		setAlertTypeChannelsRecord.setId("alertGroupChannels");

		Map<String, String> inputMap = new HashMap<>();

		JSONObject currOperationResponseJSON, currJSON;
		String currOperationResponse = StringUtils.EMPTY, timestamp = StringUtils.EMPTY;

		// Fetch Existing Association
		Set<String> associatedChannelsSet = new HashSet<>();
		inputMap.put(ODataQueryConstants.FILTER, "alertTypeId eq '" + alertTypeId + "' and companyLegalUnit eq '"+ legalEntityId + "'");
		inputMap.put(ODataQueryConstants.SELECT, "channelId");
		currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTTYPECHANNEL_READ, inputMap, null,
				requestInstance);
		currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
		if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
				|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !currOperationResponseJSON.has("alerttypechannel")) {
			alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.ALERTTYPECHANNEL_READ.name()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20960);
		}
		diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.ALERTTYPECHANNEL_READ.name()).log();
		JSONArray alertTypeChannelsJSONArray = currOperationResponseJSON.optJSONArray("alerttypechannel");
		for (Object currObj : alertTypeChannelsJSONArray) {
			if (currObj instanceof JSONObject) {
				currJSON = (JSONObject) currObj;
				if (currJSON.has("channelId")) {
					associatedChannelsSet.add(currJSON.optString("channelId"));
				}
			}
		}

		// Handle Supported Channels
		if (supportedChannelsList != null && !supportedChannelsList.isEmpty()) {
			inputMap.clear();
			inputMap.put("alertTypeId", alertTypeId);
			inputMap.put("createdby", loggedInUserId);
			inputMap.put("companyLegalUnit", legalEntityId);
			
			for (String channelId : supportedChannelsList) {
				if (!associatedChannelsSet.contains(channelId)) {
					diagnostic.prepareDebug("Adding Support for Channel:" + channelId).log();
					timestamp = CommonUtilities.getISOFormattedLocalTimestamp();
					inputMap.put("createdts", timestamp);
					inputMap.put("channelId", channelId);
					currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTTYPECHANNEL_CREATE, inputMap,
							null, requestInstance);
					currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
					if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
							|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
						alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.ALERTTYPECHANNEL_CREATE.name()).log();
						throw new ApplicationException(ErrorCodeEnum.ERR_20960);
					}
					diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.ALERTTYPECHANNEL_CREATE.name()).log();
				}
				setAlertTypeChannelsRecord
				.addParam(new Param(channelId, String.valueOf(true), FabricConstants.STRING));
				associatedChannelsSet.add(channelId);
			}
		}

			
		
		// Handle Removed Channels
		if (unSupportedChannelsList != null && !unSupportedChannelsList.isEmpty()) {			
			List<Channel> alertChannelList = new ArrayList<>();	
			inputMap.clear();
			inputMap.put("alertTypeId", alertTypeId);
			inputMap.put("companyLegalUnit", legalEntityId);
			
			for (String channelId : unSupportedChannelsList) {
				if (associatedChannelsSet.contains(channelId)) {
					diagnostic.prepareDebug("Removing Support for Channel:" + channelId).log();
					inputMap.put("channelId", channelId);
					currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTTYPECHANNEL_DELETE, inputMap,
							null, requestInstance);
					currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
					if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
							|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
						alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.ALERTTYPECHANNEL_DELETE.name()).log();
						throw new ApplicationException(ErrorCodeEnum.ERR_20960);
					}
					diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.ALERTTYPECHANNEL_DELETE.name()).log();
					alertChannelList.add(new Channel(channelId, false));
					associatedChannelsSet.remove(channelId);
				}
				setAlertTypeChannelsRecord
				.addParam(new Param(channelId, String.valueOf(false), FabricConstants.STRING));
			}
			if(!alertChannelList.isEmpty()) {							
				AlertManagementHandler.syncCustomerAlertChannels(requestInstance, "edit", alertTypeId, null,
						alertChannelList.stream().map(Channel::getId).collect(Collectors.joining(",")),
						ACConstants.ALERTPREFERNCES.GROUP.name());
			}
		}

		return setAlertTypeChannelsRecord;
	}


	/**
	 * Method to set Display Preferences of Alert Type
	 * 
	 * @param alertTypeCode
	 * @param addedDisplayPreference
	 * @param removedDisplayPreference
	 * @param loggedInUserId
	 * @param requestInstance
	 * @return Operation Record
	 * @throws ApplicationException
	 */
	private Record setAlertTypeDisplayPreferences(String alertTypeCode, String legalEntityId, String addedDisplayPreference,
			String removedDisplayPreference, String loggedInUserId, DataControllerRequest requestInstance)
					throws ApplicationException {

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20904);
		}

		Record displayPreference = new Record();
		displayPreference.setId("displayPreference");

		String currOperationResponse = StringUtils.EMPTY, currLocale = StringUtils.EMPTY;
		JSONObject currOperationResponseJSON;

		JSONObject currJSON = null;
		Map<String, String> inputMap = new HashMap<>();

		// Fetch Existing Association
		inputMap.put(ODataQueryConstants.FILTER, "AlertTypeId eq '" + alertTypeCode + "' and companyLegalUnit eq '"+ legalEntityId + "'");
		inputMap.put(ODataQueryConstants.SELECT, "LanguageCode");
		currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTTYPETEXT_READ, inputMap, null,
				requestInstance);
		currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
		if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
				|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DBXALERTTYPETEXT_READ.name()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20904);
		}
		diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DBXALERTTYPETEXT_READ.name()).log();
		Set<String> associatedDisplayPreferencesSet = new HashSet<>();
		JSONArray associatedDisplayPreferencesJSONArray = currOperationResponseJSON.optJSONArray("dbxalerttypetext");
		if (associatedDisplayPreferencesJSONArray != null && associatedDisplayPreferencesJSONArray.length() > 0) {
			for (Object currObj : associatedDisplayPreferencesJSONArray) {
				if (currObj instanceof JSONObject) {
					currJSON = (JSONObject) currObj;
					if (currJSON.has("LanguageCode")) {
						associatedDisplayPreferencesSet.add(currJSON.optString("LanguageCode"));
					}
				}
			}
		}

		// Handle Removed Display Preferences
		inputMap.clear();
		inputMap.put("AlertTypeId", alertTypeCode);
		inputMap.put("companyLegalUnit", legalEntityId);
		JSONArray removedDisplayPreferencesJSONArray = CommonUtilities.getStringAsJSONArray(removedDisplayPreference);
		if (removedDisplayPreferencesJSONArray != null && removedDisplayPreferencesJSONArray.length() > 0) {
			for (Object currObject : removedDisplayPreferencesJSONArray) {
				if (currObject instanceof String) {
					currLocale = (String) currObject;
					if (associatedDisplayPreferencesSet.contains(currLocale)) {
						diagnostic.prepareDebug("Removing Display Preference for Locale:" + currLocale).log();
						inputMap.put("LanguageCode", currLocale);
						currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTTYPETEXT_DELETE, inputMap,
								null, requestInstance);
						currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
						if (currOperationResponseJSON == null
								|| !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
								|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
							alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DBXALERTTYPETEXT_DELETE.name()).log();
							throw new ApplicationException(ErrorCodeEnum.ERR_20904);
						}
						diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DBXALERTTYPETEXT_DELETE.name()).log();
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
						inputMap.put("AlertTypeId", alertTypeCode);
						inputMap.put("companyLegalUnit", legalEntityId);
						inputMap.put("LanguageCode", currLocale);
						inputMap.put("DisplayName", StringEscapeUtils.escapeHtml(currJSON.optString("displayName")));
						inputMap.put("Description", StringEscapeUtils.escapeHtml(currJSON.optString("description")));

						if (associatedDisplayPreferencesSet.contains(currLocale)) {
							diagnostic.prepareDebug("Updating Display Preference for Locale:" + currLocale).log();
							inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
							inputMap.put("updatedby", loggedInUserId);
							currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTTYPETEXT_UPDATE,
									inputMap, null, requestInstance);
							currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
						} else {
							diagnostic.prepareDebug("Creating Display Preference for Locale:" + currLocale).log();
							inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
							inputMap.put("createdby", loggedInUserId);
							currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTTYPETEXT_CREATE,
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

	/**
	 * Method to reorder the Alert Types
	 * 
	 * @param requestInstance
	 * @param loggedInUserId
	 * @return operation Result
	 * @throws ApplicationException
	 */
	private Result reorderAlertType(DataControllerRequest requestInstance, String loggedInUserId)
			throws ApplicationException {
		
		try {
			Result operationResult = new Result();
			
			if (requestInstance == null) {
				alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_20902);
			}
			
			if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
				alert.prepareError("legalEntityId cannot be empty").log();
				ErrorCodeEnum.ERR_22232.setErrorCode(operationResult);
				return operationResult;
			}
			String[] reqPermissions = {PermissionName.UPDATE_ALERTS};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
	        {
				operationResult.addParam(new Param("Status", "Create Alert Group operation failed", FabricConstants.STRING));
	            ErrorCodeEnum.ERR_22231.setErrorCode(operationResult);
	            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	            return operationResult;        
	        }

		// Read Inputs
		String typeOrder = requestInstance.getParameter(TYPE_ORDER_PARAM);
		String categoryCode = requestInstance.getParameter(CATEGORY_CODE_PARAM);
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
		
		// Validate Inputs
		if (StringUtils.isBlank(categoryCode)) {
			alert.prepareError("Missing Mandatory Input: AlertCategoryCode").log();
			ErrorCodeEnum.ERR_20920.setErrorCode(operationResult);
			return operationResult;
		}
		
		// Checking if the alert category is present for the legal entity
		if (AlertManagementHandler.isAlertCategoryAvaialable(categoryCode, requestInstance)) {
			alert.prepareError("No Alert Category present with this name for the legal entity").log();
			ErrorCodeEnum.ERR_22234.setErrorCode(operationResult);
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			return operationResult;
		}
		
		JSONObject typeOrderJSONObject = null;
		if (StringUtils.isNotBlank(typeOrder)) {
			try {
				typeOrderJSONObject = new JSONObject(typeOrder);
			} catch (NullPointerException | JSONException e) {
				// Malformed Alert Category Channel Preference JSON
				alert.prepareError("Malformed Alert Type Order JSON").log();
				ErrorCodeEnum.ERR_20902.setErrorCode(operationResult);
				return operationResult;
			}
		}

		// Set Alert Type Order
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("modifiedby", loggedInUserId);
		inputMap.put("AlertCategoryId", categoryCode);
		inputMap.put("companyLegalUnit", legalEntityId);

		String currTypeId = StringUtils.EMPTY, currOperationResponse = StringUtils.EMPTY;
		JSONObject currOperationResponseJSON = null;
		int currTypeDisplaySequence = 0;
		if (typeOrderJSONObject != null) {
			for (String key : typeOrderJSONObject.keySet()) {
				if (typeOrderJSONObject.opt(key) instanceof Integer) {
					currTypeId = key;
					currTypeDisplaySequence = typeOrderJSONObject.optInt(key);
					inputMap.put("id", currTypeId);
					inputMap.put("DisplaySequence", String.valueOf(currTypeDisplaySequence));
					inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
					currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTTYPE_UPDATE, inputMap, null,
							requestInstance);
					currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
					if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
							|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
						alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DBXALERTTYPE_UPDATE.name()).log();
						throw new ApplicationException(ErrorCodeEnum.ERR_20902);
					}
					diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DBXALERTTYPE_UPDATE.name()).log();
				}
			}
		}

		operationResult.addParam(new Param("status", "success", FabricConstants.STRING));

		return operationResult;
		}
		catch(Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20902.setErrorCode(errorResult);
			return errorResult;
		}
	}	

	private Result reAssignAlertType(DataControllerRequest requestInstance, String loggedInUserId)
			throws ApplicationException {
		try {
			Result operationResult = new Result();
			
			if (requestInstance == null) {
				alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_20902);
			}
			
			if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
				alert.prepareError("legalEntityId cannot be empty").log();
				ErrorCodeEnum.ERR_22232.setErrorCode(operationResult);
				return operationResult;
			}
			String[] reqPermissions = {PermissionName.UPDATE_ALERTS};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
	        {
				operationResult.addParam(new Param("Status", "Create Alert Category operation failed", FabricConstants.STRING));
	            ErrorCodeEnum.ERR_22231.setErrorCode(operationResult);
	            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	            return operationResult;        
	        }
			
		// Read Inputs
		String alertTypeId = requestInstance.getParameter(ALERT_CODE_PARAM);
		String fromAlertCategoryParam = requestInstance.getParameter(FROM_ALERT_CATEGORY_PARAM);
		String toAlertCategoryParam = requestInstance.getParameter(TO_ALERT_CATEGORY_PARAM);
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
		
		

		operationResult = validateReassignInputs(alertTypeId,fromAlertCategoryParam,toAlertCategoryParam, requestInstance);
		//if validation adds dbpErrorCode param or success param, return from here
		if(!operationResult.getAllParams().isEmpty() ) {
			return operationResult;
		}
		//Check if the reassign account type is same as current accountlevel type
		Result alertCategoryResponse = fetchAlertCategories(requestInstance, fromAlertCategoryParam,
				toAlertCategoryParam,legalEntityId);
		if(!isSameAccountLevel(fromAlertCategoryParam, toAlertCategoryParam, operationResult, alertCategoryResponse)){
			return operationResult;
		}				
		// Set Alert Type Order
		Map<String, Object> inputMap = getInputParamsForReassign(loggedInUserId, alertTypeId, toAlertCategoryParam, legalEntityId);
		Result alertTypeUpdateResult = ServiceUtil.invokeService(ServiceURLEnum.DBXALERTTYPE_UPDATE, inputMap, null,
				requestInstance);
		isOperationSuccessful(alertTypeUpdateResult,ServiceURLEnum.DBXALERTTYPE_UPDATE,ErrorCodeEnum.ERR_20902);
		
		syncCustomerAlertAssociations(requestInstance, alertTypeId, legalEntityId);
		
		
		diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DBXALERTTYPE_UPDATE.name()).log();
		operationResult.addParam(new Param("status", "success", FabricConstants.STRING));	

		return operationResult;
		}
		catch(Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20902.setErrorCode(errorResult);
			return errorResult;
		}
	}

	public void syncCustomerAlertAssociations(DataControllerRequest requestInstance, String fromAlertCategoryParam, String legalEntityId)
			throws ApplicationException {
		AlertManagementHandler.syncCustomerAlertChannels(requestInstance, "reassign",
																		fromAlertCategoryParam, legalEntityId, "", "");
		AlertManagementHandler.syncCustomerAlertFrequencies(requestInstance, "reassign",fromAlertCategoryParam, legalEntityId);
		AlertManagementHandler.syncCustomerAlertEntitlements(requestInstance, "reassign",fromAlertCategoryParam, legalEntityId);
	}

	private Result getCategoriesOfSameAccountLevel(DataControllerRequest requestInstance, String loggedInUserId)
			throws ApplicationException {
		try {
			Result operationResult = new Result();
			
			if (requestInstance == null) {
				alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_20965);
			}
			
			if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
				alert.prepareError("legalEntityId cannot be empty").log();
				ErrorCodeEnum.ERR_22232.setErrorCode(operationResult);
				return operationResult;
			}
			String[] reqPermissions = {PermissionName.VIEW_ALERTS};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
	        {
				operationResult.addParam(new Param("Status", "Get Alert Category Based on Account Types failed", FabricConstants.STRING));
	            ErrorCodeEnum.ERR_22231.setErrorCode(operationResult);
	            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	            return operationResult;        
	        }
			
		// Read Inputs	
		String alertCategoryParam = requestInstance.getParameter(CATEGORY_CODE_PARAM);
		String accountLevel = requestInstance.getParameter(IS_ACCOUNT_LEVEL_PARAM);
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);

		operationResult = validategetSameCatgryInputs(alertCategoryParam,accountLevel);
		// Is validation successful
		if(operationResult.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
			return operationResult;
		}
		//Check if the reassign account type is same as current accountlevel type
		Result alertCategoryResponse = fetchCategoriesForSameAccountLevel(requestInstance, alertCategoryParam, legalEntityId,
				Boolean.parseBoolean(accountLevel));						

		return alertCategoryResponse;
		}
		catch(Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20912.setErrorCode(errorResult);
			return errorResult;
		}
	}


	private Result validategetSameCatgryInputs(String categoryCode, String accountLevel) {
		Result operationResult = new Result();
		if (StringUtils.isBlank(categoryCode)) {
			alert.prepareError("Missing Mandatory Input: AlertCategoryCode").log();
			ErrorCodeEnum.ERR_20920.setErrorCode(operationResult);
			return operationResult;
		}else if( StringUtils.isBlank(accountLevel) ||
				!(StringUtils.equalsIgnoreCase(accountLevel, "TRUE")
						|| StringUtils.equalsIgnoreCase(accountLevel, "FALSE"))) {
			alert.prepareError("Invalid Input: accountLevel").log();
			operationResult.addParam(new Param("message", "Invalid accountLevel Flag"));
			ErrorCodeEnum.ERR_20964.setErrorCode(operationResult);					
			return operationResult;
		}
		return operationResult;
	}

	private Result fetchCategoriesForSameAccountLevel(DataControllerRequest requestInstance,
			String categoryCode, String legalEntityId, boolean accountLevel) throws ApplicationException {
		Map<String, Object> inputMapRead = new HashMap<>();
		inputMapRead.put(ODataQueryConstants.FILTER, "accountLevel eq " +
				accountLevel + " and companyLegalUnit eq "+legalEntityId );				
		Result alertCategoryResponse = ServiceUtil.invokeService(ServiceURLEnum.DBXALERTCATEGORY_READ, 
				inputMapRead, null,requestInstance);

		isOperationSuccessful(alertCategoryResponse,ServiceURLEnum.DBXALERTCATEGORY_READ,ErrorCodeEnum.ERR_20965);

		if(alertCategoryResponse.getDatasetById("dbxalertcategory") != null) {
			List<Record> filteredResponse = alertCategoryResponse.getDatasetById("dbxalertcategory").getAllRecords().stream().
					filter(rec -> !rec.getParamValueByName("id").equals(categoryCode)).collect(Collectors.toList());

			alertCategoryResponse.removeDatasetById("dbxalertcategory");
			Dataset ds = new Dataset("dbxalertcategory");
			ds.addAllRecords(filteredResponse);
			alertCategoryResponse.addDataset(ds);
		}


		return alertCategoryResponse;
	}	

	private Result fetchAlertCategories(DataControllerRequest requestInstance,
			String fromAlertCategoryParam, String toAlertCategoryParam, String legalEntityId) throws ApplicationException {
		Map<String, Object> inputMapRead = new HashMap<>();
		inputMapRead.put(ODataQueryConstants.FILTER, "(id eq '" + fromAlertCategoryParam + "' or id eq '" +
				toAlertCategoryParam + "') and companyLegalUnit eq '" + legalEntityId + "'");
		
		inputMapRead.put(ODataQueryConstants.SELECT, "id,accountLevel");

		Result alertCategoryResponse = ServiceUtil.invokeService(ServiceURLEnum.DBXALERTCATEGORY_READ, 
				inputMapRead, null,requestInstance);
		isOperationSuccessful(alertCategoryResponse,ServiceURLEnum.DBXALERTCATEGORY_READ,ErrorCodeEnum.ERR_20915);
		return alertCategoryResponse;
	}

	private Map<String, Object> getInputParamsForReassign(String loggedInUserId, String alertTypeId,
			String toAlertCategoryParam, String legalEntityId) {
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("id", alertTypeId);
		inputMap.put("AlertCategoryId", toAlertCategoryParam);
		inputMap.put("companyLegalUnit", legalEntityId);
		inputMap.put("modifiedby", loggedInUserId);
		inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
		return inputMap;
	}

	private void isOperationSuccessful(Result alertCategoryResponse,ServiceURLEnum serviceUrlenum, ErrorCodeEnum errorcode) throws ApplicationException {
		if (alertCategoryResponse == null || (alertCategoryResponse.getParamValueByName(FabricConstants.OPSTATUS) != null
				&& Integer.parseInt(alertCategoryResponse.getParamValueByName(FabricConstants.OPSTATUS)) != 0)) {
			alert.prepareError("Failed CRUD Operation:" + serviceUrlenum.name()).log();
			throw new ApplicationException(errorcode);
		}
	}

	private boolean isSameAccountLevel(String fromAlertCategoryParam, String toAlertCategoryParam, Result operationResult,
			Result alertCategoryResponse) {		
		List<Record> accountlist = alertCategoryResponse.getDatasetById("dbxalertcategory").getAllRecords();
		Map<String, Boolean> readresultmap = new HashMap();
		for (Record record : accountlist) {
			boolean b = record.getParamValueByName("accountLevel") != null ? 
					Boolean.valueOf(record.getParamValueByName("accountLevel")) : null;
					readresultmap.put(record.getParamValueByName("id"), b);				
		}		
		if(!readresultmap.get(fromAlertCategoryParam).equals(readresultmap.get(toAlertCategoryParam))){
			alert.prepareError(ErrorCodeEnum.ERR_20963.getMessage()).log();
			ErrorCodeEnum.ERR_20963.setErrorCode(operationResult);	
			return false;
		}
		return true;
	}	

	private Result validateReassignInputs(String alertTypeId, 
			String fromAlertCategoryParam, String toAlertCategoryParam, DataControllerRequest requestInstance) throws ApplicationException {
		Result operationResult = new Result();
		if (StringUtils.isBlank(alertTypeId)) {
			alert.prepareError("Missing Mandatory Input: alertTypeId").log();
			ErrorCodeEnum.ERR_20961.setErrorCode(operationResult);			
		}else if (StringUtils.isBlank(FROM_ALERT_CATEGORY_PARAM) || StringUtils.isBlank(TO_ALERT_CATEGORY_PARAM)) {
			alert.prepareError("Missing Mandatory Input").log();
			ErrorCodeEnum.ERR_20962.setErrorCode(operationResult);			
		}else if(fromAlertCategoryParam.equals(toAlertCategoryParam)) {
			operationResult.addParam(new Param("status", "success", FabricConstants.STRING));
			return operationResult;
		} else if (AlertManagementHandler.isAlertCategoryAvaialable(toAlertCategoryParam, requestInstance) ||
				AlertManagementHandler.isAlertCategoryAvaialable(fromAlertCategoryParam, requestInstance)) {
			alert.prepareError("No Alert Category present with this name for the legal entity").log();
			ErrorCodeEnum.ERR_22234.setErrorCode(operationResult);
		}
		return operationResult;		
	}	
	

}

