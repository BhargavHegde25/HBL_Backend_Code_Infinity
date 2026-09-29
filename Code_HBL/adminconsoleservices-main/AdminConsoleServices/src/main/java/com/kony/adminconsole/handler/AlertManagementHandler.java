package com.kony.adminconsole.handler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.kony.adminconsole.jwt.auth.utils.LegalEntityUtil;
import com.kony.adminconsole.service.alertmanagement.utils.Constants;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.ServiceUtil;
import com.kony.adminconsole.utilities.StatusEnum;
import com.kony.adminconsole.utilities.TABLE_OPERATION_MAPPING;
import com.kony.adminconsole.utilities.ACConstants.ALERTPREFERNCES;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Handler to perform common operations on Alerts
 * 
 * @author Aditya Mankal
 */
public class AlertManagementHandler {

    public static final String DEFAULT_LOCALE = "en-US";
    public static final String IS_SUBSCRIBED_PARAM = "isSubscribed";
    public static final String IS_INITIAL_LOAD_PARAM = "isInitialLoad";
    public static final String CATEGORY_SUBSCRIPTION_PARAM = "categorySubscription";
    private static Map<String, String> ACCOUNT_TYPE_MAP = null;
    private static Map<String, String> ACCOUNT_TYPE_IDS_MAP = null;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    /**
     * Method to return the Channels selected by a customer for an Alert Category
     * 
     * @param customerId
     * @param alertCategoryId
     * @param requestInstance
     * @return Channels selected by a customer for an Alert Category
     * @throws ApplicationException
     */
    public static List<String> getCustomerChannelPreferencesOfAlertCategory(String customerId, String accountId,
            String accountTypeId, String alertCategoryId, DataControllerRequest requestInstance)
            throws ApplicationException {
        if (StringUtils.isBlank(customerId) || StringUtils.isBlank(alertCategoryId) || requestInstance == null) {
            alert.prepareError("Invalid Parameters. Failed to fetch Alert Category Preferences of Customer:" + customerId).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20913);
        }
        List<String> selectedChannelsList = new ArrayList<>();
        Map<String, String> parameterMap = new HashMap<>();
        String filter = "AlertCategoryID eq '" + alertCategoryId + "' and Customer_id eq'" + customerId + "'";

        if (StringUtils.isNotBlank(accountTypeId)) {
            filter += " and AccountType eq '" + accountTypeId + "'";
        }

        if (StringUtils.isNotBlank(accountId)) {
            filter += " and AccountId eq '" + accountId + "'";
        }

        parameterMap.put(ODataQueryConstants.FILTER, filter);
        String readCustomerAlertCategoryChannelResponse = Executor
                .invokeService(ServiceURLEnum.CUSTOMERALERTCATEGORYCHANNEL_READ, parameterMap, null, requestInstance);
        JSONObject readCustomerAlertCategoryChannelResponseJSON = CommonUtilities
                .getStringAsJSONObject(readCustomerAlertCategoryChannelResponse);
        if (readCustomerAlertCategoryChannelResponseJSON == null
                || !readCustomerAlertCategoryChannelResponseJSON.has(FabricConstants.OPSTATUS)
                || readCustomerAlertCategoryChannelResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                || !readCustomerAlertCategoryChannelResponseJSON.has("customeralertcategorychannel")) {
            // Failed read operation of Alert Category Channel Preferences
            alert.prepareError("Failed to fetch Alert Category Preferences of Customer:" + customerId).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20913);
        }

        alert.prepareError("Succesfully Fetched Alert Category Preferences of Customer:" + customerId).log();
        JSONArray customerAlertCategoryPreference = readCustomerAlertCategoryChannelResponseJSON
                .optJSONArray("customeralertcategorychannel");
        JSONObject currJSONObject;
        for (Object currObject : customerAlertCategoryPreference) {
            if (currObject instanceof JSONObject) {
                currJSONObject = (JSONObject) currObject;
                selectedChannelsList.add(currJSONObject.optString("ChannelId"));
            }
        }

        return selectedChannelsList;
    }

    /**
     * Method to fetch the List of supported channels of an Alert Category
     * 
     * @param alertCategoryId
     * @param requestInstance
     * @return List of Supported Channels
     * @throws ApplicationException
     */
    public static List<String> getSupportedChannelsOfAlertCategory(String alertCategoryId, String legalEntityId,
            DataControllerRequest requestInstance) throws ApplicationException {

        if (StringUtils.isBlank(alertCategoryId) || requestInstance == null) {
            alert.prepareError("Invalid Parameters. Failed to fetch List of Supported Channels for Alert Category:"
                    + alertCategoryId).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20912);
        }

        List<String> supportedChannelsList = new ArrayList<>();
        diagnostic.prepareDebug("Fetching List of Supported Channels for Alert Category:" + alertCategoryId).log();

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put(ODataQueryConstants.FILTER, "AlertCategoryId eq '" + alertCategoryId + "' and companyLegalUnit eq '"+ legalEntityId + "'");
        diagnostic.prepareDebug("filterQuery for alertcategorychannel:" + parameterMap.get(ODataQueryConstants.FILTER)).log();
        String readAlertCategoryChannelResponse = Executor.invokeService(ServiceURLEnum.ALERTCATEGORYCHANNEL_READ,
                parameterMap, null, requestInstance);
        JSONObject readAlertCategoryChannelResponseJSON = CommonUtilities
                .getStringAsJSONObject(readAlertCategoryChannelResponse);
        if (readAlertCategoryChannelResponseJSON == null
                || !readAlertCategoryChannelResponseJSON.has(FabricConstants.OPSTATUS)
                || readAlertCategoryChannelResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                || !readAlertCategoryChannelResponseJSON.has("alertcategorychannel")) {
            // Failed read operation of Alert Category Channel Preferences
            alert.prepareError("Failed to Fetch List of Supported Channels for Alert Category:" + alertCategoryId).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20912);
        } else {
            // Successful read operation of Alert Category Channel Preferences
            JSONObject currJSONObject;
            alert.prepareError("Succesfully Fetched List of Supported Channels for Alert Category:" + alertCategoryId).log();
            JSONArray alertCategoryChannelsArray = readAlertCategoryChannelResponseJSON
                    .optJSONArray("alertcategorychannel");
            for (Object currObject : alertCategoryChannelsArray) {
                if (currObject instanceof JSONObject) {
                    currJSONObject = (JSONObject) currObject;
                    supportedChannelsList.add(currJSONObject.optString("ChannelID"));
                }
            }
        }
        diagnostic.prepareDebug("Sucesfully Fetched List of Supported Channels for Alert Category:" + alertCategoryId).log();
        return supportedChannelsList;

    }

    /**
     * Method to get the specific Alert Types based on Alert Type ID of an Alert Category
     * 
     * @param alertTypeIdsList
     * @param alertCategoryId
     * @param acceptLanguage
     * @param requestInstance
     * @return Array of Alert Types
     * @throws ApplicationException
     */
    public static JSONArray getAlertTypes(String alertCategoryId, String legalEntityId, String acceptLanguage,
             boolean fetchActiveAlertsOnly, DataControllerRequest requestInstance)
            throws ApplicationException {

        // Construct Filter Query
        StringBuilder filterQueryBuffer = new StringBuilder();
        if (StringUtils.isBlank(acceptLanguage)) {
            acceptLanguage = DEFAULT_LOCALE;
        }
        if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
            filterQueryBuffer.append("(alerttypetext_LanguageCode eq '" + acceptLanguage +"' and alerttype_companyLegalUnit eq '"+ legalEntityId + "')");
        } else {
            filterQueryBuffer.append("(alerttypetext_LanguageCode eq '" + acceptLanguage
                    + "' or alerttypetext_LanguageCode eq '" + DEFAULT_LOCALE + "') and alerttype_companyLegalUnit eq '" +legalEntityId +"'");
        }

          // Add Alert Category Id to Filter Query
        if (StringUtils.isNotBlank(alertCategoryId)) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and ");
            }
            filterQueryBuffer.append("(alerttype_AlertCategoryId eq '" + alertCategoryId + "')");
        }
        if (fetchActiveAlertsOnly == true) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and ");
            }
            filterQueryBuffer.append("(alerttype_Status_id eq '" + StatusEnum.SID_ACTIVE.name() + "')");
        }

        String filterQuery = filterQueryBuffer.toString();
        filterQuery = filterQuery.trim();
        diagnostic.prepareDebug("Consctructed Filter Query:" + filterQuery).log();

        // Construct Input map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, filterQuery);
        inputMap.put(ODataQueryConstants.ORDER_BY, "alerttype_DisplaySequence asc");

        // Read Alert Type Texts
        String readAlertTypeTextResponse = Executor.invokeService(ServiceURLEnum.ALERTTYPE_VIEW_READ, inputMap, null,
                requestInstance);
        JSONObject readAlertTypeTextResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertTypeTextResponse);
        if (readAlertTypeTextResponseJSON != null && readAlertTypeTextResponseJSON.has(FabricConstants.OPSTATUS)
                && readAlertTypeTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readAlertTypeTextResponseJSON.has("alerttype_view")) {
            diagnostic.prepareDebug("Successful Read Operation of alerttype_view").log();
            JSONArray alertTypeTextJSONArray = readAlertTypeTextResponseJSON.optJSONArray("alerttype_view");
            alertTypeTextJSONArray = CommonUtilities.filterRecordsByLocale(alertTypeTextJSONArray,
                    "alerttypetext_LanguageCode", "alerttype_id", DEFAULT_LOCALE);
            return alertTypeTextJSONArray;
        } else {
            alert.prepareError("Failed Read Operation of alerttype_view").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20918);
        }

    }
    
    public static Function<Record,String> getAlertIdAsStringFunction = rec2 -> 
                   rec2.getParamValueByName("alerttype_id") != null ? String.valueOf(rec2.getParamValueByName("alerttype_id"))
                		   : null;
                   
    public static Function<Record,String> getAlertSubTypeIdAsStringFunction = rec2 -> 
                   rec2.getParamValueByName("alertsubtype_id") != null ? String.valueOf(rec2.getParamValueByName("alertsubtype_id"))
                		   : null;
        
    public static List<Record> getAlertTypesWithLocale(String alertCategoryId, String acceptLanguage,
    		boolean fetchActiveAlertsOnly, DataControllerRequest requestInstance, String legalEntityId)
    				throws ApplicationException {
    	// Construct Filter Query

    	if (StringUtils.isBlank(acceptLanguage)) {
    		acceptLanguage = DEFAULT_LOCALE;
    	}
    	String filterQuery = getFiltersForAlertTypesView(alertCategoryId, acceptLanguage, 
				fetchActiveAlertsOnly);

		if (filterQuery.length() > 0) {
			filterQuery = filterQuery + " and ";
		}
		filterQuery = filterQuery + "(alerttype_companyLegalUnit eq '" + legalEntityId + "')";

    	// Construct Input map
    	Map<String, Object> inputMap = new HashMap<>();
    	inputMap.put(ODataQueryConstants.FILTER, filterQuery);
    	inputMap.put(ODataQueryConstants.ORDER_BY, "alerttype_DisplaySequence asc");

    	
		
    	// Read Alert Type Texts
    	Result readAlertTypeTextResult = ServiceUtil.invokeService(ServiceURLEnum.ALERTTYPE_VIEW_READ, inputMap, null,
    			requestInstance);
    	isOperationSuccessful(readAlertTypeTextResult,ServiceURLEnum.ALERTTYPE_VIEW_READ,
    			"alerttype_view",ErrorCodeEnum.ERR_20918);

    	return filterRecordsWithGivenOrDefaultLocale(acceptLanguage, readAlertTypeTextResult, "alerttype_view",
    			"alerttypetext_LanguageCode", getAlertIdAsStringFunction, "alerttype_id");
    }

	private static List<Record> filterRecordsWithGivenOrDefaultLocale(String acceptLanguage,
			Result backendResult,String datasetName, String backendLangColName,
			Function<Record,String> funcName,String pkName) {
		if( ! backendResult.getDatasetById(datasetName).getAllRecords().isEmpty()) {
    		List<String> defaultLocaleAlerts = backendResult.getDatasetById(datasetName).getAllRecords().stream()
    				.filter(rec -> rec.getParamValueByName(backendLangColName).equalsIgnoreCase(DEFAULT_LOCALE))
     				.map(funcName).collect(Collectors.toList());
    		final String  preferredLocale = acceptLanguage;
    		List<String> preferredLocaledAlerts = backendResult.getDatasetById(datasetName).getAllRecords().stream()
    				.filter(rec -> rec.getParamValueByName(backendLangColName).equalsIgnoreCase(preferredLocale))
    				.map(funcName).collect(Collectors.toList());

    		List<String> alertsWithdefaultLocaleOnly = defaultLocaleAlerts.stream().
    				filter(alertId -> !preferredLocaledAlerts.contains(alertId)).collect(Collectors.toList());

    		preferredLocaledAlerts.addAll(alertsWithdefaultLocaleOnly);
    		return backendResult.getDatasetById(datasetName).getAllRecords().stream().filter(
    				rec -> preferredLocaledAlerts.contains(rec.getParamValueByName(pkName))).collect(Collectors.toList());

    	}
    	return new ArrayList<Record>();
	}	

	private static String getFiltersForAlertTypesView(String alertCategoryId, String acceptLanguage,
			boolean fetchActiveAlertsOnly) {
		StringBuilder filterQueryBuffer = new StringBuilder();
    	if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
    		filterQueryBuffer.append("(alerttypetext_LanguageCode eq '" + acceptLanguage + "')");
    	} else {
    		filterQueryBuffer.append("(alerttypetext_LanguageCode eq '" + acceptLanguage
    				+ "' or alerttypetext_LanguageCode eq '" + DEFAULT_LOCALE + "')");
    	}

    	// Add Alert Category Id to Filter Query
    	if (StringUtils.isNotBlank(alertCategoryId)) {
    		if (filterQueryBuffer.length() > 0) {
    			filterQueryBuffer.append(" and ");
    		}
    		filterQueryBuffer.append("(alerttype_AlertCategoryId eq '" + alertCategoryId + "')");
    	}

    	if (fetchActiveAlertsOnly == true) {
    		if (filterQueryBuffer.length() > 0) {
    			filterQueryBuffer.append(" and ");
    		}
    		filterQueryBuffer.append("(alerttype_Status_id eq '" + StatusEnum.SID_ACTIVE.name() + "')");
    	}

    	String filterQuery = filterQueryBuffer.toString();
    	filterQuery = filterQuery.trim();
    	diagnostic.prepareDebug("Consctructed Filter Query:" + filterQuery).log();
		return filterQuery;
	}
    
    public static Result getAlertPreferences(DataControllerRequest requestInstance, String legalEntityId) throws ApplicationException {
    	TABLE_OPERATION_MAPPING tableMapping = TABLE_OPERATION_MAPPING.CUSTOMERVIEWALERTCONFIGURATION;
    	//String filter = "companyLegalUnit eq '" + legalEntityId + "'";
    	return ServiceUtil.invokeGetService(requestInstance, tableMapping.getReadServiceName(),
				null,null,tableMapping.getTableName(),ErrorCodeEnum.ERR_20913);   	
  
    }  
    
     
    public static List<Record> getAlertSubTypes(List<String> alertTypeIdsList, List<String> alertSubTypeIdsList ,
    										String acceptLanguage,boolean fetchGlobalAlerts, 
    									    boolean fetchActiveAlertsOnly, DataControllerRequest requestInstance, String legalEntityId)
            throws ApplicationException {
        // Construct Filter Query
        StringBuilder filterQueryBuffer = new StringBuilder();
        if (StringUtils.isBlank(acceptLanguage)) {
            acceptLanguage = DEFAULT_LOCALE;
        }
        String filterQuery = getQueryConditionForSubAlerts(alertTypeIdsList, alertSubTypeIdsList, acceptLanguage,
				fetchGlobalAlerts, fetchActiveAlertsOnly, filterQueryBuffer);

		if (filterQuery.length() > 0) {
			filterQuery = filterQuery + " and ";
		}
		filterQuery = filterQuery + "(alertsubtype_companyLegalUnit eq '" + legalEntityId + "')";
        // Construct Input map
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, filterQuery);
       // inputMap.put(ODataQueryConstants.ORDER_BY, "alerttype_DisplaySequence asc");

        // Read Alert Type Texts
        Result subTypeResult = ServiceUtil.invokeService(ServiceURLEnum.ALERTSUBTYPETEXTVIEW_READ, inputMap, null,
                requestInstance);
        isOperationSuccessful(subTypeResult, ServiceURLEnum.ALERTSUBTYPETEXTVIEW_READ,
        									"alertsubtypetext_view", ErrorCodeEnum.ERR_20946);
     
    	return filterRecordsWithGivenOrDefaultLocale(acceptLanguage, subTypeResult, "alertsubtypetext_view",
    			"alertsubtypetext_languageCode", getAlertSubTypeIdAsStringFunction, "alertsubtype_id");       
       
    }

	private static String getQueryConditionForSubAlerts(List<String> alertTypeIdsList, List<String> alertSubTypeIdsList,
			String acceptLanguage, boolean fetchGlobalAlerts, boolean fetchActiveAlertsOnly,
			StringBuilder filterQueryBuffer) {
		if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
            filterQueryBuffer.append("(alertsubtypetext_languageCode eq '" + acceptLanguage + "')");
        } else {
            filterQueryBuffer.append("(alertsubtypetext_languageCode eq '" + acceptLanguage
                    + "' or alertsubtypetext_languageCode eq '" + DEFAULT_LOCALE + "')");
        }
              
        // Append ID values of Alert Types to Filter Querty
        getFilterQueryFromList(alertTypeIdsList,  filterQueryBuffer, "alertsubtype_alertTypeId eq '");
        getFilterQueryFromList(alertSubTypeIdsList, filterQueryBuffer, "alertsubtype_id eq '");

        if (fetchGlobalAlerts == false) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and ");
            }
            filterQueryBuffer.append("(alertsubtype_isGlobal eq '0')");
        }
        if (fetchActiveAlertsOnly == true) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and ");
            }
            filterQueryBuffer.append("(alertsubtype_StatusId eq '" + StatusEnum.SID_ACTIVE.name() + "')");
        }

        String filterQuery = filterQueryBuffer.toString();
        filterQuery = filterQuery.trim();
        diagnostic.prepareDebug("Consctructed Filter Query:" + filterQuery).log();
		return filterQuery;
	}

	public static void getFilterQueryFromList(List<String> listType,StringBuilder filterQueryBuffer,
																					String alertcondition) {
		if (listType != null && !listType.isEmpty()) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and ");
            }
            filterQueryBuffer.append("(");
            for (int index = 0; index < listType.size(); index++) {
              
				filterQueryBuffer.append(alertcondition + listType.get(index) + "'");
                if (index != listType.size() - 1) {
                    filterQueryBuffer.append(" or ");
                }
            }
            filterQueryBuffer.append(")");
            filterQueryBuffer.trimToSize();
        }
	}

    
    
    /**
     * Method to get the specific Alert Types based on Alert Type ID of an Alert Category
     * 
     * @param alertTypeIdsList
     * @param alertCategoryId
     * @param acceptLanguage
     * @param requestInstance
     * @return Array of Alert Types
     * @throws ApplicationException
     */
    public static JSONArray getSubAlertTypes(List<String> alertTypeIdsList, String alertCategoryId, String acceptLanguage,
            boolean fetchGlobalAlerts, boolean fetchActiveAlertsOnly, DataControllerRequest requestInstance)
            throws ApplicationException {

        // Construct Filter Query
        StringBuilder filterQueryBuffer = new StringBuilder();
        if (StringUtils.isBlank(acceptLanguage)) {
            acceptLanguage = DEFAULT_LOCALE;
        }
        if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
            filterQueryBuffer.append("(alerttypetext_LanguageCode eq '" + acceptLanguage + "')");
        } else {
            filterQueryBuffer.append("(alerttypetext_LanguageCode eq '" + acceptLanguage
                    + "' or alerttypetext_LanguageCode eq '" + DEFAULT_LOCALE + "')");
        }
        
        

        // Append ID values of Alert Types to Filter Querty
        if (alertTypeIdsList != null && !alertTypeIdsList.isEmpty()) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and (");
            }

            for (int index = 0; index < alertTypeIdsList.size(); index++) {
                filterQueryBuffer.append("alerttype_id eq '" + alertTypeIdsList.get(index) + "'");
                if (index != alertTypeIdsList.size() - 1) {
                    filterQueryBuffer.append(" or ");
                }
            }
            filterQueryBuffer.append(")");
            filterQueryBuffer.trimToSize();
        }

        // Add Alert Category Id to Filter Query
        if (StringUtils.isNotBlank(alertCategoryId)) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and ");
            }
            filterQueryBuffer.append("(alerttype_AlertCategoryId eq '" + alertCategoryId + "')");
        }
/*        if (fetchGlobalAlerts == false) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and ");
            }
            filterQueryBuffer.append("(alerttype_IsGlobal eq '0')");
        }*/
        if (fetchActiveAlertsOnly == true) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and ");
            }
            filterQueryBuffer.append("(alerttype_Status_id eq '" + StatusEnum.SID_ACTIVE.name() + "')");
        }

        String filterQuery = filterQueryBuffer.toString();
        filterQuery = filterQuery.trim();
        diagnostic.prepareDebug("Consctructed Filter Query:" + filterQuery).log();

        // Construct Input map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, filterQuery);
        inputMap.put(ODataQueryConstants.ORDER_BY, "alerttype_DisplaySequence asc");

        // Read Alert Type Texts
        String readAlertTypeTextResponse = Executor.invokeService(ServiceURLEnum.ALERTTYPE_VIEW_READ, inputMap, null,
                requestInstance);
        JSONObject readAlertTypeTextResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertTypeTextResponse);
        if (readAlertTypeTextResponseJSON != null && readAlertTypeTextResponseJSON.has(FabricConstants.OPSTATUS)
                && readAlertTypeTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readAlertTypeTextResponseJSON.has("alerttype_view")) {
            diagnostic.prepareDebug("Successful Read Operation of alerttype_view").log();
            JSONArray alertTypeTextJSONArray = readAlertTypeTextResponseJSON.optJSONArray("alerttype_view");
            alertTypeTextJSONArray = CommonUtilities.filterRecordsByLocale(alertTypeTextJSONArray,
                    "alerttypetext_LanguageCode", "alerttype_id", DEFAULT_LOCALE);
            return alertTypeTextJSONArray;
        } else {
            alert.prepareError("Failed Read Operation of alerttype_view").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20918);
        }

    }


   		
   	/**
   	 * Method to get alert sub types associated to alert type
   	 * 
   	 * @param alertTypeId
   	 * @param requestInstance
   	 * @param acceptLanguage
   	 * @return Dataset containing alert sub types associated to alert type
   	 * @throws ApplicationException
   	 */
   		public static Dataset getAlertSubTypesWithLocale(List<String> alertTypeIdsList, String legalEntityId,
   			DataControllerRequest requestInstance,String acceptLanguage) throws ApplicationException {

   		Dataset operationDataset = new Dataset();
   		operationDataset.setId("alertSubTypes");
   		diagnostic.prepareDebug("Received Accept-Language Header:" + acceptLanguage).log();
   		// Format Accept Language Identifier
   		if (StringUtils.isBlank(acceptLanguage)) {
   			// Consider Default Locale if Accept-Language Header is Blank
   			acceptLanguage = DEFAULT_LOCALE;
   			diagnostic.prepareDebug("Received Accept-Language Header is empty. Returning Data of Default Locale." + DEFAULT_LOCALE).log();
   		}	
   		acceptLanguage = CommonUtilities.formatLanguageIdentifier(acceptLanguage);
   		String filterQuery = getAlertSubtypeTextViewFilterString(alertTypeIdsList, legalEntityId, acceptLanguage);
   		diagnostic.prepareDebug("filterQuery for alertsubtypetext_view:" + filterQuery).log();
   		// Fetch Sub-Alerts
   		Map<String, String> inputMap = new HashMap<>();
   		inputMap.put(ODataQueryConstants.FILTER, filterQuery);
   		String readAlertSubTypeResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPETEXTVIEW_READ, inputMap, null, requestInstance);
   		JSONObject readAlertSubTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertSubTypeResponse);
   		if (readAlertSubTypeResponseJSON == null || !readAlertSubTypeResponseJSON.has(FabricConstants.OPSTATUS)
   				|| readAlertSubTypeResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
   				|| !readAlertSubTypeResponseJSON.has("alertsubtypetext_view")) {
   			alert.prepareError("Failed to Read alertsubtype").log();
   			throw new ApplicationException(ErrorCodeEnum.ERR_20946);
   		}
   		Map<String, Record> alertMap = processSubAlertTextResponse(acceptLanguage, readAlertSubTypeResponseJSON);
   		// Construct Result Dataset
   		for (Entry<String, Record> entry : alertMap.entrySet()) {
   			operationDataset.addRecord(entry.getValue());
   		}
   		return operationDataset;	        
   	}
	private static Map<String, Record> processSubAlertTextResponse(String acceptLanguage,
			JSONObject readAlertSubTypeResponseJSON) {
		Map<String, Record> userPreferredLocaleRecords = new HashMap<>();
		Map<String, Record> defaultLocaleRecords = new HashMap<>();

		JSONArray alertSubTypeJSONArray = readAlertSubTypeResponseJSON.optJSONArray("alertsubtypetext_view");
		for (Object currObject : alertSubTypeJSONArray) {
			JSONObject currJSON;
			String currAlertId;
			if (currObject instanceof JSONObject) {
				currJSON = (JSONObject) currObject;
				currAlertId = currJSON.optString("alertsubtype_id");
				Record currRecord = new Record();
				currRecord.addParam(new Param("code", currJSON.optString("alertsubtype_id"), FabricConstants.STRING));
				currRecord.addParam(new Param("status", currJSON.optString("alertsubtype_StatusId"), FabricConstants.STRING));
				currRecord.addParam(new Param("name", currJSON.optString("alertsubtypetext_displayName"), FabricConstants.STRING));
				currRecord.addParam(new Param("description", currJSON.optString("alertsubtypetext_description"), FabricConstants.STRING));
				currRecord.addParam(new Param("groupId", currJSON.optString("alertsubtype_alertTypeId"), FabricConstants.STRING));
				currRecord.addParam(new Param("legalEntityId", currJSON.optString("alertsubtype_companyLegalUnit"), FabricConstants.STRING));	

				// alerttype is needed for the story AAC-8244 in frontend mapping
				currRecord.addParam(new Param("isGlobal", currJSON.optString("alertsubtype_isGlobal"), FabricConstants.STRING));	
				currRecord.addParam(new Param("externalsystem", currJSON.optString("alertsubtype_externalSystem"), FabricConstants.STRING));

				currRecord.addParam(new Param("isAutoSubscribeEnabled", currJSON.optString("alertsubtype_isAutoSubscribeEnabled"), FabricConstants.STRING));	
				currRecord.addParam(new Param("defaultFrequencyId", currJSON.optString("alertsubtype_defaultFrequencyId"), FabricConstants.STRING));
				currRecord.setId("alertSubType");

				if (StringUtils.equalsIgnoreCase(currJSON.optString("alertsubtypetext_languageCode"), acceptLanguage)) {
					userPreferredLocaleRecords.put(currAlertId, currRecord);
				} else {
					defaultLocaleRecords.put(currAlertId, currRecord);
				}
			}

		}

		Map<String, Record> alertMap = new HashMap<>();

		for (Entry<String, Record> currEntry : userPreferredLocaleRecords.entrySet()) {
			alertMap.put(currEntry.getKey(), currEntry.getValue());
			if (defaultLocaleRecords.containsKey(currEntry.getKey())) {
				// Remove Records of Default Locale for which the records of user preferred
				// Locale are present
				defaultLocaleRecords.remove(currEntry.getKey());
			}
		}
		// Add Default Locale Records for which records of User Preferred Locale are not
		// available
		for (Entry<String, Record> currEntry : defaultLocaleRecords.entrySet()) {
			alertMap.put(currEntry.getKey(), currEntry.getValue());
		}
		return alertMap;
	}

	private static String getAlertSubtypeTextViewFilterString(List<String> alertTypeIdsList, String legalEntityId, String acceptLanguage) {
		StringBuilder filterQueryBuilder = new StringBuilder();
		if (StringUtils.equalsIgnoreCase(acceptLanguage, DEFAULT_LOCALE)) {
			filterQueryBuilder.append("(alertsubtypetext_languageCode eq '" + acceptLanguage
					+ "' or alertsubtypetext_languageCode eq '" + DEFAULT_LOCALE + "')");
		} else {
			filterQueryBuilder.append("(alertsubtypetext_languageCode eq '" + acceptLanguage + "')");
		}
		
		filterQueryBuilder.append(" and alertsubtype_companyLegalUnit eq '"+legalEntityId + "'");
		
		if (alertTypeIdsList != null && !alertTypeIdsList.isEmpty()) {
			if (filterQueryBuilder.length() > 0) {
				filterQueryBuilder.append(" and (");
			}
			for (int index = 0; index < alertTypeIdsList.size(); index++) {
				filterQueryBuilder.append("alertsubtype_alertTypeId eq '" + alertTypeIdsList.get(index) + "'");
				if (index != alertTypeIdsList.size() - 1) {
					filterQueryBuilder.append(" or ");
				}
			}
			filterQueryBuilder.append(")");
			filterQueryBuilder.trimToSize();
		}
		String filterQuery = filterQueryBuilder.toString();
		filterQuery = filterQuery.trim();
		return filterQuery;
	}


    /**
     * Method to get the Alert Types of an Alert Category
     * 
     * @param alertCategoryID
     * @param acceptLanguage
     * @param requestInstance
     * @return Array of Alert Types
     * @throws ApplicationException
     */
    public static JSONArray getAlertTypesOfAlertCategory(String alertCategoryID, String legalEntityId, String acceptLanguage,
            DataControllerRequest requestInstance) throws ApplicationException {
        return getAlertTypes(alertCategoryID, legalEntityId, acceptLanguage, false, requestInstance);
    }

    /**
     * Method to get the details of an Alert Type with Alert Id
     * 
     * @param alertTypeId
     * @param acceptLanguage
     * @param requestInstance
     * @return JSON Object representing the Alert Type
     * @throws ApplicationException
     */
    public static JSONObject getAlertTypeDefinition(String alertTypeId, String acceptLanguage,
            DataControllerRequest requestInstance) throws ApplicationException {

        if (StringUtils.isBlank(acceptLanguage)) {
            acceptLanguage = DEFAULT_LOCALE;
        }
        
		String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);

        // Construct Filter Query
        StringBuilder filterQueryBuffer = new StringBuilder();
        if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
        	filterQueryBuffer.append("(alerttypetext_LanguageCode eq '" + acceptLanguage + "')");
        } else {
        	filterQueryBuffer.append("(alerttypetext_LanguageCode eq '" + acceptLanguage
                    + "' or alerttypetext_LanguageCode eq '" + DEFAULT_LOCALE + "')");
        }
        filterQueryBuffer.append(" and alerttype_id eq '" + alertTypeId + "' and alerttype_companyLegalUnit eq '" + legalEntityId + "'");
        String filterQuery = filterQueryBuffer.toString();
        filterQuery = filterQuery.trim();

        // Construct Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, filterQuery);

        // Read Alert Type Text
        String readAlertTypeTextResponse = Executor.invokeService(ServiceURLEnum.ALERTTYPE_VIEW_READ, inputMap, null,
                requestInstance);
        JSONObject readAlertTypeTextResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertTypeTextResponse);
        if (readAlertTypeTextResponseJSON != null && readAlertTypeTextResponseJSON.has(FabricConstants.OPSTATUS)
                && readAlertTypeTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readAlertTypeTextResponseJSON.has("alerttype_view")) {
            diagnostic.prepareDebug("Successful Read Operation of alerttype_view").log();
            JSONArray alertTypeTextJSONArray = readAlertTypeTextResponseJSON.optJSONArray("alerttype_view");
            alertTypeTextJSONArray = CommonUtilities.filterRecordsByLocale(alertTypeTextJSONArray,
                    "alerttypetext_LanguageCode", "alerttype_id", DEFAULT_LOCALE);
            return alertTypeTextJSONArray.optJSONObject(0);
        } else {
            alert.prepareError("Failed Read Operation of alerttype_view").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20918);
        }
    }
    

    /**
     * Method to get the list of Alert Types applicable to given Customer and Account(Optional)
     * 
     * @param customerID
     * @param accountID
     * @param requestInstance
     * @return Array of Alert Types
     * @throws ApplicationException
     */
    public static Result getCustomerAlerts(String customerID, String accountID, String accountTypeId,
            String alertCategoryId, DataControllerRequest requestInstance, String legalEntityId) throws ApplicationException {

        if (StringUtils.isBlank(customerID) || requestInstance == null) {
            alert.prepareError("Invalid Parameters. Failed to fetch Alert Preferences of Customer").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20919);
        }
        Map<String, Object> inputMap = new HashMap<>();
        // Construct Input Map
        StringBuilder filterBuilder = new StringBuilder();       
        
        if (StringUtils.isNotBlank(accountTypeId)) {
        	filterBuilder.append(" AccountType eq '").append(accountTypeId).append("' and Customer_id eq '")
        	.append(customerID).append("'");
        } else if (StringUtils.isNotBlank(accountID)) {
        	filterBuilder.append("AccountId eq '" ).append(accountID).append("' and Customer_id eq '")
        	.append(customerID).append("'");
        } else {
        	filterBuilder.append("Customer_id eq '").append(customerID).append("'");
        }
        
        if (StringUtils.isNotBlank(alertCategoryId)) {
        	if(filterBuilder.length() > 0)
        		filterBuilder.append(" and ");
        	filterBuilder.append("alertCategoryId eq '").append(alertCategoryId).append("'");
        }
		if (filterBuilder.length() > 0) {
			filterBuilder.append(" and ");
		}
		filterBuilder.append("companyLegalUnit eq '").append(legalEntityId).append("'");
        
        inputMap.put(ODataQueryConstants.FILTER , filterBuilder.toString());
        
        Result readCustomerAlertEntitlementResponse = ServiceUtil.
                invokeService(ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_READ, inputMap, null, requestInstance);
      
        isOperationSuccessful(readCustomerAlertEntitlementResponse,
        		ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_READ, "dbxcustomeralertentitlement", ErrorCodeEnum.ERR_20919);
       return readCustomerAlertEntitlementResponse;
    }

    /**
     * Method to get the Set of Alert Types applicable to given Customer Type
     * 
     * @param customerTypeID
     * @param requestInstance
     * @return Array of Alert Types
     * @throws ApplicationException
     */
    public static Set<String> getAlertTypesofCustomerType(String customerTypeStr, DataControllerRequest requestInstance, String legalEntityId)
            throws ApplicationException {

        if (StringUtils.isBlank(customerTypeStr) || requestInstance == null) {
            alert.prepareError("Invalid Parameters. Failed to fetch Alert Types of given Customer Type").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20916);
        }        
        
        String filterQuery = getCustomerTypeIdQueryStr(customerTypeStr);
        if(StringUtils.isBlank(filterQuery)) {
        	 throw new ApplicationException(ErrorCodeEnum.ERR_20917);
        }
        
		if (filterQuery.length() > 0) {
			filterQuery = filterQuery + " and ";
		}
		filterQuery = filterQuery + "(companyLegalUnit eq '" + legalEntityId + "')";
        
        
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, filterQuery);
        inputMap.put(ODataQueryConstants.SELECT, "alertSubTypeId");
        String readAlertTypeCustomerTypeResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPECUSTOMERTYPE_READ,
                inputMap, null, requestInstance);
        JSONObject readAlertTypeCustomerTypeResponseJSON = CommonUtilities
                .getStringAsJSONObject(readAlertTypeCustomerTypeResponse);
        if (readAlertTypeCustomerTypeResponseJSON != null
                && readAlertTypeCustomerTypeResponseJSON.has(FabricConstants.OPSTATUS)
                && readAlertTypeCustomerTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readAlertTypeCustomerTypeResponseJSON.has("alertsubtypecustomertype")) {
            diagnostic.prepareDebug("Successful Read Operation").log();
            Set<String> alertTypes = new HashSet<>();
            JSONArray alertTypeCustomerTypeArray = readAlertTypeCustomerTypeResponseJSON
                    .optJSONArray("alertsubtypecustomertype");
            if (alertTypeCustomerTypeArray != null && alertTypeCustomerTypeArray.length() > 0) {
                JSONObject currJSONObject = null;
                for (Object currObject : alertTypeCustomerTypeArray) {
                    if (currObject instanceof JSONObject) {
                        currJSONObject = (JSONObject) currObject;
                        if (currJSONObject.has("alertSubTypeId")) {
                            alertTypes.add(currJSONObject.optString("alertSubTypeId"));
                        }
                    }
                }
            }
            return alertTypes;
        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_20917);
        }

    }

	public static String getCustomerTypeIdQueryStr(String customerTypeStr) {
		StringBuilder filterQueryBuffer = new StringBuilder();
        List<String> typeList = Arrays.asList(customerTypeStr.split(",", -1));
        int index = 0;
        filterQueryBuffer.append("(");
        for (String custType : typeList) {
            filterQueryBuffer.append("customerTypeId eq '" + custType + "'");
            
        	if (index != typeList.size() - 1) {
                filterQueryBuffer.append(" or ");
            }
            
            index++;
        }
        if(filterQueryBuffer.length() == 1)
        	return new StringBuilder().toString();
        filterQueryBuffer.append(")");
        return filterQueryBuffer.toString();
	}


    /**
     * Method to get map of account types
     * 
     * @param requestInstance
     * @return Map of account types
     * @throws ApplicationException
     */
    public static Map<String, String> getAccountTypesMap(DataControllerRequest requestInstance, String legalEntityId)
            throws ApplicationException {

        if (ACCOUNT_TYPE_MAP != null) {
            return ACCOUNT_TYPE_MAP;
        } else {
            Map<String, String> accountTypes = new HashMap<>();

            Map<String, String> inputMap = new HashMap<>();
    		//String filterQuery = " companyLegalUnit eq '" + legalEntityId + "'";
    		
    		//inputMap.put(ODataQueryConstants.FILTER, filterQuery);
            String readAlertTypeAccountTypeResponse = Executor.invokeService(ServiceURLEnum.ACCOUNTTYPE_READ, inputMap,
                    null, requestInstance);
            JSONObject readAlertTypeAccountTypeResponseJSON = CommonUtilities
                    .getStringAsJSONObject(readAlertTypeAccountTypeResponse);
            if (readAlertTypeAccountTypeResponseJSON != null
                    && readAlertTypeAccountTypeResponseJSON.has(FabricConstants.OPSTATUS)
                    && readAlertTypeAccountTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && readAlertTypeAccountTypeResponseJSON.has("accounttype")) {
                diagnostic.prepareDebug("Successful Read Operation").log();

                JSONArray alertTypeAccountTypeArray = readAlertTypeAccountTypeResponseJSON.optJSONArray("accounttype");
                if (alertTypeAccountTypeArray != null && alertTypeAccountTypeArray.length() > 0) {
                    JSONObject currJSONObject = null;
                    for (Object currObject : alertTypeAccountTypeArray) {
                        if (currObject instanceof JSONObject) {
                            currJSONObject = (JSONObject) currObject;
                            if (currJSONObject.has("TypeID") && currJSONObject.has("TypeDescription")) {
                                accountTypes.put(currJSONObject.getString("TypeDescription"),
                                        currJSONObject.getString("TypeID"));
                            }
                        }
                    }
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_20891);
            }
            ACCOUNT_TYPE_MAP = accountTypes;
            return ACCOUNT_TYPE_MAP;

        }
    }

    /**
     * Method to get map of account type ids
     * 
     * @param requestInstance
     * @return Map of account types
     * @throws ApplicationException
     */
    public static Map<String, String> getAccountTypeIdsMap(DataControllerRequest requestInstance,String legalEntityId)
            throws ApplicationException {

        if (ACCOUNT_TYPE_IDS_MAP != null) {
            return ACCOUNT_TYPE_IDS_MAP;
        } else {
            Map<String, String> accountTypes = new HashMap<>();
            //String filterQuery = " companyLegalUnit eq '" + legalEntityId + "'";
    		
    		
            Map<String, String> inputMap = new HashMap<>();
            //inputMap.put(ODataQueryConstants.FILTER, filterQuery);
            String readAlertTypeAccountTypeResponse = Executor.invokeService(ServiceURLEnum.ACCOUNTTYPE_READ, inputMap,
                    null, requestInstance);
            JSONObject readAlertTypeAccountTypeResponseJSON = CommonUtilities
                    .getStringAsJSONObject(readAlertTypeAccountTypeResponse);
            if (readAlertTypeAccountTypeResponseJSON != null
                    && readAlertTypeAccountTypeResponseJSON.has(FabricConstants.OPSTATUS)
                    && readAlertTypeAccountTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && readAlertTypeAccountTypeResponseJSON.has("accounttype")) {
                diagnostic.prepareDebug("Successful Read Operation").log();

                JSONArray alertTypeAccountTypeArray = readAlertTypeAccountTypeResponseJSON.optJSONArray("accounttype");
                if (alertTypeAccountTypeArray != null && alertTypeAccountTypeArray.length() > 0) {
                    JSONObject currJSONObject = null;
                    for (Object currObject : alertTypeAccountTypeArray) {
                        if (currObject instanceof JSONObject) {
                            currJSONObject = (JSONObject) currObject;
                            if (currJSONObject.has("TypeID") && currJSONObject.has("TypeDescription")) {
                                accountTypes.put(currJSONObject.getString("TypeID"),
                                        currJSONObject.getString("TypeDescription"));
                            }
                        }
                    }
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_20891);
            }
            ACCOUNT_TYPE_IDS_MAP = accountTypes;
            return ACCOUNT_TYPE_IDS_MAP;

        }
    }

    /**
     * Method to get the Set of Alert Types applicable to given Account Type
     * 
     * @param accountTypeID
     * @param requestInstance
     * @return Array of Alert Types
     * @throws ApplicationException
     */
    public static Set<String> getAlertTypesofAccountType(String accountTypeID, DataControllerRequest requestInstance)
            throws ApplicationException {

        if (StringUtils.isBlank(accountTypeID) || requestInstance == null) {
            alert.prepareError("Invalid Parameters. Failed to fetch Alert Types of given Account Type").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20916);
        }
        Set<String> alertTypes = new HashSet<>();

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, "AccountTypeId eq '" + accountTypeID + "'");
        inputMap.put(ODataQueryConstants.SELECT, "AlertTypeId");
        String readAlertTypeAccountTypeResponse = Executor.invokeService(ServiceURLEnum.ALERTTYPEACCOUNTTYPE_READ,
                inputMap, null, requestInstance);
        JSONObject readAlertTypeAccountTypeResponseJSON = CommonUtilities
                .getStringAsJSONObject(readAlertTypeAccountTypeResponse);
        if (readAlertTypeAccountTypeResponseJSON != null
                && readAlertTypeAccountTypeResponseJSON.has(FabricConstants.OPSTATUS)
                && readAlertTypeAccountTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readAlertTypeAccountTypeResponseJSON.has("alerttypeaccounttype")) {
            diagnostic.prepareDebug("Successful Read Operation").log();

            JSONArray alertTypeAccountTypeArray = readAlertTypeAccountTypeResponseJSON
                    .optJSONArray("alerttypeaccounttype");
            if (alertTypeAccountTypeArray != null && alertTypeAccountTypeArray.length() > 0) {
                JSONObject currJSONObject = null;
                for (Object currObject : alertTypeAccountTypeArray) {
                    if (currObject instanceof JSONObject) {
                        currJSONObject = (JSONObject) currObject;
                        if (currJSONObject.has("AlertTypeId")) {
                            alertTypes.add(currJSONObject.optString("AlertTypeId"));
                        }
                    }
                }
            }
        }
        return alertTypes;
    }

    /**
     * Method to get the Alert Attributes
     * 
     * @param acceptLanguage
     * @param requestInstance
     * @return Map of structure <AlertAttributeID, AlertAttributeRecord>
     * @throws ApplicationException
     */
    public static Map<String, Record> getAlertAttributes(String acceptLanguage, DataControllerRequest requestInstance, 
    		String legalEntityId)
            throws ApplicationException {
        return getAlertAttributes(null, acceptLanguage, requestInstance, legalEntityId);
    }

    /**
     * Method to get the Alert Attributes based on Alert Attribute ID
     * 
     * @param alertAttributeIds
     * @param acceptLanguage
     * @param requestInstance
     * @return Map of structure <AlertAttributeID, AlertAttributeRecord>
     * @throws ApplicationException
     */
    public static Map<String, Record> getAlertAttributes(Set<String> alertAttributeIds, String acceptLanguage,
            DataControllerRequest requestInstance,String legalEntityId) throws ApplicationException {

        if (requestInstance == null) {
            alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20922);
        }

        // Construct Filter Query
        String currentLocale;
        StringBuilder filterQueryBuffer = new StringBuilder();
        currentLocale = acceptLanguage;
        if (StringUtils.isBlank(acceptLanguage)) {
            currentLocale = DEFAULT_LOCALE;
        }
        if (StringUtils.equals(currentLocale, DEFAULT_LOCALE)) {
            filterQueryBuffer.append("(alertattribute_LanguageCode eq '" + acceptLanguage + "')");
        } else {
            filterQueryBuffer.append("(alertattribute_LanguageCode eq '" + acceptLanguage
                    + "' or alertattribute_LanguageCode eq '" + DEFAULT_LOCALE + "')");
        }
        if (alertAttributeIds != null && !alertAttributeIds.isEmpty()) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and (");
            }
            int index = 0;
            for (String currAlertAttributeId : alertAttributeIds) {
                filterQueryBuffer.append("alertattribute_id eq '" + currAlertAttributeId + "'");
                if (index != alertAttributeIds.size() - 1) {
                    filterQueryBuffer.append(" or ");
                }
                index++;
            }
            filterQueryBuffer.append(")");
            filterQueryBuffer.trimToSize();
        }
        
        
        if (filterQueryBuffer.length() > 0) {
            filterQueryBuffer.append(" and ");
        }
        filterQueryBuffer.append(" alertattribute_companyLegalUnit eq '" + legalEntityId + "'");
        String filterQuery = filterQueryBuffer.toString();
        filterQuery = filterQuery.trim();

        // Construct Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, filterQuery);

        // Read Alert Attributes View
        String readAlertAttributeViewResponse = Executor.invokeService(ServiceURLEnum.ALERTATTRIBUTE_VIEW_READ,
                inputMap, null, requestInstance);
        JSONObject readAlertAttributeViewResponseJSON = CommonUtilities
                .getStringAsJSONObject(readAlertAttributeViewResponse);
        if (readAlertAttributeViewResponseJSON == null
                || !readAlertAttributeViewResponseJSON.has(FabricConstants.OPSTATUS)
                || readAlertAttributeViewResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                || !readAlertAttributeViewResponseJSON.has("alertattribute_view")) {
            alert.prepareError("Failed CRUD Operation").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20923);
        }
        diagnostic.prepareDebug("Successful CRUD Operation").log();

        // Construct Result Map
        JSONObject currJSON;
        String currAlertAttributeId;
        Map<String, Record> alertAttributesMap = new HashMap<>();
        JSONArray alertAttributesJSONArray = readAlertAttributeViewResponseJSON.optJSONArray("alertattribute_view");

        Map<String, Record> userPreferredLocaleRecords = new HashMap<>();
        Map<String, Record> defaultLocaleRecords = new HashMap<>();
        // alertattribute_LanguageCode alertattribute_id

        for (Object currObject : alertAttributesJSONArray) {
            if (currObject instanceof JSONObject) {

                currJSON = (JSONObject) currObject;
                Record currAlertAttributeRecord = null;
                currAlertAttributeId = currJSON.optString("alertattribute_id");

                if (userPreferredLocaleRecords.containsKey(currAlertAttributeId)) {
                    currAlertAttributeRecord = userPreferredLocaleRecords.get(currAlertAttributeId);
                } else if (defaultLocaleRecords.containsKey(currAlertAttributeId)) {
                    currAlertAttributeRecord = defaultLocaleRecords.get(currAlertAttributeId);
                } else {
                    currAlertAttributeRecord = new Record();
                    for (String currKey : currJSON.keySet()) {
                        if (currKey.startsWith("alertattribute_")) {
                        	if(currKey.equals("alertattribute_companyLegalUnit"))
                        	{
                        		currAlertAttributeRecord
                                .addParam(new Param("alertattribute_legalEntityId", currJSON.optString(currKey), FabricConstants.STRING));
                        	}
                        	else {
                            currAlertAttributeRecord
                                    .addParam(new Param(currKey, currJSON.optString(currKey), FabricConstants.STRING));
                        	}
                        }
                    }
                    currAlertAttributeRecord.setId("alertAttribute");

                    if (StringUtils.equals(currJSON.optString("alertattribute_LanguageCode"), currentLocale)) {
                        userPreferredLocaleRecords.put(currAlertAttributeId, currAlertAttributeRecord);
                    } else {
                        defaultLocaleRecords.put(currAlertAttributeId, currAlertAttributeRecord);
                    }

                }

                // Add list of possible values if current Attribute is of type list
                if (currJSON.has("alertattributelistvalues_id") && StringUtils
                        .equalsIgnoreCase(currJSON.optString("alertattributelistvalues_LanguageCode"), currentLocale)) {
                    // Alert Attribute is of list type
                    Dataset valuesDataset = currAlertAttributeRecord.getDatasetById("values");
                    if (valuesDataset == null) {
                        valuesDataset = new Dataset();
                        valuesDataset.setId("values");
                        currAlertAttributeRecord.addDataset(valuesDataset);
                    }
                    Record currValueRecord = new Record();
                    for (String currKey : currJSON.keySet()) {
                        if (currKey.startsWith("alertattributelistvalues_")) {
                            currValueRecord
                                    .addParam(new Param(currKey, currJSON.optString(currKey), FabricConstants.STRING));
                        }
                    }
                    valuesDataset.addRecord(currValueRecord);
                }
            }
        }

        for (Entry<String, Record> currEntry : userPreferredLocaleRecords.entrySet()) {
            alertAttributesMap.put(currEntry.getKey(), currEntry.getValue());
            if (defaultLocaleRecords.containsKey(currEntry.getKey())) {
                // Remove Records of Default Locale for which the records of user preferred
                // Locale are present
                defaultLocaleRecords.remove(currEntry.getKey());
            }
        }

        // Add Default Locale Records for which records of User Preferred Locale are not
        // available
        for (Entry<String, Record> currEntry : defaultLocaleRecords.entrySet()) {
            alertAttributesMap.put(currEntry.getKey(), currEntry.getValue());
        }

        return alertAttributesMap;
    }

    /**
     * Method to get the Alert Conditions
     * 
     * @param acceptLanguage
     * @param requestInstance
     * @return Map of structure <AlertConditionID, AlertConditionRecord>
     * @throws ApplicationException
     */
    public static Map<String, Record> getAlertConditions(String acceptLanguage, DataControllerRequest requestInstance)
            throws ApplicationException {
        return getAlertConditions(null, acceptLanguage, requestInstance,null);
    }

    /**
     * Method to get the Alert Conditions based on Alert Condition ID
     * 
     * @param alertConditionIds
     * @param acceptLanguage
     * @param requestInstance
     * @return Map of structure <AlertConditionID, AlertConditionRecord>
     * @throws ApplicationException
     */
    public static Map<String, Record> getAlertConditions(Set<String> alertConditionIds, String acceptLanguage,
            DataControllerRequest requestInstance, String legalEntityId) throws ApplicationException {

        if (requestInstance == null) {
            alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20922);
        }

        // Construct Filter Query
        String currentLocale;
        StringBuilder filterQueryBuffer = new StringBuilder();
        if (StringUtils.isBlank(acceptLanguage)) {
            currentLocale = DEFAULT_LOCALE;
        } else {
            currentLocale = acceptLanguage;
        }
        if (StringUtils.equals(currentLocale, DEFAULT_LOCALE)) {
            filterQueryBuffer.append("(LanguageCode eq '" + acceptLanguage + "')");
        } else {
            filterQueryBuffer
                    .append("(LanguageCode eq '" + acceptLanguage + "' or LanguageCode eq '" + DEFAULT_LOCALE + "')");
        }
        if (alertConditionIds != null && !alertConditionIds.isEmpty()) {
            if (filterQueryBuffer.length() > 0) {
                filterQueryBuffer.append(" and (");
            }
            int index = 0;
            for (String currAlertConditionId : alertConditionIds) {
                filterQueryBuffer.append("id eq '" + currAlertConditionId + "'");
                if (index != alertConditionIds.size() - 1) {
                    filterQueryBuffer.append(" or ");
                }
                index++;
            }
            filterQueryBuffer.append(")");
            filterQueryBuffer.trimToSize();
        }
       /* if (filterQueryBuffer.length() > 0) {
            filterQueryBuffer.append(" and ");
        }
        filterQueryBuffer.append("(companyLegalUnit eq '" + legalEntityId + "')");*/
        
        
        String filterQuery = filterQueryBuffer.toString();
        filterQuery = filterQuery.trim();
        diagnostic.prepareDebug("Filter Query:" + filterQuery).log();

        // Construct Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, filterQuery);
        inputMap.put(ODataQueryConstants.SELECT, "id,LanguageCode,Name,NoOfFields");

        // Read Alert Condition
        String readAlertConditionResponse = Executor.invokeService(ServiceURLEnum.ALERTCONDITION_READ, inputMap, null,
                requestInstance);
        JSONObject readAlertConditionResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertConditionResponse);
        if (readAlertConditionResponseJSON == null || !readAlertConditionResponseJSON.has(FabricConstants.OPSTATUS)
                || readAlertConditionResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                || !readAlertConditionResponseJSON.has("alertcondition")) {
            alert.prepareError("Failed CRUD Operation").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20922);
        }
        diagnostic.prepareDebug("Successful CRUD Operation").log();

        JSONObject currJSON;
        Map<String, Record> alertConditionMap = new HashMap<>();
        JSONArray alertConditionJSONArray = readAlertConditionResponseJSON.optJSONArray("alertcondition");

        // Filter Alert Conditions based on Locale
        alertConditionJSONArray = CommonUtilities.filterRecordsByLocale(alertConditionJSONArray, "LanguageCode", "id",
                DEFAULT_LOCALE);

        // Construct Result Map
        for (Object currObject : alertConditionJSONArray) {
            if (currObject instanceof JSONObject) {
                currJSON = (JSONObject) currObject;
                Record currRecord = new Record();
                currRecord.setId("alertCondition");
                for (String currKey : currJSON.keySet()) {
                    currRecord.addParam(new Param(currKey, currJSON.optString(currKey), FabricConstants.STRING));
                }
                alertConditionMap.put(currJSON.optString("id"), currRecord);
            }
        }

        diagnostic.prepareDebug("Returning Success Response").log();
        return alertConditionMap;
    }

    /**
     * Method to fetch the association of Customer to the defined Alert Categories
     * 
     * @param alertCategoryID
     * @param customerId
     * @param accountId
     * @param requestInstance
     * @return
     * @throws ApplicationException
     */
	public static Map<String, JSONObject> getCustomerCategorySubscription(String customerId, String accountId,
			String accountTypeId, String acceptLanguage, DataControllerRequest requestInstance, String legalEntityId)
			throws ApplicationException {
        if (requestInstance == null) {
            alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20925);
        }

        // Fetch all of Alert Categories
        JSONArray alertCategoriesJSONArray = fetchAlertCategories(acceptLanguage, requestInstance,legalEntityId);

        // Fetch associated Alert Categories
        Map<String, JSONObject> associatedAlertCategoryMap = getCustomerAlertCategoryAssociation(customerId, accountId,
                accountTypeId, requestInstance,legalEntityId);

        // Collate associated Alert Categories list with the master list of Alert
        // Categories

        if (alertCategoriesJSONArray != null && alertCategoriesJSONArray.length() > 0) {
            JSONObject currJSONObject;
            String currAlertCategoryId = StringUtils.EMPTY;

            for (Object currObject : alertCategoriesJSONArray) {
                if (currObject instanceof JSONObject) {
                    currJSONObject = (JSONObject) currObject;

                    JSONObject currAlertCategoryObject = new JSONObject();

                    for (String key : currJSONObject.keySet()) {
                        currAlertCategoryObject.put(key, currJSONObject.optString(key));
                    }
                    currAlertCategoryId = currJSONObject.optString("alertcategory_id");
                    if (associatedAlertCategoryMap.containsKey(currAlertCategoryId)) {
                        currAlertCategoryObject.put(CATEGORY_SUBSCRIPTION_PARAM,
                                associatedAlertCategoryMap.get(currAlertCategoryId));
                    } else {
                        JSONObject currSubscriptionObject = new JSONObject();
                        currSubscriptionObject.put(IS_SUBSCRIBED_PARAM, String.valueOf(false));
                        currSubscriptionObject.put(IS_INITIAL_LOAD_PARAM, String.valueOf(true));
                        currAlertCategoryObject.put(CATEGORY_SUBSCRIPTION_PARAM, currSubscriptionObject);
                    }
                    associatedAlertCategoryMap.put(currAlertCategoryId, currAlertCategoryObject);
                }
            }
        }

        diagnostic.prepareDebug("Returning Success Response").log();
        return associatedAlertCategoryMap;
    }

    /**
     * Method to get the list of Alert Categories associated to a customer. This method returns only those Alert
     * Categories to which a customer has performed a subscribe or unsubscribe action. Alert Categories for which the
     * customer has not set any preference are not a part of the returned map.
     * 
     * @param customerId
     * @param accountId
     * @param requestInstance
     * @return Map denoting the association of Customer with Alert Categories
     * @throws ApplicationException
     */
    public static Map<String, JSONObject> getCustomerAlertCategoryAssociation(String customerId, String accountId,
            String accountTypeId, DataControllerRequest requestInstance, String legalEntityId) throws ApplicationException {
        // Construct Filter Query
        StringBuilder filterQueryBuffer = new StringBuilder();
        filterQueryBuffer.append("Customer_id eq '" + customerId + "'");
        if (StringUtils.isNotBlank(accountId)) {
            filterQueryBuffer.append(" and AccountID eq '" + accountId + "'");
        }
        if (StringUtils.isNotBlank(accountTypeId)) {
            filterQueryBuffer.append(" and AccountType eq '" + accountTypeId + "'");
        }
        filterQueryBuffer.append(" and companyLegalUnit eq '" + legalEntityId + "'");
        filterQueryBuffer.trimToSize();
        String filterQuery = filterQueryBuffer.toString();
        filterQuery = filterQuery.trim();

        // Construct Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, filterQuery);

        // Read Customer Alert Switch
        String readCustomerAlertSwitchResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERALERTSWITCH_READ,
                inputMap, null, requestInstance);
        JSONObject readCustomerAlertSwitchResponseJSON = CommonUtilities
                .getStringAsJSONObject(readCustomerAlertSwitchResponse);
        if (readCustomerAlertSwitchResponseJSON == null
                || !readCustomerAlertSwitchResponseJSON.has(FabricConstants.OPSTATUS)
                || readCustomerAlertSwitchResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                || !readCustomerAlertSwitchResponseJSON.has("customeralertswitch")) {
            alert.prepareError("Failed CRUD Operation").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20925);
        }
        diagnostic.prepareDebug("Successful CRUD Operation").log();

        // Prepare Map of Associated Alert Categories
        Map<String, JSONObject> associatedAlertCategoryMap = new HashMap<>();
        JSONArray customerAlertSwitchArray = readCustomerAlertSwitchResponseJSON.optJSONArray("customeralertswitch");
        if (customerAlertSwitchArray != null) {
            JSONObject currJSON;
            boolean isSubscribed = false;
            String currAlertCategoryId = StringUtils.EMPTY;
            for (Object currObject : customerAlertSwitchArray) {

                if (currObject instanceof JSONObject) {
                    currJSON = (JSONObject) currObject;

                    JSONObject currSubscriptionObject = new JSONObject();

                    if (StringUtils.equalsIgnoreCase(StatusEnum.SID_SUBSCRIBED.name(),
                            currJSON.optString("Status_id"))) {
                        isSubscribed = true;
                    } else {
                        isSubscribed = false;
                    }
                    currSubscriptionObject.put(IS_SUBSCRIBED_PARAM, String.valueOf(isSubscribed));
                    currSubscriptionObject.put(IS_INITIAL_LOAD_PARAM, String.valueOf(false));

                    currAlertCategoryId = currJSON.optString("AlertCategoryId");
                    associatedAlertCategoryMap.put(currAlertCategoryId, currSubscriptionObject);
                }
            }

        }
        return associatedAlertCategoryMap;
    }

    /**
     * Method to get all Alert Categories
     * 
     * @param acceptLanguage
     * @param requestInstance
     * @return JSON Array of Alert Categories
     * @throws ApplicationException
     */
    public static JSONArray fetchAlertCategories(String acceptLanguage, DataControllerRequest requestInstance, String legalEntityId)
            throws ApplicationException {
        // Fetch list of Alert Categories
        StringBuilder filterQueryBuffer = new StringBuilder();
        if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
            filterQueryBuffer.append("(alertcategorytext_LanguageCode eq '" + acceptLanguage + "')");
        } else {
            filterQueryBuffer.append("(alertcategorytext_LanguageCode eq '" + acceptLanguage
                    + "' or alertcategorytext_LanguageCode eq '" + DEFAULT_LOCALE + "')");
        }
        filterQueryBuffer.append(" and ");
        filterQueryBuffer.append("(alertcategory_companyLegalUnit eq '" + legalEntityId + "')");
        filterQueryBuffer.trimToSize();

        // Construct Input Map
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put(ODataQueryConstants.FILTER, filterQueryBuffer.toString());
        parameterMap.put(ODataQueryConstants.ORDER_BY, "alertcategory_DisplaySequence asc");

        String readAlertCategoryViewResponse = Executor.invokeService(ServiceURLEnum.ALERTCATEGORY_VIEW_READ,
                parameterMap, null, requestInstance);
        JSONObject readAlertCategoryViewResponseJSON = CommonUtilities
                .getStringAsJSONObject(readAlertCategoryViewResponse);
        if (readAlertCategoryViewResponseJSON == null
                || !readAlertCategoryViewResponseJSON.has(FabricConstants.OPSTATUS)
                || readAlertCategoryViewResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                || !readAlertCategoryViewResponseJSON.has("alertcategory_view")) {
            alert.prepareError("Failed CRUD Operation").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20921);
        }
        diagnostic.prepareDebug("Successful CRUD Operation").log();
        JSONArray alertCategoriesJSONArray = readAlertCategoryViewResponseJSON.optJSONArray("alertcategory_view");

        // Filter Alert Category Records based on Locale
        alertCategoriesJSONArray = CommonUtilities.filterRecordsByLocale(alertCategoriesJSONArray,
                "alertcategorytext_LanguageCode", "alertcategory_id", DEFAULT_LOCALE);
        return alertCategoriesJSONArray;
    }
    
    public static boolean isAlertCategoryAvaialable(String code, DataControllerRequest requestInstance)
            throws ApplicationException {

        if (requestInstance == null) {
            alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20918);
        }
        String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
        // Prepare Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, "id eq '" + code + "' and companyLegalUnit eq '" + legalEntityId + "'");
        inputMap.put(ODataQueryConstants.SELECT, "Name");

        // Read Alert Categories
        String readAlertCategoryResponse = Executor.invokeService(ServiceURLEnum.DBXALERTCATEGORY_READ, inputMap, null,
                requestInstance);
        JSONObject readAlertCategoryResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertCategoryResponse);

        if (readAlertCategoryResponseJSON != null && readAlertCategoryResponseJSON.has(FabricConstants.OPSTATUS)
                && readAlertCategoryResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readAlertCategoryResponseJSON.optJSONArray("dbxalertcategory") != null) {
            diagnostic.prepareDebug("Successful CRUD Operation").log();

            // Check if an Alert Category with the given code already exists
            JSONArray alertCategoryRecords = readAlertCategoryResponseJSON.optJSONArray("dbxalertcategory");
            if (alertCategoryRecords.length() > 0) {
                diagnostic.prepareDebug("Alert Category with the Code " + code + " already exists. The same code cannot be reused").log();
                return false;
            } else {
                diagnostic.prepareDebug("Alert Category with the Code " + code + " does not exist. The code can be used").log();
                return true;
            }
        }

        alert.prepareError("Failed CRUD Operation").log();
        throw new ApplicationException(ErrorCodeEnum.ERR_20918);

    }
    

    /**
     * Method to determine if an Alert with the alertCode already exists
     * 
     * @param alertCode
     * @param requestInstance
     * @return availability status
     * @throws ApplicationException
     */
    public static boolean isAlertCodeAvaialable(String alertCode, DataControllerRequest requestInstance)
            throws ApplicationException {

        if (requestInstance == null) {
            alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20918);
        }
        String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
        // Prepare Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, "id eq '" + alertCode + "' and companyLegalUnit eq '" + legalEntityId + "'");
        inputMap.put(ODataQueryConstants.SELECT, "id");

        // Read Alert Types
        String readAlertTypeResponse = Executor.invokeService(ServiceURLEnum.DBXALERTTYPE_READ, inputMap, null,
                requestInstance);
        JSONObject readAlertTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertTypeResponse);

        if (readAlertTypeResponseJSON != null && readAlertTypeResponseJSON.has(FabricConstants.OPSTATUS)
                && readAlertTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readAlertTypeResponseJSON.optJSONArray("dbxalerttype") != null) {
            diagnostic.prepareDebug("Successful CRUD Operation").log();

            // Check if an Alert with the given code already exists
            JSONArray alertTypeRecords = readAlertTypeResponseJSON.optJSONArray("dbxalerttype");
            if (alertTypeRecords.length() > 0) {
                diagnostic.prepareDebug("Alert with the Code " + alertCode + " already exists. The same code cannot be reused").log();
                return false;
            } else {
                diagnostic.prepareDebug("Alert with the Code " + alertCode + " does not exist. The code can be used").log();
                return true;
            }
        }

        alert.prepareError("Failed CRUD Operation").log();
        throw new ApplicationException(ErrorCodeEnum.ERR_20918);

    }
    
    public static boolean isAlertSubTypeCodeAvaialable(String code, DataControllerRequest requestInstance)
            throws ApplicationException {

        if (requestInstance == null) {
            alert.prepareError("DataControllerRequest Instance is NULL. Returning Error Response").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20918);
        }
        String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
        // Prepare Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, "id eq '" + code + "' and companyLegalUnit eq '" + legalEntityId + "'");
        inputMap.put(ODataQueryConstants.SELECT, "id");

        // Read Alert Sub Types
        String readAlertSubTypeResponse = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPE_READ, inputMap, null,
                requestInstance);
        JSONObject readAlertSubTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertSubTypeResponse);

        if (readAlertSubTypeResponseJSON != null && readAlertSubTypeResponseJSON.has(FabricConstants.OPSTATUS)
                && readAlertSubTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readAlertSubTypeResponseJSON.optJSONArray("alertsubtype") != null) {
            diagnostic.prepareDebug("Successful CRUD Operation").log();

            // Check if an Alert sub type with the given code already exists
            JSONArray alertSubTypeRecords = readAlertSubTypeResponseJSON.optJSONArray("alertsubtype");
            if (alertSubTypeRecords.length() > 0) {
                diagnostic.prepareDebug("Alert Sub Type with the Code " + code + " already exists. The same code cannot be reused").log();
                return false;
            } else {
                diagnostic.prepareDebug("Alert Sub Type with the Code " + code + " does not exist. The code can be used").log();
                return true;
            }
        }

        alert.prepareError("Failed CRUD Operation").log();
        throw new ApplicationException(ErrorCodeEnum.ERR_20918);

    }
    

    public static String getAccountTypeIdFromName(String accountTypeName, DataControllerRequest requestInstance)
            throws ApplicationException {
        // Prepare Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, "TypeDescription eq '" + accountTypeName + "'");
        inputMap.put(ODataQueryConstants.SELECT, "TypeID");

        // Read Alert Types
        String readAccountTypeResponse = Executor.invokeService(ServiceURLEnum.ACCOUNTTYPE_READ, inputMap, null,
                requestInstance);
        JSONObject readAccountTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readAccountTypeResponse);

        if (readAccountTypeResponseJSON != null && readAccountTypeResponseJSON.has(FabricConstants.OPSTATUS)
                && readAccountTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readAccountTypeResponseJSON.optJSONArray("accounttype") != null) {
            diagnostic.prepareDebug("Successful CRUD Operation").log();

            JSONArray accountTypeResponse = readAccountTypeResponseJSON.optJSONArray("accounttype");

            if (accountTypeResponse.length() == 0 || !accountTypeResponse.getJSONObject(0).has("TypeID")) {
                return null;
            }
            return accountTypeResponse.getJSONObject(0).getString("TypeID");
        }
        alert.prepareError("Failed CRUD Operation").log();
        throw new ApplicationException(ErrorCodeEnum.ERR_20889);
    }

    /**
     * Method to custom format the locale as per Kony Client SDK
     * 
     * @param locale
     * @return
     */
    public static String formatLocaleAsPerKonyMobileSDK(String locale) {

        if (StringUtils.equalsIgnoreCase(locale, "en")) {
            return DEFAULT_LOCALE;
        }
        return locale;
    }

    /**
     * Method to construct the customer account numbers/types map
     * 
     * @param isAlertsAccountNumberLevel
     * @return
     */
    public static Set<String> getAccountStatusArray(boolean isAlertsAccountNumberLevel, JSONArray customeralerts) {
        Set<String> accountsArray = new HashSet<>();
        customeralerts.forEach((alert) -> {
            JSONObject alertObject = (JSONObject) alert;
            if (isAlertsAccountNumberLevel && alertObject.has("AccountID")
                    && alertObject.getString("Status_id").equalsIgnoreCase(StatusEnum.SID_SUBSCRIBED.name())) {
                accountsArray.add(alertObject.getString("AccountID"));

            } else if (alertObject.has("AccountType")
                    && alertObject.getString("Status_id").equalsIgnoreCase(StatusEnum.SID_SUBSCRIBED.name())) {
                accountsArray.add(alertObject.getString("AccountType"));
            }
        });

        return accountsArray;
    }
    
    public static void isOperationSuccessful(Result result,ServiceURLEnum serviceUrlenum,
							String datasetName , ErrorCodeEnum errorcode) throws ApplicationException {
		if (result == null || (result.getParamValueByName(FabricConstants.OPSTATUS) == null
				|| Integer.parseInt(result.getParamValueByName(FabricConstants.OPSTATUS)) != 0) ||
				(!serviceUrlenum.name().contains("DELETE") && result.getDatasetById(datasetName) == null)) {
			alert.prepareError("Failed CRUD Operation:" + serviceUrlenum.name()).log();
			throw new ApplicationException(errorcode);
		}
	}
    
    public static void isOperationSuccessful(Result result,ServiceURLEnum serviceUrlenum,
    		 ErrorCodeEnum errorcode) throws ApplicationException {
    	if (result == null || (result.getParamValueByName(FabricConstants.OPSTATUS) == null
    			|| Integer.parseInt(result.getParamValueByName(FabricConstants.OPSTATUS)) != 0) )
    	{
    		String errmsg = result.getParamValueByName(ACConstants.ERRMSG) != null ? 
    				      result.getParamValueByName(ACConstants.ERRMSG) : "unknown error";
    		alert.prepareError("Failed CRUD Operation:" + serviceUrlenum.name() + errmsg).log();
    		throw new ApplicationException(errorcode, new Throwable(errmsg));
    	}   	
    }
    
    public static void isOperationHasErrmsg(Result result,ServiceURLEnum serviceUrlenum,
      		 ErrorCodeEnum errorcode) throws ApplicationException {
      	if (result == null || ( result.getParamValueByName(FabricConstants.OPSTATUS) == null
      							|| Integer.parseInt(result.getParamValueByName(FabricConstants.OPSTATUS)) != 0
      							|| (Integer.parseInt(result.getParamValueByName(FabricConstants.OPSTATUS)) == 0 && 
      							    StringUtils.isNotEmpty(isResultHasErrMsg(result))))) {
      									
      		String errmsg = result.getDatasetById("records").getAllRecords().get(0).getParamValueByName(ACConstants.ERRMSG); 
        	alert.prepareError("Failed CRUD Operation:" + serviceUrlenum.name() + errmsg).log();
      		throw new ApplicationException(errorcode, new Throwable(errmsg));
      	}   	
      }
       
       public static String isResultHasErrMsg(Result result) {
       	String errmsg = null;
       	if(result.getDatasetById("records") != null &&	!result.getDatasetById("records").getAllRecords().isEmpty()) {
       		 errmsg = result.getDatasetById("records").getAllRecords().get(0).getParamValueByName("errmsg");	
       	}
       	
       	return errmsg;
       }    
   
	public static boolean validateFrequencyFormat(String id, String value, String time) {
        boolean result = false;
        if (StringUtils.isEmpty(id) || id.equalsIgnoreCase("NONE")) {
        	result = true;
			return result;
		}
		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
		try {
			dateFormat.parse(time);
		} catch (ParseException e) {
			return result;
		}
		if (id.equalsIgnoreCase("DAILY")) {
			result = true;
		} else if (id.equalsIgnoreCase("MONTHLY")) {			
			int val = Integer.valueOf(value);
			if (val >= 1 && val <= 30) {
				result = true;
			}			
		} else if (id.equalsIgnoreCase("WEEKLY")) {
			String[] weekdays = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
			for (int i = 0; i < weekdays.length; i++) {
				if (weekdays[i].equalsIgnoreCase(value)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}	
	
	public static void syncCustomerAlertEntitlements(DataControllerRequest requestInstance,String operationName,
			String groupcode, String legalEntityId) throws ApplicationException {
		Map<String, Object> attrParameterMap = new HashMap<>();
		attrParameterMap.put("operationType", operationName);
		attrParameterMap.put("filterValue", groupcode);
		attrParameterMap.put("companyLegalUnit", legalEntityId);
		invokeSyncProcedure(requestInstance, attrParameterMap,
				ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_SYNC,  ErrorCodeEnum.ERR_20974);	
	}
	
	
	public static void syncCustomerAlertFrequencies(DataControllerRequest requestInstance, String operation,
								String filtervalue, String legalEntityId)	throws ApplicationException {		
			Result alertPreferencesReadResult = AlertManagementHandler.getAlertPreferences(requestInstance,legalEntityId);
			if(!alertPreferencesReadResult.
						getDatasetById(ACConstants.CUSTOMERVIEWALERTCONFIGURATION_TN).getAllRecords().isEmpty()) {
				Record rec = alertPreferencesReadResult.
									getDatasetById(ACConstants.CUSTOMERVIEWALERTCONFIGURATION_TN).getRecord(0);
				String alertPreferenceFetched = rec.getParamValueByName("alertPreferenceView");
				if(operation.equalsIgnoreCase("edit") && 
						ACConstants.ALERTPREFERNCES.ALERT.name().equalsIgnoreCase(alertPreferenceFetched)) {
					invokeFreqProcedure(requestInstance, operation, filtervalue, legalEntityId);						
				}else {
				   if(!ALERTPREFERNCES.CATEGORY.name().equalsIgnoreCase(alertPreferenceFetched)) {					
					invokeFreqProcedure(requestInstance, operation, filtervalue, legalEntityId);					
				}
			}
		}
	}
	

	public static void invokeFreqProcedure(DataControllerRequest requestInstance, String operation,
			String alertgroup, String legalEntityId) throws ApplicationException {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("operationType", operation);			
		parameterMap.put("filterValue", alertgroup);
		parameterMap.put("companyLegalUnit", legalEntityId);
		invokeSyncProcedure(requestInstance, parameterMap,
				ServiceURLEnum.CUSTOMERALERTFREQUENCY_SYNC,  ErrorCodeEnum.ERR_20973);
	}
	
	public static  void syncCustomerAlertChannels(DataControllerRequest requestInstance, String operation,
			String filterValue, String legalEntityId, String channels, String changedLevel)	throws ApplicationException {		
		if(operation.equalsIgnoreCase("reassign")) {
			Result alertPreferencesReadResult = AlertManagementHandler.getAlertPreferences(requestInstance, legalEntityId);
			if(!alertPreferencesReadResult.getDatasetById(
					ACConstants.CUSTOMERVIEWALERTCONFIGURATION_TN).getAllRecords().isEmpty()) {
				Record rec = alertPreferencesReadResult.
						getDatasetById(ACConstants.CUSTOMERVIEWALERTCONFIGURATION_TN).getRecord(0);
				String alertPreferenceFetched = rec.getParamValueByName("alertPreferenceView");
				if(!ACConstants.ALERTPREFERNCES.CATEGORY.name().equalsIgnoreCase(alertPreferenceFetched)) {
					invokeChannelSyncProcedure(requestInstance, operation, filterValue,legalEntityId, channels,
							changedLevel);
				}
			}			
		} else if(operation.equalsIgnoreCase("edit")) {			
			invokeChannelSyncProcedure(requestInstance, operation, filterValue, legalEntityId, channels,
					changedLevel);
		}	
	}

	public static void invokeChannelSyncProcedure(DataControllerRequest requestInstance, String operation,
			String filterValue, String legalEntityId, String channels, String changedLevel) throws ApplicationException {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("operationType", operation);
		parameterMap.put("changedLevel", changedLevel);
		parameterMap.put("channelsStr", channels);
		parameterMap.put("filterValue", filterValue);
		parameterMap.put("companyLegalUnit", legalEntityId);
		invokeSyncProcedure(requestInstance, parameterMap, ServiceURLEnum.CUSTOMERALERTCHANNEL_SYNC,
				ErrorCodeEnum.ERR_20972);
	}

	public static void invokeSyncProcedure(DataControllerRequest requestInstance, Map<String, Object> parameterMap,
			ServiceURLEnum customeralertfrequencySync,ErrorCodeEnum errcode) throws ApplicationException {
		Result freqResult =
				ServiceUtil.invokeService(customeralertfrequencySync,parameterMap , null, requestInstance);
		AlertManagementHandler.isOperationSuccessful(freqResult,customeralertfrequencySync,errcode);
	}

}