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

public class CompanyAccountsGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        long startTime = System.currentTimeMillis();
        long svcEndTime = 0;
        long svcStartTime = 0;
        String Organization_id = null;
        try {
            if (requestInstance.getParameter("Organization_id") == null) {
                ErrorCodeEnum.ERR_21011.setErrorCode(result);
                return result;
            } else {
                Organization_id = requestInstance.getParameter("Organization_id");
                svcStartTime = System.currentTimeMillis();
                JSONObject getCompanyAccountsresponse = DBPServices.getCompanyAccounts(Organization_id,
                        requestInstance);
                svcEndTime = System.currentTimeMillis();
                if (getCompanyAccountsresponse.has("errmsg")) {
                    result.addParam(new Param("errMsg", getCompanyAccountsresponse.getString("errmsg"),
                            FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                            ActivityStatusEnum.FAILED, "Failed to fetch Company accounts. Organization_id: " + Organization_id);
                    return result;

                } else if (getCompanyAccountsresponse == null
                        || !getCompanyAccountsresponse.has(FabricConstants.OPSTATUS)
                        || getCompanyAccountsresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21016.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                } else {
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", getCompanyAccountsresponse.get("opstatus").toString(),
                            FabricConstants.STRING));

                    // Creating Dataset and adding to result
                    JSONArray readResponseJSONArray = getCompanyAccountsresponse.getJSONArray("OgranizationAccounts");
                    Dataset dataSet = new Dataset();
                    dataSet.setId("OgranizationAccounts");
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
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                            ActivityStatusEnum.SUCCESSFUL, "Successfully fetched Company accounts. Organization_id: " + Organization_id);
                }
            }

        } catch (Exception e) {
            alert.prepareError("Unexepected Error in get Company Accounts", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch Company accounts. Organization_id: " + Organization_id);
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        long endTime = System.currentTimeMillis();
        diagnostic.prepareDebug("MF Time get accounts send rsp:" + (endTime - startTime) + "service time"
                + (svcEndTime - svcStartTime)).log();

        return result;
    }
}
