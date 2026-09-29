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

public class GetAccountsForAdminExceptRetailPostProcessor  extends BasePostProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		Dataset accountTypeDS = result.getDatasetById(DS_ACCOUNTS);
		List<Record> accountTypeRecords = accountTypeDS != null ? accountTypeDS.getAllRecords() : null;
		List<Record> accountFinals = new ArrayList<Record>();
		if (null == accountTypeRecords || accountTypeRecords.isEmpty()) {
			alert.prepareError("Accounts empty return result").log();
			Result emptyResult = new Result();
			emptyResult.addDataset(new Dataset(DS_DB_ACCOUNTS));
			emptyResult.addOpstatusParam(0);
			emptyResult.addHttpStatusCodeParam(200);
			return emptyResult;
		}

		alert.prepareError("Result " + result.getAllParams().toString()).log();
		String loginUserId = request.getParameter(TemenosConstants.PARAM_LOGINUSERID);
		HashMap<String, String> favouriteAccounts = new HashMap<String, String>();
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		if (!"".equalsIgnoreCase(loginUserId)) {
			
			inputParams.put("$filter", "User_id eq " + loginUserId);
			Result readAccounts = CommonUtils.callIntegrationService(request, inputParams, headerParams,
					TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_ACCOUNTS_GET, false);
			Dataset getAccountsDS = readAccounts.getDatasetById(DS_ACCOUNTS);

			if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
				for (Record rec : getAccountsDS.getAllRecords()) {
					favouriteAccounts.put(rec.getParamValueByName(DB_ACCOUNTID),
							rec.getParamValueByName(DB_FAVOURITESTATUS));
				}
			}
		}

		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		temenosUtils.loadAccountTypeProperties(request);

		for (Record record : accountTypeRecords) {
			JsonObject accountHolderjson = new JsonObject();
			String accountHolder = CommonUtils.getParamValue(record, PARAM_ACCOUNT_HOLDER);
			accountHolderjson.addProperty(PARAM_USERNAME, request.getParameter(AccountsConstants.PARAM_USERNAME));
			accountHolderjson.addProperty(PARAM_FULLNAME, accountHolder);
			record.addStringParam(PARAM_ACCOUNT_HOLDER, accountHolderjson.toString());

			String accountType = record.getParamValueByName(PARAM_ACCOUNT_TYPE);
			accountType = temenosUtils.accountTypesMap.get(accountType);// getDBXAccountType(accountType);
			if (accountType != null && !"".equalsIgnoreCase(accountType)) {
				String principalBalance = record.getParamValueByName(PRINCIPAL_BALANCE);
				if (ACCOUNT_TYPE_DEPOSIT.equalsIgnoreCase(accountType)) {
					record.addStringParam(AVAILABLE_BALANCE, principalBalance);
				}
				if (!favouriteAccounts.isEmpty()) {
					String favouriteStatus = favouriteAccounts.get(record.getParamValueByName(RESPONSE_ACCOUNTID)) != null
							? favouriteAccounts.get(record.getParamValueByName(RESPONSE_ACCOUNTID))
							: "0";
					record.addStringParam(FAVOURITE_STATUS, favouriteStatus);
				}
				record.addParam(PARAM_ACCOUNT_TYPE, accountType);
				//Check if eStatement is enabled
				String accountID = record.getParamValueByName(RESPONSE_ACCOUNTID);
				inputParams.put("$filter", "Account_id eq " + accountID +" and "+"EStatementmentEnable eq "+"true");
				
				Result resultAcc = CommonUtils.callIntegrationService(request, inputParams, headerParams,
						TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_ACCOUNTS_GET, false);
				
				Dataset ds = resultAcc.getDatasetById(TemenosConstants.DS_ACCOUNTS);
				if (ds != null && !ds.getAllRecords().isEmpty()) {
					Record getAccountRecord = ds.getRecord(0);
					String email = getAccountRecord.getParamValueByName(PARAM_EMAIL);
					String updatedBy = getAccountRecord.getParamValueByName(PARAM_UPDATEDBY);
					String lastUpdated = getAccountRecord.getParamValueByName(PARAM_LASTUPDATED);
					record.addStringParam(PARAM_UPDATEDBY, updatedBy);
					record.addStringParam(PARAM_LASTUPDATED, lastUpdated);
					record.addStringParam(PARAM_ESTATEMENTENABLE, Constants.TRUE);
					record.addStringParam(PARAM_EMAIL, email);
				} else {
					record.addStringParam(PARAM_ESTATEMENTENABLE, Constants.FALSE);
				}
				if (!"".equalsIgnoreCase(loginUserId)) {
					record.addStringParam(PARAM_SUPPORT_BILLPAY, YES);
					record.addStringParam(PARAM_SUPPORT_CHECKS, YES);
					record.addStringParam(PARAM_DEPOSIT_DESTINATION_ACCOUNT, YES);
					record.addStringParam(PARAM_TRANSFER_SOURCE_ACCOUNT, YES);
					record.addStringParam(PARAM_TRANSFER_DESTINATION_ACCOUNT, YES);
				}

				accountFinals.add(record);
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
