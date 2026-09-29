package com.kony.adminconsole.service.business;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MapUtil;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class FetchAccountSignatories implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        long startTime = System.currentTimeMillis();
        long svcEndTime = 0;
        long svcStartTime = 0;
        String organizationId = null;
        try {
        	
        	if (StringUtils.isEmpty(requestInstance.getParameter("organizationId"))) {
                ErrorCodeEnum.ERR_21797.setErrorCode(result);
                return result;
            }
        	
        	organizationId = requestInstance.getParameter("organizationId");
            svcStartTime = System.currentTimeMillis();
            JSONObject fasResponse = DBPServices.fetchAccountSignatories( organizationId, requestInstance);
            svcEndTime = System.currentTimeMillis();

            if (fasResponse == null || !fasResponse.has(FabricConstants.OPSTATUS)
                    || fasResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21037.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch account-centric authorised signatory . organizationId: " + organizationId);
                return result;
            } else if (fasResponse.has("dbpErrMsg")) {
					result.addParam(new Param("errMsg", fasResponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
			}else {
                result.addParam(new Param("status", "Success", FabricConstants.STRING));
                result.addParam(new Param("opstatus", fasResponse.get("opstatus").toString(),
                        FabricConstants.STRING));
             // Creating Dataset and adding to result
                if (fasResponse.has("AccountSignatories")) {
                	JSONArray readResponseJSONArray = fasResponse.getJSONArray("AccountSignatories");
                	Dataset dataSet = new Dataset();
                    dataSet.setId("AccountSignatories");
                    for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
                    	JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
                        Record currRecord = new Record();
                        if (currJSONObject.length() != 0) {
                        	
                        	String accountId = String.valueOf(currJSONObject.get("accountId"));
                        	
                        	if(currJSONObject.has("AccountHolderDetails")) {
                        		Dataset dataSetAHD = new Dataset();
                        		dataSetAHD.setId("AccountHolderDetails");
                        		JSONArray ahdJSONArray = currJSONObject.getJSONArray("AccountHolderDetails");
                        		 for (int i = 0; i < ahdJSONArray.length(); i++) {
                        			 JSONObject innerJSONObject = ahdJSONArray.getJSONObject(i);
                        			 Record innerRecord = new Record();
                        			 if (innerJSONObject.length() != 0) {
                        				 
                        				 if(innerJSONObject.has("SignatoryTypes")) {
                        					 Dataset dataSetST = new Dataset();
                        					 dataSetST.setId("SignatoryTypes");
                        					 JSONArray stJSONArray = innerJSONObject.getJSONArray("SignatoryTypes");
                        					 for (int j = 0; j < stJSONArray.length(); j++) {
                        						 JSONObject stCurrJSONObject = stJSONArray.getJSONObject(j);
                                    			 Record stCurrRecord = new Record();
                                    			 if (stCurrJSONObject.length() != 0) {
                                    				 for (String currKey : stCurrJSONObject.keySet()) {
                                    					 stCurrRecord.addParam(new Param(currKey, String.valueOf(stCurrJSONObject.get(currKey)),
                                                                 FabricConstants.STRING));
                                    				 }
                                    				 dataSetST.addRecord(stCurrRecord);
                                    			 }
                        					 }
                        					 
                        					 innerRecord.addDataset(dataSetST);
                        				 }
                        				 
                        				 for (String currKey : innerJSONObject.keySet()) {
                        					 innerRecord.addParam(new Param(currKey, String.valueOf(innerJSONObject.get(currKey)),
                                                     FabricConstants.STRING));
                        				 }
                        				 
                        				 String serviceKey = String.valueOf(CommonUtilities.getNewId());
                        				 Map<String,String> infoMap = new HashMap<String,String>();
                                     	 infoMap.put("firstName", String.valueOf(innerJSONObject.get("firstName")));
                                     	 infoMap.put("lastName",  String.valueOf(innerJSONObject.get("lastName")));
                                     	 infoMap.put("dateOfBirth",  String.valueOf(innerJSONObject.get("dateOfBirth")));
                                     	 infoMap.put("ssn",  String.valueOf(innerJSONObject.get("ssn")));
                                     	 infoMap.put("accountId", accountId);
										 infoMap.put("id", String.valueOf(innerJSONObject.get("id")));
                                     	 MemoryManager.saveIntoCache(serviceKey, MapUtil.mapToString(infoMap));
                                     	 innerRecord.addParam(new Param("serviceKey", serviceKey , FabricConstants.STRING));
                                     	 
                                     	dataSetAHD.addRecord(innerRecord);
                        				 
                        			 }
                        			 
                        		 }
                        		 
                        		 currRecord.addDataset(dataSetAHD);
                        	}
                        	
                        	for (String currKey : currJSONObject.keySet()) {
                        		currRecord.addParam(new Param(currKey, String.valueOf(currJSONObject.get(currKey)),
                                        FabricConstants.STRING));
           				 	}
                        	
                        	dataSet.addRecord(currRecord);
                        	
                        }
                    }
                    
                    result.addDataset(dataSet);
                    
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                            ActivityStatusEnum.SUCCESSFUL, "Successfully fetched account-centric authorised signatory . organizationId: " + organizationId);
                	
                }
                
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in Fetch account signatories", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch account-centric authorised signatory . organizationId: " + organizationId);
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        long endTime = System.currentTimeMillis();
        alert.prepareError("MF Time company details send rsp:" + (endTime - startTime) + "service time"
                + (svcEndTime - svcStartTime)).log();
        return result;

    }

}
