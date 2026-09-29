package com.infinity.dbx.temenos.accounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.Gson;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class AccountsPostProcessor extends BasePostProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        alert.prepareError("backend response " + response.getResponse()).log();
        Dataset accountTypeDS = result.getDatasetById(DS_ACCOUNTS);
        HashMap<String, Account> accounts = new HashMap<String, Account>();
        List<Record> accountTypeRecords = accountTypeDS != null ? accountTypeDS.getAllRecords() : null;
        List<Record> accountFinals = new ArrayList<Record>();
        String errmsg = CommonUtils.getParamValue(result, PARAM_ERR_MSG);
        if (accountTypeRecords == null || accountTypeRecords.isEmpty() || errmsg.contains("No records")) {
            alert.prepareError("Accounts empty return result").log();
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset(DS_ACCOUNTS));
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
            for (Record rec : getAccountsDS.getAllRecords()) {
                favouriteAccounts.put(rec.getParamValueByName(DB_ACCOUNTID),
                        rec.getParamValueByName(DB_FAVOURITESTATUS));
            }
        }
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);
        String accountHolder = request.getParameter(PARAM_ACCOUNT_HOLDER);
        for (Record record : accountTypeRecords) {
            List<Record> products = record.getDatasetById(DS_PRODUCTS) != null
                    ? record.getDatasetById(DS_PRODUCTS).getAllRecords() : null;
            for (Record product : products) {
                String accountType = product.getParamValueByName(PARAM_ACCOUNT_TYPE);
                accountType = temenosUtils.accountTypesMap.get(accountType);// getDBXAccountType(accountType);
                if (accountType != null && !"".equalsIgnoreCase(accountType)) {
                    String principalBalance = product.getParamValueByName(PRINCIPAL_BALANCE);
                    if (ACCOUNT_TYPE_DEPOSIT.equalsIgnoreCase(accountType)) {
                        product.addStringParam(AVAILABLE_BALANCE, principalBalance);
                    }
                    if (ACCOUNT_TYPE_SPROUT.equalsIgnoreCase(accountType)) {
                        String currentBalance = CommonUtils.getParamValue(product, CURRENT_BALANCE);
                        product.getParamByName(OUTSTANDING_BALANCE).setValue(currentBalance);
                    }
                    String customerReference = CommonUtils.getParamValue(product, CUSTOMER_REFERENCE);
                    if (!"".equalsIgnoreCase(customerReference)) {
                        product.addStringParam(NICKNAME, customerReference);
                        product.addStringParam(DISPLAY_NAME, customerReference);
                    }
                    
					String estatement = CommonUtils.getParamValue(product, STATEMENT);
					int accountMatchFlag = 0;
					if (StringUtils.isNotBlank(estatement) && StringUtils.equalsIgnoreCase(estatement, ESTATEMENT)) {
						product.addStringParam(PARAM_ESTATEMENTENABLE, "true");
						if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
							alert.prepareError("if true").log();
							for (Record rec : getAccountsDS.getAllRecords()) {
								if (StringUtils.equalsIgnoreCase(rec.getParamValueByName(DB_ACCOUNTID),
										product.getParamValueByName(PARAM_ACCOUNT_ID))) {
									String email = rec.getParamValueByName(PARAM_EMAIL);
									accountMatchFlag = 1;
									if (StringUtils.isNotBlank(email))
										product.addStringParam(PARAM_EMAIL, email);
									else
										product.addStringParam(PARAM_ESTATEMENTENABLE, "false");
									break;
								}
							}
						}
					} else
						product.addStringParam(PARAM_ESTATEMENTENABLE, "false");
					
					if (accountMatchFlag != 1) {
						product.addStringParam(PARAM_ESTATEMENTENABLE, "false");
					}

                    String favouriteStatus = favouriteAccounts.get(product.getParamValueByName("accountId")) != null
                            ? favouriteAccounts.get(product.getParamValueByName("accountId")) : "0";
                    product.addStringParam(FAVOURITE_STATUS, favouriteStatus);
                    product.addParam(PARAM_ACCOUNT_TYPE, accountType);
                    product.addStringParam(PARAM_SUPPORT_BILLPAY, YES);
                    product.addStringParam(PARAM_SUPPORT_CHECKS, YES);
                    product.addStringParam(PARAM_DEPOSIT_DESTINATION_ACCOUNT, YES);
                    product.addStringParam(PARAM_TRANSFER_SOURCE_ACCOUNT, YES);
                    product.addStringParam(PARAM_TRANSFER_DESTINATION_ACCOUNT, YES);
                    product.addStringParam("isBusinessAccount", Constants.FALSE);
                    product.addStringParam(PARAM_ACCOUNT_HOLDER, accountHolder);

                    Account account = TemenosUtils.copyToAccount(Account.class, product);
                    accounts.put(account.getAccountId(), account);
                    accountFinals.add(product);
                }
            }
        }

        Gson gson = new Gson();

        String gsonAccounts = gson.toJson(accounts);

        temenosUtils.insertIntoSession(SESSION_ATTRIB_ACCOUNT, gsonAccounts, request);

        result.removeDatasetById(DS_ACCOUNTS);
        Result finalResult = new Result();
        Dataset ds = new Dataset(DS_ACCOUNTS);
        ds.addAllRecords(accountFinals);
        finalResult.addDataset(ds);
        finalResult.addOpstatusParam(0);
        finalResult.addHttpStatusCodeParam(200);

        return finalResult;
    }

}