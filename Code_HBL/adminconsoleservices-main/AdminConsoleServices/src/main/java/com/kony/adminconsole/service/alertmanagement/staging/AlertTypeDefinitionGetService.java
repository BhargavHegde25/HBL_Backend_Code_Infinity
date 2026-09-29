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
 * Service to Fetch the Alert Type Definition
 * 
 * @author Aditya Mankal
 */
public class AlertTypeDefinitionGetService implements JavaService2 {

	private static final String ALERT_TYPE_ID_PARAM = "AlertTypeId";
	private static final String DEFAULT_LOCALE = AlertManagementHandler.DEFAULT_LOCALE;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		Result processedResult = new Result();
		try {
			// Read Inputs
			String alertTypeId = requestInstance.getParameter(ALERT_TYPE_ID_PARAM);
			diagnostic.prepareDebug("Received Alert Type ID:" + alertTypeId).log();
			String acceptLanguage = requestInstance.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
			diagnostic.prepareDebug("Received Accept-Language Header:" + acceptLanguage).log();

			// Validate Inputs
			if (StringUtils.isBlank(alertTypeId)) {
				// Missing Alert Category ID
				alert.prepareError("Missing Alert Type ID").log();
				ErrorCodeEnum.ERR_20920.setErrorCode(processedResult);
				return processedResult;
			}

			// Format Accept Language Identifier
			if (StringUtils.isBlank(acceptLanguage)) {
				// Consider Default Locale if Accept-Language Header is Blank
				acceptLanguage = DEFAULT_LOCALE;
				diagnostic.prepareDebug("Received Accept-Language Header is empty. Returning Data of Default Locale." + DEFAULT_LOCALE).log();
			}
			acceptLanguage = CommonUtilities.formatLanguageIdentifier(acceptLanguage);
			
			if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
				alert.prepareError("legalEntityId cannot be empty").log();
				ErrorCodeEnum.ERR_22232.setErrorCode(processedResult);
				return processedResult;
			}
			
			String[] reqPermissions = {PermissionName.VIEW_ALERTS};

			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
	        {
				processedResult.addParam(new Param("Status", "Get Alert Type Definition operation failed", FabricConstants.STRING));
	            ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
	            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	            return processedResult;        
	        }
			String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
			// Fetch Alert Type Definition
			Record alertTypeDefintion = getAlertTypeDefinition(alertTypeId, acceptLanguage, requestInstance);
			processedResult.addRecord(alertTypeDefintion);

			Dataset alertTypeChannels = getAlertTypeChannels(alertTypeId,  requestInstance, legalEntityId);
            processedResult.addDataset(alertTypeChannels);
			
			// Fetch associated Alert Sub Types
			List<String> list = new ArrayList<String>();
			list.add(alertTypeId);
			Dataset alertSubTypes = AlertManagementHandler.getAlertSubTypesWithLocale(list, legalEntityId, requestInstance,acceptLanguage);
			processedResult.addDataset(alertSubTypes);

			// Fetch Display Preferences
			Dataset alertTypeDisplayPreferences = getAlertTypeDisplayPreferences(alertTypeId, requestInstance, legalEntityId);
			processedResult.addDataset(alertTypeDisplayPreferences);

		} catch (ApplicationException e) {
			Result errorResult = new Result();
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception in Fetching Customer Alert Category Definition. Checked Involved Operations. Exception:", e).log();
			ErrorCodeEnum.ERR_20920.setErrorCode(errorResult);
			return errorResult;
		}
		return processedResult;
	}

	private Dataset getAlertTypeChannels(String alertTypeId, DataControllerRequest requestInstance, String legalEntityId) throws ApplicationException {
		Dataset operationDataset = new Dataset();
		operationDataset.setId("alertGroupChannels");

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20899);
		}
		if (StringUtils.isBlank(alertTypeId)) {
			// Missing Mandatory Input. Return Empty Record
			alert.prepareError("Alert Type Id value is empty. Returning empty record.").log();
			return operationDataset;
		}
		// Construct Filter Query
		StringBuilder filterQueryBuffer = new StringBuilder();
		filterQueryBuffer.append("alertTypeId eq '" + alertTypeId + "' and companyLegalUnit eq '" + legalEntityId + "'");
		
		// Prepare Input Map
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, filterQueryBuffer.toString());
		inputMap.put(ODataQueryConstants.SELECT, "channelId");
		
		String operationResponse = Executor.invokeService(ServiceURLEnum.ALERTTYPECHANNEL_READ, inputMap, null, requestInstance);
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS) || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !operationResponseJSON.has("alerttypechannel")) {
			alert.prepareError("Failed to Read alerttypechannel").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20899);
		}
		diagnostic.prepareDebug("Successfully Read alerttypechannel").log();
		JSONArray alertTypeTextJSONArray = operationResponseJSON.optJSONArray("alerttypechannel");
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
	 * Method to get the Alert Type Definition
	 * 
	 * @param alertTypeId
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return Record containing Alert Type Definition
	 * @throws ApplicationException
	 */
	private Record getAlertTypeDefinition(String alertTypeId, String acceptLanguage, DataControllerRequest requestInstance) throws ApplicationException {
		Record operationRecord = new Record();
		operationRecord.setId("alertTypeDefinition");

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20927);
		}
		if (StringUtils.isBlank(alertTypeId)) {
			// Missing Mandatory Input. Return Empty Record
			alert.prepareError("Alert Type Id value is empty. Returning empty record.").log();
			return operationRecord;
		}

		diagnostic.prepareDebug("Attempting to Fetch Alert Type Definition").log();
		// Fetch Alert Type Definition
		JSONObject alertTypeDefintionJSON = AlertManagementHandler.getAlertTypeDefinition(alertTypeId, acceptLanguage, requestInstance);
		diagnostic.prepareDebug("Fetched Alert Type Definition").log();
		if (alertTypeDefintionJSON != null) {
			// Construct Record Object
			addStringParamToResult(operationRecord, alertTypeDefintionJSON ,"alerttype_Name" ,"name");
			addStringParamToResult(operationRecord, alertTypeDefintionJSON ,"alerttypetext_DisplayName","displayName");
			addStringParamToResult(operationRecord, alertTypeDefintionJSON ,"alerttype_id","alertCode");
			checkBooleanAndSetParam(operationRecord, alertTypeDefintionJSON,"alerttype_isAccountLevel",
																				"isAccountLevel","yes","no");
			addStringParamToResult(operationRecord, alertTypeDefintionJSON , "alerttypetext_LanguageCode",  "languageCode");
			addStringParamToResult(operationRecord, alertTypeDefintionJSON , "alerttype_Status_id", "typeStatus");
			addStringParamToResult(operationRecord, alertTypeDefintionJSON , "alerttypetext_Description", "typeDescription");
			addStringParamToResult(operationRecord, alertTypeDefintionJSON , "alerttype_freqId",  "defaultFrequencyId");
			addStringParamToResult(operationRecord, alertTypeDefintionJSON , "alerttype_freqValue" , "defaultFrequencyValue" );
			addStringParamToResult(operationRecord, alertTypeDefintionJSON , "alerttype_freqTime" , "defaultFrequencyTime" );
			addStringParamToResult(operationRecord, alertTypeDefintionJSON , "alerttype_companyLegalUnit" , "legalEntityId" );
		}
		return operationRecord;
	}

	private void checkBooleanAndSetParam(Record operationRecord, JSONObject alertTypeDefintionJSON,String jsonParamName,
			String respPrmName,	String trueValue, String falseValue) {
		String respValue = falseValue;
		if (StringUtils.equalsIgnoreCase(alertTypeDefintionJSON.optString(jsonParamName), "TRUE")
				|| StringUtils.equalsIgnoreCase(alertTypeDefintionJSON.optString(jsonParamName), "1")) {
			respValue = trueValue;			
		}		
		operationRecord.addParam(new Param(respPrmName, respValue, FabricConstants.STRING));
	}
	
	private void addStringParamToResult(Record operationRecord, JSONObject alertTypeDefintionJSON , 
			String paramNameInJson,String paramNameInResult) {
		operationRecord.addParam(new Param(paramNameInResult, alertTypeDefintionJSON.optString(paramNameInJson),
																					FabricConstants.STRING));
	}

	/**
	 * Method to fetch the display preferences of Alert Type
	 * 
	 * @param alertTypeId
	 * @param acceptLanguage
	 * @param requestInstance
	 * @return Display Preferences of Alert Type
	 * @throws ApplicationException
	 */
	private Dataset getAlertTypeDisplayPreferences(String alertTypeId, DataControllerRequest requestInstance, String legalEntityId) throws ApplicationException {
		Dataset operationDataset = new Dataset();
		operationDataset.setId("displayPreferences");
		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20899);
		}
		if (StringUtils.isBlank(alertTypeId)) {
			// Missing Mandatory Input. Return Empty Record
			alert.prepareError("Alert Type Id value is empty. Returning empty record.").log();
			return operationDataset;
		}
		// Construct Filter Query
		StringBuffer filterQueryBuffer = new StringBuffer();
		filterQueryBuffer.append("AlertTypeId eq '" + alertTypeId + "' and companyLegalUnit eq '" + legalEntityId + "'");

		// Prepare Input Map
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, filterQueryBuffer.toString());
		String operationResponse = Executor.invokeService(ServiceURLEnum.DBXALERTTYPETEXT_READ, inputMap, null, requestInstance);
		JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
		if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS) || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !operationResponseJSON.has("dbxalerttypetext")) {
			alert.prepareError("Failed to Read dbxalerttypetext").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20899);
		}
		diagnostic.prepareDebug("Successfully Read dbxalerttypetext").log();
		JSONArray alertTypeTextJSONArray = operationResponseJSON.optJSONArray("dbxalerttypetext");
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
	
}
