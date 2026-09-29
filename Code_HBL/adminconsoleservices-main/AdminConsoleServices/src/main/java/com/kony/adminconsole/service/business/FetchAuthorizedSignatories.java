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

public class FetchAuthorizedSignatories   implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        long startTime = System.currentTimeMillis();
        long svcEndTime = 0;
        long svcStartTime = 0;
        String cif = null;
        try {
        	
        	if (StringUtils.isEmpty(requestInstance.getParameter("Cif"))) {
                ErrorCodeEnum.ERR_21798.setErrorCode(result);
                return result;
            }
        	
        	if (StringUtils.isEmpty(requestInstance.getParameter("Organization_id"))) {
                ErrorCodeEnum.ERR_21807.setErrorCode(result);
                return result;
            }
        	
        	cif = requestInstance.getParameter("Cif");
        	String userName = StringUtils.isEmpty(requestInstance.getParameter("UserName")) ? "" : requestInstance.getParameter("UserName");
        	String ssn = StringUtils.isEmpty(requestInstance.getParameter("Ssn")) ? "" : requestInstance.getParameter("Ssn");
        	String dateOfBirth = StringUtils.isEmpty(requestInstance.getParameter("DateOfBirth")) ? "" : requestInstance.getParameter("DateOfBirth");
        	String organizationId = StringUtils.isEmpty(requestInstance.getParameter("Organization_id")) ? "" : requestInstance.getParameter("Organization_id");
        	
            svcStartTime = System.currentTimeMillis();
            JSONObject fasResponse = DBPServices.fetchAuthorizedSignatories( cif, userName, ssn, dateOfBirth, organizationId, requestInstance);
            svcEndTime = System.currentTimeMillis();

            if (fasResponse == null || !fasResponse.has(FabricConstants.OPSTATUS)
                    || fasResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21033.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch customer-centric authorised signatory . cif: " + cif);
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
                if (fasResponse.has("Customers")) {
                    JSONArray readResponseJSONArray = fasResponse.getJSONArray("Customers");
                    Dataset dataSet = new Dataset();
                    dataSet.setId("Customers");
                    for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
                        JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
                        Record currRecord = new Record();
                        if (currJSONObject.length() != 0) {
                        	if(currJSONObject.has("membershipDTO")) {
                        		Record membershipDTORecord = new Record();
                        		membershipDTORecord.setId("membershipDTO");
                        		JSONObject membershipDTO = currJSONObject.getJSONObject("membershipDTO");
                        		if(membershipDTO.has("addressDTO")) {
                        			Record addressDTORecord = new Record();
                        			addressDTORecord.setId("addressDTO");
                            		JSONObject addressDTO = membershipDTO.getJSONObject("addressDTO");
                            		for (String currKey : addressDTO.keySet()) {
                            			if (addressDTO.has(currKey)) {
                            				addressDTORecord.addParam(new Param(currKey, String.valueOf(addressDTO.get(currKey)),
                                                    FabricConstants.STRING));
                            			}
                                    }
                            		membershipDTORecord.addRecord(addressDTORecord);
                            		membershipDTO.remove("addressDTO");
                            		
                        		}
                        		for (String currKey : membershipDTO.keySet()) {
                        			if (membershipDTO.has(currKey)) {
                        				membershipDTORecord.addParam(new Param(currKey, String.valueOf(membershipDTO.get(currKey)),
                                                FabricConstants.STRING));
                        			}
                                }
                        		currRecord.addRecord(membershipDTORecord);
                        		currJSONObject.remove("membershipDTO");
                        	}
                        	
                        	String serviceKey = String.valueOf(CommonUtilities.getNewId());
                        	Map<String,String> infoMap = new HashMap<String,String>();
                        	
                        	infoMap.put("firstName", String.valueOf(currJSONObject.get("firstName")));
                        	infoMap.put("lastName",  String.valueOf(currJSONObject.get("lastName")));
                        	infoMap.put("dateOfBirth",  String.valueOf(currJSONObject.get("dateOfBirth")));
                        	infoMap.put("ssn",  String.valueOf(currJSONObject.get("ssn")));
                        	infoMap.put("membershipId", String.valueOf(currJSONObject.get("membershipId")));
                        	infoMap.put("id", String.valueOf(currJSONObject.get("id")));
                        	MemoryManager.saveIntoCache(serviceKey, MapUtil.mapToString(infoMap));
                        	
                            for (String currKey : currJSONObject.keySet()) {
                   
                                if (currJSONObject.has(currKey)) {
                                    currRecord.addParam(new Param(currKey, String.valueOf(currJSONObject.get(currKey)),
                                            FabricConstants.STRING));
                                }
                            }
                            
                            currRecord.addParam(new Param("serviceKey", serviceKey , FabricConstants.STRING));
                            dataSet.addRecord(currRecord);
                        }
                    }
                    result.addDataset(dataSet);
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                            ActivityStatusEnum.SUCCESSFUL, "Successfully fetched customer-centric authorised signatory . cif: " + cif);
                }
                
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in fetch authrised signatories", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch customer-centric authorised signatory . cif: " + cif);
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        long endTime = System.currentTimeMillis();
        alert.prepareError("MF Time company details send rsp:" + (endTime - startTime) + "service time"
                + (svcEndTime - svcStartTime)).log();
        return result;

    }

}
