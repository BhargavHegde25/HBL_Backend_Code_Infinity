package com.kony.adminconsole.alertmanage.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.Gson;
import com.kony.adminconsole.alertmanage.businessdelegate.api.CustExternalAlertSubscriptionBD;
import com.kony.adminconsole.alertmanage.resource.CustExternalAlertSubscriptionResource;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertSubscription;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustExternalAlertSubscriptionResourceImpl implements CustExternalAlertSubscriptionResource {	

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");


	@SuppressWarnings("unchecked")
	@Override
	public Result registerExtnlSubcription(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result res = new Result();
		try {
			Map<String, String> inputMap = (HashMap<String, String>) inputArray[1];           

			String alertSubscription = inputMap.get(ACConstants.ALERT_SUBSCRIPTION);
			String externalAlertGroup = inputMap.get(ACConstants.EXTERNAL_ALERT_GROUP_MAP);      
			String alertExtlReferenceMap = inputMap.get(ACConstants.ALERT_EXTL_REFERENCE_MAP); 
			String legalEntityId = inputMap.get("legalEntityId"); 

			AlertSubscription alertSubscriptionObj = new Gson().fromJson(alertSubscription, AlertSubscription.class);
			Map<String,String> externalAlertGroupObj =  new Gson().fromJson(externalAlertGroup, HashMap.class);
			Map<String,String> alertExtlReferenceMapObj =  new Gson().fromJson(alertExtlReferenceMap, HashMap.class);
			
			CustExternalAlertSubscriptionBD alertBusinessDelegate =
					 DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustExternalAlertSubscriptionBD.class);
			Map<String, String> extAlertReferenceMap = alertBusinessDelegate.registerExtnlSubcription(
					alertSubscriptionObj, alertExtlReferenceMapObj, externalAlertGroupObj,legalEntityId);

			if(extAlertReferenceMap.containsKey(ACConstants.DBP_ERROR_MESSAGE)) {
				res.addParam(ACConstants.DBP_ERROR_MESSAGE,extAlertReferenceMap.get(ACConstants.DBP_ERROR_MESSAGE));
				extAlertReferenceMap.remove(ACConstants.DBP_ERROR_MESSAGE);
			}

			Record rec = new Record();
			rec.setId(ACConstants.EXTERNAL_SUBSCRIPTION_MAP);	          
			rec.addAllParams(extAlertReferenceMap.entrySet().stream().map( getParamFromEntry()).collect(Collectors.toList()));	           
			res.addRecord(rec);	                 

		} catch (Exception e) {
			alert.prepareError("Error while calling externalSubscription " + e.getMessage()).log();
			ErrorCodeEnum.ERR_20847.setErrorCode(res);
			res.addParam(ACConstants.DBP_ERROR_MESSAGE,ErrorCodeEnum.ERR_20847.getMessage()+ " " + e.getMessage());			
		}
		return res;
	}	 


	public Function<Entry<String, String>, Param> getParamFromEntry() {
		return e -> new Param(e.getKey(), e.getValue());
	}


}
