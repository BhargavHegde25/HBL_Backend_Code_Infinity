package com.infinity.dbx.temenos.accounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsForAdminRetailPostProcessor extends BasePostProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		alert.prepareError("backend response "+response.getResponse()).log();
		Dataset accountTypeDS = result.getDatasetById(DS_ACCOUNTS);
		List<Record> accountTypeRecords = accountTypeDS != null ? accountTypeDS.getAllRecords() : null;
		List<Record> accountFinals = new ArrayList<Record>();
		String errmsg = CommonUtils.getParamValue(result, PARAM_ERR_MSG);
		if (accountTypeRecords == null || accountTypeRecords.isEmpty() || errmsg.contains("No records")) {
			alert.prepareError("Accounts empty return result").log();
			Result emptyResult = new Result();
			emptyResult.addDataset(new Dataset(DS_DB_ACCOUNTS));
			emptyResult.addOpstatusParam(0);
			emptyResult.addHttpStatusCodeParam(200);
			return emptyResult;
		}
		
		String loginUserId = request.getParameter(TemenosConstants.PARAM_LOGINUSERID);
		
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		inputParams.put("$filter", "User_id eq " + loginUserId);
		request.addRequestParam_("$filter", "User_id eq " + loginUserId);
		Result readAccounts = CommonUtils.callIntegrationService(request, inputParams, headerParams,
				TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_ACCOUNTS_GET, true);
		Dataset getAccountsDS = readAccounts.getDatasetById(DS_DB_ACCOUNTS);
		HashMap<String, String> favouriteAccounts = new HashMap<String, String>();
		
		if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
			alert.prepareError("if true").log();
			for(Record rec : getAccountsDS.getAllRecords()) {
				favouriteAccounts.put(rec.getParamValueByName(DB_ACCOUNTID), rec.getParamValueByName(DB_FAVOURITESTATUS));
			}
		}
		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		temenosUtils.loadAccountTypeProperties(request);

		for (Record record : accountTypeRecords) {

			List<Record> products = record.getDatasetById(DS_PRODUCTS) != null
					? record.getDatasetById(DS_PRODUCTS).getAllRecords()
					: null;
			for (Record product : products) {
				
				String accountType = product.getParamValueByName(PARAM_ACCOUNT_TYPE);
				accountType = temenosUtils.accountTypesMap.get(accountType);// getDBXAccountType(accountType);
				if(accountType != null && !"".equalsIgnoreCase(accountType)) {
					String principalBalance = product.getParamValueByName(PRINCIPAL_BALANCE);
					if (ACCOUNT_TYPE_DEPOSIT.equalsIgnoreCase(accountType)) {
						product.addStringParam(AVAILABLE_BALANCE, principalBalance);
					}
					if(ACCOUNT_TYPE_SPROUT.equalsIgnoreCase(accountType)) {
						String currentBalance = CommonUtils.getParamValue(product, CURRENT_BALANCE);
						product.getParamByName(OUTSTANDING_BALANCE).setValue(currentBalance);
					}
					String customerReference = CommonUtils.getParamValue(product, CUSTOMER_REFERENCE);
					if(!"".equalsIgnoreCase(customerReference)) {
						product.addStringParam(NICKNAME, customerReference);
						product.addStringParam(DISPLAY_NAME, customerReference);
					} else {
						customerReference = CommonUtils.getParamValue(product, DISPLAY_NAME);
					}
					JsonObject accountHolderjson = new JsonObject();
					accountHolderjson.addProperty(PARAM_USERNAME, request.getParameter(AccountsConstants.PARAM_USERNAME));
					accountHolderjson.addProperty(PARAM_FULLNAME, customerReference);
					product.addStringParam(PARAM_ACCOUNT_HOLDER, accountHolderjson.toString());
					
					
					String favouriteStatus = favouriteAccounts.get(product.getParamValueByName(RESPONSE_ACCOUNTID)) != null ? favouriteAccounts.get(product.getParamValueByName(RESPONSE_ACCOUNTID)) : "0";
					product.addStringParam(FAVOURITE_STATUS, favouriteStatus);
					product.addParam(PARAM_ACCOUNT_TYPE, accountType);
					product.addStringParam(PARAM_SUPPORT_BILLPAY, YES);
					product.addStringParam(PARAM_SUPPORT_CHECKS, YES);
					product.addStringParam(PARAM_DEPOSIT_DESTINATION_ACCOUNT, YES);
					product.addStringParam(PARAM_TRANSFER_SOURCE_ACCOUNT, YES);
					product.addStringParam(PARAM_TRANSFER_DESTINATION_ACCOUNT, YES);
					product.addStringParam("isBusinessAccount", Constants.FALSE);
					
					//Check if eStatement is enabled
					String accountID = product.getParamValueByName("accountId");
					inputParams.put("$filter", "Account_id eq " + accountID +" and "+"EStatementmentEnable eq "+"true");
					
					Result resultAcc = CommonUtils.callIntegrationService(request, inputParams, headerParams,
							TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_ACCOUNTS_GET, false);
					
					Dataset ds = resultAcc.getDatasetById(TemenosConstants.DS_ACCOUNTS);
					if (ds != null && !ds.getAllRecords().isEmpty()) {
						Record getAccountRecord = ds.getRecord(0);
						String email = getAccountRecord.getParamValueByName(PARAM_EMAIL);
						String updatedBy = getAccountRecord.getParamValueByName(PARAM_UPDATEDBY);
						String lastUpdated = getAccountRecord.getParamValueByName(PARAM_LASTUPDATED);
						product.addStringParam(PARAM_UPDATEDBY, updatedBy);
						product.addStringParam(PARAM_LASTUPDATED, lastUpdated);
						product.addStringParam(PARAM_ESTATEMENTENABLE, Constants.TRUE);
						product.addStringParam(PARAM_EMAIL, email);
					} else {
						product.addStringParam(PARAM_ESTATEMENTENABLE, Constants.FALSE);
					}

					accountFinals.add(product);
				}
			}
		}
		
		result  = new Result();
		Dataset dataset = new Dataset();
        dataset.setId(DS_DB_ACCOUNTS);
        dataset.addAllRecords(accountFinals);
        result.addDataset(dataset);
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
       
		return result;
	}

}
