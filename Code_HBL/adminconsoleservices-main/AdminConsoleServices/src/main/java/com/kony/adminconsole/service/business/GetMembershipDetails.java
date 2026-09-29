package com.kony.adminconsole.service.business;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
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

public class GetMembershipDetails implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        long startTime = System.currentTimeMillis();
        long svcEndTime = 0;
        long svcStartTime = 0;
        String membershipId = null;
        try {
        	
            if (requestInstance.getParameter("Membership_id") != null) {
            	membershipId = requestInstance.getParameter("Membership_id");
            }
            svcStartTime = System.currentTimeMillis();
            JSONObject getMembershipDetailsResponse = DBPServices.getMembershipDetails( membershipId,  requestInstance);
            svcEndTime = System.currentTimeMillis();

            if (getMembershipDetailsResponse == null || !getMembershipDetailsResponse.has(FabricConstants.OPSTATUS)
                    || getMembershipDetailsResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21030.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch Membership details. membershipId: " + membershipId);
                return result;
            } else if (getMembershipDetailsResponse.has("dbpErrMsg")) {
					result.addParam(new Param("errMsg", getMembershipDetailsResponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
			}else {
                result.addParam(new Param("status", "Success", FabricConstants.STRING));
                result.addParam(new Param("opstatus", getMembershipDetailsResponse.get("opstatus").toString(),
                        FabricConstants.STRING));
                // Creating Dataset and adding to result
                if (getMembershipDetailsResponse.has("Address")) {
                    JSONArray readResponseJSONArray = getMembershipDetailsResponse.getJSONArray("Address");
                    Dataset dataSet = new Dataset();
                    dataSet.setId("Address");
                    for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
                        JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
                        Record currRecord = new Record();
                        if (currJSONObject.length() != 0) {
                            for (String currKey : currJSONObject.keySet()) {
                                if (currJSONObject.has(currKey)) {
                                    currRecord.addParam(new Param(currKey, String.valueOf(currJSONObject.get(currKey)),
                                            FabricConstants.STRING));
                                }
                            }
                            dataSet.addRecord(currRecord);
                        }
                    }
                    result.addDataset(dataSet);
                }
                result.addParam(new Param("phone", getMembershipDetailsResponse.get("phone").toString(),FabricConstants.STRING));
                result.addParam(new Param("taxId", getMembershipDetailsResponse.get("taxId").toString(),FabricConstants.STRING));
                result.addParam(new Param("name", getMembershipDetailsResponse.get("name").toString(),FabricConstants.STRING));
                result.addParam(new Param("id", getMembershipDetailsResponse.get("id").toString(),FabricConstants.STRING));
                result.addParam(new Param("businessType", getMembershipDetailsResponse.get("businessType").toString(),FabricConstants.STRING));
                result.addParam(new Param("email", getMembershipDetailsResponse.get("email").toString(),FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully fetched Membership details. membershipId: " + membershipId);
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in get Membership details", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch Membership details. membershipId: " + membershipId);
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        long endTime = System.currentTimeMillis();
        alert.prepareError("MF Time company details send rsp:" + (endTime - startTime) + "service time"
                + (svcEndTime - svcStartTime)).log();
        return result;

    }

}
