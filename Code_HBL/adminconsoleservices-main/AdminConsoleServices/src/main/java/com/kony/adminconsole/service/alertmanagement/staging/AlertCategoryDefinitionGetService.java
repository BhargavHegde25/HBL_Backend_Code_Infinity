package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
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
import com.kony.adminconsole.service.alertmanagement.utils.Constants;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.PermissionName;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to Fetch the Alert Category Definition
 * 
 * @author Aditya Mankal
 */
public class AlertCategoryDefinitionGetService implements JavaService2 {

	private static final String ALERT_CATEGORY_ID_PARAM = "AlertCategoryId";

	private static final String DEFAULT_LOCALE = AlertManagementHandler.DEFAULT_LOCALE;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		Result processedResult = new Result();

		try {
			if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
				alert.prepareError("legalEntityId cannot be empty").log();
				ErrorCodeEnum.ERR_22232.setErrorCode(processedResult);
				return processedResult;
			}
			String[] reqPermissions = {PermissionName.VIEW_ALERTS};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
				processedResult.addParam(new Param("Status", "Alert Category Definition Get operation failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return processedResult;        
            }
			
			// Read Inputs
			diagnostic.prepareDebug("request parameters: "+requestInstance.getParameterNames()).log();
			String alertCategoryId = requestInstance.getParameter(ALERT_CATEGORY_ID_PARAM);
			diagnostic.prepareDebug("Received Alert Category ID:" + alertCategoryId).log();
			String acceptLanguage = requestInstance.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
			diagnostic.prepareDebug("Received Accept-Language Header:" + acceptLanguage).log();
			String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
			diagnostic.prepareDebug("Received legalEntityId: " + legalEntityId).log();

			// Validate Inputs
			if (StringUtils.isBlank(alertCategoryId)) {
				// Missing Alert Category ID
				alert.prepareError("Missing Alert Category ID").log();
				ErrorCodeEnum.ERR_20920.setErrorCode(processedResult);
				return processedResult;
			}

			// Fetch Logged In User Info
			String loggedInUser = StringUtils.EMPTY;
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			if (userDetailsBeanInstance != null) {
				loggedInUser = userDetailsBeanInstance.getId();
			}

			// Format Accept Language Identifier
			if (StringUtils.isBlank(acceptLanguage)) {
				// Consider Default Locale if Accept-Language Header is Blank
				acceptLanguage = DEFAULT_LOCALE;
				diagnostic.prepareDebug("Received Accept-Language Header is empty. Returning Data of Default Locale." + DEFAULT_LOCALE).log();
			}
			acceptLanguage = CommonUtilities.formatLanguageIdentifier(acceptLanguage);
			if (StringUtils.equalsIgnoreCase(loggedInUser, APICustomIdentityService.API_USER_ID)) {
				acceptLanguage = AlertManagementHandler.formatLocaleAsPerKonyMobileSDK(acceptLanguage);
			}

			// Get Alert Category Definition
			Record alertCategoryDefintionRecord = getAlertCategoryDefintion(alertCategoryId, legalEntityId, acceptLanguage, requestInstance);
			processedResult.addRecord(alertCategoryDefintionRecord);

			// Get Alert Category Channels
			Dataset alertCategoryChannelsDataset = getAlertCategoryChannels(alertCategoryId, legalEntityId, acceptLanguage, requestInstance);
			processedResult.addDataset(alertCategoryChannelsDataset);

			// Get Alert Groups
			processedResult = getAlertTypesOfAlertCategory(alertCategoryId, legalEntityId, acceptLanguage, requestInstance, processedResult);
		
			// Get Alert Category Display Preference
			Dataset alertCategoryDisplayPreferenceDataset = getAlertCategoryDisplayPreferences(alertCategoryId, legalEntityId, requestInstance);
			processedResult.addDataset(alertCategoryDisplayPreferenceDataset);

		} catch (ApplicationException e) {
			Result errorResult = new Result();
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception in Fetching Customer Alert Category Definition Preference. Exception:", e).log();
			ErrorCodeEnum.ERR_20914.setErrorCode(errorResult);
			return errorResult;
		}
		return processedResult;
	}

	/**
	 * Method to get basic attributes of Alert category as a Record
	 * 
	 * @param alertCategoryId
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return Record containing Alert Category Information
	 * @throws ApplicationException
	 */
	private Record getAlertCategoryDefintion(String alertCategoryId, String legalEntityId, String acceptLanguage, DataControllerRequest requestInstance) throws ApplicationException {

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20927);
		}

		Record operationRecord = new Record();
		operationRecord.setId("categoryDefintion");

		if (StringUtils.isBlank(alertCategoryId)) {
			// Missing Mandatory Input. Return Empty Record
			alert.prepareError("Alert Category Id value is empty. Returning empty record.").log();
			return operationRecord;
		}

		if (StringUtils.isBlank(acceptLanguage)) {
			// Consider Default Locale if Accept-Language Header is Blank
			acceptLanguage = DEFAULT_LOCALE;
			diagnostic.prepareDebug("Received Accept-Language Header is empty. Returning Data of Default Locale." + DEFAULT_LOCALE).log();
		}

		// Construct Filter Query
		String filterQuery;
		if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
			filterQuery = "(alertcategory_id eq '" + alertCategoryId + "' and alertcategorytext_LanguageCode eq '" + acceptLanguage + "' and alertcategory_companyLegalUnit eq '"+ legalEntityId + "')";
		} else {
			filterQuery = "alertcategory_id eq '" + alertCategoryId +"' and alertcategory_companyLegalUnit eq '" + legalEntityId + "' and (alertcategorytext_LanguageCode eq '" + acceptLanguage + "' or alertcategorytext_LanguageCode eq '"
					+ DEFAULT_LOCALE + "')";
		}
		diagnostic.prepareDebug("filterQuery for alertcategory_view:" + filterQuery).log();
		// Construct Input Map
		Map<String, String> paramaterMap = new HashMap<>();
		paramaterMap.put(ODataQueryConstants.FILTER, filterQuery);
		paramaterMap.put(ODataQueryConstants.ORDER_BY, "alertcategory_DisplaySequence");
		paramaterMap.put(ODataQueryConstants.SELECT,
				"alertcategory_id,alertcategory_status_id,alertcategory_companyLegalUnit,alertcategory_accountLevel,alertcategory_DisplaySequence,alertcategorytext_LanguageCode,alertcategory_Name,alertcategorytext_DisplayName,alertcategory_freqId,alertcategory_freqTime,alertcategory_freqValue");

		// Read Alert Category View
		String readAlertCategoryViewResponse = Executor.invokeService(ServiceURLEnum.ALERTCATEGORY_VIEW_READ, paramaterMap, null, requestInstance);
		JSONObject readAlertCategoryViewResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertCategoryViewResponse);
		if (readAlertCategoryViewResponseJSON == null || !readAlertCategoryViewResponseJSON.has(FabricConstants.OPSTATUS)
				|| readAlertCategoryViewResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readAlertCategoryViewResponseJSON.has("alertcategory_view")) {
			// Failed CRUD Operation
			alert.prepareError("Failed to Read Alert Category View").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20915);
		}

		diagnostic.prepareDebug("Read Alert Category View").log();
		// Parse Response and construct result Record
		JSONArray alertCategoriesJSONArray = readAlertCategoryViewResponseJSON.optJSONArray("alertcategory_view");
		alertCategoriesJSONArray = CommonUtilities.filterRecordsByLocale(alertCategoriesJSONArray, "alertcategorytext_LanguageCode", "alertcategory_id", DEFAULT_LOCALE);

		// Construct Response Record
		diagnostic.prepareDebug("Constructing Response Record").log();
		if (alertCategoriesJSONArray != null && alertCategoriesJSONArray.length() > 0) {
			JSONObject currJSON;
			if (alertCategoriesJSONArray.optJSONObject(0) != null) {
				currJSON = alertCategoriesJSONArray.optJSONObject(0);
				operationRecord.addParam(new Param("categoryCode", currJSON.optString("alertcategory_id"), FabricConstants.STRING));
				operationRecord.addParam(new Param("categoryName", currJSON.optString("alertcategory_Name"), FabricConstants.STRING));
				operationRecord.addParam(new Param("displayName", currJSON.optString("alertcategorytext_DisplayName"), FabricConstants.STRING));
				operationRecord.addParam(new Param("languageCode", currJSON.optString("alertcategorytext_LanguageCode"), FabricConstants.STRING));
				operationRecord.addParam(new Param("categoryDisplaySequence", currJSON.optString("alertcategory_DisplaySequence"), FabricConstants.STRING));
				operationRecord.addParam(new Param("containsAccountLevelAlerts", currJSON.optString("alertcategory_accountLevel"), FabricConstants.STRING));
				operationRecord.addParam(new Param("categoryStatus", currJSON.optString("alertcategory_status_id"), FabricConstants.STRING));
				operationRecord.addParam(new Param("frequencyId", currJSON.optString("alertcategory_freqId"), FabricConstants.STRING));
				operationRecord.addParam(new Param("frequencyValue", currJSON.optString("alertcategory_freqValue"), FabricConstants.STRING));
				operationRecord.addParam(new Param("frequencyTime", currJSON.optString("alertcategory_freqTime"), FabricConstants.STRING));
				operationRecord.addParam(new Param("legalEntityId", currJSON.optString("alertcategory_companyLegalUnit"), FabricConstants.STRING));
			}
		}

		diagnostic.prepareDebug("Returning success response").log();
		return operationRecord;
	}

	/**
	 * Method to fetch the display preferences of Alert Category
	 * 
	 * @param alertCategoryId
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return Display Preferences of Alert Category
	 * @throws ApplicationException
	 */
	private Dataset getAlertCategoryDisplayPreferences(String alertCategoryId, String legalEntityId, DataControllerRequest requestInstance) throws ApplicationException {

		Dataset operationDataset = new Dataset();
		operationDataset.setId("displayPreferences");

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20898);
		}
		if (StringUtils.isBlank(alertCategoryId)) {
			// Missing Mandatory Input. Return Empty Record
			alert.prepareError("Alert Category Id value is empty. Returning empty record.").log();
			return operationDataset;
		}

		// Construct Filter Query
		StringBuffer filterQueryBuffer = new StringBuffer();
		filterQueryBuffer.append("AlertCategoryId eq '" + alertCategoryId + "' and companyLegalUnit eq '"+ legalEntityId + "'");
   		diagnostic.prepareDebug("filterQuery for dbxalertcategorytext:" + filterQueryBuffer).log();

		// Prepare Input Map
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, filterQueryBuffer.toString());
		String operationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORYTEXT_READ, inputMap, null, requestInstance);
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS) || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !operationResponseJSON.has("dbxalertcategorytext")) {
			alert.prepareError("Failed to Read dbxalertcategorytext").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20898);
		}
		diagnostic.prepareDebug("Successfully Read dbxalertcategorytext").log();
		JSONArray alertTypeTextJSONArray = operationResponseJSON.optJSONArray("dbxalertcategorytext");
		if (alertTypeTextJSONArray != null && alertTypeTextJSONArray.length() > 0) {
			JSONObject jsonObject = null;
			for (Object currObject : alertTypeTextJSONArray) {
				jsonObject = (JSONObject) currObject;
				Record currRecord = new Record();
				for (String currKey : jsonObject.keySet()) {
					currRecord.addParam(new Param(currKey, jsonObject.optString(currKey), FabricConstants.STRING));
				}
				operationDataset.addRecord(currRecord);
			}
		}
		return operationDataset;
	}

	/**
	 * Method to Fetch Channels of Alert Category
	 * 
	 * @param alertCategoryId
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return Dataset containing Channels of Alert Category
	 * @throws ApplicationException
	 */
	private Dataset getAlertCategoryChannels(String alertCategoryId, String legalEntityId, String acceptLanguage, DataControllerRequest requestInstance) throws ApplicationException {
		Dataset categoryChannelsDataset = new Dataset();
		categoryChannelsDataset.setId("categoryChannels");

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20912);
		}
		if (StringUtils.isBlank(alertCategoryId)) {
			// Missing Mandatory Input. Return Empty Record
			alert.prepareError("Alert Category Id value is empty. Returning empty record.").log();
			return categoryChannelsDataset;
		}

		// Fetch supported channels of Alert Category
		diagnostic.prepareDebug("Fetching supported channels of Alert Category").log();
		List<String> supportedChannelsList = AlertManagementHandler.getSupportedChannelsOfAlertCategory(alertCategoryId, legalEntityId, requestInstance);
		diagnostic.prepareDebug("Fetched supported channels of Alert Category").log();

		// Construct input map
		Map<String, String> inputMap = new HashMap<>();
		if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
			inputMap.put(ODataQueryConstants.FILTER, "(channeltext_LanguageCode eq '" + acceptLanguage + "')");
		} else {
			inputMap.put(ODataQueryConstants.FILTER, "(channeltext_LanguageCode eq '" + acceptLanguage + "' or channeltext_LanguageCode eq '" + DEFAULT_LOCALE + "')");
		}

		// Fetch all channels and collate supported channel information
		String readChannelViewResponse = Executor.invokeService(ServiceURLEnum.CHANNEL_VIEW_READ, inputMap, null, requestInstance);
		JSONObject readChannelViewResponseJSON = CommonUtilities.getStringAsJSONObject(readChannelViewResponse);

		if (readChannelViewResponseJSON == null || !readChannelViewResponseJSON.has(FabricConstants.OPSTATUS) ||
				readChannelViewResponseJSON.getInt(FabricConstants.OPSTATUS) != 0 || !readChannelViewResponseJSON.has("channel_view")) {
			// Failed CRUD Operation
			alert.prepareError("Failed to Communication Channels").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20912);
		}
		// Successful CRUD Operation
		diagnostic.prepareDebug("Read Communication Channels").log();

		String currChannelID;
		JSONObject currJSONObject;

		// Parse response and construct result Dataset
		JSONArray channelViewJSONArray = readChannelViewResponseJSON.optJSONArray("channel_view");
		channelViewJSONArray = CommonUtilities.filterRecordsByLocale(channelViewJSONArray, "channeltext_LanguageCode", "channel_id", DEFAULT_LOCALE);
		for (Object currObject : channelViewJSONArray) {
			if (currObject instanceof JSONObject) {
				currJSONObject = (JSONObject) currObject;
				currChannelID = currJSONObject.optString("channel_id");
				Record currRecord = new Record();
				currRecord.addParam(new Param("channelID", currChannelID, FabricConstants.STRING));
				currRecord.addParam(new Param("channelDisplayName", currJSONObject.optString("channeltext_Description"), FabricConstants.STRING));
				currRecord.addParam(new Param("isChannelSupported", String.valueOf(supportedChannelsList.contains(currChannelID)), FabricConstants.STRING));
				categoryChannelsDataset.addRecord(currRecord);
			}
		}

		return categoryChannelsDataset;
	}

	/**
	 * Method to fetch the Alert Types of Alert Category
	 * 
	 * @param alertCategoryId
	 * @param acceptLanguage
	 * @param requestInstance
	 * @param processedResult 
	 * @return Dataset containing Alert Types
	 * @throws ApplicationException
	 */
	private Result getAlertTypesOfAlertCategory(String alertCategoryId, String legalEntityId, String acceptLanguage, DataControllerRequest requestInstance, Result processedResult) throws ApplicationException {		
		Dataset alertTypesDataset = new Dataset();
		alertTypesDataset.setId("alertGroups");

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20927);
		}
		if (StringUtils.isBlank(alertCategoryId)) {
			// Missing Mandatory Input. Return Empty Record
			alert.prepareError("Alert Category Id value is empty. Returning empty record.").log();
			processedResult.addDataset(alertTypesDataset);
			return processedResult;
		}

		// Fetch the Alert Types of Alert Category
		diagnostic.prepareDebug("Fetching Alert Types of Alert Category. Alert Category Id:" + alertCategoryId).log();
		JSONArray alertTypesArray = AlertManagementHandler.getAlertTypesOfAlertCategory(alertCategoryId, legalEntityId, acceptLanguage, requestInstance);
		diagnostic.prepareDebug("Fetched Alert Types of Alert Category. Alert Category Id:" + alertCategoryId).log();

		// Parse Alert Types Data and construct Result Dataset
		ArrayList<String> list = new ArrayList<String>();
		if (alertTypesArray != null && alertTypesArray.length() > 0) {
			JSONObject currJSON;
			for (Object currObject : alertTypesArray) {
				if (currObject instanceof JSONObject) {
					Record currAlertTypeRecord = new Record();
					currJSON = (JSONObject) currObject;
					currAlertTypeRecord.addParam(new Param("name", currJSON.optString("alerttype_Name"), FabricConstants.STRING));
					currAlertTypeRecord.addParam(new Param("code", currJSON.optString("alerttype_id"), FabricConstants.STRING));
					currAlertTypeRecord.addParam(new Param("statusId", currJSON.optString("alerttype_Status_id"), FabricConstants.STRING));
					currAlertTypeRecord.addParam(new Param("displayName", currJSON.optString("alerttypetext_DisplayName"), FabricConstants.STRING));
					currAlertTypeRecord.addParam(new Param("languageCode", currJSON.optString("alerttypetext_LanguageCode"), FabricConstants.STRING));
					currAlertTypeRecord.addParam(new Param("displaySequence", currJSON.optString("alerttype_DisplaySequence"), FabricConstants.STRING));
					currAlertTypeRecord.addParam(new Param("legalEntityId", currJSON.optString("alerttype_companyLegalUnit"), FabricConstants.STRING));
					list.add(currJSON.optString("alerttype_id"));
					
					alertTypesDataset.addRecord(currAlertTypeRecord);
				}
			}
			Dataset alertSubTypes = AlertManagementHandler.getAlertSubTypesWithLocale(list, legalEntityId, requestInstance, acceptLanguage);
			processedResult.addDataset(alertSubTypes);
			processedResult.addDataset(alertTypesDataset);
					
		}

		return processedResult;
	}

}
