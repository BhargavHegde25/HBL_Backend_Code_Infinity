package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.kony.adminconsole.service.alertmanagement.staging.util.Alert;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertGroup;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertSubscription;
import com.kony.adminconsole.service.alertmanagement.staging.util.Frequency;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ACConstants.ALERTPREFERNCES;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.ServiceUtil;
import com.kony.adminconsole.utilities.TABLE_OPERATION_MAPPING;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerFrequencySubscriptionProcessor {
	private static final String FREQUENCY_SUBSCRIPTION = "frequencySubscription";
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final List<String> INSERT_AND_DELETE_PROC_PARAMS = Arrays.asList("customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "accountId", "accountType","alertFrequencyId","frequencyValue","frequencyTime","createdby","companyLegalUnit" );
	private static final List<String> UPDATE_PROC_PARAMS = Arrays.asList("customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "accountId", "accountType","alertFrequencyId","frequencyValue","frequencyTime","modifiedby","companyLegalUnit");


	public Dataset setFrequenctSubscriptions(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			String loggedInUserId, String legalEntityId)	throws ApplicationException {	
		TABLE_OPERATION_MAPPING tableOpEnum = TABLE_OPERATION_MAPPING.CUSTOMERALERTFREQUENCY;
		Map<String, Object> parameterMap = new HashMap<>(); 		
		addCommonInputParams(alertSubObj, parameterMap, legalEntityId);
		Dataset ds = null;
		if(ALERTPREFERNCES.CATEGORY.name().equalsIgnoreCase(alertSubObj.getPreferenceLevel()) &&
				alertSubObj.getFreq() != null) {
			ds = processFreqAtCategoryLevel(alertSubObj, dcReq, loggedInUserId, tableOpEnum, parameterMap,legalEntityId);				
		} else if ( ALERTPREFERNCES.GROUP.name().equalsIgnoreCase(alertSubObj.getPreferenceLevel() ) &&
				!alertSubObj.getGroups().isEmpty() && alertSubObj.getGroups().
				stream().anyMatch( group -> group.getFreq() != null) ) {
			ds = processFreqAtGroupLevel(alertSubObj, dcReq, loggedInUserId, tableOpEnum,  parameterMap,legalEntityId);
		}else if ( ALERTPREFERNCES.ALERT.name().equalsIgnoreCase(alertSubObj.getPreferenceLevel() ) &&
				!alertSubObj.getGroups().isEmpty() && alertSubObj.getGroups().
				stream().anyMatch( group -> group.getAlerts() != null) ) {
			ds = processFreqAtAlertLevel(alertSubObj, dcReq, loggedInUserId,  tableOpEnum, parameterMap,legalEntityId);			
		}
		return ds != null ? ds : new Dataset(FREQUENCY_SUBSCRIPTION);
	}


	private Dataset processFreqAtAlertLevel(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			String loggedInUserId, TABLE_OPERATION_MAPPING tableOpEnum,	Map<String, Object> parameterMap,
			String legalEntityId) throws ApplicationException {
		Result readResult = getExistingAlertFreqRecords(alertSubObj, dcReq, tableOpEnum,legalEntityId);
		Function<Record, String> getAlertSubTypeID = rec -> rec.getParamValueByName(ACConstants.ALERT_SUB_TYPE_ID);
		Map<String, List<Record>> associatedAlertChannelsRecords = readResult.getDatasetById(tableOpEnum.getTableName()).getAllRecords()
				.stream().collect(Collectors.groupingBy(getAlertSubTypeID));
		Dataset ds = new Dataset(FREQUENCY_SUBSCRIPTION);
		ProcedureDTO procDTO = new ProcedureDTO();
		for (AlertGroup alertGroupObj : alertSubObj.getGroups()) {
			parameterMap.put(ACConstants.ALERT_TYPE_ID, alertGroupObj.getTypeID());			
			for(Alert alertObj :alertGroupObj.getAlerts()) {		
					Record associatedAlertRecord = null;
					parameterMap.put(ACConstants.ALERT_SUB_TYPE_ID, alertObj.getId());
					if (associatedAlertChannelsRecords.get(alertObj.getId()) != null
							&& !associatedAlertChannelsRecords.get(alertObj.getId()).isEmpty()) {
						associatedAlertRecord = associatedAlertChannelsRecords.get(alertObj.getId()).get(0);
					}	
					if(alertObj.getFreq() != null || associatedAlertRecord!= null) {
						processCustomerFreqSubscription(alertObj.getFreq() , loggedInUserId,
								associatedAlertRecord, parameterMap, alertSubObj,procDTO);
						 resetParams(parameterMap);	
					}
			}
		}
		updateChannelsToDatabase(dcReq, ds, procDTO, parameterMap);
		return ds;
	}


	private Dataset processFreqAtGroupLevel(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			String loggedInUserId, TABLE_OPERATION_MAPPING tableOpEnum,
			Map<String, Object> parameterMap,String legalEntityId) throws ApplicationException {
		Result readResult = getExistingAlertFreqRecords(alertSubObj, dcReq, tableOpEnum,legalEntityId);
		Function<Record, String> getAlertTypeID = rec -> rec.getParamValueByName(ACConstants.ALERT_TYPE_ID);
		Map<String, List<Record>> associatedalertGroups = readResult.getDatasetById(tableOpEnum.getTableName()).getAllRecords()
				.stream().collect(Collectors.groupingBy(getAlertTypeID));
		Dataset ds = new Dataset(FREQUENCY_SUBSCRIPTION);
		ProcedureDTO procDTO = new ProcedureDTO();
		for (AlertGroup alertGroupObj : alertSubObj.getGroups()) {
			Frequency groupFreqobj = alertGroupObj.getFreq() ;
			Record associatedAlertRecord = null;
				if(associatedalertGroups.containsKey(alertGroupObj.getTypeID()) &&
						associatedalertGroups.get(alertGroupObj.getTypeID()) != null &&
						!associatedalertGroups.get(alertGroupObj.getTypeID()).isEmpty() ) {
					associatedAlertRecord = associatedalertGroups.get(alertGroupObj.getTypeID()).get(0);
				}
				if(groupFreqobj != null || associatedAlertRecord!= null) {
					parameterMap.put(ACConstants.ALERT_TYPE_ID,alertGroupObj.getTypeID());
					parameterMap.put(ACConstants.ALERT_SUB_TYPE_ID, ACConstants.STAR_VALUE);					
					processCustomerFreqSubscription(alertGroupObj.getFreq(),loggedInUserId,
							associatedAlertRecord ,parameterMap,alertSubObj,procDTO);					
					resetParams(parameterMap);
				}
			}	
		updateChannelsToDatabase(dcReq, ds, procDTO, parameterMap);
		return ds;
	}


	private Dataset processFreqAtCategoryLevel(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			String loggedInUserId, TABLE_OPERATION_MAPPING tableOpEnum,
			Map<String, Object> parameterMap, String legalEntityId) throws ApplicationException {
		Dataset ds = new Dataset(FREQUENCY_SUBSCRIPTION);
		ProcedureDTO procDTO = new ProcedureDTO();
		Result readResult = getExistingAlertFreqRecords(alertSubObj, dcReq, tableOpEnum, legalEntityId);
		Record associatedfreqRec = null;
		if(!readResult.getDatasetById(tableOpEnum.getTableName()).getAllRecords().isEmpty()) {
			associatedfreqRec = readResult.getDatasetById(tableOpEnum.getTableName()).getRecord(0);
		}
		if(alertSubObj.getFreq() != null || associatedfreqRec != null) {	
			parameterMap.put(ACConstants.ALERT_TYPE_ID, ACConstants.STAR_VALUE);
			parameterMap.put(ACConstants.ALERT_SUB_TYPE_ID, ACConstants.STAR_VALUE);		
			processCustomerFreqSubscription(alertSubObj.getFreq(),loggedInUserId,
					associatedfreqRec,parameterMap,alertSubObj,procDTO);
			updateChannelsToDatabase(dcReq, ds, procDTO, parameterMap);
		}
		return ds;
	}


	private Result getExistingAlertFreqRecords(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			TABLE_OPERATION_MAPPING tableOpEnum, String legalEntityId) throws ApplicationException {
		return invokeAlertFreqRead(alertSubObj, tableOpEnum.getReadServiceName(),
				tableOpEnum.getTableName(), ErrorCodeEnum.ERR_20927, dcReq, legalEntityId);
	} 


	private void processCustomerFreqSubscription(Frequency frequency,
			String loggedInUserId,Record associatedfreqRec, Map<String, Object> parameterMap,
			AlertSubscription alertSubObj, ProcedureDTO procDTO) throws ApplicationException {
		String  currOperation = null;			
		ServiceURLEnum serviceURLName = determineOperationToBePerformed(frequency, associatedfreqRec,
																		loggedInUserId, parameterMap);
		currOperation = serviceURLName != null ? getOperationName(serviceURLName) : "removeFrequencyPreference";
		prepareProcInputAndResponse(alertSubObj, parameterMap, currOperation, procDTO);
						
	}
	
	public void prepareProcInputAndResponse(AlertSubscription alertSubObj, Map<String, Object> parameterMap,
			 String currOperation, ProcedureDTO procDto) {		
	
		if(currOperation.contains("create")) {			
			 CustomerChannelSubscriptionProcessor.addInsertRowPrefix(procDto.getInsertSB());	
			 procDto.getInsertSB().append("\"").append(parameterMap.get(INSERT_AND_DELETE_PROC_PARAMS.get(0))).append("\"");
			 INSERT_AND_DELETE_PROC_PARAMS.stream().skip(1).
			 		forEach( p -> procDto.getInsertSB().append(",\"").append(parameterMap.get(p)).append("\""));
			 procDto.getInsertSB().append(')');
			 procDto.getInsertReccList().add(prepareResponseRecord(parameterMap, alertSubObj, currOperation));		
		} else if(currOperation.contains("update")) {
			preProcessNullValues(parameterMap);
			procDto.getUpdateSB().append("\"").append(parameterMap.get(UPDATE_PROC_PARAMS.get(0))).append("\"");
			UPDATE_PROC_PARAMS.stream().skip(1).
			 forEach(p -> procDto.getUpdateSB().append(",\"").append(parameterMap.get(p)).append("\""));	
			 procDto.getUpdateRecList().add(prepareResponseRecord(parameterMap, alertSubObj, currOperation));	
			 CustomerChannelSubscriptionProcessor.addRowSeparator(procDto.getUpdateSB());				 
		} else if(currOperation.equalsIgnoreCase("removeSubscription")) {
			procDto.getDeleteSB().append("\"").append(parameterMap.get(INSERT_AND_DELETE_PROC_PARAMS.get(0))).append("\"");
			INSERT_AND_DELETE_PROC_PARAMS.stream().skip(1).limit(5).	
			 forEach(p -> procDto.getDeleteSB().append(",\"").append(parameterMap.get(p)).append("\""));	
			CustomerChannelSubscriptionProcessor.addRowSeparator(procDto.getDeleteSB());	 
			 procDto.getDeleteRecList().add(prepareResponseRecord(parameterMap, alertSubObj, currOperation));
		} else if(currOperation.equalsIgnoreCase("removePreference")) {
			 procDto.getDeleteRecList().add(prepareResponseRecord(parameterMap, alertSubObj, currOperation));	
			 CustomerChannelSubscriptionProcessor.addRowSeparator(procDto.getDeleteSB());
		}
	}
	
	public void preProcessNullValues(Map<String, Object> parameterMap) {
		// Adding this for proper param processing of procedure,
		// can be removed if procedure is changed to better param handling
		if(parameterMap.get("frequencyValue") == null) {
			parameterMap.put("frequencyValue", "null");
		}
		if(parameterMap.get("frequencyTime") == null) {
			parameterMap.put("frequencyTime", "null");
		}
	}
	
	public Record prepareResponseRecord(Map<String, Object> parameterMap,
			AlertSubscription alertSubObj, String currOperation) {
		Record currChannelRecord = new Record();
		// Set Operation Meta
		currChannelRecord.addParam(new Param("CustomerId", alertSubObj.getCustomerId(), FabricConstants.STRING));
		currChannelRecord.addParam(new Param("operation", currOperation, FabricConstants.STRING));
		if (StringUtils.isNotBlank(alertSubObj.getAccountID())) {
			currChannelRecord.addParam(new Param("AccountId", alertSubObj.getAccountID(), FabricConstants.STRING));
		}
		if (StringUtils.isNotBlank(alertSubObj.getAccountType())) {
			currChannelRecord.addParam(new Param("AccountTypeId", alertSubObj.getAccountType(), FabricConstants.STRING));
		}
		currChannelRecord.addParam(new Param(ACConstants.ALERT_TYPE_ID,
				String.valueOf(parameterMap.get(ACConstants.ALERT_TYPE_ID)),FabricConstants.STRING));
		currChannelRecord.addParam(new Param(ACConstants.ALERT_SUB_TYPE_ID, 
				String.valueOf(parameterMap.get(ACConstants.ALERT_SUB_TYPE_ID)),FabricConstants.STRING));
		return currChannelRecord;
	}


	private ServiceURLEnum determineOperationToBePerformed(Frequency frequency, Record associatedfreqRec, 
			String loggedInUserId, Map<String, Object> parameterMap) {
		ServiceURLEnum serviceURLName = null;
		String currTimestamp = CommonUtilities.getISOFormattedLocalTimestamp(); 
		if(associatedfreqRec != null ) {			   
			if(frequency != null && frequency.getId() != null) {
				diagnostic.prepareDebug("Updating Customer Alert Category Channel Preference").log();
				parameterMap.put(ACConstants.ALERT_FREQUENCY_ID, frequency.getId());
				parameterMap.put("frequencyTime",  frequency.getTime());
				parameterMap.put("frequencyValue", frequency.getValue());
				parameterMap.put("modifiedby", loggedInUserId);
				parameterMap.put("lastmodifiedts", currTimestamp);
				serviceURLName = ServiceURLEnum.CUSTOMERALERTFREQUENCY_UPDATE;
			}else {
				serviceURLName = ServiceURLEnum.CUSTOMERALERTFREQUENCY_DELETE;
			}
		}else {
			if(frequency != null &&  frequency.getId() != null) {
				parameterMap.put(ACConstants.ALERT_FREQUENCY_ID, frequency.getId());
				parameterMap.put("frequencyTime",  frequency.getTime());
				parameterMap.put("frequencyValue", frequency.getValue());
				parameterMap.put("createdby", loggedInUserId);
				parameterMap.put("createdts", currTimestamp);
				serviceURLName = ServiceURLEnum.CUSTOMERALERTFREQUENCY_CREATE;
			}
		}
		return serviceURLName;
	}

	private Result invokeAlertFreqRead(AlertSubscription alertSub,
			ServiceURLEnum serviceName, String tableName, ErrorCodeEnum errorcode, DataControllerRequest requestInstance,
			String legalEntityId)
					throws ApplicationException {
		Map<String, Object> parameterMap = new HashMap<>();
		StringBuilder queryBuilder = getCommonFilterCondition(alertSub, legalEntityId);
		queryBuilder.trimToSize();            
		parameterMap.put(ODataQueryConstants.FILTER, queryBuilder.toString());
		parameterMap.put(ODataQueryConstants.SELECT, ACConstants.FRQY_SELECT_STR);	
		Result res = ServiceUtil.invokeService(serviceName,parameterMap,null,requestInstance);
		AlertManagementHandler.isOperationSuccessful(res, serviceName , tableName, errorcode);
		//ServiceURLEnum.CUSTOMERALERTCHANNEL_READ,ErrorCodeEnum.ERR_20927
		return res;
	}

	private  StringBuilder getCommonFilterCondition(AlertSubscription alertSub, String legalEntityId) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("customerId eq '").append(alertSub.getCustomerId()).append("'");
		queryBuilder.append(" and alertCategoryId eq '").append(alertSub.getCatId()).append("'");
		queryBuilder.append(" and companyLegalUnit eq '").append(legalEntityId).append("'");

		if (StringUtils.isNotBlank(alertSub.getAccountType())) {
			queryBuilder.append(" and accountType eq '").append(alertSub.getAccountType()).append("'");
		} else {
			queryBuilder.append(" and accountType eq '").append(ACConstants.STAR_VALUE).append("'");
		}

		if (StringUtils.isNotBlank(alertSub.getAccountID())) {
			queryBuilder.append(" and accountId eq '").append(alertSub.getAccountID()).append("'");
		} else {
			queryBuilder.append(" and accountId eq '").append(ACConstants.STAR_VALUE).append("'");
		}
		return queryBuilder;	

	}


	private void addCommonInputParams(AlertSubscription alertSubObj,Map<String, Object> parameterMap, String legalEntityId) {
		parameterMap.put("customerId", alertSubObj.getCustomerId());
		parameterMap.put("alertCategoryId", alertSubObj.getCatId());
		parameterMap.put("companyLegalUnit", legalEntityId);
		if (StringUtils.isNotBlank(alertSubObj.getAccountID())) {
			parameterMap.put("accountId", alertSubObj.getAccountID());
		} else {
			parameterMap.put("accountId",  ACConstants.STAR_VALUE);
		}

		if (StringUtils.isNotBlank(alertSubObj.getAccountType())) {
			parameterMap.put("accountType", alertSubObj.getAccountType());
		} else {
			parameterMap.put("accountType",  ACConstants.STAR_VALUE);
		}	
	}

	private String getOperationName(ServiceURLEnum serviceURLName) {
		String opName = null;
		switch (serviceURLName) {
		case CUSTOMERALERTFREQUENCY_DELETE:
			opName = "removeSubscription";
			break;
		case CUSTOMERALERTFREQUENCY_CREATE:
			opName = "createSubscription";
			break;
		case CUSTOMERALERTFREQUENCY_UPDATE:
			opName = "updateSubscription";
			break;	
		default:
			break;	
		}
		return opName;
	}
	
	private void resetParams(Map<String, Object> parameterMap) {
		parameterMap.remove("createdby");
		parameterMap.remove("createdts");
		parameterMap.remove("modifiedby");
		parameterMap.remove("lastmodifiedts");
	}
	
	public void updateChannelsToDatabase(DataControllerRequest dcReq, Dataset subscriptionDataset,
			ProcedureDTO procDTO, Map<String, Object> parameterMap)
			throws ApplicationException {
		if(procDTO.getInsertSB().length() > 0 ) {
			parameterMap.clear();
			parameterMap.put("_recordvalues", procDTO.getInsertSB().toString());			
			parameterMap.put("success", false);
			CustomerAlertSubscriptionProcessor.invokeServiceWrapper(
						dcReq, parameterMap, ServiceURLEnum.CUSTOMERALERTFREQUENCY_CREATEPROC);
			subscriptionDataset.addAllRecords(procDTO.getInsertReccList());			
		}
		if(procDTO.getUpdateSB().length() > 0 ) {
			parameterMap.clear();
			parameterMap.put("_updateRecords", procDTO.getUpdateSB().toString());
			CustomerAlertSubscriptionProcessor.invokeServiceWrapper(
						dcReq, parameterMap, ServiceURLEnum.CUSTOMERALERTFREQUENCY_UPDATEPROC);
			subscriptionDataset.addAllRecords(procDTO.getUpdateRecList());
		}
		if(procDTO.getDeleteSB().length() > 0 ) {
			parameterMap.clear();
			parameterMap.put("_deleteRecords", procDTO.getDeleteSB().toString());
			CustomerAlertSubscriptionProcessor.invokeServiceWrapper(
						dcReq, parameterMap, ServiceURLEnum.CUSTOMERALERTFREQUENCY_DELETEPROC);
			subscriptionDataset.addAllRecords(procDTO.getDeleteRecList());
		}
	}

}
