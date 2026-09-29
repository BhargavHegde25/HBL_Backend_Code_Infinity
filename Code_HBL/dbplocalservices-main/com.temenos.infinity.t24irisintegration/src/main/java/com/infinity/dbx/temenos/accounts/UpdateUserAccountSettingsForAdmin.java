package com.infinity.dbx.temenos.accounts;

import java.util.Calendar;
import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateUserAccountSettingsForAdmin implements JavaService2, AccountsConstants, TemenosConstants {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		
		try {
			
	
			String accountId = request.getParameter(Constants.PARAM_ID);
			String eStatementEnable = request.getParameter(PARAM_ESTATEMENTENABLE);
			String email = request.getParameter(PARAM_EMAIL) !=null ? request.getParameter(PARAM_EMAIL) : "" ;
			String updatedBy = request.getParameter(PARAM_UPDATEDBY);
			Calendar cal = Calendar.getInstance();
			String lastUpdated = TemenosUtils.getFormattedTimeStamp(cal.getTime(), null);
	
			
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
	
			inputParams.put("$filter", "Account_id eq " + accountId);
	
			Result readAccounts = CommonUtils.callIntegrationService(request, inputParams, headerParams,
					TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_ACCOUNTS_GET, false);
			
			Dataset getAccountsDS = readAccounts.getDatasetById(TemenosConstants.DS_ACCOUNTS);
			
			inputParams.put(DB_ACCOUNTID, accountId);
			inputParams.put(DB_PARAM_ESTATEMENTENABLE, eStatementEnable);
			inputParams.put(PARAM_EMAIL, email);
			inputParams.put(PARAM_UPDATEDBY, updatedBy);
			inputParams.put(PARAM_LASTUPDATED, lastUpdated);
			
			if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
	
				CommonUtils.callIntegrationService(request, inputParams, headerParams,
						TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_ACCOUNTS_UPDATE, false);
			}
			else {

				CommonUtils.callIntegrationService(request, inputParams, headerParams,
						TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_ACCOUNTS_CREATE, false);
			}
			
			result.addParam(new Param(PARAM_RESULT, "success", "String"));
            result.addParam(new Param(PARAM_UPDATEDBY, updatedBy, "String"));
            result.addParam(new Param(PARAM_LASTUPDATED, lastUpdated, "String"));
			
		} catch(Exception e) {
			
			diagnostic.prepareDebug("Update Failed for EStatementmentEnable").log();
			diagnostic.prepareDebug(e.getMessage()).log();
			result.addErrMsgParam("Update Failed");
			
		}
		return result;
	}
}
