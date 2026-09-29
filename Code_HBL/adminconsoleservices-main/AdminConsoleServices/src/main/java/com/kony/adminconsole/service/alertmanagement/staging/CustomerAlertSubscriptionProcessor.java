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
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.Gson;
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.kony.adminconsole.service.alertmanagement.staging.util.Alert;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertGroup;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertSubscription;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ACConstants.ALERTPREFERNCES;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.ServiceUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerAlertSubscriptionProcessor {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String ALERT_SUBSCRIPTION = "alertSubscription";
	
	private static final List<String> INSERT_AND_DELETE_PROC_PARAMS = Arrays.asList("Customer_id","alertCategoryId","AlertTypeId","alertSubTypeId","AccountId","AccountType","Value1","Value2","alertRequestId","createdby","companyLegalUnit");
	private static final List<String> UPDATE_PROC_PARAMS = Arrays.asList("Customer_id","alertCategoryId","AlertTypeId","alertSubTypeId","AccountId","modifiedby","alertRequestId","Value1","Value2","AccountType","companyLegalUnit");

	private static Function<Record, String> alertsubtypeAlertTypeIdFunc = rec -> rec.getParamValueByName("alertsubtype_alertTypeId");

	public Record setAlertSubscriptions(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			String loggedInUserId, String customerTypeStr, String legalEntityId)	throws ApplicationException {
		Record operationRecord = new Record();
		operationRecord.setId(ALERT_SUBSCRIPTION);	
		Dataset ds =  processCustomerAlertSubscription(dcReq, loggedInUserId,  alertSubObj, customerTypeStr,legalEntityId);
		ds= ds != null ? ds: new Dataset(ALERT_SUBSCRIPTION);
		operationRecord.addDataset(ds);
		return operationRecord;
	}	

	private Dataset processCustomerAlertSubscription(DataControllerRequest dcReq,
			String loggedInUserId, AlertSubscription alertSubObj, String customerTypeStr,String legalEntityId) throws ApplicationException {
		Dataset subscriptionDataset = new Dataset();
		subscriptionDataset.setId("subscriptionDataset");
				
		if (alertSubObj != null && !alertSubObj.getGroups().isEmpty()) {
			Result readResponse = getExistingAlertSubscriptions(dcReq, alertSubObj,legalEntityId);
			String preferenceLevel = alertSubObj.getPreferenceLevel();
			 List<Record> alertRecords = getAllUserAlertsOfTheGroup(dcReq, alertSubObj, customerTypeStr, 
											preferenceLevel, legalEntityId);
			
			diagnostic.prepareDebug("final alert Records" + alertRecords).log();
			// get the alert which are mapped to external
			 Map<String, String> externalAlertGroupMap = alertRecords.stream().
					filter(rec -> "1".equalsIgnoreCase(rec.getParamValueByName("alertsubtype_externalSystem"))).
					collect(Collectors.toMap(rec -> rec.getParamValueByName("alertsubtype_id"),
											  rec -> rec.getParamValueByName("alertsubtype_alertTypeId")));
		  diagnostic.prepareDebug("externalAlertGroupMap" + externalAlertGroupMap).log();
			resetAlertAttributeValues(alertSubObj, alertRecords);			 
			 
					
			 Map<String,String> externalAlertSubs = new HashMap<>();
			 Map<String, String> referenceIdofAlert = getexternalReferencesForAlertEntitlements(readResponse);
			if(externalAlertGroupMap!= null && !externalAlertGroupMap.isEmpty()) {
				externalAlertSubs = processExternalAlertSubscription(alertSubObj, referenceIdofAlert, externalAlertGroupMap,legalEntityId);
			}
	
			 Map<String, List<Record>> alertTypeGroupMap = alertRecords.stream().collect(
		 				Collectors.groupingBy(alertsubtypeAlertTypeIdFunc));
			 diagnostic.prepareDebug("Read total alerts").log();
			Set<String> associatedAlertEntitlements = getExistingAlertEntitlements(readResponse);
			
			Map<String, Object> parameterMap = new HashMap<>(); 	
			ProcedureDTO procDTO = new ProcedureDTO();
			parameterMap.put("companyLegalUnit", legalEntityId);				
			for ( AlertGroup currGroup : alertSubObj.getGroups()) {				
				Set<String> totalalertsList = null;				
				String currAlertGroupId = currGroup.getTypeID();
				if (StringUtils.isNotBlank(currAlertGroupId)) {					
					totalalertsList = getTotalAlertsOfGroup(alertTypeGroupMap, preferenceLevel, currGroup);							
					for (Alert alert : currGroup.getAlerts()) {	
						parameterMap.put("companyLegalUnit", legalEntityId);	
						parameterMap.put("AlertTypeId", currAlertGroupId); 
						addCommonInputParams(alertSubObj, parameterMap);
						boolean isSubscribed =	ALERTPREFERNCES.ALERT.name().equalsIgnoreCase(preferenceLevel) 
								? alert.isSub() : currGroup.isSub();
						parameterMap.put("alertSubTypeId", alert.getId());						
						ServiceURLEnum serviceURLName =	determineServiceName( 
								loggedInUserId, parameterMap, associatedAlertEntitlements, alert, isSubscribed, null);						
						parameterMap.put("alertRequestId", getAlertRequestId(externalAlertSubs, referenceIdofAlert, alert));
						String currOperation = serviceURLName != null ? getOperationName(serviceURLName) : "removePreference";
						// Based on the operation prepare required bulk record string and its correspoding response
						prepareProcInputAndResponse(alertSubObj, parameterMap, currAlertGroupId,
								alert.getId(), currOperation,procDTO);
						if(totalalertsList != null) {
							totalalertsList.remove(alert.getId());
						}
						resetParams(parameterMap);
					}
					processNonInputAlerts(alertSubObj,totalalertsList, currGroup,associatedAlertEntitlements,
							loggedInUserId,externalAlertSubs, procDTO,legalEntityId);
				}				
			}			
			updateAlertsToDatabase(dcReq, subscriptionDataset, parameterMap,procDTO);
		}
		return subscriptionDataset;
	}

	public void updateAlertsToDatabase(DataControllerRequest dcReq, Dataset subscriptionDataset,
			Map<String, Object> parameterMap, ProcedureDTO procDTO)
			throws ApplicationException {
		if(procDTO.getInsertSB().length() > 0 ) {
            parameterMap.clear();
			parameterMap.put("_recordvalues", procDTO.getInsertSB().toString());			
			parameterMap.put("success", false);
			invokeServiceWrapper(dcReq, parameterMap, ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_CREATEPROC);
			subscriptionDataset.addAllRecords(procDTO.getInsertReccList());
			
		}
		if(procDTO.getUpdateSB().length() > 0 ) {
			parameterMap.clear();
			parameterMap.put("_updateRecords", procDTO.getUpdateSB().toString());
			invokeServiceWrapper(dcReq, parameterMap, ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_UPDATEPROC);
			subscriptionDataset.addAllRecords(procDTO.getUpdateRecList());
		}
		if(procDTO.getDeleteSB().length() > 0 ) {
			parameterMap.clear();
			parameterMap.put("_deleteRecords", procDTO.getDeleteSB().toString());
			invokeServiceWrapper(dcReq, parameterMap, ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_DELETEPROC);
			subscriptionDataset.addAllRecords(procDTO.getDeleteRecList());
		}
	}

	
	public void prepareProcInputAndResponse(AlertSubscription alertSubObj, Map<String, Object> parameterMap,
			String currAlertGroupId, String alertId,String currOperation,ProcedureDTO procDTO) {
		if(currOperation.contains("create")) {
			 addInsertRowPrefix(procDTO.getInsertSB());	
			 procDTO.getInsertSB().append("\"").append(parameterMap.get(INSERT_AND_DELETE_PROC_PARAMS.get(0))).append("\"");
			 INSERT_AND_DELETE_PROC_PARAMS.stream().skip(1).
			 		forEach( p -> procDTO.getInsertSB().append(",\"").append(parameterMap.get(p)).append("\""));
			 procDTO.getInsertSB().append(')');
			 procDTO.getInsertReccList().add(prepareResponsRecord(alertSubObj, currAlertGroupId, alertId, currOperation));		
		} else if(currOperation.contains("update")) {
			// Adding this for proper param processing of procedure,
			// can be removed if procedure is changed to better param handling
			if(parameterMap.get("alertRequestId") == null) {
				parameterMap.put("alertRequestId", "null");
			}
			procDTO.getUpdateSB().append("\"").append(parameterMap.get(UPDATE_PROC_PARAMS.get(0))).append("\"");
			UPDATE_PROC_PARAMS.stream().skip(1).filter( p -> parameterMap.get(p) != null).
			 forEach(p -> procDTO.getUpdateSB().append(",\"").append(parameterMap.get(p)).append("\""));	
			procDTO.getUpdateRecList().add(prepareResponsRecord(alertSubObj, currAlertGroupId, alertId, currOperation));
			 addRowSeparator(procDTO.getUpdateSB());				 
		} else if(currOperation.equalsIgnoreCase("removeSubscription")) {
			procDTO.getDeleteSB().append("\"").append(parameterMap.get(INSERT_AND_DELETE_PROC_PARAMS.get(0))).append("\"");
			INSERT_AND_DELETE_PROC_PARAMS.stream().skip(1).	
			 forEach(p -> procDTO.getDeleteSB().append(",\"").append(parameterMap.get(p)).append("\""));	
			 addRowSeparator(procDTO.getDeleteSB());	 
			procDTO.getDeleteRecList().add(prepareResponsRecord(alertSubObj, currAlertGroupId, alertId, currOperation));
		} else if(currOperation.equalsIgnoreCase("removePreference")) {
			procDTO.getDeleteRecList().add(prepareResponsRecord(alertSubObj, currAlertGroupId, alertId, currOperation));	
			 addRowSeparator(procDTO.getDeleteSB());
		}
	}

	public void addRowSeparator(StringBuilder stringBuilder) {
		if(stringBuilder.length() > 0) {
			 stringBuilder.append("|");
		 }
	}

	public void addInsertRowPrefix(StringBuilder insertSB) {
		if( insertSB.length() > 0)
			insertSB.append(",(");
		else
			insertSB.append("(") ;
	}

	public String getAlertRequestId(Map<String, String> externalAlertSubs, Map<String, String> referenceIdofAlert,
			Alert alert) {
		return StringUtils.isNotBlank(externalAlertSubs.get(alert.getId())) ? 
				externalAlertSubs.get(alert.getId()) :
				referenceIdofAlert.get(alert.getId());
	}

	public void resetAlertAttributeValues(AlertSubscription alertSubObj, List<Record> alertRecords) {
		// get the alert which are have attributes set mapped to external
		 Set<String> alertAttributesSet = alertRecords.stream().
				filter(rec -> StringUtils.isNotBlank(rec.getParamValueByName("alertsubtype_attributeId"))).
				map(rec -> rec.getParamValueByName("alertsubtype_id")).
				collect(Collectors.toSet());		
		 for ( AlertGroup currGroup : alertSubObj.getGroups()) {				
				 for (Alert alert : currGroup.getAlerts()) {
					 if(!alertAttributesSet.contains(alert.getId())) {
						 alert.setValue1(null);
						 alert.setValue2(null);
					 }
			 }
		 }
	}

	private Map<String,String> processExternalAlertSubscription(AlertSubscription alertSubObj, 
			Map<String, String> referenceIdofAlert,
			Map<String, String> externalAlertGroupMap,String legalEntityId) throws ApplicationException {
		try {
							
			String alertSubscription = new Gson().toJson(alertSubObj, AlertSubscription.class);
			String alertReferenceIdStr =  new Gson().toJson(referenceIdofAlert, HashMap.class);
			String externalAlertGroupMapStr = new Gson().toJson(externalAlertGroupMap, HashMap.class);
		
			Map<String, Object> subMap = new HashMap<>();
			subMap.put(ACConstants.ALERT_SUBSCRIPTION, alertSubscription);
			subMap.put(ACConstants.ALERT_EXTL_REFERENCE_MAP, alertReferenceIdStr);
			subMap.put(ACConstants.EXTERNAL_ALERT_GROUP_MAP, externalAlertGroupMapStr);
			subMap.put("legalEntityId", legalEntityId);
			
			
			Result result = CampaignUtil.invokeService(ACConstants.ALERT_MANAGEMENT, 
												ACConstants.EXTERNAL_SUBSCRIPTION_INITIATOR, subMap);
			diagnostic.prepareDebug("Received alertSubsription response" ).log();
			if(result.getParamByName(ACConstants.DBP_ERROR_MESSAGE) != null || 
					result.getRecordById(ACConstants.EXTERNAL_SUBSCRIPTION_MAP) == null) {
		         
				throw new ApplicationException(ErrorCodeEnum.ERR_20847, new Throwable("Error while process external"
						+ "alertSubscription " + result.getParamValueByName(ACConstants.DBP_ERROR_MESSAGE)));	
			}else {
				Record rec = result.getRecordById(ACConstants.EXTERNAL_SUBSCRIPTION_MAP);
			    return rec.getAllParams().stream()
					 						.collect(Collectors.toMap(Param::getName, Param::getValue));
			}
			
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20847, new Throwable("Error while process external"
					+ "alertSubscription " + e.getMessage()));			
		}
	}

	private static Map<String, String> getexternalReferencesForAlertEntitlements(Result readResponse) {
		Map<String, String> alertRequestIdMap = null;	
		if(readResponse.getDatasetById(ACConstants.SUBSCRIPTION_TABLENAME) != null) {
			alertRequestIdMap = readResponse.getDatasetById(ACConstants.SUBSCRIPTION_TABLENAME).getAllRecords()
					.stream().collect(Collectors.toMap(rec -> String.valueOf( rec.getParamValueByName("alertSubTypeId")) ,
							rec -> String.valueOf(rec.getParamValueByName("alertRequestId"))));
					
		}	
		return alertRequestIdMap;
	}
	
	public List<Record> getAllUserAlertsOfTheGroup(DataControllerRequest dcReq,
			AlertSubscription alertSubObj, String customerTypeID, String preferenceLevel, String legalEntityId) throws ApplicationException {		
		   return getTotalAlertGroupMap(dcReq, alertSubObj,customerTypeID,legalEntityId);		
	}

	public void processNonInputAlerts(AlertSubscription alertSubObj,  Set<String> totalalertsList,
			AlertGroup currGroup, Set<String> associatedAlertEntitlements, 
			String loggedInUserId, Map<String, String> externalAlertSubs, ProcedureDTO procDTO, String legalEntityId) throws ApplicationException {
		if(totalalertsList != null) {
			subscribeRemainingAlertsInGroup(alertSubObj ,
					totalalertsList, currGroup,associatedAlertEntitlements, loggedInUserId,
					externalAlertSubs,procDTO,legalEntityId);
		}
	}

	public Set<String> getTotalAlertsOfGroup(Map<String, List<Record>> alertTypeGroupMap, String preferenceLevel,
			AlertGroup currGroup) {
		Set<String> totalalertsSet = null;
		if(!ALERTPREFERNCES.ALERT.name().equalsIgnoreCase(preferenceLevel) && 
						alertTypeGroupMap.containsKey( currGroup.getTypeID() ) ) {
			Function<Record, String> getAlertSubTypeFunction = rec -> rec.getParamValueByName("alertsubtype_id");
			totalalertsSet = alertTypeGroupMap.get(currGroup.getTypeID()).stream()
					.map(getAlertSubTypeFunction).collect(Collectors.toSet());
		}
		return  totalalertsSet;
	}
	
	public List<Record> getTotalAlertRecordsOfGroup(Map<String, List<Record>> alertTypeGroupMap, String preferenceLevel,
			AlertGroup currGroup) {		 
		List<Record> totalalertsList = null;		
		if(!ALERTPREFERNCES.ALERT.name().equalsIgnoreCase(preferenceLevel) && 
				currGroup.isSub() && alertTypeGroupMap.containsKey( currGroup.getTypeID() ) ) {			
			totalalertsList = alertTypeGroupMap.get(currGroup.getTypeID()).stream()
					.filter(rec -> currGroup.getTypeID().equalsIgnoreCase(rec.getParamValueByName("alertsubtype_id")))
					.collect(Collectors.toList());
		}
		return  totalalertsList;
	}


	public Set<String> getExistingAlertEntitlements(Result readResponse) {
		Set<String> associatedAlertEntitlements = null;
		if(readResponse.getDatasetById(ACConstants.SUBSCRIPTION_TABLENAME) != null) {
			associatedAlertEntitlements = readResponse.getDatasetById(ACConstants.SUBSCRIPTION_TABLENAME).getAllRecords()
					.stream().map(rec -> rec.getParamValueByName("alertSubTypeId"))
					.collect(Collectors.toSet());
		}
		return associatedAlertEntitlements;
	}
	
	private List<Record> getTotalAlertGroupMap(DataControllerRequest dcReq, AlertSubscription alertSubObj,
			 String customerTypeID,String legalEntityId) throws ApplicationException {
	
		// Get IDs of Alerts Applicable to current Customer Type
		Set<String> alertSubTypesOfCustomerType = AlertManagementHandler.getAlertTypesofCustomerType(
				customerTypeID, dcReq,legalEntityId);
		List<String> applicableAlertTypes = new ArrayList<>();
		applicableAlertTypes.addAll(alertSubTypesOfCustomerType);		
		// Get Active and Non-Global Alert Types Data
		List<String> alertypesIDList = alertSubObj.getGroups().stream()
				.map( AlertGroup:: getTypeID ).collect(Collectors.toList());
		List<Record> alertSubTypesRecList = AlertManagementHandler.getAlertSubTypes(alertypesIDList,
				applicableAlertTypes, null, false, true, dcReq,legalEntityId);		

		List<String> validAccountAlerts = null;
		diagnostic.prepareDebug("Alerts fetched before account validity "+ alertSubTypesRecList).log();
		//Get valid account alerts
		String accountID = alertSubObj.getAccountID();
		if (StringUtils.isNotBlank(accountID) || StringUtils.isNotBlank(alertSubObj.getAccountType())) {
			String accounts = dcReq.getParameter("accounts");
			diagnostic.prepareDebug("Is Accounts Null?" + StringUtils.isBlank(accounts)).log();
			String accountTypeDesc = AlertTypePreferenceGetService.
					getAccountTypeIdFromAccounts(dcReq, accountID, alertSubObj.getAccountType(), accounts,legalEntityId);
			validAccountAlerts = AlertTypePreferenceGetService.getListOfValidAccountAlerts(dcReq, 
					accountTypeDesc,legalEntityId);
			diagnostic.prepareDebug("valid account alerts "+ validAccountAlerts).log();
		}	
		final List<String> validAccAlerts = validAccountAlerts;
		List<Record> validAlertsRecList =  null;
		if(validAccAlerts != null) {
			validAlertsRecList = alertSubTypesRecList.stream()
					.filter(rec -> !validAccAlerts.contains(rec.getParamValueByName("TypeID")))
					.collect(Collectors.toList());			   
		}else {
			validAlertsRecList = alertSubTypesRecList;
		}		
		return validAlertsRecList;	
	}

	private void subscribeRemainingAlertsInGroup(AlertSubscription alertSubObj,
			Set<String> totalalertsList, AlertGroup currGroup, Set<String> associatedAlertEntitlements,
			String loggedInUserId, Map<String, String> externalAlertSubs, ProcedureDTO procDTO,String legalEntityId) throws ApplicationException {	
		Map<String, Object> parameterMap = new HashMap<>();
		for(String alertId : totalalertsList) {
			addCommonInputParams(alertSubObj, parameterMap);
			parameterMap.put("alertSubTypeId", alertId);
			parameterMap.put("AlertTypeId", currGroup.getTypeID());
			parameterMap.put("alertRequestId", externalAlertSubs.get(alertId));
			parameterMap.put("companyLegalUnit",legalEntityId);
			ServiceURLEnum serviceURLName= determineServiceName( 
					loggedInUserId, parameterMap, associatedAlertEntitlements, null, currGroup.isSub(), alertId);
			String currOperation = serviceURLName != null ? getOperationName(serviceURLName) : "removePreference";			
			prepareProcInputAndResponse(alertSubObj, parameterMap,  currGroup.getTypeID(), alertId, currOperation,procDTO);			
			resetParams(parameterMap);
		}
	}

	public static void invokeServiceWrapper(DataControllerRequest dcReq, Map<String, Object> parameterMap,
			ServiceURLEnum serviceURLName) throws ApplicationException {	
		if(serviceURLName != null) {			
			Result operationResult = ServiceUtil.invokeService(serviceURLName,parameterMap, null, dcReq);
			AlertManagementHandler.isOperationHasErrmsg(operationResult, serviceURLName, 
					ErrorCodeEnum.ERR_20927);			
		}
				
	}

	public Record prepareResponsRecord(AlertSubscription alertSubObj, String currAlertGroupId, String alertId, String currOperation) {
		Record currAlertRecord = new Record();
		// Set Operation Meta
		currAlertRecord.addParam(new Param("CustomerId", alertSubObj.getCustomerId(), FabricConstants.STRING));
		currAlertRecord.addParam(new Param("AlertTypeId", currAlertGroupId, FabricConstants.STRING));
		currAlertRecord.addParam(new Param("alertSubTypeId", alertId, FabricConstants.STRING));
		currAlertRecord.addParam(new Param("operation", currOperation, FabricConstants.STRING));
		String accountID = alertSubObj.getAccountID();
		if (StringUtils.isNotBlank(accountID)) {
			currAlertRecord.addParam(new Param("AccountId", accountID, FabricConstants.STRING));
		}
		String accountType =alertSubObj.getAccountType();
		if (StringUtils.isNotBlank(accountType)) {
			currAlertRecord.addParam(new Param("AccountType", accountType, FabricConstants.STRING));
		}
		return currAlertRecord;
	}


	private ServiceURLEnum determineServiceName(String loggedInUserId, Map<String, Object> parameterMap,
			Set<String> associatedAlertEntitlements, Alert alert, boolean isSubscribed, String alertId) {
		ServiceURLEnum serviceURLName = null;
		String id = alert != null ? alert.getId() : alertId;
		if (isSubscribed) {	                        	
			if (alert != null) {
				if (StringUtils.isNotBlank(alert.getValue1())) {
					parameterMap.put("Value1", alert.getValue1());
				}
				if (StringUtils.isNotBlank(alert.getValue2())) {
					parameterMap.put("Value2", alert.getValue2());
				} 
			}
			String currTimestamp = CommonUtilities.getISOFormattedLocalTimestamp();
			if (associatedAlertEntitlements != null && associatedAlertEntitlements.contains(id)) {
				diagnostic.prepareDebug("Update Customer Alert Preference").log();
				parameterMap.put("modifiedby", loggedInUserId);
				parameterMap.put("lastmodifiedts", currTimestamp);	                        			
				serviceURLName = ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_UPDATE;
			} else {
				diagnostic.prepareDebug("Creating Customer Alert Preference").log();
				parameterMap.put("createdby", loggedInUserId);
				parameterMap.put("createdts", currTimestamp);
				serviceURLName = ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_CREATE;
			}

		} else {	                            
			if (associatedAlertEntitlements != null && associatedAlertEntitlements.contains(id)) {
				diagnostic.prepareDebug("Removing Customer Alert Preference").log();
				serviceURLName = ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_DELETE;	                                
			}

		}
		return serviceURLName;
	}

	private Result getExistingAlertSubscriptions(DataControllerRequest dcReq, 
			AlertSubscription alertSubObj, String legalEntityId) throws ApplicationException {
		Map<String, Object> parameterMap = new HashMap<>();
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("Customer_id eq '" + alertSubObj.getCustomerId() + "' and ");
		queryBuilder.append("companyLegalUnit eq '" + legalEntityId + "' and ");
		queryBuilder.append("alertCategoryId eq '" + alertSubObj.getCatId() + "' and (");
		int index = 0; 
		for (AlertGroup alertGroup : alertSubObj.getGroups()) {           
			if (StringUtils.isNotBlank(alertGroup.getTypeID())) {
				queryBuilder.append("AlertTypeId eq '" + alertGroup.getTypeID() + "'");
			}
			if (index < alertSubObj.getGroups().size() - 1) {
				queryBuilder.append(" or ");
			}  
		  index++;
		}
		queryBuilder.append(")");
		if (StringUtils.isNotBlank(alertSubObj.getAccountID())) {
			queryBuilder.append(" and AccountId eq '" + alertSubObj.getAccountID() + "'");
		}else {			
			queryBuilder.append(" and AccountId eq '").append(ACConstants.STAR_VALUE).append("'");
		}
		if (StringUtils.isNotBlank(alertSubObj.getAccountType())) {
			queryBuilder.append(" and AccountType eq '" + alertSubObj.getAccountType() + "'");
		}else {			
			queryBuilder.append(" and AccountType eq '").append(ACConstants.STAR_VALUE).append("'");
		}

		queryBuilder.trimToSize();
		String filterQuery = queryBuilder.toString();
		parameterMap.put(ODataQueryConstants.FILTER, filterQuery);
		Result readResponse = ServiceUtil.invokeService(
				ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_READ, parameterMap, null, dcReq);
		AlertManagementHandler.isOperationSuccessful(readResponse,
				ServiceURLEnum.DBXCUSTOMERALERTENTITLEMENT_READ, ErrorCodeEnum.ERR_20927);
		return readResponse;
	}	

	private void resetParams(Map<String, Object> parameterMap) {
		parameterMap.clear();
	}

	private void addCommonInputParams(AlertSubscription alertSubObj, Map<String, Object> parameterMap) {
		parameterMap.put("Customer_id", alertSubObj.getCustomerId());
		parameterMap.put("alertCategoryId", alertSubObj.getCatId());

		String accountID = alertSubObj.getAccountID();
		if (StringUtils.isNotBlank(accountID)) {
			parameterMap.put("AccountId", accountID);
		} else {
			parameterMap.put("AccountId", ACConstants.STAR_VALUE);
		}
		
		String accountType = alertSubObj.getAccountType();
		if (StringUtils.isNotBlank(accountType)) {
			parameterMap.put("AccountType", accountType);
		} else {
			parameterMap.put("AccountType", ACConstants.STAR_VALUE);
		}

	}

	private String getOperationName(ServiceURLEnum serviceURLName) {
		String opName = null;
		switch (serviceURLName) {
		case DBXCUSTOMERALERTENTITLEMENT_DELETE:
			opName = "removeSubscription";
			break;
		case DBXCUSTOMERALERTENTITLEMENT_CREATE:
			opName = "createSubscription";
			break;
		case DBXCUSTOMERALERTENTITLEMENT_UPDATE:
			opName = "updateSubscription";
			break;
		default:
			break;	
		}
		return opName;
	}
	
	
	}
