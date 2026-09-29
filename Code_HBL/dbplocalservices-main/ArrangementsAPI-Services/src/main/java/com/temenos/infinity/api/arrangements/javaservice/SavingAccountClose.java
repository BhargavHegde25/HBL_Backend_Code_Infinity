package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.Constants;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.kony.dbx.util.CommonUtils;

public class SavingAccountClose implements JavaService2 {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String customerId = inputParams.get("customerid");
        String accountId = inputParams.get("accountNumber");
        String status = Constants.CLOSURE_PENDING;
        updateCustomerAccountsDBX(dcRequest,customerId,accountId,status);
        diagnostic.prepareDebug("Input Params from SRFMS SavingAccountClose",inputParams.toString()).log();
        result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
        result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
       
        return result;
    }
    public static void updateCustomerAccountsDBX(DataControllerRequest request, String customerId, String accountId, String status) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		String companyId = com.temenos.infinity.api.arrangements.utils.CommonUtils.getCompanyId(request);
		inputParams.put("$filter", "Account_id eq " + accountId +" and "+"Customer_id eq "+customerId);
		inputParams.put("accountId", accountId);
		inputParams.put("statusFlag",status );
		inputParams.put("legalEntityId", companyId);
		Result readAccounts = CommonUtils.callIntegrationService(request, inputParams, headerParams,
				TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.CLOSE_ACCOUNT_PROC, false);
		
	}
}
