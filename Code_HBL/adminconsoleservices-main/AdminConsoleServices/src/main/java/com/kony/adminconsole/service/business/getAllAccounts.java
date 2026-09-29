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

public class getAllAccounts implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        String Account_id = "";
        String Membership_id = "";
        try {
            
            if (requestInstance.getParameter("Account_id") != null) {
                Account_id = requestInstance.getParameter("Account_id");
            }
            
            if (requestInstance.getParameter("Membership_id") != null) {
                Membership_id = requestInstance.getParameter("Membership_id");
            }
            JSONObject getAccountsresponse = DBPServices.getAllAccounts(Account_id, Membership_id,
                    requestInstance);
			
            if (getAccountsresponse == null || !getAccountsresponse.has(FabricConstants.OPSTATUS)
                    || getAccountsresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21019.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch account details. Membership_id: " + Membership_id+", Account_id= "+Account_id);
                return result;
            }else if (getAccountsresponse.has("dbpErrMsg")) {
				result.addParam(new Param("errMsg", getAccountsresponse.getString("dbpErrMsg"),
						FabricConstants.STRING));
				return result;
			}else {
                result.addParam(new Param("status", "Success", FabricConstants.STRING));
                result.addParam(
                        new Param("opstatus", getAccountsresponse.get("opstatus").toString(), FabricConstants.STRING));
                // Creating Dataset and adding to result
                JSONArray readResponseJSONArray = getAccountsresponse.getJSONArray("Accounts");
                Dataset dataSet = new Dataset();
                dataSet.setId("Accounts");
                for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
                    JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
                    Record currRecord = new Record();
                    if (currJSONObject.length() != 0) {
                        for (String currKey : currJSONObject.keySet()) {
                            if (currJSONObject.has(currKey)) {
                                currRecord.addParam(
                                        new Param(currKey, currJSONObject.getString(currKey), FabricConstants.STRING));
                            }
                        }
                        dataSet.addRecord(currRecord);
                    }
                }
                result.addDataset(dataSet);
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Failed to fetch account details. Membership_id: " + Membership_id+", Account_id= "+Account_id);
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in get Accounts", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch account details. Membership_id: " + Membership_id+", Account_id= "+Account_id);
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        return result;
    }
}
