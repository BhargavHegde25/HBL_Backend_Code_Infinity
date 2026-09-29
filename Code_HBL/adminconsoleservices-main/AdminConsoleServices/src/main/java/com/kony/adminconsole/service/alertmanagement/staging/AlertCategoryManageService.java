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
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to manage the Alert Category Settings
 * 
 * @author Aditya Mankal
 */
public class AlertCategoryManageService implements JavaService2 {

	private static final String EDIT_ALERT_CATEGORY_METHOD_ID = "editAlertCategory";
	private static final String CREATE_ALERT_CATEGORY_METHOD_ID = "createAlertCategory";
	private static final String REORDER_ALERT_CATEGORY_METHOD_ID = "reorderAlertCategory";

	private static final String CATEGORY_ORDER_PARAM = "categoryOrder";
	private static final String CATEGORY_NAME_PARAM = "categoryName";
	private static final String CATEGORY_CODE_PARAM = "categoryCode";
	private static final String CHANNELS_PARAM = "channels";
	private static final String FREQUENCY_PARAM = "frequency";
	private static final String STATUS_ID_PARAM = "statusId";
	private static final String DISPLAY_PREFERENCE_PARAM = "displayPreferences";
	private static final String ADDED_DISPLAY_PREFERENCE_PARAM = "addedDisplayPreferences";
	private static final String REMOVED_DISPLAY_PREFERENCE_PARAM = "removedDisplayPreferences";
	private static final String CONTAINS_ACCOUNT_LEVEL_ALERTS_PARAM = "containsAccountLevelAlerts";
		
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
			activityMessage = "Alert Category Id:" + requestInstance.getParameter(CATEGORY_CODE_PARAM);

			// Fetch Logged In User Info
			String loggedInUser = StringUtils.EMPTY;
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			if (userDetailsBeanInstance != null) {
				loggedInUser = userDetailsBeanInstance.getId();
			}
			if (StringUtils.equalsIgnoreCase(methodID, CREATE_ALERT_CATEGORY_METHOD_ID)) {
				eventEnum = EventEnum.CREATE;
				processedResult = createAlertCategory(requestInstance, loggedInUser);
				processedResult.addParam(new Param("status", "success", FabricConstants.STRING));

			} else if (StringUtils.equalsIgnoreCase(methodID, EDIT_ALERT_CATEGORY_METHOD_ID)) {
				eventEnum = EventEnum.UPDATE;
				processedResult = editAlertCategory(requestInstance, loggedInUser);
				processedResult.addParam(new Param("status", "success", FabricConstants.STRING));

			} else if (StringUtils.equalsIgnoreCase(methodID, REORDER_ALERT_CATEGORY_METHOD_ID)) {
				eventEnum = EventEnum.UPDATE;
				processedResult = reorderAlertCategory(requestInstance, loggedInUser);
				processedResult.addParam(new Param("status", "success", FabricConstants.STRING));

			} else {
				// Unsupported Method Id. Return Empty Result
				diagnostic.prepareDebug("Unsupported Method Id. Returning Empty Result").log();
				return new Result();
			}

			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, eventEnum,
					ActivityStatusEnum.SUCCESSFUL, activityMessage);

		} catch (ApplicationException e) {
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();

			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, eventEnum,
					ActivityStatusEnum.FAILED, activityMessage);

			Result errorResult = new Result();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			errorResult.addParam(new Param("status", "failure", FabricConstants.STRING));
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
	 * Method to create the ALert Category
	 * 
	 * @param requestInstance
	 * @return operation Result
	 * @throws ApplicationException
	 */
	private Result createAlertCategory(DataControllerRequest requestInstance, String loggedInUser)
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
		String[] reqPermissions = {PermissionName.CREATE_ALERTS};
		if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
        {
			operationResult.addParam(new Param("Status", "Create Alert Category operation failed", FabricConstants.STRING));
            ErrorCodeEnum.ERR_22231.setErrorCode(operationResult);
            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
            return operationResult;        
        }
		
		String freqId = StringUtils.EMPTY, freqValue = StringUtils.EMPTY, freqTime = StringUtils.EMPTY;
		// Read Inputs
		String categoryCode = null;
		String categoryName = StringEscapeUtils.escapeHtml(requestInstance.getParameter(CATEGORY_NAME_PARAM));
		String channels = requestInstance.getParameter(CHANNELS_PARAM);
		String frequency = requestInstance.getParameter(FREQUENCY_PARAM);
		String containsAccountLevelAlerts = requestInstance.getParameter(CONTAINS_ACCOUNT_LEVEL_ALERTS_PARAM);
		String statusId = requestInstance.getParameter(STATUS_ID_PARAM);
		String displayPreference = requestInstance.getParameter(DISPLAY_PREFERENCE_PARAM);
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);

		if(StringUtils.isBlank(categoryName))
		{
			alert.prepareError("Please Enter CategoryName").log();
			ErrorCodeEnum.ERR_22217.setErrorCode(operationResult);
			return operationResult;
			
		}
		
		boolean isAlertCategoryAvaialble = AlertManagementHandler.isAlertCategoryAvaialable(categoryName, requestInstance);
		if (isAlertCategoryAvaialble == false) {
			alert.prepareError("Alert Category with the name :" + categoryName + " has already been defined").log();
			ErrorCodeEnum.ERR_22214.setErrorCode(operationResult);
			operationResult.addParam(new Param("status", "failure", FabricConstants.STRING));
			return operationResult;
		}
		
		// Display preferences input validation
		JSONArray displayPreferencesJSONArray = CommonUtilities.getStringAsJSONArray(displayPreference);
		if (displayPreferencesJSONArray != null && displayPreferencesJSONArray.length() > 0) {

			for (Object currObject : displayPreferencesJSONArray) {
				if (currObject instanceof JSONObject) {
					JSONObject currJSON = (JSONObject) currObject;
				
					if (currJSON.has("displayName")) {
						if(CommonUtilities.containSpecialChars(currJSON.optString("displayName"))) {
							String message = "Display Name should not conatin special characters";
							operationResult.addParam(new Param("message", message, FabricConstants.STRING));
				        	 ErrorCodeEnum.ERR_20541.setErrorCode(operationResult);
				             return operationResult;
						}
					}
					
					if (currJSON.has("description")) {
						if(CommonUtilities.containSpecialChars(currJSON.optString("description"))) {
							String message = "Description should not conatin special characters";
							operationResult.addParam(new Param("message", message, FabricConstants.STRING));
				        	 ErrorCodeEnum.ERR_20541.setErrorCode(operationResult);
				             return operationResult;
						}
					}
					
				}
			}
		}
		
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

		// Create Alert Category Definition
		diagnostic.prepareDebug("Setting Alert Category Definition...").log();
		Record createAlerCategoryDefinitionRecord = setAlertCategoryDefinition(categoryCode, categoryName, statusId,
				containsAccountLevelAlerts, freqId, freqValue, freqTime, loggedInUser, legalEntityId, requestInstance);
		operationResult.addRecord(createAlerCategoryDefinitionRecord);
		categoryCode = createAlerCategoryDefinitionRecord.getParamValueByName("categoryId");
		diagnostic.prepareDebug("Alert Category Definition Set").log();
		// Create Alert Category Channels
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
			diagnostic.prepareDebug("Setting Alert Category Channels...").log();
			Record alertCategoryChannels = createAlertCategoryChannels(categoryCode, legalEntityId, supportedChannelsList,
					loggedInUser, requestInstance);
			operationResult.addRecord(alertCategoryChannels);
			diagnostic.prepareDebug("Alert Category Channels Set").log();
		}

		// Edit Alert Category Text
		diagnostic.prepareDebug("Setting Alert Category Display Preferences...").log();
		Record setAlertCategoryDisplayPreferencesRecord = createAlertCategoryDisplayPreferences(categoryCode, legalEntityId,
				displayPreference, loggedInUser, requestInstance);
		operationResult.addRecord(setAlertCategoryDisplayPreferencesRecord);
		diagnostic.prepareDebug("Alert Category Display Preferences Set").log();

		return operationResult;
	}
	catch (Exception e) {
		Result errorResult = new Result();
		diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
		ErrorCodeEnum.ERR_20907.setErrorCode(errorResult);
		return errorResult;
	}
	}

	/**
	 * Method to edit the ALert Category
	 * 
	 * @param requestInstance
	 * @return operation Result
	 * @throws ApplicationException
	 */
	private Result editAlertCategory(DataControllerRequest requestInstance, String loggedInUser)
			throws ApplicationException {
	try {
		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20908);
		}
		
		Result operationResult = new Result();
		
		if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
			alert.prepareError("legalEntityId cannot be empty").log();
			ErrorCodeEnum.ERR_22232.setErrorCode(operationResult);
			return operationResult;
		}
		String[] reqPermissions = {PermissionName.UPDATE_ALERTS};
		if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
        {
			operationResult.addParam(new Param("Status", "Edit Alert Category operation failed", FabricConstants.STRING));
            ErrorCodeEnum.ERR_22231.setErrorCode(operationResult);
            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
            return operationResult;        
        }
		
		String freqId = StringUtils.EMPTY, freqValue = StringUtils.EMPTY, freqTime = StringUtils.EMPTY;
		// Read Inputs
		String categoryName = StringEscapeUtils.escapeHtml(requestInstance.getParameter(CATEGORY_NAME_PARAM));
		String categoryCode = requestInstance.getParameter(CATEGORY_CODE_PARAM);
		String channels = requestInstance.getParameter(CHANNELS_PARAM);
		String frequency = requestInstance.getParameter(FREQUENCY_PARAM);
		String containsAccountLevelAlerts = requestInstance.getParameter(CONTAINS_ACCOUNT_LEVEL_ALERTS_PARAM);
		String statusId = requestInstance.getParameter(STATUS_ID_PARAM);
		String addedDisplayPreference = requestInstance.getParameter(ADDED_DISPLAY_PREFERENCE_PARAM);
		String removedDisplayPreference = requestInstance.getParameter(REMOVED_DISPLAY_PREFERENCE_PARAM);
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
		
		
		if (StringUtils.isBlank(categoryCode)) {
			alert.prepareError("Missing Mandatory Input: AlertCategoryCode").log();
			ErrorCodeEnum.ERR_20920.setErrorCode(operationResult);
			return operationResult;
		}
		
		// Display preferences input validation
				JSONArray displayPreferencesJSONArray = CommonUtilities.getStringAsJSONArray(addedDisplayPreference);
				if (displayPreferencesJSONArray != null && displayPreferencesJSONArray.length() > 0) {

					for (Object currObject : displayPreferencesJSONArray) {
						if (currObject instanceof JSONObject) {
							JSONObject currJSON = (JSONObject) currObject;
							if (currJSON.has("displayName")) {
								if(CommonUtilities.containSpecialChars(currJSON.optString("displayName"))) {
									String message = "Display Name should not conatin special characters";
									operationResult.addParam(new Param("message", message, FabricConstants.STRING));
						        	 ErrorCodeEnum.ERR_20541.setErrorCode(operationResult);
						             return operationResult;
								}
							}
							
							if (currJSON.has("description")) {
								if(CommonUtilities.containSpecialChars(currJSON.optString("description"))) {
									String message = "Description should not conatin special characters";
									operationResult.addParam(new Param("message", message, FabricConstants.STRING));
						        	 ErrorCodeEnum.ERR_20541.setErrorCode(operationResult);
						             return operationResult;
								}
							}
							
						}
					}
				}
		
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

		// Edit Alert Category Definition
		diagnostic.prepareDebug("Setting Alert Category Definition...").log();
		containsAccountLevelAlerts = null; 
		Record editAlerCategoryDefinitionRecord = setAlertCategoryDefinition(categoryCode, categoryName, statusId,
				containsAccountLevelAlerts, freqId, freqValue, freqTime, loggedInUser, legalEntityId, requestInstance);
		operationResult.addRecord(editAlerCategoryDefinitionRecord);
		diagnostic.prepareDebug("Alert Category Definition Set").log();

		// Edit Alert Category Channels
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
			diagnostic.prepareDebug("Setting Alert Category Channels...").log();
			Record editAlertCategoryChannels = setAlertCategoryChannels(categoryCode,legalEntityId, supportedChannelsList,
					unSupportedChannelsList, loggedInUser, requestInstance);
			operationResult.addRecord(editAlertCategoryChannels);
			diagnostic.prepareDebug("Alert Category Channels Set").log();
		}

		// Edit Alert Category Text
		diagnostic.prepareDebug("Setting Alert Category Display Preferences...").log();
		Record setAlertCategoryDisplayPreferencesRecord = setAlertCategoryDisplayPreferences(categoryCode, legalEntityId,
				addedDisplayPreference, removedDisplayPreference, loggedInUser, requestInstance);
		operationResult.addRecord(setAlertCategoryDisplayPreferencesRecord);
		diagnostic.prepareDebug("Alert Category Display Preferences Set").log();

		return operationResult;
	}
		
	catch(Exception e){
		Result errorResult = new Result();
		diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
		ErrorCodeEnum.ERR_20908.setErrorCode(errorResult);
		return errorResult;
	}
	}

	/**
	 * Method to create the Alert Category Channel Texts
	 * 
	 * @param categoryCode
	 * @param displayPreference
	 * @param loggedInUserId
	 * @param requestInstance
	 * @return operation Record
	 * @throws ApplicationException
	 */
	private Record createAlertCategoryDisplayPreferences(String categoryCode, String legalEntityId, String displayPreference,
			String loggedInUserId, DataControllerRequest requestInstance) throws ApplicationException {

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20903);
		}

		Record categoryTextsRecord = new Record();
		categoryTextsRecord.setId("categoryTexts");

		String currOperationResponse = StringUtils.EMPTY, currLocale = StringUtils.EMPTY;
		JSONObject currOperationResponseJSON;

		JSONObject currJSON = null;
		Map<String, String> inputMap = new HashMap<>();

		// Handle Added Display Preferences
		JSONArray displayPreferencesJSONArray = CommonUtilities.getStringAsJSONArray(displayPreference);
		if (displayPreferencesJSONArray != null && displayPreferencesJSONArray.length() > 0) {

			for (Object currObject : displayPreferencesJSONArray) {
				if (currObject instanceof JSONObject) {
					currJSON = (JSONObject) currObject;
					if (currJSON.has("languageCode") && currJSON.has("displayName") && currJSON.has("description")) {
												
						inputMap.clear();
						currLocale = currJSON.optString("languageCode");
						inputMap.put("AlertCategoryId", categoryCode);
						inputMap.put("companyLegalUnit", legalEntityId);
						inputMap.put("LanguageCode", currLocale);
						inputMap.put("DisplayName", StringEscapeUtils.escapeHtml(currJSON.optString("displayName")));
						inputMap.put("Description", StringEscapeUtils.escapeHtml(currJSON.optString("description")));

						diagnostic.prepareDebug("Creating Display Preference for Locale:" + currLocale).log();
						inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
						inputMap.put("createdby", loggedInUserId);
						currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORYTEXT_CREATE,
								inputMap, null, requestInstance);
						currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);

						if (currOperationResponseJSON == null
								|| !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
								|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
							throw new ApplicationException(ErrorCodeEnum.ERR_20904);
						}
						diagnostic.prepareDebug("Successful CRUD Operation").log();
						categoryTextsRecord.addParam(new Param(currLocale, "Text Added", FabricConstants.STRING));
					}
				}
			}
		}
		return categoryTextsRecord;
	}

	/**
	 * Method to set the Alert Category Channel Texts
	 * 
	 * @param categoryCode
	 * @param addedDisplayPreference
	 * @param removedDisplayPreference
	 * @param loggedInUserId
	 * @param requestInstance
	 * @return operation Record
	 * @throws ApplicationException
	 */
	private Record setAlertCategoryDisplayPreferences(String categoryCode, String legalEntityId, String addedDisplayPreference,
			String removedDisplayPreference, String loggedInUserId, DataControllerRequest requestInstance)
			throws ApplicationException {

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20903);
		}

		Record categoryTextsRecord = new Record();
		categoryTextsRecord.setId("categoryTexts");

		String currOperationResponse = StringUtils.EMPTY, currLocale = StringUtils.EMPTY;
		JSONObject currOperationResponseJSON;

		JSONObject currJSON = null;
		Map<String, String> inputMap = new HashMap<>();

		// Fetch Existing Association
		inputMap.put(ODataQueryConstants.FILTER, "AlertCategoryId eq '" + categoryCode + "' and companyLegalUnit eq '" + legalEntityId + "'");
		inputMap.put(ODataQueryConstants.SELECT, "LanguageCode");
		diagnostic.prepareDebug("filterQuery for dbxalertcategorytext:" + inputMap.get(ODataQueryConstants.FILTER)).log();
		
		currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORYTEXT_READ, inputMap, null,
				requestInstance);
		currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
		if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
				|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DBXALERTCATEGORYTEXT_READ.name()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20903);
		}
		diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DBXALERTCATEGORYTEXT_READ.name()).log();
		Set<String> associatedDisplayPreferencesSet = new HashSet<>();
		JSONArray associatedDisplayPreferencesJSONArray = currOperationResponseJSON
				.optJSONArray("dbxalertcategorytext");
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
		inputMap.put("AlertCategoryId", categoryCode);
		inputMap.put("companyLegalUnit", legalEntityId);
		JSONArray removedDisplayPreferencesJSONArray = CommonUtilities.getStringAsJSONArray(removedDisplayPreference);
		if (removedDisplayPreferencesJSONArray != null && removedDisplayPreferencesJSONArray.length() > 0) {
			for (Object currObject : removedDisplayPreferencesJSONArray) {
				if (currObject instanceof String) {
					currLocale = (String) currObject;
					if (associatedDisplayPreferencesSet.contains(currLocale)) {
						diagnostic.prepareDebug("Removing Display Preference for Locale:" + currLocale).log();
						inputMap.put("LanguageCode", currLocale);
						currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORYTEXT_DELETE,
								inputMap, null, requestInstance);
						currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
						if (currOperationResponseJSON == null
								|| !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
								|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
							alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DBXALERTCATEGORYTEXT_DELETE.name()).log();
							throw new ApplicationException(ErrorCodeEnum.ERR_20903);
						}
						diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DBXALERTCATEGORYTEXT_DELETE.name()).log();
						associatedDisplayPreferencesSet.remove(currLocale);
					}
					categoryTextsRecord.addParam(new Param(currLocale, "Text Removed", FabricConstants.STRING));
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
						inputMap.put("AlertCategoryId", categoryCode);
						inputMap.put("companyLegalUnit", legalEntityId);
						inputMap.put("LanguageCode", currLocale);
						inputMap.put("DisplayName", StringEscapeUtils.escapeHtml(currJSON.optString("displayName")));
						inputMap.put("Description", StringEscapeUtils.escapeHtml(currJSON.optString("description")));

						if (associatedDisplayPreferencesSet.contains(currLocale)) {
							diagnostic.prepareDebug("Updating Display Preference for Locale:" + currLocale).log();
							inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
							inputMap.put("updatedby", loggedInUserId);
							currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORYTEXT_UPDATE,
									inputMap, null, requestInstance);
							currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
						} else {
							diagnostic.prepareDebug("Creating Display Preference for Locale:" + currLocale).log();
							inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
							inputMap.put("createdby", loggedInUserId);
							currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORYTEXT_CREATE,
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
						categoryTextsRecord.addParam(new Param(currLocale, "Text Added", FabricConstants.STRING));
					}
				}
			}
		}

		return categoryTextsRecord;
	}

	/**
	 * Method to create Alert Category Channels
	 * 
	 * @param categoryCode
	 * @param channelsList
	 * @param loggedInUserId
	 * @param requestInstance
	 * @return operation Record
	 * @throws ApplicationException
	 */
	private Record createAlertCategoryChannels(String categoryCode, String legalEntityId, List<String> channelsList, String loggedInUserId,
			DataControllerRequest requestInstance) throws ApplicationException {

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20906);
		}

		Record createtAlertCategoryChannelsRecord = new Record();
		createtAlertCategoryChannelsRecord.setId("categoryChannels");

		Map<String, String> inputMap = new HashMap<>();

		JSONObject currOperationResponseJSON;
		String currOperationResponse = StringUtils.EMPTY, timestamp = StringUtils.EMPTY;

		// Handle Supported Channels
		if (channelsList != null && !channelsList.isEmpty()) {
			inputMap.clear();
			inputMap.put("AlertCategoryId", categoryCode);
			inputMap.put("companyLegalUnit", legalEntityId);
			inputMap.put("createdby", loggedInUserId);
			for (String channelId : channelsList) {
				diagnostic.prepareDebug("Adding Support for Channel:" + channelId).log();
				timestamp = CommonUtilities.getISOFormattedLocalTimestamp();
				inputMap.put("createdts", timestamp);
				inputMap.put("ChannelID", channelId);
				currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTCATEGORYCHANNEL_CREATE, inputMap,
						null, requestInstance);
				currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
				if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
						|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
					alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.ALERTCATEGORYCHANNEL_CREATE.name()).log();
					throw new ApplicationException(ErrorCodeEnum.ERR_20906);
				}
				diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.ALERTCATEGORYCHANNEL_CREATE.name()).log();

				createtAlertCategoryChannelsRecord
						.addParam(new Param(channelId, String.valueOf(true), FabricConstants.STRING));
			}
		}
		return createtAlertCategoryChannelsRecord;
	}

	/**
	 * Method to Set Alert Category Channels
	 * 
	 * @param categoryCode
	 * @param supportedChannelsList
	 * @param unSupportedChannelsList
	 * @param loggedInUserId
	 * @param requestInstance
	 * @return operation Record
	 * @throws ApplicationException
	 */
	private Record setAlertCategoryChannels(String categoryCode, String legalEntityId, List<String> supportedChannelsList,
			List<String> unSupportedChannelsList, String loggedInUserId, DataControllerRequest requestInstance)
			throws ApplicationException {

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20906);
		}

		Record setAlertCategoryChannelsRecord = new Record();
		setAlertCategoryChannelsRecord.setId("categoryChannels");

		Map<String, String> inputMap = new HashMap<>();

		JSONObject currOperationResponseJSON, currJSON;
		String currOperationResponse = StringUtils.EMPTY, timestamp = StringUtils.EMPTY;

		// Fetch Existing Association
		Set<String> associatedChannelsSet = new HashSet<>();
		inputMap.put(ODataQueryConstants.FILTER, "AlertCategoryId eq '" + categoryCode + "' and companyLegalUnit eq '" + legalEntityId + "'");
		inputMap.put(ODataQueryConstants.SELECT, "ChannelID");
		diagnostic.prepareDebug("filterQuery for alertcategorychannel:" + inputMap.get(ODataQueryConstants.FILTER)).log();
		currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTCATEGORYCHANNEL_READ, inputMap, null,
				requestInstance);
		currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
		if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
				|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !currOperationResponseJSON.has("alertcategorychannel")) {
			alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.ALERTCATEGORYCHANNEL_READ.name()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20906);
		}
		diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.ALERTCATEGORYCHANNEL_READ.name()).log();
		JSONArray alertCategoryChannelsJSONArray = currOperationResponseJSON.optJSONArray("alertcategorychannel");
		for (Object currObj : alertCategoryChannelsJSONArray) {
			if (currObj instanceof JSONObject) {
				currJSON = (JSONObject) currObj;
				if (currJSON.has("ChannelID")) {
					associatedChannelsSet.add(currJSON.optString("ChannelID"));
				}
			}
		}

		// Handle Supported Channels
		if (supportedChannelsList != null && !supportedChannelsList.isEmpty()) {
			inputMap.clear();
			inputMap.put("AlertCategoryId", categoryCode);
			inputMap.put("companyLegalUnit", legalEntityId);
			inputMap.put("createdby", loggedInUserId);
			for (String channelId : supportedChannelsList) {
				if (!associatedChannelsSet.contains(channelId)) {
					diagnostic.prepareDebug("Adding Support for Channel:" + channelId).log();
					timestamp = CommonUtilities.getISOFormattedLocalTimestamp();
					inputMap.put("createdts", timestamp);
					inputMap.put("ChannelID", channelId);
					currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTCATEGORYCHANNEL_CREATE, inputMap,
							null, requestInstance);
					currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
					if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
							|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
						alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.ALERTCATEGORYCHANNEL_CREATE.name()).log();
						throw new ApplicationException(ErrorCodeEnum.ERR_20906);
					}
					diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.ALERTCATEGORYCHANNEL_CREATE.name()).log();
				}
				setAlertCategoryChannelsRecord
						.addParam(new Param(channelId, String.valueOf(true), FabricConstants.STRING));
				associatedChannelsSet.add(channelId);
			}
		}
		
		// Handle Removed Channels
		if (unSupportedChannelsList != null && !unSupportedChannelsList.isEmpty()) {
			inputMap.clear();
			inputMap.put("AlertCategoryId", categoryCode);
			inputMap.put("companyLegalUnit", legalEntityId);
			List<Channel> alertChannelList = new ArrayList<>();	
		
			for (String channelId : unSupportedChannelsList) {
				if (associatedChannelsSet.contains(channelId)) {
					diagnostic.prepareDebug("Removing Support for Channel:" + channelId).log();
					inputMap.put("ChannelID", channelId);
					currOperationResponse = Executor.invokeService(ServiceURLEnum.ALERTCATEGORYCHANNEL_DELETE, inputMap,
							null, requestInstance);
					currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
					if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
							|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
						alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.ALERTCATEGORYCHANNEL_DELETE.name()).log();
						throw new ApplicationException(ErrorCodeEnum.ERR_20905);
					}
					diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.ALERTCATEGORYCHANNEL_DELETE.name()).log();
					alertChannelList.add(new Channel(channelId, false));
					associatedChannelsSet.remove(channelId);
				}
				setAlertCategoryChannelsRecord
						.addParam(new Param(channelId, String.valueOf(false), FabricConstants.STRING));
			}
			if(!alertChannelList.isEmpty()) {				
				AlertManagementHandler.syncCustomerAlertChannels(requestInstance, "edit", categoryCode, legalEntityId,
						alertChannelList.stream().map(Channel::getId).collect(Collectors.joining(",")),
						ACConstants.ALERTPREFERNCES.CATEGORY.name());
			}			
		}
		return setAlertCategoryChannelsRecord;
	}

	private int getAletCategoriesCount(DataControllerRequest requestInstance) throws ApplicationException {
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.SELECT, "id");
        String operationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORY_READ, inputMap, null,
				requestInstance);
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
		if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
				&& operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& operationResponseJSON.has("dbxalertcategory")) {
			JSONArray categoryRecordsArray = operationResponseJSON.optJSONArray("dbxalertcategory");
			if (categoryRecordsArray != null) {
				return categoryRecordsArray.length();
			} else {
				return 0;
			}
		}
		alert.prepareError("Failed to Fetch alert categories count. Response" + operationResponse).log();
		throw new ApplicationException(ErrorCodeEnum.ERR_20134);
	}

	/**
	 * Method to set the Alert Category Definition
	 * 
	 * @param categoryCode
	 * @param categoryName
	 * @param statusId
	 * @param containsAccountLevelAlerts
	 * @param loggedInUserId
	 * @param requestInstance
	 * @return operation Record
	 * @throws ApplicationException
	 */
	private Record setAlertCategoryDefinition(String categoryCode, String categoryName, String statusId,
			String containsAccountLevelAlerts, String freqId, String freqValue, String freqTime, String loggedInUserId, String legalEntityId,
			DataControllerRequest requestInstance) throws ApplicationException {

		Record setAlertCategoryRecord = new Record();
		setAlertCategoryRecord.setId("setAlertCategoryDefinition");

		boolean isCreateMode = true;
		if (StringUtils.isBlank(categoryCode)) {
			// Consider as Create Mode when CategroyCode is Empty
			isCreateMode = true;
			categoryCode = CommonUtilities.getNewId().toString();
		} else {
			// Consider as Update Mode when CategroyCode is Non-Empty
			isCreateMode = false;
		}
		diagnostic.prepareDebug("isCreateMode:" + String.valueOf(isCreateMode)).log();

		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			if (isCreateMode == true) {
				throw new ApplicationException(ErrorCodeEnum.ERR_20907);
			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_20908);
			}
		}

		// Set Input Map
		Map<String, String> inputMap = new HashMap<>();
		
		inputMap.put("id", categoryCode);
    	inputMap.put("companyLegalUnit", legalEntityId);
		
		if (StringUtils.isNotBlank(categoryName)) {
			inputMap.put("Name", categoryName);
		}
		if (StringUtils.isNotBlank(statusId)) {
			inputMap.put("status_id", statusId);
		}
		// Checking for null because in editAlertCategory making this value as null as this is non-updateable field.
		if (containsAccountLevelAlerts!= null && StringUtils.equalsIgnoreCase(containsAccountLevelAlerts, "TRUE")) {
			inputMap.put("accountLevel", "1");
		} else if (containsAccountLevelAlerts!=null && StringUtils.equalsIgnoreCase(containsAccountLevelAlerts, "FALSE")) {
			inputMap.put("accountLevel", "0");
		}
		if(StringUtils.isNotBlank(freqId)){
    		inputMap.put("defaultFrequencyId", freqId);
		}
		if(StringUtils.isNotBlank(freqValue)){
		    inputMap.put("defaultFrequencyValue", freqValue);
		}
		if(StringUtils.isNotBlank(freqTime)){
    		inputMap.put("defaultFrequencyTime", freqTime);
		}
		
		
		// Create/Update Alert Category
		String setAlertCategoryResponse = StringUtils.EMPTY;
		String timestamp = CommonUtilities.getISOFormattedLocalTimestamp();
		if (isCreateMode) {
			inputMap.put("createdts", timestamp);
			inputMap.put("createdby", loggedInUserId);
			inputMap.put("DisplaySequence", String.valueOf(getAletCategoriesCount(requestInstance) + 1));
			setAlertCategoryResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORY_CREATE, inputMap, null,
					requestInstance);
		} else {
			inputMap.put("lastmodifiedts", timestamp);
			inputMap.put("modifiedby", loggedInUserId);
			setAlertCategoryResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORY_UPDATE, inputMap, null,
					requestInstance);
		}
		JSONObject setAlertCategoryResponseJSON = CommonUtilities.getStringAsJSONObject(setAlertCategoryResponse);
		if (setAlertCategoryResponseJSON == null || !setAlertCategoryResponseJSON.has(FabricConstants.OPSTATUS)
				|| setAlertCategoryResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			if (isCreateMode) {
				alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DBXALERTCATEGORY_CREATE.name()).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_20907);
			} else {
				alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DBXALERTCATEGORY_UPDATE.name()).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_20908);
			}
		}
		diagnostic.prepareDebug("Successful CRUD Operation").log();
		setAlertCategoryRecord.addParam(new Param("categoryId", categoryCode.toString(), FabricConstants.STRING));
		setAlertCategoryRecord.addParam(new Param("legalEntityId", legalEntityId.toString(), FabricConstants.STRING));
		setAlertCategoryRecord.addParam(new Param("status", "Success", FabricConstants.STRING));
		return setAlertCategoryRecord;
	}

	/**
	 * Method to set the Alert Category Order
	 * 
	 * @param requestInstance
	 * @param loggedInUserId
	 * @return operation Result
	 * @throws ApplicationException
	 */
	private Result reorderAlertCategory(DataControllerRequest requestInstance, String loggedInUserId)
			throws ApplicationException {
		try{
		if (requestInstance == null) {
			alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20908);
		}
		
		Result operationResult = new Result();

		if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
			alert.prepareError("legalEntityId cannot be empty").log();
			ErrorCodeEnum.ERR_22232.setErrorCode(operationResult);
			return operationResult;
		}
		String[] reqPermissions = {PermissionName.UPDATE_ALERTS};
		if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
        {
			operationResult.addParam(new Param("Status", "AlertCategory reorder operation failed", FabricConstants.STRING));
            ErrorCodeEnum.ERR_22231.setErrorCode(operationResult);
            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
            return operationResult;        
        }
	
		// Read Input
		String categoryOrder = requestInstance.getParameter(CATEGORY_ORDER_PARAM);
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);

		// Validate Input
		JSONObject categoryOrderJSONObject = null;
		if (StringUtils.isNotBlank(categoryOrder)) {
			try {
				categoryOrderJSONObject = new JSONObject(categoryOrder);
			} catch (NullPointerException | JSONException e) {
				// Malformed Alert Category Channel Preference JSON
				alert.prepareError("Malformed Alert Category Order JSON").log();
				ErrorCodeEnum.ERR_20928.setErrorCode(operationResult);
				return operationResult;
			}
		}

		// Set Alert Category Order
		String currCategoryId = StringUtils.EMPTY, currOperationResponse = StringUtils.EMPTY;
		JSONObject currOperationResponseJSON = null;
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("modifiedby", loggedInUserId);
		int currCategoryDisplaySequence = 0;
		if (categoryOrderJSONObject != null) {
			for (String key : categoryOrderJSONObject.keySet()) {
				if (categoryOrderJSONObject.opt(key) instanceof Integer) {
					currCategoryId = key;
					currCategoryDisplaySequence = categoryOrderJSONObject.optInt(key);
					inputMap.put("id", currCategoryId);
					inputMap.put("companyLegalUnit", legalEntityId);
					inputMap.put("DisplaySequence", String.valueOf(currCategoryDisplaySequence));
					inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
					currOperationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORY_UPDATE, inputMap,
							null, requestInstance);
					currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
					if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
							|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
						alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DBXALERTCATEGORY_UPDATE.name()).log();
						throw new ApplicationException(ErrorCodeEnum.ERR_20908);
					}
					diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DBXALERTCATEGORY_UPDATE.name()).log();

				}
			}
		}
		return operationResult;
	}
		catch(Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception in Reordering Customer Alert Categories. Exception:", e).log();
			ErrorCodeEnum.ERR_20908.setErrorCode(errorResult);
			return errorResult;
		}
	}
	
}
