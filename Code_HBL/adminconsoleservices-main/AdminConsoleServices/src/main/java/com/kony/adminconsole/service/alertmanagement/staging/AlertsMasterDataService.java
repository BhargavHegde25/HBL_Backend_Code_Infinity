package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.kony.adminconsole.service.alertmanagement.utils.Constants;
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
 * Service to retrieve the Master Data of Alert Mangement
 * 
 * @author Aditya Mankal
 */
public class AlertsMasterDataService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String DEFAULT_LOCALE = AlertManagementHandler.DEFAULT_LOCALE;

	private static final String GET_EVENT_TYPES_METHOD_NAME = "getEventTypes";
	private static final String GET_ALERT_CHANNELS_METHOD_NAME = "getAlertChannels";
	private static final String GET_ALERT_FREQUENCY_METHOD_NAME = "getAlertFrequency";
	private static final String GET_EVENT_SUBTYPES_METHOD_NAME = "getEventSubTypes";
	private static final String GET_ALERT_ATTRIBUTES_METHOD_NAME = "getAlertAttributes";
	private static final String GET_ALERT_CONDITIONS_METHOD_NAME = "getAlertConditions";
	private static final String GET_ALERT_CONTENT_FIELDS_METHOD_NAME = "getAlertContentFields";
	private static final String GET_ALERT_RECIPIENT_TYPES_METHOD_NAME = "getAlertRecipientTypes";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		try {
			// Read Inputs
			String acceptLanguage = requestInstance.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
			diagnostic.prepareDebug("Received Accept-Language Header:" + acceptLanguage).log();

			// Format Accept Language Identifier
			if (StringUtils.isBlank(acceptLanguage)) {
				// Consider Default Locale if Accept-Language Header is Blank
				acceptLanguage = DEFAULT_LOCALE;
				diagnostic.prepareDebug(
						"Received Accept-Language Header is empty. Returning Data of Default Locale." + DEFAULT_LOCALE).log();
			}
			acceptLanguage = CommonUtilities.formatLanguageIdentifier(acceptLanguage);

			// Get Alert Attributes
			if (StringUtils.equalsIgnoreCase(methodID, GET_ALERT_ATTRIBUTES_METHOD_NAME)) {
				return getAlertAttributes(acceptLanguage, requestInstance);
			}

			// Get Alert Conditions
			if (StringUtils.equalsIgnoreCase(methodID, GET_ALERT_CONDITIONS_METHOD_NAME)) {
				return getAlertConditions(acceptLanguage, requestInstance);
			}

			// Get Alert Channels
			if (StringUtils.equalsIgnoreCase(methodID, GET_ALERT_CHANNELS_METHOD_NAME)) {
				return getAlertChannels(acceptLanguage, requestInstance);
			}

			// Get Alert Frequency
			if (StringUtils.equalsIgnoreCase(methodID, GET_ALERT_FREQUENCY_METHOD_NAME)) {
				return getAlertFrequency(acceptLanguage, requestInstance);
			}

			// Get Event Types
			if (StringUtils.equalsIgnoreCase(methodID, GET_EVENT_TYPES_METHOD_NAME)) {
				return getEventTypes(requestInstance);
			}

			// Get Event SubTypes
			if (StringUtils.equalsIgnoreCase(methodID, GET_EVENT_SUBTYPES_METHOD_NAME)) {
				return getEventSubTypes(requestInstance);
			}

			// Get Alert Content Fields
			if (StringUtils.equalsIgnoreCase(methodID, GET_ALERT_CONTENT_FIELDS_METHOD_NAME)) {
				return getAlertContentFields(requestInstance);
			}
			// Get Alert Recepient Fields
			if (StringUtils.equalsIgnoreCase(methodID, GET_ALERT_RECIPIENT_TYPES_METHOD_NAME)) {
				return getAlertRecipientTypes(requestInstance);
			}

			return new Result();
		} catch (ApplicationException e) {
			Result errorResult = new Result();
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20927.setErrorCode(errorResult);
			return errorResult;
		}

	}

	/**
	 * Method to get the list of frequencies supported by Alert Engine
	 * 
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return operation Result
	 * @throws ApplicationException
	 */
	private Result getAlertFrequency(String acceptLanguage, DataControllerRequest requestInstance)
			throws ApplicationException {

		Result operationResult = new Result();

		// Construct Input Map
		Map<String, String> inputMap = new HashMap<>();
		if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
			inputMap.put(ODataQueryConstants.FILTER, "(alertfrequencytext_languageCode eq '" + acceptLanguage + "')");
		} else {
			inputMap.put(ODataQueryConstants.FILTER, "(alertfrequencytext_languageCode eq '" + acceptLanguage
					+ "' or alertfrequencytext_languageCode eq '" + DEFAULT_LOCALE + "')");
		}
		diagnostic.prepareDebug("FILTER QUERY:" + inputMap.get(ODataQueryConstants.FILTER)).log();
		inputMap.put(ODataQueryConstants.ORDER_BY, "alertfrequency_sequence asc");

		// Fetch Channels
		String readFreqViewResponse = Executor.invokeService(ServiceURLEnum.ALERTFREQUENCY_VIEW_READ, inputMap, null,
				requestInstance);
		JSONObject readFreqViewResponseJSON = CommonUtilities.getStringAsJSONObject(readFreqViewResponse);

		if (readFreqViewResponseJSON == null || !readFreqViewResponseJSON.has(FabricConstants.OPSTATUS)
				|| readFreqViewResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readFreqViewResponseJSON.has("alertfrequency_view")) {
			alert.prepareError("Failed CRUD Operation").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20897);
		}

		// Successful Fabric Operation
		diagnostic.prepareDebug("Successful CRUD Operation").log();

		JSONObject currJSONObject;
		Dataset dataset = new Dataset();
		dataset.setId("frequency");

		JSONArray frequencyViewJSONArray = readFreqViewResponseJSON.optJSONArray("alertfrequency_view");

		// Filter Records based on Locale
		frequencyViewJSONArray = CommonUtilities.filterRecordsByLocale(frequencyViewJSONArray,
				"alertfrequencytext_languageCode", "alertfrequency_id", DEFAULT_LOCALE);
		frequencyViewJSONArray = CommonUtilities.sortJSONArrayOfJSONObjects(frequencyViewJSONArray,
				"alertfrequency_sequence", true, true);
		// Construct Response Dataset
		for (Object currObject : frequencyViewJSONArray) {
			if (currObject instanceof JSONObject) {
				currJSONObject = (JSONObject) currObject;
				Record currRecord = new Record();
				for (String currKey : currJSONObject.keySet()) {
					currRecord.addParam(new Param(currKey, currJSONObject.optString(currKey), FabricConstants.STRING));
				}
				currRecord.removeParamByName("alertfrequency_sequence");
				dataset.addRecord(currRecord);
			}
		}
		// Add current Dataset to Result Object
		operationResult.addDataset(dataset);

		return operationResult;
	}

	/**
	 * Method to get the list of channels supported by Alert Engine
	 * 
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return operation Result
	 * @throws ApplicationException
	 */
	private Result getAlertChannels(String acceptLanguage, DataControllerRequest requestInstance)
			throws ApplicationException {

		Result operationResult = new Result();

		// Construct Input Map
		Map<String, String> inputMap = new HashMap<>();
		if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
			inputMap.put(ODataQueryConstants.FILTER, "(channeltext_LanguageCode eq '" + acceptLanguage + "')");
		} else {
			inputMap.put(ODataQueryConstants.FILTER, "(channeltext_LanguageCode eq '" + acceptLanguage
					+ "' or channeltext_LanguageCode eq '" + DEFAULT_LOCALE + "')");
		}

		diagnostic.prepareDebug("FILTER QUERY:" + inputMap.get(ODataQueryConstants.FILTER)).log();

		// Fetch Channels
		String readChannelViewResponse = Executor.invokeService(ServiceURLEnum.CHANNEL_VIEW_READ, inputMap, null,
				requestInstance);
		JSONObject readChannelViewResponseJSON = CommonUtilities.getStringAsJSONObject(readChannelViewResponse);

		if (readChannelViewResponseJSON == null || !readChannelViewResponseJSON.has(FabricConstants.OPSTATUS)
				|| readChannelViewResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readChannelViewResponseJSON.has("channel_view")) {
			alert.prepareError("Failed CRUD Operation").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20897);
		}

		// Successful Fabric Operation
		diagnostic.prepareDebug("Successful CRUD Operation").log();

		JSONObject currJSONObject;
		Dataset dataset = new Dataset();
		dataset.setId("channels");

		JSONArray channelViewJSONArray = readChannelViewResponseJSON.optJSONArray("channel_view");

		// Filter Records based on Locale
		channelViewJSONArray = CommonUtilities.filterRecordsByLocale(channelViewJSONArray, "channeltext_LanguageCode",
				"channel_id", DEFAULT_LOCALE);
		channelViewJSONArray = CommonUtilities.sortJSONArrayOfJSONObjects(channelViewJSONArray, "channel_sequence",
				true, true);
		// Construct Response Dataset
		for (Object currObject : channelViewJSONArray) {
			if (currObject instanceof JSONObject) {
				currJSONObject = (JSONObject) currObject;
				Record currRecord = new Record();
				for (String currKey : currJSONObject.keySet()) {
					currRecord.addParam(new Param(currKey, currJSONObject.optString(currKey), FabricConstants.STRING));
				}
				currRecord.removeParamByName("channel_sequence");
				dataset.addRecord(currRecord);
			}
		}
		// Add current Dataset to Result Object
		operationResult.addDataset(dataset);

		return operationResult;
	}

	/**
	 * Method to get the Alert Attributes
	 * 
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return Operation Result
	 * @throws ApplicationException
	 */
	private Result getAlertAttributes(String acceptLanguage, DataControllerRequest requestInstance)
			throws ApplicationException, Exception {

		Result processedResult = new Result();
		
		if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
			alert.prepareError("legalEntityId cannot be empty").log();
			ErrorCodeEnum.ERR_22232.setErrorCode(processedResult);
			return processedResult;
		}
		
		String[] reqPermissions = {PermissionName.VIEW_ALERTS};

		if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
        {
			processedResult.addParam(new Param("Status", "Get Alert Attributes operation failed", FabricConstants.STRING));
            ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
            return processedResult;        
        }

		Dataset alertAttributesDataset = new Dataset();
		alertAttributesDataset.setId("alertAttributes");
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
		diagnostic.prepareDebug("Fetching Alert Attributes").log();
		Map<String, Record> alertAttributesMap = AlertManagementHandler.getAlertAttributes(acceptLanguage,requestInstance, legalEntityId);
		diagnostic.prepareDebug("Fetched Alert Attributes").log();

		// Construct Result Dataset
		for (Entry<String, Record> entry : alertAttributesMap.entrySet()) {
			alertAttributesDataset.addRecord(entry.getValue());
		}
		processedResult.addDataset(alertAttributesDataset);
		return processedResult;
	}

	/**
	 * Method to get the Alert Conditions
	 * 
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return Operation Result
	 * @throws ApplicationException
	 */
	private Result getAlertConditions(String acceptLanguage, DataControllerRequest requestInstance)
			throws ApplicationException {

		Result processedResult = new Result();

		Dataset alertConditionsDataset = new Dataset();
		alertConditionsDataset.setId("alertConditions");

		diagnostic.prepareDebug("Fetching Alert Conditions").log();
		Map<String, Record> alertConditionMap = AlertManagementHandler.getAlertConditions(acceptLanguage,
				requestInstance);
		diagnostic.prepareDebug("Fetched Alert Conditions").log();

		// Construct Result Dataset
		for (Entry<String, Record> entry : alertConditionMap.entrySet()) {
			alertConditionsDataset.addRecord(entry.getValue());
		}
		processedResult.addDataset(alertConditionsDataset);
		return processedResult;

	}

	/**
	 * Method to get the Event Types
	 * 
	 * @param requestInstance
	 * @return Operation Result
	 * @throws ApplicationException
	 */
	private Result getEventTypes(DataControllerRequest requestInstance) throws ApplicationException {
		Result processedResult = new Result();

		Dataset dataset = new Dataset();
		dataset.setId("eventTypes");

		// Fetch Event Types
		String operationResponse = Executor.invokeService(ServiceURLEnum.EVENTTYPE_READ, new HashMap<>(),
				new HashMap<>(), requestInstance);
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
				|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !operationResponseJSON.has("eventtype")) {
			alert.prepareError("Failed CRUD Operation").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20895);
		}
		// Successful Fabric Operation
		diagnostic.prepareDebug("Successful CRUD Operation").log();

		JSONArray recordsArray = operationResponseJSON.optJSONArray("eventtype");

		// Construct Response Dataset
		JSONObject currJSONObject;
		for (Object currObject : recordsArray) {
			if (currObject instanceof JSONObject) {
				currJSONObject = (JSONObject) currObject;
				Record currRecord = new Record();
				for (String currKey : currJSONObject.keySet()) {
					currRecord.addParam(new Param(currKey, currJSONObject.optString(currKey), FabricConstants.STRING));
				}
				dataset.addRecord(currRecord);
			}
		}
		// Add current Dataset to Result Object
		processedResult.addDataset(dataset);

		return processedResult;

	}

	/**
	 * Method to get the Event SubTypes
	 * 
	 * @param requestInstance
	 * @return Operation Result
	 * @throws ApplicationException
	 */
	private Result getEventSubTypes(DataControllerRequest requestInstance) throws ApplicationException {
		Result processedResult = new Result();

		String eventTypeId = requestInstance.getParameter("eventTypeId");
		if (StringUtils.isBlank(eventTypeId)) {
			alert.prepareError("Missing mandatory Input: Event Type Id").log();
			ErrorCodeEnum.ERR_20896.setErrorCode(processedResult);
			processedResult.addParam(new Param("message", "EventTypeId is a mandatory input", FabricConstants.STRING));
			return processedResult;
		}

		Dataset dataset = new Dataset();
		dataset.setId("eventSubTypes");

		// Prepare Input Map
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, "eventtypeid eq '" + eventTypeId + "'");

		// Fetch Event Types
		String operationResponse = Executor.invokeService(ServiceURLEnum.EVENTSUBTYPE_READ, inputMap, null,
				requestInstance);
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
				|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !operationResponseJSON.has("eventsubtype")) {
			alert.prepareError("Failed CRUD Operation. Response:" + operationResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20896);
		}
		// Successful Fabric Operation
		diagnostic.prepareDebug("Successful CRUD Operation").log();

		JSONArray recordsArray = operationResponseJSON.optJSONArray("eventsubtype");

		// Construct Response Dataset
		JSONObject currJSONObject;
		for (Object currObject : recordsArray) {
			if (currObject instanceof JSONObject) {
				currJSONObject = (JSONObject) currObject;
				Record currRecord = new Record();
				for (String currKey : currJSONObject.keySet()) {
					currRecord.addParam(new Param(currKey, currJSONObject.optString(currKey), FabricConstants.STRING));
				}
				dataset.addRecord(currRecord);
			}
		}
		// Add current Dataset to Result Object
		processedResult.addDataset(dataset);

		return processedResult;
	}

	/**
	 * Method to get the Alert Content Fields
	 * 
	 * @param requestInstance
	 * @return Operation Result
	 * @throws ApplicationException
	 */
	private Result getAlertContentFields(DataControllerRequest requestInstance) throws ApplicationException {
		Result processedResult = new Result();

		Dataset dataset = new Dataset();
		dataset.setId("alertContentFields");

		// Fetch Event Types
		String operationResponse = Executor.invokeService(ServiceURLEnum.ALERTCONTENTFIELDS_READ, new HashMap<>(),
				new HashMap<>(), requestInstance);
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
				|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !operationResponseJSON.has("alertcontentfields")) {
			alert.prepareError("Failed CRUD Operation").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20895);
		}
		// Successful Fabric Operation
		diagnostic.prepareDebug("Successful CRUD Operation").log();

		JSONArray recordsArray = operationResponseJSON.optJSONArray("alertcontentfields");

		// Construct Response Dataset
		JSONObject currJSONObject;
		for (Object currObject : recordsArray) {
			if (currObject instanceof JSONObject) {
				currJSONObject = (JSONObject) currObject;
				Record currRecord = new Record();
				for (String currKey : currJSONObject.keySet()) {
					currRecord.addParam(new Param(currKey, currJSONObject.optString(currKey), FabricConstants.STRING));
				}
				dataset.addRecord(currRecord);
			}
		}
		// Add current Dataset to Result Object
		processedResult.addDataset(dataset);

		return processedResult;

	}

	/**
	 * Method to get the list of recipient types supported by Alert Engine
	 * 
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return operation Result
	 * @throws ApplicationException
	 */
	private Result getAlertRecipientTypes(DataControllerRequest requestInstance) throws ApplicationException, Exception {
		Result processedResult = new Result();
		Dataset dataset = new Dataset();
		dataset.setId("recepientTypes");
		String isAccountLevelStr = requestInstance.getParameter("isAccountLevel");
		
		if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
			alert.prepareError("legalEntityId cannot be empty").log();
			ErrorCodeEnum.ERR_22232.setErrorCode(processedResult);
			return processedResult;
		}
		
		String[] reqPermissions = {PermissionName.VIEW_ALERTS};

		if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
        {
			processedResult.addParam(new Param("Status", "Get Alert Recipient types operation failed", FabricConstants.STRING));
            ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
            return processedResult;        
        }
		
		
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
		// Prepare Input Map
		Map<String, String> inputMap = new HashMap<>();
		if (isAccountLevelStr == null || StringUtils.equals(isAccountLevelStr, "0")) {
			inputMap.put(ODataQueryConstants.FILTER, "isaccountlevel eq '0' and companyLegalUnit eq '" + legalEntityId + "'");
		}
		else {
			inputMap.put(ODataQueryConstants.FILTER, "companyLegalUnit eq '" + legalEntityId + "'");
		}
		// Fetch recipient types
		String operationResponse = Executor.invokeService(ServiceURLEnum.ALERTRECIPIENTTYPE_READ, inputMap, null,
				requestInstance);
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
				|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !operationResponseJSON.has("alertrecipienttype")) {
			alert.prepareError("Failed CRUD Operation. Response:" + operationResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20975);
		}
		// Successful Fabric Operation
		diagnostic.prepareDebug("Successful CRUD Operation").log();

		JSONArray recordsArray = operationResponseJSON.optJSONArray("alertrecipienttype");

		// Construct Response Dataset
		
		for (Object currObject : recordsArray) {
			if (currObject instanceof JSONObject) {
				JSONObject currJSONObject = (JSONObject) currObject;
				Record currRecord = new Record();
				currRecord.addParam(new Param("id", currJSONObject.optString("id"), FabricConstants.INT));
				currRecord.addParam(new Param("Name", currJSONObject.optString("name"), FabricConstants.STRING));
				currRecord.addParam(new Param("isAccountLevel", currJSONObject.optString("isaccountlevel"),
						FabricConstants.INT));
				currRecord.addParam(new Param("service", currJSONObject.optString("servicename"), FabricConstants.STRING));
				currRecord.addParam(
						new Param("operation", currJSONObject.optString("operationname"), FabricConstants.STRING));
				currRecord.addParam(
						new Param("legalEntityId", currJSONObject.optString("companyLegalUnit"), FabricConstants.STRING));
				dataset.addRecord(currRecord);
			}
		}
		// Add current Dataset to Result Object
		processedResult.addDataset(dataset);

		return processedResult;
	}

}
