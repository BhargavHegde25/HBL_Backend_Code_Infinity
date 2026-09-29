package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.ServiceUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class ExternalAlertSubscription implements JavaService2 {
	private static final String SUCCESS = "success";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String paramString, Object[] paramArrayOfObject,
			DataControllerRequest dcReq, DataControllerResponse dcResp)
					throws Exception {
		Result result = new Result();
		try {
			Map<String, String> inputParams = (HashMap<String, String>) paramArrayOfObject[1];
			String inputStr = inputParams.get("inputparamsStr");
			diagnostic.prepareDebug("InputStr for t24 subscription error"+ inputStr).log();
			Map<String, Object> paramMap = getInputParamMap(result, inputStr);
			result.addParam("alertSubTypeId", String.valueOf(paramMap.get("eventId")));
		
			if (paramMap == null || paramMap.isEmpty()) {
				setInvalidInputMsg(result);				
				return result;
			}		
			if(diagnostic.isDebugEnabled()) {
				diagnostic.prepareDebug("paramMap " + paramMap).log();
			} 
			executeExternalSubscription(dcReq, result, paramMap);

		} catch (Exception e) {
			alert.prepareError("Error occured while processing of external alertSubscription", e).log();
			addReqParamOnError(result);  

		}		
		return result;		
	}

	public void addReqParamOnError(Result result) {
		result.addParam(SUCCESS, "false" );
		result.addParam("referenceID",  null );
	}

	public void executeExternalSubscription(DataControllerRequest dcReq, Result result, Map<String, Object> paramMap)
			throws MiddlewareException {
		ServiceURLEnum operationEnum = ServiceURLEnum.T24_ALERT_SUBSCRIPTION_UPDATE;
		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put("companyId", paramMap.get("legalEntityId"));
		try {			
		    String alertRequestIdvalue = String.valueOf(paramMap.get("alertRequestId"));
			if(StringUtils.isBlank(alertRequestIdvalue) || "null".equalsIgnoreCase(alertRequestIdvalue)) {
				if(paramMap.get("subscribe") != null && String.valueOf(paramMap.get("subscribe")).equalsIgnoreCase("NO"))
				{
					alert.prepareError("======== No need to hit external system in this scenario =============" + paramMap).log();
					result.addParam(SUCCESS, "true" );
					result.addParam("referenceID",  null );
					return;
				}
				operationEnum = ServiceURLEnum.T24_ALERT_SUBSCRIPTION_CREATE;
				paramMap.remove("alertRequestId");
				paramMap.remove("subscribe");
			}
		
			Result serviceRes = ServiceUtil.invokeService(operationEnum, paramMap, headerMap, dcReq);
			boolean successParam = false;
			if(serviceRes != null) {
				if( serviceRes.getRecordById("header") != null ) {			
					Record headerRecord = serviceRes.getRecordById("header");
					successParam = headerRecord.getParamValueByName("status").equalsIgnoreCase(SUCCESS);
					result.addParam(SUCCESS, String.valueOf(successParam) );					
					result.addParam("referenceID", headerRecord.getParamValueByName("id") );
					if(diagnostic.isDebugEnabled()) {
					diagnostic.prepareDebug(String.format("T24Subscription operation %s is %s with referenceID %s",
							operationEnum.getOperationName(), successParam , headerRecord.getParamValueByName("id"))).log();
					}
				}
				if(!successParam && serviceRes.getParamValueByName("errorDetailsMessage") != null ) {
					addReqParamOnError(result); 
					alert.prepareError(String.format("T24Subscription opearation %s failed for eventID %s with errmessage %s ",
							operationEnum, paramMap.get("eventId"),serviceRes.getParamValueByName("errorDetailsMessage") )).log();
					if(serviceRes.getParamValueByName("errorDetailsCode") != null) {
						alert.prepareError(String.format("with Code %s" , serviceRes.getParamValueByName("errorDetailsCode")) ).log();
					}
				}
			}else {
				addReqParamOnError(result); 
			}			

		} catch (Exception e) {
			alert.prepareError("Error occured while invoking service", e).log();
			result.addErrMsgParam("Error occured while invoking service "+ operationEnum.getOperationName());
			addReqParamOnError(result); 
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getInputParamMap(Result result, String inputStr) {
		Map<String,Object> paramMap = null;
		try {
			if (inputStr != null) {
				paramMap = JSONUtils.parse(inputStr, HashMap.class);
			}
		} catch (Exception e1) {
			setInvalidInputMsg(result);
		} 
		return paramMap;
	}

	public void setInvalidInputMsg(Result result) {
		alert.prepareError("Invalid Input passed").log();
		ErrorCodeEnum.ERR_20848.setErrorCode(result);
		addReqParamOnError(result);	
	}
}
