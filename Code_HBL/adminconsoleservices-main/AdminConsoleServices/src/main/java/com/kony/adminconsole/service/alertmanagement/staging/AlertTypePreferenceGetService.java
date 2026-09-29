package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ACConstants.ALERTPREFERNCES;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
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
 * Service to retrieve the Alert Type Preference of Customers
 * 
 * @author Aditya Mankal
 */
public class AlertTypePreferenceGetService implements JavaService2 {

	private static final String SUBSCRIBED_FREQUENCY = "subscribedFrequency";

	private static final String SUPPORTED_CHANNELS = "supportedChannels";

	private static final String SUBSCRIBED_CHANNELS = "subscribedChannels";

	private static final String DEFAULT_LOCALE = AlertManagementHandler.DEFAULT_LOCALE;

	private static final List<String> ALERT_PARAMS = Arrays.asList("alertsubtype_id","alertsubtypetext_displayName","alertsubtypetext_description","alertsubtypetext_languageCode",
			"alertsubtype_value1","alertsubtype_value2","alertsubtype_defaultFrequencyId","alertsubtype_alertTypeId");
	private static final List<String> ALERT_GROUP_PARAMS = Arrays.asList("alerttype_id","alerttypetext_Description","alerttypetext_DisplayName","alerttypetext_LanguageCode");

	private static final String ACCOUNT_ID_PARAM = "AccountId";
	private static final String ACCOUNT_TYPE_PARAM = "AccountTypeId";
	private static final String CUSTOMER_ID_PARAM = "CustomerId";
	private static final String ALERT_CATEGORY_ID_PARAM = "AlertCategoryId";
	private static final String CUSTOMER_TYPE_STR = "customerTypeStr";
	private static final String ACCOUNTS = "accounts";

	private static final String IS_SUBSCRIBED_PARAM = "isSubscribed";
	private static final String IS_INITIAL_LOAD_PARAM = "isInitialLoad";
	private static final String LEGALENTITYID_PARAM = "legalEntityId";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private Function<Record,String> getAlertTypeIDFromRecord = rec -> 
	String.valueOf(rec.getParamValueByName("alertsubtype_alertTypeId"));
	Function<Record, String> getChannelIDFromRecord = rec -> String.valueOf(rec.getParamValueByName("channelId"));
	Function<Record, String> getFrequencyIDFromRecord = rec -> String.valueOf(rec.getParamValueByName("alertFrequencyId"));

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		Result processedResult = new Result();
		try {
			// Read Inputs
			String alertCategoryID = requestInstance.getParameter(ALERT_CATEGORY_ID_PARAM);
			diagnostic.prepareDebug("Received Alert Category ID:" + alertCategoryID).log(); 
			String customerID = requestInstance.getParameter(CUSTOMER_ID_PARAM);
			diagnostic.prepareDebug("Is Customer ID Null?" + StringUtils.isBlank(customerID)).log();
			String accountID = requestInstance.getParameter(ACCOUNT_ID_PARAM);
			diagnostic.prepareDebug("Is Account ID Null?" + StringUtils.isBlank(accountID)).log();
			String accountTypeId = requestInstance.getParameter(ACCOUNT_TYPE_PARAM);
			diagnostic.prepareDebug("Is Account Type Null?" + StringUtils.isBlank(accountTypeId)).log();
			String accounts = requestInstance.getParameter(ACCOUNTS);
			diagnostic.prepareDebug("Is Accounts Null?" + StringUtils.isBlank(accounts)).log();
			String customerTypeStr = requestInstance.getParameter(CUSTOMER_TYPE_STR);
			diagnostic.prepareDebug("Is customer type string Null?" + StringUtils.isBlank(customerTypeStr)).log();
			String acceptLanguage = requestInstance.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
			diagnostic.prepareDebug("Received Accept-Language Header:" + acceptLanguage).log();

			String legalEntityId = requestInstance.getParameter(LEGALENTITYID_PARAM);
			diagnostic.prepareDebug("Is Account ID Null?" + StringUtils.isBlank(accountID)).log();
			if (StringUtils.isBlank(customerTypeStr)) {
				// Unrecognized Customer type. Customer Type Could not be resolved
				alert.prepareError("Unrecognized Customer Type str. Customer Type  Could not be resolved").log();
				ErrorCodeEnum.ERR_20792.setErrorCode(processedResult);
				return processedResult;
			}
			
			if (StringUtils.isBlank(legalEntityId)) {
				alert.prepareError("legalEntityId is empty").log();
				ErrorCodeEnum.ERR_22230.setErrorCode(processedResult);
				return processedResult;
			}
			 // Validate Customer Id by resolving username
            String customerUsername = CustomerHandler.getCustomerUsername(customerID, requestInstance);
            if (StringUtils.isBlank(customerUsername)) {
                // Unrecognized Customer. Customer Type Could not be resolved
                alert.prepareError("Unrecognized Customer").log();
                ErrorCodeEnum.ERR_20539.setErrorCode(processedResult);
                return processedResult;
            }
			if(StringUtils.isNotBlank(accountTypeId) && !AlertManagementHandler.getAccountTypeIdsMap(requestInstance,legalEntityId).containsKey(accountTypeId)) {
				alert.prepareError("Invalid account type").log();
				ErrorCodeEnum.ERR_20549.setErrorCode(processedResult);
				return processedResult;
			}
			
			String accountTypeDerieved = StringUtils.isNotBlank(accountTypeId) ? accountTypeId : null;
			if(StringUtils.isNotBlank(accountID) && StringUtils.isBlank(accountTypeDerieved)) {
				/*Need to re-visit this part*/
				accountTypeDerieved = getAccountTypeIdFromAccounts(requestInstance, accountID, accountTypeId, accounts, legalEntityId);
				if(StringUtils.isBlank(accountTypeDerieved)) {
					alert.prepareError("Invalid account type").log();
					ErrorCodeEnum.ERR_20549.setErrorCode(processedResult);
					return processedResult;
				} 
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

			//Get AlertPreferences
			Dataset alertConfigDS = getAlertPreferenceRecord(requestInstance, processedResult,legalEntityId);		
			if(processedResult.getParamValueByName(ACConstants.DBP_ERROR_CODE) != null) {
				return processedResult;
			}
			String alertPreference = alertConfigDS.getRecord(0).getParamValueByName("alertPreferenceView");
			boolean isEditFrequencyEnabled = false;

			isEditFrequencyEnabled = isFrequencyEditable(alertConfigDS);
			processedResult.addDataset(alertConfigDS);

			Result cusAlertChannels = invokeCustomerService(requestInstance, customerID, accountID, accountTypeId,
					alertCategoryID,legalEntityId ,ServiceURLEnum.CUSTOMERALERTCHANNEL_READ, ErrorCodeEnum.ERR_20967);

			Result cusAlertFreq = null;
			if(isEditFrequencyEnabled)  {
				cusAlertFreq = invokeCustomerService(requestInstance, customerID, accountID, accountTypeId,
						alertCategoryID,legalEntityId ,ServiceURLEnum.CUSTOMERALERTFREQUENCY_READ, ErrorCodeEnum.ERR_20968);
			}

			// Get Alert Types subscribed by customer
			Result subscribedAlertTypes = AlertManagementHandler.getCustomerAlerts(customerID, accountID,
					accountTypeId, alertCategoryID, requestInstance,legalEntityId);

			// Get Active Alert Types Data
			List<Record> alertTypes = AlertManagementHandler.getAlertTypesWithLocale(alertCategoryID, 
					acceptLanguage, true, requestInstance,legalEntityId);
			
			alertTypes = alertTypes.stream().filter(rec -> Integer.valueOf(rec.getParamValueByName("userAlerts_count")) > 0).collect(Collectors.toList());
			
			List<String> alertypesIDList = alertTypes.stream().map(
					rec -> rec.getParamValueByName("alerttype_id")).collect(Collectors.toList());
			
			List<Record> alertSubTypesRecList = getAlertSubTypesOfCustomerType(requestInstance, acceptLanguage,
					customerTypeStr, alertypesIDList, legalEntityId);
			
			List<String>  validAccountAlerts = null ;
			if (alertSubTypesRecList != null && !alertSubTypesRecList.isEmpty()) {
				//Get valid account alerts
				if (StringUtils.isNotBlank(accountTypeDerieved)) {
					validAccountAlerts = getListOfValidAccountAlerts(requestInstance, accountTypeDerieved, legalEntityId);
				}
				final List<String> validAccAlerts = validAccountAlerts;
				if (validAccountAlerts != null && !validAccountAlerts.isEmpty()) {
					alertSubTypesRecList = alertSubTypesRecList.stream()
							.filter(rec -> validAccAlerts.contains(rec.getParamValueByName("alertsubtype_id")))
							.collect(Collectors.toList());
				} 
			}
			// filter the alertTypes based on valid subalerts,This will refine alert groups
			//having inactive alerts and invalid customer types
			filterAlertTypesFromSubAlerts(alertypesIDList, alertSubTypesRecList);
			
			alertTypes = alertTypes.stream().filter(rec -> alertypesIDList.
					contains(rec.getParamValueByName("alerttype_id"))).collect(Collectors.toList());
			
				
			Dataset alertSubTypesDataset = getAlertsDataSet(requestInstance, accountID,
					accountTypeId, acceptLanguage, cusAlertChannels,
					cusAlertFreq, alertPreference, isEditFrequencyEnabled,alertSubTypesRecList,subscribedAlertTypes, legalEntityId);
		
			
			// Sort Alert Types as per Display-Sequence Order
			alertTypes = CommonUtilities.sortListOfRecords(alertTypes, "alerttype_DisplaySequence", true, true);
			
			Set<String> groupsWithFrequencySet = alertSubTypesRecList.stream().filter(
					rec -> rec.getParamValueByName("alertsubtype_defaultFrequencyId") != null)
					.map(getAlertTypeIDFromRecord).collect(Collectors.toSet());

			Map<String, List<Record>> supChannelAlertGroupsMap = null;
			Map<String, List<Record>> subChannelAlertGroupsMap = null;

			if(ACConstants.ALERTPREFERNCES.GROUP.name().equalsIgnoreCase(alertPreference)){
				Result res = getAlertGroupChannels(requestInstance, alertypesIDList,legalEntityId);
				Function<Record, String> getAlerTypeAsString = rec -> rec.getParamValueByName("alertTypeId");

				supChannelAlertGroupsMap = res.getDatasetById(ACConstants.ALERTTYPECHANNEL_TN)
						.getAllRecords().stream().collect(Collectors.groupingBy(getAlerTypeAsString));

				subChannelAlertGroupsMap = cusAlertChannels.getDatasetById("customeralertchannel").getAllRecords().
						stream().collect(Collectors.groupingBy(getAlerTypeAsString));

			}

			alertTypes = addAlertsAndSubscribedChnlsFreqToGroup(alertPreference, isEditFrequencyEnabled, subscribedAlertTypes,
					alertTypes, alertSubTypesDataset, 
					supChannelAlertGroupsMap, subChannelAlertGroupsMap, cusAlertFreq,groupsWithFrequencySet);


			Dataset alertTypesDataset = new Dataset("alertTypes");
			alertTypesDataset.addAllRecords(alertTypes);
			processedResult.addDataset(alertTypesDataset);			

			Record alertCategoryRecord = getCategoryRecord(requestInstance, alertCategoryID, alertPreference,
					isEditFrequencyEnabled, cusAlertChannels, cusAlertFreq, groupsWithFrequencySet,legalEntityId);
			processedResult.addRecord(alertCategoryRecord);

			// Add Alert Category Subscription and Initial Load Status
			Record categorySwitchRecord = getCategorySubscriptionStatus(alertCategoryID, customerID, 
					accountID,accountTypeId, requestInstance, legalEntityId);
			processedResult.addRecord(categorySwitchRecord);

		} catch (ApplicationException e) {
			Result errorResult = new Result();
			errorResult.addParam("FailureReason", e.getMessage());
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			errorResult.addParam("FailureReason", e.getMessage());
			alert.prepareError("Unexpected Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20926.setErrorCode(errorResult);
			return errorResult;
		}
		return processedResult;
	}

	public Record getCategoryRecord(DataControllerRequest requestInstance, String alertCategoryID,
			String alertPreference, boolean isEditFrequencyEnabled, Result cusAlertChannels, Result cusAlertFreq,
			Set<String> groupsWithFrequencySet, String legalEntityId) throws ApplicationException {
		Record alertCategoryRecord = new Record();
		alertCategoryRecord.setId("alertCategory");
		alertCategoryRecord.addParam(new Param("name",alertCategoryID,FabricConstants.STRING));

		if(ACConstants.ALERTPREFERNCES.CATEGORY.name().equalsIgnoreCase(alertPreference)){
			// Category_Channel
			TABLE_OPERATION_MAPPING channelCategoryTM = TABLE_OPERATION_MAPPING.ALERTCATEGORYCHANNEL;
			Result alertCategoryChannelResult = invokeService(requestInstance, alertCategoryID, channelCategoryTM,legalEntityId);
			Function<Record, String> getAlertCatIdAsString = rec -> rec.getParamValueByName("alertCategoryId");

			if(!(alertCategoryChannelResult.
					getDatasetById(channelCategoryTM.getTableName()).getAllRecords().isEmpty())) {
				Function<Record,String> getChannelIDColFromRecord = rec -> String.valueOf(rec.getParamValueByName("ChannelID"));
				alertCategoryRecord.addParam(SUPPORTED_CHANNELS, alertCategoryChannelResult.
						getDatasetById(channelCategoryTM.getTableName()).getAllRecords().stream()
						.map(getChannelIDColFromRecord).distinct().collect(Collectors.joining(",")),FabricConstants.STRING);
			}

			Map<String, List<Record>> customerSubCatChannels = cusAlertChannels.getDatasetById("customeralertchannel").
					getAllRecords().stream().collect(Collectors.groupingBy(getAlertCatIdAsString));


			if (customerSubCatChannels != null && customerSubCatChannels.get(alertCategoryID) != null) {
				alertCategoryRecord.addParam(
						SUBSCRIBED_CHANNELS, customerSubCatChannels.get(alertCategoryID).stream()
						.map(getChannelIDFromRecord).distinct().collect(Collectors.joining(",")),
						FabricConstants.STRING);
			}
			if(isEditFrequencyEnabled && !groupsWithFrequencySet.isEmpty()) {
				Dataset subFreq = getClientFrequencyAtCategoryLevelDataset(cusAlertFreq,
						"customeralertfrequency", SUBSCRIBED_FREQUENCY);
				if(subFreq != null) {
					alertCategoryRecord.addDataset(subFreq);
				}else {
					Map<String, Object> inputMap = new HashMap<>();
					String filter  =  "id eq '"+ alertCategoryID+"'";
					filter = filter +" and  companyLegalUnit eq '" + legalEntityId + "'";
					inputMap.put(ODataQueryConstants.FILTER,filter);				    	
					Result catReadResult = ServiceUtil.invokeService(						
							ServiceURLEnum.DBXALERTCATEGORY_READ, inputMap, null, requestInstance);
					AlertManagementHandler.isOperationSuccessful(catReadResult, ServiceURLEnum.DBXALERTCATEGORY_READ,
							ErrorCodeEnum.ERR_20915);
					if(!catReadResult.getDatasetById("dbxalertcategory").getAllRecords().isEmpty()) {
						Record catRecord = catReadResult.getDatasetById("dbxalertcategory").getAllRecords().get(0);
						if(catRecord.getParamValueByName("defaultFrequencyId") != null ) {
							alertCategoryRecord.addDataset(getCategoryDefaultFreqDataset(catRecord));
						}						
					}
				}
			}

		}
		return alertCategoryRecord;
	}

	public List<Record> addAlertsAndSubscribedChnlsFreqToGroup(String alertPreference, boolean isEditFrequencyEnabled,
			Result subscribedAlertTypes, List<Record> alertTypes, Dataset alertSubTypesDataset,
			Map<String, List<Record>> supChannelAlertGroupsMap, Map<String, List<Record>> subChannelAlertGroupsMap,
			Result cusAlertFreq, Set<String> groupsWithFrequencySet) {
		List<Record> alertGroups = new ArrayList();
		Function<Record, String> getAlerTypeAsString = rec -> rec.getParamValueByName("alertTypeId");
		Map<String, List<Record>> alertTypeMap = alertSubTypesDataset.getAllRecords().stream().collect(
				Collectors.groupingBy(getAlertTypeIDFromRecord));
		
		for (Record alertTypeRec : alertTypes) {
			String alertTypeID = alertTypeRec.getParamValueByName("alerttype_id");	
			Record clientRec = getClientAlertRecord(alertTypeRec, ALERT_GROUP_PARAMS);
			Dataset ds = new Dataset("alertSubTypes");
			if(	alertTypeMap.containsKey(alertTypeID)) {
				alertTypeMap.get(alertTypeID).stream().forEach(rec -> {
					rec.removeParam(rec.getParam("alertsubtype_alertTypeId"));
					if(rec.getParam("alertsubtype_defaultFrequencyId") != null) {
						rec.removeParamByName("alertsubtype_defaultFrequencyId");
					}});
				ds.addAllRecords(alertTypeMap.get(alertTypeID));
			}
			clientRec.addDataset(ds);
			Map<String, List<Record>> subscribedAlertTypesMap = getSubsribedAlertMap(subscribedAlertTypes,"AlertTypeId");
			String isSubscribe = subscribedAlertTypesMap.containsKey(alertTypeID) ? "true" : "false";
			clientRec.addParam(getSubscribedParam(isSubscribe));

			if(ACConstants.ALERTPREFERNCES.GROUP.name().equalsIgnoreCase(alertPreference)) {
				if (supChannelAlertGroupsMap != null && supChannelAlertGroupsMap.get(alertTypeID) != null) {
					clientRec.addParam(
							SUPPORTED_CHANNELS, supChannelAlertGroupsMap.get(alertTypeID).stream()
							.map(getChannelIDFromRecord).distinct().collect(Collectors.joining(",")),
							FabricConstants.STRING);
				}
				if(subChannelAlertGroupsMap != null && subChannelAlertGroupsMap.get(alertTypeID) != null) {
					clientRec.addParam(SUBSCRIBED_CHANNELS,  subChannelAlertGroupsMap.get(alertTypeID).stream()
							.map(getChannelIDFromRecord).distinct().collect(Collectors.joining(",")),FabricConstants.STRING);
				}
				Map<String, List<Record>> subscribedGroupFreq = null;
				if(isEditFrequencyEnabled && !groupsWithFrequencySet.isEmpty() && 
						groupsWithFrequencySet.contains(alertTypeID)) {
					subscribedGroupFreq = cusAlertFreq.getDatasetById("customeralertfrequency")
							.getAllRecords().stream().collect(Collectors.groupingBy(getAlerTypeAsString));
					if( subscribedGroupFreq != null && subscribedGroupFreq.get(alertTypeID) != null) {
						clientRec.addDataset(
								getCustSubscribedFreqDataset(subscribedGroupFreq.get(alertTypeID).get(0)));
					} else if(alertTypeRec.getParamValueByName("alerttype_freqId") != null ) {
						clientRec.addDataset(getAlertTypeDefaultFreqDataset(alertTypeRec));
					}

				}
			}
			alertGroups.add(clientRec);
		}
		return alertGroups;
	}

	public static boolean isFrequencyEditable(Dataset alertConfigDS) {
		boolean isEditFrequencyEnabled = false;
		String alertFrequencyPreferenceStr = alertConfigDS.getRecord(0).getParamValueByName("enableFrequency");
		if (StringUtils.equalsIgnoreCase(alertFrequencyPreferenceStr, "TRUE")
				|| StringUtils.equalsIgnoreCase(alertFrequencyPreferenceStr, "1")) {
			isEditFrequencyEnabled = true;			
		}
		return isEditFrequencyEnabled;
	}

	private Param getSubscribedParam(String isSubscribe) {
		return new Param(IS_SUBSCRIBED_PARAM, isSubscribe , FabricConstants.BOOLEAN);
	}

	private Result getAlertGroupChannels(DataControllerRequest requestInstance, List<String> alertypesIDList, String legalEntityId)
			throws ApplicationException {
		// Construct Filter Query
		StringBuilder filterStrBuilder = new StringBuilder();
		AlertManagementHandler.getFilterQueryFromList(alertypesIDList, filterStrBuilder, "alertTypeId eq '");
		if (filterStrBuilder.length() > 0) {
			filterStrBuilder.append(" and ");
		}
		filterStrBuilder.append("(companyLegalUnit eq '" + legalEntityId + "')");
		// Prepare Input Map
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, filterStrBuilder.toString());
		inputMap.put(ODataQueryConstants.SELECT, "channelId,alertTypeId");

		Result res = ServiceUtil.invokeService(ServiceURLEnum.ALERTTYPECHANNEL_READ, inputMap, null, requestInstance);
		AlertManagementHandler.isOperationSuccessful(res, ServiceURLEnum.ALERTTYPECHANNEL_READ, 
				ACConstants.ALERTTYPECHANNEL_TN, ErrorCodeEnum.ERR_20899);
		return res;
	}

	private Dataset getAlertsDataSet(DataControllerRequest requestInstance,
			String accountID, String accountTypeId, String acceptLanguage,
			Result cusAlertChannels, Result cusAlertFreq, String alertPreference,
			boolean isEditFrequencyEnabled, List<Record> alertSubTypesRecList,
			Result subscribedAlertTypes, String legalEntityId) throws ApplicationException {
					
		if (alertSubTypesRecList != null && !alertSubTypesRecList.isEmpty()) {
		
			// Get List of Alert Conditions and Alert Attributes of Applicable Alert Types
			Map<String, Record> alertAttributesMap = getAlertAttributes(requestInstance, acceptLanguage,
					alertSubTypesRecList,legalEntityId);
			Map<String, Record> alertConditionMap = getAlertConditionsMap(requestInstance, acceptLanguage,
					alertSubTypesRecList,legalEntityId);
			// Convert Subscribed Alert Types data to Map
			Map<String, List<Record>> subscribedAlertTypesMap = getSubsribedAlertMap(subscribedAlertTypes,
					"alertSubTypeId");
			// Collate applicable Alert Type Data and Subscribed Alert Type Data
			Map<String, List<Record>> supChannelAlertsMap = null;
			Map<String, List<Record>> subChannelAlertMap = null;
			Map<String, List<Record>> subscribedAlertFreq = null;
			if (ACConstants.ALERTPREFERNCES.ALERT.name().equalsIgnoreCase(alertPreference)) {
				Function<Record, String> getAlerSubTypeAsString = rec -> rec.getParamValueByName("alertSubTypeId");
				Result res = getAlertSubTypeChannels(requestInstance, alertSubTypesRecList, legalEntityId);
				supChannelAlertsMap = res.getDatasetById(ACConstants.ALERTSUBTYPECHANNEL_TN).getAllRecords().stream()
						.collect(Collectors.groupingBy(getAlerSubTypeAsString));

				subChannelAlertMap = cusAlertChannels.getDatasetById("customeralertchannel").getAllRecords().stream()
						.collect(Collectors.groupingBy(getAlerSubTypeAsString));

				if (isEditFrequencyEnabled) {
					subscribedAlertFreq = cusAlertFreq.getDatasetById("customeralertfrequency").getAllRecords().stream()
							.collect(Collectors.groupingBy(getAlerSubTypeAsString));
				}  

			}
			
			List<Record> validAlertsRecList = getValidAlertsRecords(alertPreference, alertSubTypesRecList);
			return prepareAlertDataSet(alertPreference, isEditFrequencyEnabled, alertAttributesMap, alertConditionMap,
					subscribedAlertTypesMap, supChannelAlertsMap, subChannelAlertMap, subscribedAlertFreq,
					validAlertsRecList);
		}
		return new Dataset("alertSubTypes");
	}

	public List<Record> getAlertSubTypesOfCustomerType(DataControllerRequest requestInstance, String acceptLanguage,
			String customerTypeID, List<String> alertypesIDList, String legalEntityId) throws ApplicationException {
		// Get IDs of Alerts Applicable to current Customer Type
		Set<String> alertSubTypesOfCustomerType = AlertManagementHandler.getAlertTypesofCustomerType(
				customerTypeID, requestInstance, legalEntityId);
		List<String> applicableAlertTypes = new ArrayList<>();
		applicableAlertTypes.addAll(alertSubTypesOfCustomerType);		
		// Get Active and Non-Global Alert Types Data
		List<Record> alertSubTypesRecList = AlertManagementHandler.getAlertSubTypes(alertypesIDList,
				applicableAlertTypes, acceptLanguage, false, true, requestInstance, legalEntityId);
		return alertSubTypesRecList;
	}

	public void filterAlertTypesFromSubAlerts(List<String> alertypesIDList, List<Record> alertSubTypesRecList) {
		
		List<String> filteredalertypesFromSubTypeIDList = null;
		if (alertSubTypesRecList != null && !alertSubTypesRecList.isEmpty()) {
		Set<String> alertTypeFromSubTypesRecList = alertSubTypesRecList.stream()
				.filter(rec -> 	rec.getParamValueByName("alertsubtype_alertTypeId") != null )
				.map(rec -> rec.getParamValueByName("alertsubtype_alertTypeId"))					
				.collect(Collectors.toSet());

		filteredalertypesFromSubTypeIDList = alertypesIDList.stream().
				filter(alertTypeId -> alertTypeFromSubTypesRecList.contains(alertTypeId)).
				collect(Collectors.toList());
		} else {
			filteredalertypesFromSubTypeIDList = new ArrayList<String>();
		}
		alertypesIDList.retainAll(filteredalertypesFromSubTypeIDList);
	}

	private List<Record> getValidAlertsRecords(String alertPreference, List<Record> alertSubTypesRecList ) {
		 return  ALERTPREFERNCES.ALERT.name().equals(alertPreference)
					? alertSubTypesRecList
							: alertSubTypesRecList.stream()
							.filter(rec -> rec.getParamValueByName("alertsubtype_attributeId") != null)
							.collect(Collectors.toList());
		
	}

	private Dataset prepareAlertDataSet(String alertPreference, boolean isEditFrequencyEnabled, 
			Map<String, Record> alertAttributesMap,
			Map<String, Record> alertConditionMap, Map<String, List<Record>> subscribedAlertTypesMap,
			Map<String, List<Record>> supChannelAlertsMap,
			Map<String, List<Record>> subChannelAlertMap, Map<String, List<Record>> subscribedAlertFreq,
			List<Record> validAlertsRecList) {
		Dataset alertSubTypesDataset = new Dataset();
		alertSubTypesDataset.setId("alertSubTypes");	  
		String currAlertTypeId;
		String currAlertConditionId;
		String currAlertAttributeId;
		for (Record alertRecord : validAlertsRecList) {		    		
			// Add Alert Type Subscription Status
			currAlertTypeId =  alertRecord.getParamValueByName("alertsubtype_id");
			Record clientRecord = getClientAlertRecord(alertRecord, ALERT_PARAMS);
			overrideCustomerAttributeValues(subscribedAlertTypesMap, currAlertTypeId, clientRecord);

			// Add Alert Type Attributes Information
			currAlertAttributeId = alertRecord.getParamValueByName("alertsubtype_attributeId");
			if (StringUtils.isNotBlank(currAlertAttributeId) && alertAttributesMap.containsKey(currAlertAttributeId)) {
				clientRecord.addRecord(alertAttributesMap.get(currAlertAttributeId));
			}
			// Add Alert Type Condition Information
			currAlertConditionId = alertRecord.getParamValueByName("alertsubtype_alertConditionId");
			if (StringUtils.isNotBlank(currAlertConditionId) && alertConditionMap.containsKey(currAlertConditionId)) {
				clientRecord.addRecord(alertConditionMap.get(currAlertConditionId));
			}

			if(ACConstants.ALERTPREFERNCES.ALERT.name().equalsIgnoreCase(alertPreference)) {
				addDefaultAndSubsChannelFrequency(supChannelAlertsMap, subChannelAlertMap, subscribedAlertFreq,
						currAlertTypeId, alertRecord ,isEditFrequencyEnabled,clientRecord);
			}					


			alertSubTypesDataset.addRecord(clientRecord);

		}
		return alertSubTypesDataset;
	}

	private Record getClientAlertRecord(Record currentRecord, List<String> paramList) {
		Record clientRecord = new Record();
		for (String paramName : paramList) {
			if(currentRecord.getParamValueByName(paramName) != null) {
				clientRecord.addParam(currentRecord.getParam(paramName)); 
			}
		}   
		return clientRecord;
	}

	private void addDefaultAndSubsChannelFrequency(Map<String, List<Record>> supChannelAlertsMap,
			Map<String, List<Record>> subChannelAlertMap, Map<String, List<Record>> subscribedAlertFreq,
			String currAlertTypeId, Record alertRecord, boolean isEditFrequencyEnabled,Record clientRecord) {
		if (supChannelAlertsMap != null && supChannelAlertsMap.get(currAlertTypeId) != null) {
			clientRecord.addParam(
					SUPPORTED_CHANNELS, supChannelAlertsMap.get(currAlertTypeId).stream()
					.map(getChannelIDFromRecord).distinct().collect(Collectors.joining(",")),
					FabricConstants.STRING);
		}
		if(subChannelAlertMap != null && subChannelAlertMap.get(currAlertTypeId) != null) {
			clientRecord.addParam(SUBSCRIBED_CHANNELS,subChannelAlertMap.get(currAlertTypeId).stream()
					.map(getChannelIDFromRecord).distinct().collect(Collectors.joining(",")),FabricConstants.STRING);
		}

		if(isEditFrequencyEnabled){
			if(subscribedAlertFreq!= null && subscribedAlertFreq.get(currAlertTypeId) != null) {
				clientRecord.addDataset(getCustSubscribedFreqDataset(subscribedAlertFreq.get(currAlertTypeId).get(0)));
			}else if(alertRecord.getParamValueByName("alertsubtype_defaultFrequencyId") != null) {
				clientRecord.addDataset(getAlertDefaultFreqDataset(alertRecord));					
			}					
		}		
	}

	private void overrideCustomerAttributeValues(Map<String, List<Record>> subscribedAlertTypesMap,
			String currAlertTypeId, Record alertRecord) {
		if (subscribedAlertTypesMap.containsKey(currAlertTypeId)) {
			alertRecord.addParam(getSubscribedParam("true"));
			if(subscribedAlertTypesMap.get(currAlertTypeId).get(0).getParamByName("Value1") != null) {
				alertRecord.addParam(new Param("alertsubtype_value1",
						subscribedAlertTypesMap.get(currAlertTypeId).get(0).getParamByName("Value1").getValue()));
			}
			if(subscribedAlertTypesMap.get(currAlertTypeId).get(0).getParamByName("Value2") != null) {
				alertRecord.addParam(new Param("alertsubtype_value2",
						subscribedAlertTypesMap.get(currAlertTypeId).get(0).getParamByName("Value2").getValue()));
			}
		} else {
			alertRecord.addParam(getSubscribedParam("false"));
		}
	}

	private Map<String, Record> getAlertAttributes(DataControllerRequest requestInstance, String acceptLanguage,
			List<Record> alertSubTypesRecList, String legalEntityId) throws ApplicationException {
		Set<String> alertAttributeIds = alertSubTypesRecList.stream()
				.filter(rec -> 	rec.getParamValueByName("alertsubtype_attributeId") != null )
				.map(rec -> rec.getParamValueByName("alertsubtype_attributeId"))					
				.collect(Collectors.toSet());

		// Get Alert Attributes
		Map<String, Record> alertAttributesMap = AlertManagementHandler.getAlertAttributes(alertAttributeIds, acceptLanguage, requestInstance, legalEntityId);
		return alertAttributesMap;
	}

	private Map<String, Record> getAlertConditionsMap(DataControllerRequest requestInstance, String acceptLanguage,
			List<Record> alertSubTypesRecList, String legalEntityId) throws ApplicationException {
		Set<String> alertConditionIds = alertSubTypesRecList.stream()
				.filter(rec -> 	rec.getParamValueByName("alertsubtype_alertConditionId") != null )
				.map(rec -> rec.getParamValueByName("alertsubtype_alertConditionId"))					
				.collect(Collectors.toSet());

		// Get Alert Conditions
		Map<String, Record> alertConditionMap = AlertManagementHandler.getAlertConditions(alertConditionIds, acceptLanguage, requestInstance, legalEntityId);
		return alertConditionMap;
	}

	private Result getAlertSubTypeChannels(DataControllerRequest requestInstance, List<Record> alertSubTypesRecList,String legalEntityId)
			throws ApplicationException {
		// Construct Filter Query
		StringBuilder filterStrBuilder = new StringBuilder();				
		List<String> alerSubtypesIDList = alertSubTypesRecList.stream().map(
				rec -> rec.getParamValueByName("alertsubtype_id")).collect(Collectors.toList());				
		AlertManagementHandler.getFilterQueryFromList(alerSubtypesIDList, filterStrBuilder, "alertSubTypeId eq '");		
		if (filterStrBuilder.length() > 0) {
			filterStrBuilder.append(" and ");
        }
		filterStrBuilder.append("(companyLegalUnit eq '" + legalEntityId + "')");
		// Prepare Input Map
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, filterStrBuilder.toString());
		inputMap.put(ODataQueryConstants.SELECT, "channelId,alertSubTypeId");
		Result res = ServiceUtil.invokeService(ServiceURLEnum.ALERTSUBTYPECHANNEL_READ, inputMap, null, requestInstance);
		AlertManagementHandler.isOperationSuccessful(res, ServiceURLEnum.ALERTSUBTYPECHANNEL_READ, 
				ACConstants.ALERTSUBTYPECHANNEL_TN, ErrorCodeEnum.ERR_20960);
		return res;
	}

	private Map<String, List<Record>> getSubsribedAlertMap(Result subscribedAlertTypes, String columnname) {
		Function< Record, String> getColumnNameAsString = rec -> rec.getParamValueByName(columnname);
		return subscribedAlertTypes.getDatasetById("dbxcustomeralertentitlement").getAllRecords().stream()
				.collect(Collectors.groupingBy(getColumnNameAsString));

	}


	private Dataset getClientFrequencyAtCategoryLevelDataset(Result backendResult, String dsName,
			String clientDataSetName) {
		Dataset clientDs= null;
		if(!backendResult.getDatasetById(dsName).getAllRecords().isEmpty()) {
			clientDs= new Dataset(clientDataSetName);			
			Record rec  = backendResult.getDatasetById(dsName).getRecord(0);
			Record freqRec = new Record();
			freqRec.addParam(new Param("alertFrequencyId",rec.getParamValueByName("alertFrequencyId"), FabricConstants.STRING));
			freqRec.addParam (new Param("frequencyValue",rec.getParamValueByName("frequencyValue"), FabricConstants.STRING));
			freqRec.addParam(new Param("frequencyTime",rec.getParamValueByName("frequencyTime"), FabricConstants.STRING));
			clientDs.addRecord(freqRec);
		}		 
		return clientDs;
	}

	private Dataset getCustSubscribedFreqDataset(Record rec) {
		Dataset clientDs= new Dataset(SUBSCRIBED_FREQUENCY);
		Record freqRec = new Record();
		freqRec.addParam(new Param("alertFrequencyId",rec.getParamValueByName("alertFrequencyId"), FabricConstants.STRING));
		freqRec.addParam(new Param("frequencyValue",rec.getParamValueByName("frequencyValue"), FabricConstants.STRING));
		freqRec.addParam(new Param("frequencyTime",rec.getParamValueByName("frequencyTime"), FabricConstants.STRING));
		clientDs.addRecord(freqRec);				 
		return clientDs;
	}

	private Dataset getAlertTypeDefaultFreqDataset(Record rec) {
		Dataset clientDs= new Dataset(SUBSCRIBED_FREQUENCY);
		Record freqRec = new Record();
		freqRec.addParam(new Param("alertFrequencyId",rec.getParamValueByName("alerttype_freqId"), FabricConstants.STRING));
		freqRec.addParam(new Param("frequencyValue",rec.getParamValueByName("alerttype_freqValue"), FabricConstants.STRING));
		freqRec.addParam(new Param("frequencyTime",rec.getParamValueByName("alerttype_freqTime"), FabricConstants.STRING));
		clientDs.addRecord(freqRec);				 
		return clientDs;
	}

	private Dataset getAlertDefaultFreqDataset(Record rec) {
		Dataset clientDs= new Dataset(SUBSCRIBED_FREQUENCY);
		Record freqRec = new Record();
		freqRec.addParam(new Param("alertFrequencyId",rec.getParamValueByName("alertsubtype_defaultFrequencyId"), FabricConstants.STRING));
		freqRec.addParam(new Param("frequencyValue",rec.getParamValueByName("alertsubtype_defaultFrequencyValue"), FabricConstants.STRING));
		freqRec.addParam(new Param("frequencyTime",rec.getParamValueByName("alertsubtype_defaultFrequencyTime"), FabricConstants.STRING));
		clientDs.addRecord(freqRec);				 
		return clientDs;
	}

	private Dataset getCategoryDefaultFreqDataset(Record rec) {
		Dataset clientDs= new Dataset(SUBSCRIBED_FREQUENCY);
		Record freqRec = new Record();
		freqRec.addParam(new Param("alertFrequencyId",rec.getParamValueByName("defaultFrequencyId"), FabricConstants.STRING));
		freqRec.addParam(new Param("frequencyValue",rec.getParamValueByName("defaultFrequencyValue"), FabricConstants.STRING));
		freqRec.addParam(new Param("frequencyTime",rec.getParamValueByName("defaultFrequencyTime"), FabricConstants.STRING));
		clientDs.addRecord(freqRec);				 
		return clientDs;
	}


	private Result invokeService(DataControllerRequest requestInstance, String pkChannel,
			TABLE_OPERATION_MAPPING tableOpMaping,String legalEntityId) throws ApplicationException {
		String filter  = tableOpMaping.getAlertpkId1()+ " eq '" + pkChannel + "'";
		filter = filter +  " and  companyLegalUnit eq '" + legalEntityId + "'";
		
		return ServiceUtil.invokeGetService(requestInstance, tableOpMaping.getReadServiceName(),
				filter, tableOpMaping.getCompositePKid(),
				tableOpMaping.getTableName(),ErrorCodeEnum.ERR_20912);
	}

	private void addFilterCondition(StringBuilder condBuilder,String columnName, String columnValue) {
		if (StringUtils.isNotBlank(columnValue)) {
			if (condBuilder.length() > 0) {
				condBuilder.append(" and ");
			}//alerttype_AlertCategoryId
			condBuilder.append("(").append(columnName). append(" eq '").append(columnValue).append("')");
		}
	}

	private Result invokeCustomerService(DataControllerRequest dcRequest,String customerId,
			String accountId , String accountType, String categoryId, String legalEntityId,
			ServiceURLEnum serviceUrl,ErrorCodeEnum errorCode) throws ApplicationException {
		Map<String, Object> inputMap = new HashMap<>();
		StringBuilder filterCondition = new StringBuilder();      
		addFilterCondition(filterCondition,"customerId",customerId);
		addFilterCondition(filterCondition,"alertCategoryId",categoryId);
		if (StringUtils.isNotBlank(accountType)){            
			addFilterCondition(filterCondition,"accountType",accountType);
		}
		if (StringUtils.isNotBlank(accountId)) {            
			addFilterCondition(filterCondition,"accountId",accountId);
		}
		addFilterCondition(filterCondition,"companyLegalUnit",legalEntityId);
		filterCondition.trimToSize();
		inputMap.put(ODataQueryConstants.FILTER, filterCondition.toString());
		Result result = ServiceUtil.invokeService(serviceUrl, inputMap,null, dcRequest);
		AlertManagementHandler.isOperationSuccessful(result,serviceUrl,errorCode);
		return result;
	}

	/* Method to fetch Subscription Status of Customer to an Alert Category

	 *@param alertCategoryID
	 *@param customerID
	 *@param accountID
	 *@param requestInstance
	 *@return
	 *@throws ApplicationException 
	 */
	public Record getCategorySubscriptionStatus(String alertCategoryID, String customerID, String accountID, String accountTypeId, DataControllerRequest requestInstance,
			String legalEntityId) throws ApplicationException {
		if (requestInstance == null) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20925);
		}

		Map<String, String> inputMap = new HashMap<>();

		StringBuffer filterQueryBuffer = new StringBuffer();
		filterQueryBuffer.append("Customer_id eq '" + customerID + "' and AlertCategoryId eq '" + alertCategoryID + "'");

		if (StringUtils.isNotBlank(accountID)) {
			filterQueryBuffer.append(" and AccountID eq '" + accountID + "'");
		}
		if (StringUtils.isNotBlank(accountTypeId)) {
			filterQueryBuffer.append(" and AccountType eq '" + accountTypeId + "'");
		}
		filterQueryBuffer.append(" and companyLegalUnit eq '" + legalEntityId + "'");
		filterQueryBuffer.trimToSize();
		String filterQuery = filterQueryBuffer.toString();
		filterQuery = filterQuery.trim();
		inputMap.put(ODataQueryConstants.FILTER, filterQuery);

		String readCustomerAlertSwitchResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERALERTSWITCH_READ, inputMap, null, requestInstance);
		JSONObject readCustomerAlertSwitchResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerAlertSwitchResponse);
		if (readCustomerAlertSwitchResponseJSON == null || !readCustomerAlertSwitchResponseJSON.has(FabricConstants.OPSTATUS)
				|| readCustomerAlertSwitchResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCustomerAlertSwitchResponseJSON.has("customeralertswitch")) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20925);
		}

		boolean isSubscribed = false;
		boolean isInitialLoad = false;

		JSONArray customerAlertSwitchArray = readCustomerAlertSwitchResponseJSON.optJSONArray("customeralertswitch");
		if (customerAlertSwitchArray != null && customerAlertSwitchArray.length() > 0 && customerAlertSwitchArray.get(0) instanceof JSONObject) {
			JSONObject alertSwitchJSONObject = customerAlertSwitchArray.optJSONObject(0);
			isInitialLoad = false;
			if (alertSwitchJSONObject.has("Status_id") && StringUtils.equalsIgnoreCase(StatusEnum.SID_SUBSCRIBED.name(), alertSwitchJSONObject.optString("Status_id"))) {
				isSubscribed = true;
			} else {
				isSubscribed = false;
			}
		} else {
			isInitialLoad = true;
			isSubscribed = false;
		}

		Record subscriptionRecord = new Record();
		subscriptionRecord.setId("categorySubscription");
		subscriptionRecord.addParam(getSubscribedParam(String.valueOf(isSubscribed)));
		subscriptionRecord.addParam(new Param(IS_INITIAL_LOAD_PARAM, String.valueOf(isInitialLoad), FabricConstants.STRING));

		return subscriptionRecord;
	}

	public static List<String> getListOfValidAccountAlerts(DataControllerRequest requestInstance,
			 String accountTypeId, String legalEntityId) throws ApplicationException {
		//Construct valid alert type list
		Map<String,String> inputMap = new HashMap<>();
		String filer = "accountTypeId eq '"+accountTypeId+"'";
		filer = filer + " and companyLegalUnit eq '"+legalEntityId+"'";
		
		inputMap.put(ODataQueryConstants.FILTER, filer);
		String readAlertTypeAccountTypeStr = Executor.invokeService(ServiceURLEnum.ALERTSUBTYPEACCOUNTTYPE_READ, inputMap, null, requestInstance);
		JSONObject readAlertTypeAccountType = CommonUtilities.getStringAsJSONObject(readAlertTypeAccountTypeStr);
		if (readAlertTypeAccountType == null || !readAlertTypeAccountType.has(FabricConstants.OPSTATUS)
				|| readAlertTypeAccountType.getInt(FabricConstants.OPSTATUS) != 0 || !readAlertTypeAccountType.has("alertsubtypeaccounttype")) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20846);
		}
		List<String> validAlerts = new ArrayList<>();
		JSONArray alertTypeAccountTypesArray = readAlertTypeAccountType.getJSONArray("alertsubtypeaccounttype");
		alertTypeAccountTypesArray.forEach(alertObject -> {
			JSONObject alert = (JSONObject)alertObject;
			validAlerts.add(alert.getString("alertSubTypeId"));
		});

		return validAlerts;
	}

	public static String getAccountTypeIdFromAccounts(DataControllerRequest requestInstance, String accountID,
			String accountTypeId, String accounts, String legalEntityId) throws ApplicationException {
		 if(StringUtils.isNotBlank(accountTypeId)) {
			 return accountTypeId;
		 }
			 
		if(StringUtils.isNotBlank(accountID) && StringUtils.isBlank(accountTypeId)) {
			//Fetch account type from accounts information
			if(StringUtils.isBlank(accounts)) {
				return null;
			}
			JSONArray accountsJSON = null;
			try {
				accountsJSON = new JSONArray(accounts);
			}catch(Exception e) {
				alert.prepareError("Invalid accounts object received").log();
				return null;
			}
			for(int index=0; index < accountsJSON.length(); index++) {
				JSONObject account = accountsJSON.getJSONObject(index);
				if(account.getString("accountID").equalsIgnoreCase(accountID)) {
					accountTypeId = AlertManagementHandler.getAccountTypesMap(requestInstance,legalEntityId).
							get(account.getString("accountType"));
					break;
				}
			}
		}
		return accountTypeId;
	}

	public static Dataset getAlertPreferenceRecord(DataControllerRequest dcReq,Result finalResult, String legalEntityId) 
			throws ApplicationException{
		Result alertPreferences = AlertManagementHandler.getAlertPreferences(dcReq, legalEntityId);
		return AlertCategoryPreferenceGetService.
				getAlertConfigRecord(finalResult, alertPreferences);

	}

}

