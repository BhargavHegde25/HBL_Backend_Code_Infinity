package com.kony.adminconsole.service.alertmanagement.staging;

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

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.kony.adminconsole.jwt.auth.utils.LegalEntityUtil;
import com.kony.adminconsole.service.alertmanagement.staging.util.Alert;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertGroup;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertSubscription;
import com.kony.adminconsole.service.alertmanagement.staging.util.Channel;
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

public class CustomerChannelSubscriptionProcessor {
	private static final String CHANNEL_SUBSRIPTION = "channelSubsription";
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final List<String> INSERT_AND_DELETE_PROC_PARAMS = Arrays.asList("customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "accountId", "accountType","channelId","createdby","companyLegalUnit" );
	private static final List<String> UPDATE_PROC_PARAMS = Arrays.asList("customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "accountId", "accountType","channelId","modifiedby","companyLegalUnit");

	
	public Dataset setChannelSubscriptions(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			String loggedInUserId, String legalEntityId)	throws ApplicationException{
		TABLE_OPERATION_MAPPING tableOpEnum = TABLE_OPERATION_MAPPING.CUSTOMERALERTCHANNEL;
		Map<String, Object> parameterMap = new HashMap<>(); 		
		addCommonInputParams(alertSubObj, parameterMap,legalEntityId);
		Dataset ds = null;
		if(ALERTPREFERNCES.CATEGORY.name().equalsIgnoreCase(alertSubObj.getPreferenceLevel()) &&
				!alertSubObj.getChanls().isEmpty()) {
			ds = processChannelAtCategoryLevel(alertSubObj, dcReq, loggedInUserId, tableOpEnum, parameterMap,legalEntityId);

		} else if ( ALERTPREFERNCES.GROUP.name().equalsIgnoreCase(alertSubObj.getPreferenceLevel() ) &&
				!alertSubObj.getGroups().isEmpty() && alertSubObj.getGroups().
				stream().anyMatch( group -> group.getChanls() != null && !group.getChanls().isEmpty()) ) {
			ds = processChannelsAtGroupLevel(alertSubObj, dcReq, loggedInUserId, tableOpEnum, parameterMap,legalEntityId);
		}else if ( ALERTPREFERNCES.ALERT.name().equalsIgnoreCase(alertSubObj.getPreferenceLevel() ) &&
				alertSubObj.getGroups()!= null &&  alertSubObj.getGroups().
						stream().anyMatch( group -> group.getAlerts() != null) ) {
			ds = processChannelsAtAlertLevel(alertSubObj, dcReq, loggedInUserId, tableOpEnum, parameterMap,legalEntityId);			
		}	
		return ds != null ? ds : new Dataset(CHANNEL_SUBSRIPTION);
	}


	private Dataset processChannelsAtAlertLevel(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			String loggedInUserId, TABLE_OPERATION_MAPPING tableOpEnum,
			Map<String, Object> parameterMap, String legalEntityId) throws ApplicationException {
		Result readResult = getExistingAlertChannelRecords(alertSubObj, dcReq, tableOpEnum,legalEntityId);
		Function<Record, String> getAlertSubTypeID = rec -> rec.getParamValueByName(ACConstants.ALERT_SUB_TYPE_ID);
		Map<String, List<Record>> associatedAlertChannelsRecords = readResult.getDatasetById(tableOpEnum.getTableName()).getAllRecords()
				.stream().collect(Collectors.groupingBy(getAlertSubTypeID));
		Dataset channelds = new Dataset(CHANNEL_SUBSRIPTION);
		ProcedureDTO procDTO = new ProcedureDTO();
		for (AlertGroup alertGroupObj : alertSubObj.getGroups()) {
			parameterMap.put(ACConstants.ALERT_TYPE_ID, alertGroupObj.getTypeID());				
			for(Alert alertObj :alertGroupObj.getAlerts() ) {
				if(alertObj.getChanls() != null) {
					parameterMap.put(ACConstants.ALERT_SUB_TYPE_ID, alertObj.getId());
					Set<String> associatedAlertChannels = null;
					if (associatedAlertChannelsRecords != null && 
								associatedAlertChannelsRecords.get(alertObj.getId()) != null) {
						associatedAlertChannels = associatedAlertChannelsRecords.get(alertObj.getId()).stream()
								.map(rec -> rec.getParamValueByName(tableOpEnum.getCompositePKid()))
								.collect(Collectors.toSet());
					}					
					 processCustomerChanlSubscription(alertObj.getChanls(),loggedInUserId,
								associatedAlertChannels,parameterMap,alertSubObj,procDTO,legalEntityId);
					}
			}
		}
		 
		 parameterMap.clear();
		 updateChannelsToDatabase(dcReq, channelds, procDTO, parameterMap);		 	
		return channelds;
	}
	
	private Dataset processChannelsAtGroupLevel(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			String loggedInUserId, TABLE_OPERATION_MAPPING tableOpEnum,
			Map<String, Object> parameterMap, String legalEntityId) throws ApplicationException {
		Result readResult = getExistingAlertChannelRecords(alertSubObj, dcReq, tableOpEnum,legalEntityId);
		Function<Record, String> getAlertTypeID = rec -> rec.getParamValueByName(ACConstants.ALERT_TYPE_ID);
		Map<String, List<Record>> associatedalertGroups = readResult.getDatasetById(tableOpEnum.getTableName()).getAllRecords()
				.stream().collect(Collectors.groupingBy(getAlertTypeID));
		Dataset channelds = new Dataset(CHANNEL_SUBSRIPTION);
		ProcedureDTO procDTO = new ProcedureDTO();
		for (AlertGroup alertGroupObj : alertSubObj.getGroups()) {
			Set<String> associatedAlertChannels = null;
			if(!alertGroupObj.getChanls().isEmpty()) {
				  if(	associatedalertGroups != null && 
								associatedalertGroups.get(alertGroupObj.getTypeID()) != null ) {
				        associatedAlertChannels = associatedalertGroups.get(alertGroupObj.getTypeID()).stream()
				        			.map(rec -> rec.getParamValueByName(tableOpEnum.getCompositePKid()))
				        				.collect(Collectors.toSet());
				  }
				  parameterMap.put(ACConstants.ALERT_TYPE_ID,alertGroupObj.getTypeID());
				  parameterMap.put(ACConstants.ALERT_SUB_TYPE_ID, ACConstants.STAR_VALUE);
				  processCustomerChanlSubscription(alertGroupObj.getChanls(),loggedInUserId,
						associatedAlertChannels,parameterMap,alertSubObj,procDTO,legalEntityId);
			}
		}
		parameterMap.clear();	
		updateChannelsToDatabase(dcReq, channelds, procDTO, parameterMap);
		return channelds;
	}


	private Dataset processChannelAtCategoryLevel(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			String loggedInUserId, TABLE_OPERATION_MAPPING tableOpEnum,
			Map<String, Object> parameterMap,String legalEntityId) throws ApplicationException {
		Result readResult = getExistingAlertChannelRecords(alertSubObj, dcReq, tableOpEnum, legalEntityId);
		Set<String> associatedAlertChannels = readResult.getDatasetById(tableOpEnum.getTableName()).getAllRecords()
				.stream().map(rec -> rec.getParamValueByName(tableOpEnum.getCompositePKid()))
				.collect(Collectors.toSet());
		Dataset channelds = new Dataset(CHANNEL_SUBSRIPTION);
		parameterMap.put(ACConstants.ALERT_TYPE_ID, ACConstants.STAR_VALUE);
		parameterMap.put(ACConstants.ALERT_SUB_TYPE_ID, ACConstants.STAR_VALUE);
		ProcedureDTO procDTO = new ProcedureDTO();
		processCustomerChanlSubscription(alertSubObj.getChanls(),loggedInUserId,
				associatedAlertChannels,parameterMap,alertSubObj,procDTO,legalEntityId);
		parameterMap.clear();
		updateChannelsToDatabase(dcReq, channelds, procDTO, parameterMap);		
		return channelds;
	}


	private Result getExistingAlertChannelRecords(AlertSubscription alertSubObj, DataControllerRequest dcReq,
			TABLE_OPERATION_MAPPING tableOpEnum,String legalEntityId) throws ApplicationException {
		return invokeAlertChannelRead(alertSubObj, tableOpEnum.getReadServiceName(),
				tableOpEnum.getTableName(), ErrorCodeEnum.ERR_20927, dcReq,legalEntityId);
	} 


	private void processCustomerChanlSubscription( List<Channel> chnlList,
			String loggedInUserId,Set<String> associatedAlertChannels, Map<String, Object> parameterMap,
			AlertSubscription alertSubObj, ProcedureDTO procDTO, String legalEntityId) {
		String  currOperation = null;
		for (Channel currChannel : chnlList) {         
			ServiceURLEnum serviceURLName = null; 
			parameterMap.put("channelId", currChannel.getId());  
			serviceURLName = determineOperationTobePerformed(loggedInUserId, associatedAlertChannels,
					parameterMap, currChannel);			
			currOperation = serviceURLName != null ? getOperationName(serviceURLName) : "removePreference";
			prepareProcInputAndResponse(alertSubObj, parameterMap, currChannel.getId(), currOperation, procDTO,legalEntityId);
			resetParams(parameterMap);			
		}		
	}


	public static Record prepareResponseRecord(Map<String, Object> parameterMap, AlertSubscription alertSubObj,
			String currOperation, String currChannel, String legalEntityId) {
		Record currChannelRecord = new Record();
		currChannelRecord.addParam(new Param("CustomerId", alertSubObj.getCustomerId(), FabricConstants.STRING));
		currChannelRecord.addParam(new Param("ChannelId", currChannel, FabricConstants.STRING));
		currChannelRecord.addParam(new Param("companyLegalUnit", legalEntityId, FabricConstants.STRING));
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

	private void resetParams(Map<String, Object> parameterMap) {
		parameterMap.remove("ChannelId");
		parameterMap.remove("createdby");
		parameterMap.remove("createdts");
		parameterMap.remove("modifiedby");
		parameterMap.remove("lastmodifiedts");
		//parameterMap.remove("companyLegalUnit");
	}

	private ServiceURLEnum determineOperationTobePerformed(String loggedInUserId, Set<String> associatedAlertChannels,
			Map<String, Object> parameterMap, Channel currChannel) {
		ServiceURLEnum serviceURLName = null;
		if ( Boolean.TRUE.booleanValue() == currChannel.isSub()) {
			String currTimestamp = CommonUtilities.getISOFormattedLocalTimestamp();                      
			if (associatedAlertChannels != null && 
						associatedAlertChannels.contains(currChannel.getId())) {
				diagnostic.prepareDebug("Updating Customer Alert Category Channel Preference").log();                          
				parameterMap.put("modifiedby", loggedInUserId);
				parameterMap.put("lastmodifiedts", currTimestamp);
				serviceURLName = ServiceURLEnum.CUSTOMERALERTCHANNEL_UPDATE;                           
			} else {
				diagnostic.prepareDebug("Creating Customer Alert Category Channel Preference").log();
				parameterMap.put("createdby", loggedInUserId);
				parameterMap.put("createdts", currTimestamp);
				serviceURLName = ServiceURLEnum.CUSTOMERALERTCHANNEL_CREATE;                           
			}                                              
		} else {
			// Remove Alert Type Preference
			if (associatedAlertChannels != null && associatedAlertChannels.contains(currChannel.getId())) {
				diagnostic.prepareDebug("Removing Customer Alert Category Channel Preference").log();                            
				serviceURLName = ServiceURLEnum.CUSTOMERALERTCHANNEL_DELETE;                                            
			}                        
		}
		return serviceURLName;
	}

	private Result invokeAlertChannelRead(AlertSubscription alertSub,
			ServiceURLEnum serviceName, String tableName, ErrorCodeEnum errorcode, DataControllerRequest requestInstance, String legalEntityId)
					throws ApplicationException {
		Map<String, Object> parameterMap = new HashMap<>();
		StringBuilder queryBuilder = getCommonFilterCondition(alertSub,legalEntityId);
		queryBuilder.trimToSize();            
		parameterMap.put(ODataQueryConstants.FILTER, queryBuilder.toString());
		parameterMap.put(ODataQueryConstants.SELECT, ACConstants.CHANL_SELECT_STR);	
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

	private void addCommonInputParams(AlertSubscription alertSubObj,Map<String, Object> parameterMap,String legalEntityId) {
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
		case CUSTOMERALERTCHANNEL_DELETE:
			opName = "removeSubscription";
			break;
		case CUSTOMERALERTCHANNEL_CREATE:
			opName = "createSubscription";
			break;
		case CUSTOMERALERTCHANNEL_UPDATE:
			opName = "updateSubscription";
			break;
		default:
			break;	
		}
		return opName;
	}
	
	public static void addInsertRowPrefix(StringBuilder insertSB) {
		if( insertSB.length() > 0)
			insertSB.append(",(");
		else
			insertSB.append("(") ;
	}
	
	public static void prepareProcInputAndResponse(AlertSubscription alertSubObj, Map<String, Object> parameterMap,
			String	currChannel, String currOperation, ProcedureDTO procDto, String legalEntityId) {		
		if(currOperation.contains("create")) {			
			 addInsertRowPrefix(procDto.getInsertSB());	
			 procDto.getInsertSB().append("\"").append(parameterMap.get(INSERT_AND_DELETE_PROC_PARAMS.get(0))).append("\"");
			 INSERT_AND_DELETE_PROC_PARAMS.stream().skip(1).
			 		forEach( p -> procDto.getInsertSB().append(",\"").append(parameterMap.get(p)).append("\""));
			 procDto.getInsertSB().append(')');
			 procDto.getInsertReccList().add(prepareResponseRecord(parameterMap, alertSubObj, currOperation, currChannel,legalEntityId));		
		} else if(currOperation.contains("update")) {
			procDto.getUpdateSB().append("\"").append(parameterMap.get(UPDATE_PROC_PARAMS.get(0))).append("\"");
			UPDATE_PROC_PARAMS.stream().skip(1).
			 forEach(p -> procDto.getUpdateSB().append(",\"").append(parameterMap.get(p)).append("\""));	
			 procDto.getUpdateRecList().add(prepareResponseRecord(parameterMap, alertSubObj, currOperation, currChannel,legalEntityId));	
			 addRowSeparator(procDto.getUpdateSB());				 
		} else if(currOperation.equalsIgnoreCase("removeSubscription")) {
			procDto.getDeleteSB().append("\"").append(parameterMap.get(INSERT_AND_DELETE_PROC_PARAMS.get(0))).append("\"");
			INSERT_AND_DELETE_PROC_PARAMS.stream().skip(1).	
			 forEach(p -> procDto.getDeleteSB().append(",\"").append(parameterMap.get(p)).append("\""));	
			 addRowSeparator(procDto.getDeleteSB());	 
			 procDto.getDeleteRecList().add(prepareResponseRecord(parameterMap, alertSubObj, currOperation, currChannel,legalEntityId));
		} else if(currOperation.equalsIgnoreCase("removePreference")) {
			 procDto.getDeleteRecList().add(prepareResponseRecord(parameterMap, alertSubObj, currOperation, currChannel,legalEntityId));	
			 addRowSeparator(procDto.getDeleteSB());
		}
	}
	
	public static void addRowSeparator(StringBuilder stringBuilder) {
		if(stringBuilder.length() > 0) {
			 stringBuilder.append("|");
		 }
	}
	
	public void updateChannelsToDatabase(DataControllerRequest dcReq, Dataset subscriptionDataset,
			ProcedureDTO procDTO, Map<String, Object> parameterMap)
			throws ApplicationException {
		if(procDTO.getInsertSB().length() > 0 ) {
			parameterMap.clear();
			parameterMap.put("_recordvalues", procDTO.getInsertSB().toString());			
			parameterMap.put("success", false);
			CustomerAlertSubscriptionProcessor.invokeServiceWrapper(
						dcReq, parameterMap, ServiceURLEnum.CUSTOMERALERTCHANNEL_CREATEPROC);
			subscriptionDataset.addAllRecords(procDTO.getInsertReccList());			
		}
		if(procDTO.getUpdateSB().length() > 0 ) {
			parameterMap.clear();
			parameterMap.put("_updateRecords", procDTO.getUpdateSB().toString());
			CustomerAlertSubscriptionProcessor.invokeServiceWrapper(
						dcReq, parameterMap, ServiceURLEnum.CUSTOMERALERTCHANNEL_UPDATEPROC);
			subscriptionDataset.addAllRecords(procDTO.getUpdateRecList());
		}
		if(procDTO.getDeleteSB().length() > 0 ) {
			parameterMap.clear();
			parameterMap.put("_deleteRecords", procDTO.getDeleteSB().toString());
			CustomerAlertSubscriptionProcessor.invokeServiceWrapper(
						dcReq, parameterMap, ServiceURLEnum.CUSTOMERALERTCHANNEL_DELETEPROC);
			subscriptionDataset.addAllRecords(procDTO.getDeleteRecList());
		}
	}

}
