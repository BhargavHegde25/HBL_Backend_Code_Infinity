package com.kony.adminconsole.alertmanage.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.alertmanage.businessdelegate.api.CustExternalAlertSubscriptionBD;
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.service.alertmanagement.staging.util.Alert;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertGroup;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertSubscription;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ACConstants.ALERTPREFERNCES;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustExternalAlertSubscriptionBDImpl implements CustExternalAlertSubscriptionBD {

	private static final String PERFORM_EXTERNL_SUBSCRIPTION = "performExternlSubscription";
	private static final String ALERT_ORCHESTRATION = "AlertOrchestration";
	private static final com.temenos.logger.alert.Alert alert_Logger = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	public static final String DOLLAR_LOOP_SEPARATOR = "$$$";

	public Map<String, String> registerExtnlSubcription(AlertSubscription alertSubObj,
			Map<String, String> alertExtlReferenceMap, Map<String, String> externalAlertGroupMap,String legalEntityId) {

		Map<String, String> alertRegistrationMap = new HashMap<>();
		try {
			
			diagnostic.prepareDebug("externalAlertGroupMap "+ externalAlertGroupMap).log();
			
			Map<String, Object> orchParams = getOrchParams(alertSubObj, 
					alertSubObj.getPreferenceLevel(), externalAlertGroupMap, alertExtlReferenceMap,legalEntityId);
			if(diagnostic.isDebugEnabled()) {
				diagnostic.prepareDebug("alertExtlReferenceMap " + alertExtlReferenceMap).log();
				diagnostic.prepareDebug("orchParams error "+ orchParams).log();	
				diagnostic.prepareDebug("alertSubObj  "+ alertSubObj).log();	
			}
			
			Result res = CampaignUtil.invokeService(ALERT_ORCHESTRATION,PERFORM_EXTERNL_SUBSCRIPTION,orchParams);			
			if(res.getParamValueByName(ACConstants.DBP_ERROR_MESSAGE) == null) {
				return res.getDatasetById(ACConstants.LOOP_DATASET).getAllRecords().
						stream().filter(rec -> rec.getParamValueByName("referenceID") != null)
						.collect(Collectors.toMap((Record rec) ->  rec.getParamValueByName("alertSubTypeId"),
								(Record rec) ->  rec.getParamValueByName("referenceID")));

			} else {				
				alertRegistrationMap.put(ACConstants.DBP_ERROR_MESSAGE, res.getParamValueByName(ACConstants.DBP_ERROR_MESSAGE));
				alertRegistrationMap.put(ACConstants.DBP_ERROR_CODE, res.getParamValueByName(ACConstants.DBP_ERROR_CODE));				 
			}
		} catch (Exception e) {
			alert_Logger.prepareError("Error while calling looping orchestration", e).log();
			alertRegistrationMap.put(ACConstants.DBP_ERROR_MESSAGE, "Got error while external registration " + e.getMessage());		 
		}

		return alertRegistrationMap;		
	}	

	private static Map<String, Object> getOrchParams(AlertSubscription alertSubObj, String preferenceLevel,
			Map<String, String> externalAlertGroupMap ,	Map<String, String> referenceIdofAlert,String legalEntityId) {
		StringBuilder subParamsStrBuilder = new StringBuilder();
		Map<String, Object> orchReqParameters = new HashMap<>();
		Map<String, String> groupSubscriptionMap = new HashMap<>();
		int loopCount = 0;
		Map<String,Object> paramMap = new HashMap<>();	
		paramMap.put("legalEntityId", legalEntityId);
		for ( AlertGroup currGroup : alertSubObj.getGroups()) {			
			Set<Alert> alertList = currGroup.getAlerts().stream()
					.filter(alert -> externalAlertGroupMap.keySet().
												contains(alert.getId()))					 
					.collect(Collectors.toSet());			
			groupSubscriptionMap.put(currGroup.getTypeID(),currGroup.isSub() ? "YES" : "NO");
			for (Alert alert : alertList) {							
				populateCommonParamMap(alertSubObj, currGroup.getTypeID(),referenceIdofAlert,
						paramMap, alert.getId(),getSubscribeStr(preferenceLevel, currGroup, alert));					
				paramMap.put("value", alert.getValue1());
				prepareLoopInput(subParamsStrBuilder, paramMap, alert.getId());
				++loopCount;				
				externalAlertGroupMap.remove(alert.getId());
				
			}	
			paramMap.clear();
		}	
			paramMap.put("legalEntityId", legalEntityId);
			if(externalAlertGroupMap != null && !externalAlertGroupMap.isEmpty()){
				for(Entry<String, String> alertGroupentry : externalAlertGroupMap.entrySet()) {
						populateCommonParamMap(alertSubObj, alertGroupentry.getValue(),referenceIdofAlert,
							paramMap, alertGroupentry.getKey(), 
							groupSubscriptionMap.get(alertGroupentry.getValue()));					
						prepareLoopInput(subParamsStrBuilder, paramMap, alertGroupentry.getKey());
						++loopCount;
					paramMap.clear();
				}
			}
			orchReqParameters.put("inputparamsStr", subParamsStrBuilder.toString());
			orchReqParameters.put(ACConstants.LOOP_SEPERATOR, DOLLAR_LOOP_SEPARATOR);
			orchReqParameters.put(ACConstants.LOOP_COUNT, loopCount);			
		
		return orchReqParameters;
	}

	public static void prepareLoopInput(StringBuilder subParamsStrBuilder,
								Map<String, Object> paramMap, String alertId) {
		String paramStr = stringifyMap(paramMap, alertId);
		subParamsStrBuilder.append(paramStr).append(DOLLAR_LOOP_SEPARATOR);
	}

	public static void populateCommonParamMap(AlertSubscription alertSubObj, String groupName, Map<String, String> referenceIdofAlert,
			Map<String, Object> paramMap, String alertId,String subscribe) {
		paramMap.put("eventId", alertId);	
		paramMap.put("eventType", groupName);
		paramMap.put("externalUserId", alertSubObj.getCustomerId());
		paramMap.put("contractReference", alertSubObj.getAccountID());
		paramMap.put("alertRequestId",referenceIdofAlert.get(alertId));
		paramMap.put("subscribe", subscribe);
		paramMap.put("externalCustomerId", alertSubObj.getBackendId());
	}

	public static String getSubscribeStr(String preferenceLevel, AlertGroup currGroup, Alert alert) {
		boolean issubscribe = ALERTPREFERNCES.ALERT.name().equalsIgnoreCase(preferenceLevel) 
				? alert.isSub() : currGroup.isSub();
				return issubscribe ? "YES" : "NO";		
	}

	public static String stringifyMap(Map<String, Object> paramMap, String alertId) {
		String paramStr = null;
		try {
			paramStr = JSONUtils.stringify(paramMap);
			if(diagnostic.isDebugEnabled()) {
				diagnostic.prepareDebug(String.format("externalSub String is %s %s", 
						alertId , paramStr)).log();	
			}
		} catch (Exception e) {
			paramStr = "";
			diagnostic.prepareDebug("Error in parsing externalSub input map", e).log();
		}
		return paramStr;
	}

}
