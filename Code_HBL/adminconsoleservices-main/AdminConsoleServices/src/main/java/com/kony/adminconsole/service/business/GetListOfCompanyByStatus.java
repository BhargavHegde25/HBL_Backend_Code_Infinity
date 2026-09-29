package com.kony.adminconsole.service.business;

import org.apache.commons.lang3.StringUtils;
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

public class GetListOfCompanyByStatus  implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        long startTime = System.currentTimeMillis();
        long svcEndTime = 0;
        long svcStartTime = 0;
        String statusId = null;
        try {
        	
        	if (StringUtils.isEmpty(requestInstance.getParameter("statusId"))) {
                ErrorCodeEnum.ERR_21717.setErrorCode(result);
                return result;
            }
        	
        	statusId = requestInstance.getParameter("statusId");
            svcStartTime = System.currentTimeMillis();
            JSONObject getListOfCompanyByStatusResponse = DBPServices.getListOfCompanyByStatus( statusId,  requestInstance);
            svcEndTime = System.currentTimeMillis();

            if (getListOfCompanyByStatusResponse == null || !getListOfCompanyByStatusResponse.has(FabricConstants.OPSTATUS)
                    || getListOfCompanyByStatusResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21032.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch list of company by status. statusId: " + statusId);
                return result;
            } else if (getListOfCompanyByStatusResponse.has("dbpErrMsg")) {
					result.addParam(new Param("errMsg", getListOfCompanyByStatusResponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
			}else {
                result.addParam(new Param("status", "Success", FabricConstants.STRING));
                result.addParam(new Param("opstatus", getListOfCompanyByStatusResponse.get("opstatus").toString(),
                        FabricConstants.STRING));
                // Creating Dataset and adding to result
                if (getListOfCompanyByStatusResponse.has("organisation")) {
                    JSONArray readResponseJSONArray = getListOfCompanyByStatusResponse.getJSONArray("organisation");
                    Dataset dataSet = new Dataset();
                    dataSet.setId("organisation");
                    for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
                        JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
                        Record currRecord = new Record();
                        if (currJSONObject.length() != 0) {
                        	if(currJSONObject.has("businessType")) {
                        		Record businessTypeRecord = new Record();
                        		businessTypeRecord.setId("businessType");
                        		JSONObject businessType = currJSONObject.getJSONObject("businessType");
                        		for (String currKey : businessType.keySet()) {
                        			if (businessType.has(currKey)) {
                        				businessTypeRecord.addParam(new Param(currKey, String.valueOf(businessType.get(currKey)),
                                                FabricConstants.STRING));
                        			}
                                }
                        		currRecord.addRecord(businessTypeRecord);
                        		currJSONObject.remove("businessType");
                        	}
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
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully fetched list of company by status. statusId: " + statusId);
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in get list of company by status", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch list of company by status. statusId: " + statusId);
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        long endTime = System.currentTimeMillis();
        alert.prepareError("MF Time company details send rsp:" + (endTime - startTime) + "service time"
                + (svcEndTime - svcStartTime)).log();
        return result;

    }

}
