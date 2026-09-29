package com.infinity.dbx.temenos.accounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsByAccountIdPostProcessor extends BasePostProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        Dataset accountTypeDS = result.getDatasetById(DS_ACCOUNTS);
        HashMap<String, Account> accounts = new HashMap<String, Account>();
        List<Record> accountTypeRecords = accountTypeDS != null ? accountTypeDS.getAllRecords() : null;
        List<Record> accountFinals = new ArrayList<Record>();
        if (accountTypeRecords == null || accountTypeRecords.isEmpty()) {
            alert.prepareError("Accounts empty return result").log();
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset(DS_ACCOUNTS));
            emptyResult.addOpstatusParam(0);
            emptyResult.addHttpStatusCodeParam(200);
            return emptyResult;
        }

        alert.prepareError("Result " + result.getAllParams().toString()).log();
        String loginUserId = request.getParameter(TemenosConstants.PARAM_LOGINUSERID);
        String organizationId = request.getParameter(TemenosConstants.PARAM_ORGANIZATIONID);

        HashMap<String, String> favouriteAccounts = new HashMap<String, String>();
        HashMap<String, String> membershiIds = new HashMap<String, String>();
        HashMap<String, String> membershipNames = new HashMap<String, String>();
        HashMap<String, String> businessAccounts = new HashMap<String, String>();

        if (!"".equalsIgnoreCase(loginUserId)) {
            HashMap<String, Object> inputParams = new HashMap<String, Object>();
            HashMap<String, Object> headerParams = new HashMap<String, Object>();
            if (StringUtils.isBlank(organizationId)) {
                inputParams.put("$filter", "User_id eq " + loginUserId);
            } else {
                inputParams.put("$filter", "Organization_id eq " + organizationId);
            }
            Result readAccounts = CommonUtils.callIntegrationService(request, inputParams, headerParams,
                    TemenosConstants.SERVICE_BACKEND_CERTIFICATE,
                    TemenosConstants.OP_CUSTOMERACCOUNTS_CORECUSTOMERINFO_VIEW, false);
            Dataset getAccountsDS = readAccounts.getDatasetById("customeraccounts_corecustomerinfo_view");

            if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
                for (Record rec : getAccountsDS.getAllRecords()) {
                    favouriteAccounts.put(rec.getParamValueByName(DB_ACCOUNTID),
                            rec.getParamValueByName(DB_FAVOURITESTATUS));
                    membershiIds.put(rec.getParamValueByName(DB_ACCOUNTID),
                            rec.getParamValueByName(DB_MEMBERSHIP_ID));
                    membershipNames.put(rec.getParamValueByName(DB_ACCOUNTID),
                            rec.getParamValueByName(DB_MEMBERSHIP_NAME));
                    businessAccounts.put(rec.getParamValueByName(DB_ACCOUNTID),
                            rec.getParamValueByName("isBusiness"));
                }
            }
        }

        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);

        String membership_id = "";
        for (Record record : accountTypeRecords) {
            List<Record> products = record.getDatasetById(DS_PRODUCTS) != null
                    ? record.getDatasetById(DS_PRODUCTS).getAllRecords()
                    : null;
            for (Record product : products) {
                membership_id = CommonUtils.getParamValue(product, MEMBERSHIP_ID);
                JsonObject accountHolderjson = new JsonObject();
                String accountHolder = CommonUtils.getParamValue(product, PARAM_ACCOUNT_HOLDER);
                accountHolderjson.addProperty(PARAM_USERNAME, accountHolder);
                accountHolderjson.addProperty(PARAM_FULLNAME, accountHolder);
                product.addStringParam(PARAM_ACCOUNT_HOLDER, accountHolderjson.toString());

                String accountType = product.getParamValueByName(PARAM_ACCOUNT_TYPE);
                if(temenosUtils.accountTypesMap.containsKey(accountType)) {
                    accountType = temenosUtils.accountTypesMap.get(accountType);// getDBXAccountType(accountType);
                }
                
                String customerReference = CommonUtils.getParamValue(product, CUSTOMER_REFERENCE);
                if (!"".equalsIgnoreCase(customerReference)) {
                    product.addStringParam(NICKNAME, customerReference);
                    product.addStringParam(DISPLAY_NAME, customerReference);
                } else {
                    String accountName = product.getParamValueByName(PARAM_ACC_NAME) != ""
                            ? product.getParamValueByName(PARAM_ACC_NAME) : "";
                    if (StringUtils.isNotBlank(accountName)) {
                        product.addStringParam(NICKNAME, accountName);
                        product.addStringParam(DISPLAY_NAME, accountName);
                    }
                }
                
                if (accountType != null && !"".equalsIgnoreCase(accountType)) {
                    String principalBalance = product.getParamValueByName(PRINCIPAL_BALANCE);
                    if (ACCOUNT_TYPE_DEPOSIT.equalsIgnoreCase(accountType)) {
                        product.addStringParam(AVAILABLE_BALANCE, principalBalance);
                    }
                    if (!favouriteAccounts.isEmpty()) {
                        String favouriteStatus = favouriteAccounts.get(product.getParamValueByName("accountId")) != null
                                ? favouriteAccounts.get(product.getParamValueByName("accountId"))
                                : "0";
                        product.addStringParam(FAVOURITE_STATUS, favouriteStatus);
                    }
                    if (!membershiIds.isEmpty()) {
                        String membershipId = membershiIds.get(product.getParamValueByName("accountId")) != null
                                ? membershiIds.get(product.getParamValueByName("accountId"))
                                : "";
                        product.addStringParam(MEMBERSHIP_ID, membershipId);
                    }
                    if (!membershipNames.isEmpty()) {
                        String membershipName = membershipNames.get(product.getParamValueByName("accountId")) != null
                                ? membershipNames.get(product.getParamValueByName("accountId"))
                                : "";
                        product.addStringParam(MEMBERSHIP_NAME, membershipName);
                    }
                    if (!businessAccounts.isEmpty()) {
                        String isBusinessAccount = businessAccounts
                                .get(product.getParamValueByName("accountId")) != null
                                        ? businessAccounts.get(product.getParamValueByName("accountId"))
                                        : "false";
                        product.addStringParam(IS_BUSINESS_ACCOUNT, isBusinessAccount);
                    }

                    product.addParam(PARAM_ACCOUNT_TYPE, accountType);
                    if (!"".equalsIgnoreCase(loginUserId)) {
                        product.addStringParam(PARAM_SUPPORT_BILLPAY, YES);
                        product.addStringParam(PARAM_SUPPORT_CHECKS, YES);
                        product.addStringParam(PARAM_DEPOSIT_DESTINATION_ACCOUNT, YES);
                        product.addStringParam(PARAM_TRANSFER_SOURCE_ACCOUNT, YES);
                        product.addStringParam(PARAM_TRANSFER_DESTINATION_ACCOUNT, YES);

                        Account account = TemenosUtils.copyToAccount(Account.class, product);
                        accounts.put(account.getAccountId(), account);
                    }

                    String estatement = CommonUtils.getParamValue(product, STATEMENT);
                    if (StringUtils.isNotBlank(estatement) && StringUtils.equalsIgnoreCase(estatement, ESTATEMENT)) {
                        product.addStringParam(PARAM_ESTATEMENTENABLE, "true");
                    } else
                        product.addStringParam(PARAM_ESTATEMENTENABLE, "false");
                    accountFinals.add(product);
                }
            }
        }

        if (!accounts.isEmpty()) {
            Gson gson = new Gson();
            String gsonAccounts = gson.toJson(accounts);
            temenosUtils.insertIntoSession(SESSION_ATTRIB_ACCOUNT, gsonAccounts, request);
        }
        // temenosUtils.insertIntoSession(TemenosConstants.MEMBERSHIPID, membership_id, request);

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