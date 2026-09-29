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

public class CompanyDetailsGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        long startTime = System.currentTimeMillis();
        long svcEndTime = 0;
        long svcStartTime = 0;
        String Name = "";
        String Email = "";
        String id = "";
        try {
            String searchType = "Search";
            if (requestInstance.getParameter("Name") != null) {
                Name = requestInstance.getParameter("Name");
            }
            
            if (requestInstance.getParameter("Email") != null) {
                Email = requestInstance.getParameter("Email");
            }
            
            if (requestInstance.getParameter("id") != null) {
                id = requestInstance.getParameter("id");
            }
            svcStartTime = System.currentTimeMillis();
            JSONObject getCompanySearchresponse = DBPServices.getCompanySearch(searchType, Email, Name, id,
                    requestInstance);
            svcEndTime = System.currentTimeMillis();

            if (getCompanySearchresponse == null || !getCompanySearchresponse.has(FabricConstants.OPSTATUS)
                    || getCompanySearchresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21018.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch Company details. Name= " +Name +", Email= "+Email+", id= "+id );
                return result;
            } else if (getCompanySearchresponse.has("dbpErrMsg")) {
					result.addParam(new Param("errMsg", getCompanySearchresponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
			}else {
                result.addParam(new Param("status", "Success", FabricConstants.STRING));
                result.addParam(new Param("opstatus", getCompanySearchresponse.get("opstatus").toString(),
                        FabricConstants.STRING));
                // Creating Dataset and adding to result
                if (getCompanySearchresponse.has("OrganisationDetails")) {
                    JSONArray readResponseJSONArray = getCompanySearchresponse.getJSONArray("OrganisationDetails");
                    Dataset dataSet = new Dataset();
                    dataSet.setId("OrganisationDetails");
                    for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
                        JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
                        Record currRecord = new Record();
                        if (currJSONObject.length() != 0) {
                            for (String currKey : currJSONObject.keySet()) {
                                if (currJSONObject.has(currKey)) {
                                    currRecord.addParam(new Param(currKey, currJSONObject.getString(currKey),
                                            FabricConstants.STRING));
                                }
                            }
                            dataSet.addRecord(currRecord);
                        }
                    }
                    result.addDataset(dataSet);
                }
                Dataset dataSet1 = new Dataset();
                dataSet1.setId("OrganisationOwnerDetails");
                if (getCompanySearchresponse.has("OrganisationOwnerDetails")) {
                    JSONArray readownerResponseJSONArray = getCompanySearchresponse
                            .getJSONArray("OrganisationOwnerDetails");
                    for (int indexVar = 0; indexVar < readownerResponseJSONArray.length(); indexVar++) {
                        JSONObject currJSONObject = readownerResponseJSONArray.getJSONObject(indexVar);
                        Record currRecord = new Record();
                        if (currJSONObject.length() != 0) {
                            for (String currKey : currJSONObject.keySet()) {
                                if (currJSONObject.has(currKey)) {
                                    currRecord.addParam(new Param(currKey, currJSONObject.getString(currKey),
                                            FabricConstants.STRING));
                                }
                            }
                            dataSet1.addRecord(currRecord);
                        }
                    }
                }
                result.addDataset(dataSet1);
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully fetched Company details. Name= " +Name +", Email= "+Email+", id= "+id );
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in get Company search", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch Company details. Name= " +Name +", Email= "+Email+", id= "+id );
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        long endTime = System.currentTimeMillis();
        alert.prepareError("MF Time company details send rsp:" + (endTime - startTime) + "service time"
                + (svcEndTime - svcStartTime)).log();
        return result;

    }

}
