package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.PermissionName;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.ServiceUtil;
import com.kony.adminconsole.utilities.TABLE_OPERATION_MAPPING;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to fetch the Alert Sub Type Details
 *
 * @author Aditya Mankal
 */
public class AlertSubTypeDefinitionGetService implements JavaService2 {

	private static final String LOCALE_PARAM = "Locale";
	private static final String SUB_ALERT_ID_PARAM = "SubAlertId";
	private static final String TEMPLATE_STATUS_PARAM = "TemplateStatus";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		Result processedResult = new Result();
		try {
			// Read Inputs
			String subAlertId = requestInstance.getParameter(SUB_ALERT_ID_PARAM);
			diagnostic.prepareDebug("Received Sub-Alert ID:" + subAlertId).log();

			String templateStatusId = requestInstance.getParameter(TEMPLATE_STATUS_PARAM);
			diagnostic.prepareDebug("Received Template Status ID:" + templateStatusId).log();

			String locale = requestInstance.getParameter(LOCALE_PARAM);
			diagnostic.prepareDebug("Received Locale ID:" + locale).log();

			// Validate Inputs
			if (StringUtils.isBlank(subAlertId)) {
				// Missing Alert Category ID
				alert.prepareError("Missing Sub Alert ID").log();
				ErrorCodeEnum.ERR_20910.setErrorCode(processedResult);
				return processedResult;
			}

			// Format Language Identifier
			locale = CommonUtilities.formatLanguageIdentifier(locale);

			if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
				alert.prepareError("legalEntityId cannot be empty").log();
				ErrorCodeEnum.ERR_22232.setErrorCode(processedResult);
				return processedResult;
			}
			
			String[] reqPermissions = {PermissionName.VIEW_APP_CONTENT};

			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
	        {
				processedResult.addParam(new Param("Status", "Get Alert SUbType Definition operation failed", FabricConstants.STRING));
	            ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
	            alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	            return processedResult;        
	        }
			
			String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
			
			// Get Alert SubType Definition
			Record alertSubTypeDefinitionRecord = getAlertSubTypeDefinition(subAlertId, requestInstance, legalEntityId);
			processedResult.addRecord(alertSubTypeDefinitionRecord);

			// Get Alert SubType Communication Templates
			Record communicationTemplatesRecord = getCommunicationTemplatesOfAlertSubType(subAlertId, locale,
					templateStatusId, requestInstance, legalEntityId);
			processedResult.addRecord(communicationTemplatesRecord);

			// Fetch associated Applications of Alert Type
			Dataset alertTypeApps = invokealertassociations(subAlertId, requestInstance,
					TABLE_OPERATION_MAPPING.ALERTSUBTYPEAPP, "appTypes", ErrorCodeEnum.ERR_20929, legalEntityId);
			processedResult.addDataset(alertTypeApps);

			// Fetch associated User Types of Alert Type
			Dataset alertTypeUserTypes = invokealertassociations(subAlertId, requestInstance,
					TABLE_OPERATION_MAPPING.ALERTSUBTYPECUSTOMERTYPE, "userTypes", ErrorCodeEnum.ERR_20917, legalEntityId);
			processedResult.addDataset(alertTypeUserTypes);

			// Fetch associated Account Types of Alert Types
			Dataset accountTypes = invokealertassociations(subAlertId, requestInstance,
					TABLE_OPERATION_MAPPING.ALERTSUBTYPEACCOUNTTYPE, "accountTypes", ErrorCodeEnum.ERR_20916, legalEntityId);
			processedResult.addDataset(accountTypes);

			Dataset channelTypes = invokealertassociations(subAlertId, requestInstance,
					TABLE_OPERATION_MAPPING.ALERTSUBTYPECHANNEL, "alertsubtypeChannels", ErrorCodeEnum.ERR_20960, legalEntityId);
			processedResult.addDataset(channelTypes);

			Dataset displayTextTypes = invokealertassociations(subAlertId, requestInstance,
					TABLE_OPERATION_MAPPING.ALERTSUBTYPETEXT, "displayPreferences", ErrorCodeEnum.ERR_20899, legalEntityId);
			processedResult.addDataset(displayTextTypes);

		} catch (ApplicationException e) {
			Result errorResult = new Result();
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Unexpected Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20926.setErrorCode(errorResult);
			return errorResult;
		}
		return processedResult;
	}

	private Dataset invokealertassociations(String subAlertId, DataControllerRequest requestInstance,
			TABLE_OPERATION_MAPPING tableMapping, String responseDSName, ErrorCodeEnum errcode, String legalEntityId)
					throws ApplicationException {
		String selectStr = tableMapping.getTableName().equals(ACConstants.ALERTSUBTYPETEXT_TN) ? null
				: tableMapping.getCompositePKid();
		Result alertsubTypeResult = invokeGetService(requestInstance, tableMapping.getReadServiceName(),
				tableMapping.getAlertpkId1() + " eq '" + subAlertId + "' and companyLegalUnit eq '" + legalEntityId + "'", selectStr, tableMapping.getTableName(),
				errcode);
		Dataset ds = new Dataset(responseDSName);
		ds.addAllRecords(alertsubTypeResult.getDatasetById(tableMapping.getTableName()).getAllRecords());
		return ds;
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

	/**
	 * Method to get the Alert Sub Type data as a Record
	 * 
	 * @param subAlertId
	 * @param requestInstance
	 * @return Record containing the details of Alert Sub Type
	 * @throws ApplicationException
	 */
	private Record getAlertSubTypeDefinition(String subAlertId, DataControllerRequest requestInstance, String legalEntityId)
			throws ApplicationException {
		Record operationRecord = new Record();
		operationRecord.setId("subAlertDefinition");

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20927);
		}
		if (StringUtils.isBlank(subAlertId)) {
			// Missing Mandatory Inputs. Returning Empty Record
			alert.prepareError("Missing Mandatory Inputs. Returning Empty Record").log();
			return operationRecord;
		}

		String acceptLanguage = requestInstance.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		diagnostic.prepareDebug("Received Accept-Language Header:" + acceptLanguage).log();
		// Format Accept Language Identifier
		if (StringUtils.isBlank(acceptLanguage)) {
			// Consider Default Locale if Accept-Language Header is Blank
			acceptLanguage = AlertManagementHandler.DEFAULT_LOCALE;
			diagnostic.prepareDebug("Received Accept-Language Header is empty. Returning Data of Default Locale."
					+ AlertManagementHandler.DEFAULT_LOCALE).log();
		}
		acceptLanguage = CommonUtilities.formatLanguageIdentifier(acceptLanguage);
		Map<String, String> inputMap = new HashMap<>();
		String filterQuery = "id eq '" + subAlertId + "' and companyLegalUnit eq '" + legalEntityId + "'";
		inputMap.put(ODataQueryConstants.FILTER, filterQuery);
		String readAlertSubTypeResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPE_READ, inputMap, null,
				requestInstance);
		JSONObject readAlertSubTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertSubTypeResponse);
		if (readAlertSubTypeResponseJSON == null || !readAlertSubTypeResponseJSON.has(FabricConstants.OPSTATUS)
				|| readAlertSubTypeResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readAlertSubTypeResponseJSON.has("alertsubtype")) {
			alert.prepareError("Failed to Read alertsubtype").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20946);
		}
		diagnostic.prepareDebug("Successfully Read alertsubtype").log();
		
		JSONArray alertSubTypeJSONArray = readAlertSubTypeResponseJSON.optJSONArray("alertsubtype");
		if (alertSubTypeJSONArray != null && alertSubTypeJSONArray.length() > 0) {
			// Construct Operation Result
			if (alertSubTypeJSONArray.opt(0) instanceof JSONObject) {
				JSONObject currJSON = alertSubTypeJSONArray.optJSONObject(0);
				operationRecord.addParam(new Param("code", currJSON.optString("id"), FabricConstants.STRING));
				operationRecord.addParam(new Param("name", currJSON.optString("Name"), FabricConstants.STRING));
				operationRecord
				.addParam(new Param("description", currJSON.optString("Description"), FabricConstants.STRING));

				// adding the isAutoSubscribeEnabled  params 
				String autoSubscribe = currJSON.optString("isAutoSubscribeEnabled");
				if (StringUtils.equalsIgnoreCase(autoSubscribe, "1") || StringUtils.equalsIgnoreCase(autoSubscribe, "true")) {
					operationRecord.addParam(new Param("isAutoSubscribeEnabled","true" , FabricConstants.STRING));
				}else{
					operationRecord.addParam(new Param("isAutoSubscribeEnabled","false" , FabricConstants.STRING));
				}
				
				operationRecord.addParam(new Param("status", currJSON.optString("Status_id"), FabricConstants.STRING));
				operationRecord.addParam(new Param("defaultFrequencyId", currJSON.optString("defaultFrequencyId")));
				operationRecord
				.addParam(new Param("defaultFrequencyValue", currJSON.optString("defaultFrequencyValue")));
				operationRecord.addParam(new Param("defaultFrequencyTime", currJSON.optString("defaultFrequencyTime")));
				checkBooleanAndSetParam(operationRecord, currJSON, "isAccountLevel", "isAccountLevel", "yes", "no");
				checkBooleanAndSetParam(operationRecord, currJSON, "isGlobal", "alertType", "Global Alert",
						"User Alert");
				operationRecord.addParam(new Param("value1", currJSON.optString("value1"), FabricConstants.STRING));
				operationRecord.addParam(new Param("value2", currJSON.optString("value2"), FabricConstants.STRING));
				operationRecord.addParam(
						new Param("recipientType", currJSON.optString("recipienttype"), FabricConstants.STRING));
				operationRecord.addParam(new Param("externalsystem", currJSON.optString("externalSystem"), FabricConstants.STRING));
				operationRecord.addParam(new Param("legalEntityId", currJSON.optString("companyLegalUnit"), FabricConstants.STRING));
				Set<String> hashset = new HashSet<>();

				// Get Alert Attributes
				String alertAttributeId = currJSON.optString("attributeId");
				if (StringUtils.isNotBlank(alertAttributeId)) {
					hashset.add(alertAttributeId);
					Map<String, Record> alertAttributesMap = AlertManagementHandler.getAlertAttributes(hashset,
							acceptLanguage, requestInstance,legalEntityId);
					if (alertAttributesMap.containsKey(alertAttributeId)
							&& alertAttributesMap.get(alertAttributeId) != null)
						operationRecord.addRecord(alertAttributesMap.get(alertAttributeId));
				}

				// Get Alert Conditions
				String alertConditionId = currJSON.optString("alertConditionId");
				if (StringUtils.isNotBlank(alertConditionId)) {
					hashset.clear();
					hashset.add(alertConditionId);
					Map<String, Record> alertConditionMap = AlertManagementHandler.getAlertConditions(hashset,
							acceptLanguage, requestInstance,legalEntityId);
					if (alertConditionMap.containsKey(alertConditionId)
							&& alertConditionMap.get(alertConditionId) != null)
						operationRecord.addRecord(alertConditionMap.get(alertConditionId));
				}
			}

		}
		return operationRecord;
	}

	private void checkBooleanAndSetParam(Record operationRecord, JSONObject alertTypeDefintionJSON,
			String jsonParamName, String respPrmName, String trueValue, String falseValue) {
		String respValue = falseValue;
		if (StringUtils.equalsIgnoreCase(alertTypeDefintionJSON.optString(jsonParamName), "TRUE")
				|| StringUtils.equalsIgnoreCase(alertTypeDefintionJSON.optString(jsonParamName), "1")) {
			respValue = trueValue;
		}
		operationRecord.addParam(new Param(respPrmName, respValue, FabricConstants.STRING));
	}

	/**
	 * Method to get the Communication Templates of an Alert SubType
	 * 
	 * @param subAlertId
	 * @param acceptLanguage
	 * @param templateStatus
	 * @param requestInstance
	 * @return Record containing the Communication Templates of an Alert SubType
	 * @throws ApplicationException
	 */
	private Record getCommunicationTemplatesOfAlertSubType(String subAlertId, String acceptLanguage,
			String templateStatusId, DataControllerRequest requestInstance, String legalEntityId) throws ApplicationException {

		Record resultRecord = new Record();
		resultRecord.setId("communicationTemplates");

		if (requestInstance == null) {
			// Data Controller Request Instance is NULL. Throw Application Exception.
			alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20927);
		}
		if (StringUtils.isBlank(subAlertId)) {
			// Missing Mandatory Inputs. Returning Empty Record
			alert.prepareError("Missing Mandatory Inputs. Returning Empty Record").log();
			return resultRecord;
		}

		// Construct Filter Query
		String filterQuery = "communicationtemplate_AlertSubTypeId eq '" + subAlertId + "'";
		if (StringUtils.isNoneBlank(acceptLanguage)) {
			filterQuery = filterQuery + " and communicationtemplate_LanguageCode eq '" + acceptLanguage + "'";
		}
		if (StringUtils.isNotBlank(templateStatusId)) {
			filterQuery = filterQuery + " and communicationtemplate_Status_id eq '" + templateStatusId + "'";
		}
		filterQuery = filterQuery + " and communicationtemplate_companyLegalUnit eq '" + legalEntityId + "'";

		// Prepare Input Map
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, filterQuery);

		// Fetch Communication Templates
		String readCommunicationTemplateChannelViewResponse = Executor
				.invokeService(ServiceURLEnum.COMMUNICATIONTEMPLATE_CHANNEL_VIEW_READ, inputMap, null, requestInstance);

		// Check Fetch Status
		JSONObject readCommunicationTemplateChannelViewResponseJSON = CommonUtilities
				.getStringAsJSONObject(readCommunicationTemplateChannelViewResponse);
		if (readCommunicationTemplateChannelViewResponseJSON == null
				|| !readCommunicationTemplateChannelViewResponseJSON.has(FabricConstants.OPSTATUS)
				|| readCommunicationTemplateChannelViewResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCommunicationTemplateChannelViewResponseJSON.has("communicationtemplate_channel_view")) {
			alert.prepareError("Failed to Read communicationtemplate_channel_view").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20911);
		}
		diagnostic.prepareDebug("Successfully Read communicationtemplate_channel_view").log();

		JSONObject currJSON = null;
		String currLocale, currStatus, currChannel;
		Map<String, Record> localeChannelMap = null;
		Map<String, Map<String, Record>> statusRecordMap = new HashMap<>();
		JSONArray communicationTemplateArray = readCommunicationTemplateChannelViewResponseJSON
				.optJSONArray("communicationtemplate_channel_view");

		// Construct Response in hierarchical order. Nesting Sequence: Status ->
		// Channels -> Locales ->
		for (Object currObj : communicationTemplateArray) {
			if (currObj instanceof JSONObject) {

				currJSON = (JSONObject) currObj;
				currLocale = currJSON.optString("communicationtemplate_LanguageCode");
				currStatus = currJSON.optString("communicationtemplate_Status_id");
				currChannel = currJSON.optString("communicationtemplate_ChannelID");

				// Verify values
				if (StringUtils.isBlank(currStatus) || StringUtils.isBlank(currLocale)
						|| StringUtils.isBlank(currChannel)) {
					continue;
				}

				/*
				 * Each Status Record contains a list of Locale Records, and each Locale Record
				 * contains a list of Channel Records
				 */
				if (statusRecordMap.containsKey(currStatus)) {
					localeChannelMap = statusRecordMap.get(currStatus);
				} else {
					localeChannelMap = new HashMap<>();
					statusRecordMap.put(currStatus, localeChannelMap);
				}

				// Check if the current locale has already been associated to the current Status
				// Record
				Record currLocaleRecord = null;
				if (localeChannelMap.containsKey(currLocale)) {
					currLocaleRecord = localeChannelMap.get(currLocale);
				} else {
					currLocaleRecord = new Record();
					currLocaleRecord.setId(currLocale);
					localeChannelMap.put(currLocale, currLocaleRecord);
				}

				// Create a new record for every channel of the current locale
				Record currChannelRecord = new Record();
				currChannelRecord.setId(currChannel);

				// Populate the template content
				currChannelRecord.addParam(new Param("templateId", currJSON.optString("communicationtemplate_id"),
						FabricConstants.STRING));
				currChannelRecord.addParam(new Param("templateName", currJSON.optString("communicationtemplate_Name"),
						FabricConstants.STRING));
				currChannelRecord.addParam(new Param("languageCode",
						currJSON.optString("communicationtemplate_LanguageCode"), FabricConstants.STRING));
				currChannelRecord.addParam(new Param("statusId", currJSON.optString("communicationtemplate_Status_id"),
						FabricConstants.STRING));

				currChannelRecord.addParam(new Param("channelId", currJSON.optString("communicationtemplate_ChannelID"),
						FabricConstants.STRING));
				currChannelRecord.addParam(new Param("channelDisplayName",
						currJSON.optString("channeltext_Description"), FabricConstants.STRING));
				currChannelRecord.addParam(
						new Param("content", currJSON.optString("communicationtemplate_Text"), FabricConstants.STRING));

				if (currJSON.has("communicationtemplate_Subject")) {
					currChannelRecord.addParam(new Param("templateSubject",
							currJSON.optString("communicationtemplate_Subject"), FabricConstants.STRING));
				}
				if (currJSON.has("communicationtemplate_SenderName")) {
					currChannelRecord.addParam(new Param("senderName",
							currJSON.optString("communicationtemplate_SenderName"), FabricConstants.STRING));
				}
				if (currJSON.has("communicationtemplate_SenderEmail")) {
					currChannelRecord.addParam(new Param("senderEmail",
							currJSON.optString("communicationtemplate_SenderEmail"), FabricConstants.STRING));
				}
				currChannelRecord.addParam(new Param("softdeleteflag",
						currJSON.optString("communicationtemplate_softdeleteflag"), FabricConstants.STRING));
				currChannelRecord.addParam(new Param("legalEntityId",
						currJSON.optString("communicationtemplate_companyLegalUnit"), FabricConstants.STRING));

				// Add the current Channel Record to the Current Locale Record
				currLocaleRecord.addRecord(currChannelRecord);
			}
		}

		// Traverse the prepared map to construct the Result Record
		for (Entry<String, Map<String, Record>> statusRecordMapEntry : statusRecordMap.entrySet()) {

			currStatus = statusRecordMapEntry.getKey();

			// Allocate one record per status
			Record currStatusRecord = new Record();
			currStatusRecord.setId(currStatus);

			// Add the Templates of the current status to the status record
			localeChannelMap = statusRecordMapEntry.getValue();
			for (Entry<String, Record> localeChannelMapEntry : localeChannelMap.entrySet()) {
				currStatusRecord.addRecord(localeChannelMapEntry.getValue());
			}

			// Add the prepared status record to the Result Record
			resultRecord.addRecord(currStatusRecord);
		}

		return resultRecord;
	}

}
