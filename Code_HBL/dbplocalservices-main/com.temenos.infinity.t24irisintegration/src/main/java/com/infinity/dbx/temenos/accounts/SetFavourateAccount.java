package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;
import java.util.UUID;


import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class SetFavourateAccount implements JavaService2, AccountsConstants, TemenosConstants {


	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];

		String accountId = CommonUtils.getParamValue(params, AccountsConstants.ACCOUNTID);
		String nickName = CommonUtils.getParamValue(params, AccountsConstants.NICKNAME);
		String favouriteStatus = CommonUtils.getParamValue(params, FAVOURITE_STATUS);

		Result result = new Result();
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		String loginUserId = CommonUtils.getUserAttributeFromIdentity(request, "customer_id"); 

		inputParams.put("$filter", "Account_id eq " + accountId +" and "+"Customer_id eq "+loginUserId);

		Result readAccounts = CommonUtils.callIntegrationService(request, inputParams, headerParams,
				TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OPERATION_CUSTOMER_ACCOUNTS_GET, false);
		
		Dataset getAccountsDS = readAccounts.getDatasetById(AccountsConstants.DS_CUSTOMERACCOUNTS);
		
		if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
			Record getAccountRecord = getAccountsDS.getRecord(0);
			
			if (!nickName.equalsIgnoreCase(getAccountRecord.getParamValueByName(DB_NICKNAME))) {
				result.addStringParam(UPDATENICKNAME_CALL, "true");
			} else {
				result.addStringParam(UPDATENICKNAME_CALL, "false");
			}
			inputParams.put(DB_ACCOUNTID, accountId);
			inputParams.put(DB_USER_ID, params.get(PARAM_LOGINUSERID).toString());
			inputParams.put(DB_NICKNAME, nickName);
			String primaryId = getAccountRecord.getParamValueByName("id");	
			inputParams.put("id", primaryId);
			inputParams.put("Customer_id", loginUserId);
			if("".equalsIgnoreCase(favouriteStatus)) {
				if ("1".equalsIgnoreCase(getAccountRecord.getParamValueByName(DB_FAVOURITESTATUS))) {
					inputParams.put(DB_FAVOURITESTATUS, "0");
				}else {
					inputParams.put(DB_FAVOURITESTATUS, "1");
				}
			}else {
				inputParams.put(DB_FAVOURITESTATUS, favouriteStatus);
			}

			CommonUtils.callIntegrationService(request, inputParams, headerParams,
					TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_CUSTOMERACCOUNTS_UPDATE, false);
		}
		else {
			result.addStringParam(UPDATENICKNAME_CALL, "true");
			
			inputParams.put("id", UUID.randomUUID().toString());
			inputParams.put("Customer_id", loginUserId);
			inputParams.put(DB_ACCOUNTID, accountId);
			inputParams.put(DB_USER_ID, params.get(PARAM_LOGINUSERID).toString());
			inputParams.put(DB_NICKNAME, nickName);
			if("".equalsIgnoreCase(favouriteStatus)) {
				inputParams.put(DB_FAVOURITESTATUS, "1");
			}else {
				inputParams.put(DB_FAVOURITESTATUS, favouriteStatus);
			}
			CommonUtils.callIntegrationService(request, inputParams, headerParams,
					TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_CUSTOMERACCOUNTS_CREATE, false);
		}
		result.addStringParam("updatedRecords", "1");
		return result;
	}
}
