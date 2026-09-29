package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.auth.Authentication;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class AccountDetailsForCombinedStatementsOperation implements JavaService2, AccountsConstants, TemenosConstants {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
		String accountId = CommonUtils.getParamValue(params, ACCOUNTID);
		Authentication authentication = Authentication.getInstance();
		request.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
		String authToken = TokenUtils.getT24AuthToken(request);
		diagnostic.prepareInfo(authToken).log();
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		headerParams.put(TemenosConstants.PARAM_AUTHORIZATION, authToken);
		inputParams.put(DB_ACCOUNTID, accountId);
		Result accountResult = CommonUtils.callIntegrationService(request, inputParams, headerParams,
				TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_ACCOUNT_DETAILS_FOR_COMBINED_STATEMENT,
				false);
		Record accountRecord = new Record();
		Record productRecord = new Record();
		if (accountResult.getDatasetById("Accounts") != null
				&& accountResult.getDatasetById("Accounts").getAllRecords().size() > 0) {
			accountRecord = accountResult.getDatasetById("Accounts").getRecord(0);
		}
		if (accountRecord != null && accountRecord.getDatasetById("products") != null
				&& accountRecord.getDatasetById("products").getAllRecords().size() > 0) {
			productRecord = accountRecord.getDatasetById("products").getRecord(0);
		}
		result.addParam("accountName", productRecord.getParamValueByName("accountName"));
		result.addParam("nickName", productRecord.getParamValueByName("nickName"));
		result.addParam("currencyCode", productRecord.getParamValueByName("currencyCode"));
		result.addParam("accountType", productRecord.getParamValueByName("accountType"));

		return result;
	}

}
