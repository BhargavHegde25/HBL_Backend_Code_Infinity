package com.infinity.dbx.temenos.accounts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.InfinityConstants;

public class GetAccountDetailsByAccountIdPostProcessor extends BasePostProcessor
        implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        String loginUserId = null;
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            loginUserId = HelperMethods.getCustomerIdFromSession(request);
        } else {
            loginUserId = (String) request.getParameter(InfinityConstants.id);
        }
        
        Dataset accountTypeDS = result.getDatasetById(DS_ACCOUNTS);
        HashMap<String, Account> accounts = new HashMap<String, Account>();
        List<Record> accountTypeRecords = accountTypeDS != null ? accountTypeDS.getAllRecords() : null;
        List<Record> accountFinals = new ArrayList<Record>();
        String newAccounts = request.getParameter("newAccounts");
        Map<String, String> newAcntCoreCustomerId = new HashMap<>();
        HashSet<String> newAccountCoreCustomerIdList = new HashSet<>();
        if (StringUtils.isNotBlank(newAccounts)) {
            String[] accountsInfo = newAccounts.split("\\|");
            for (int i = 0; i < accountsInfo.length; i++) {
                String[] singleAccountInfo = accountsInfo[i].split(":");
                newAcntCoreCustomerId.put(singleAccountInfo[1], singleAccountInfo[0]);
                newAccountCoreCustomerIdList.add(singleAccountInfo[0]);
            }
        }
        Map<String, Set> coreCustomerActions = new HashMap<String, Set>();
        
        if(StringUtils.isNotBlank(loginUserId)) {
            coreCustomerActions = fetchDefaultAccountActions(request, loginUserId, newAccountCoreCustomerIdList);
        }
        if (accountTypeRecords == null || accountTypeRecords.isEmpty()) {
            alert.prepareError("Accounts empty return result").log();
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset(DS_ACCOUNTS));
            emptyResult.addOpstatusParam(0);
            emptyResult.addHttpStatusCodeParam(200);
            return emptyResult;
        }

        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);

        for (Record record : accountTypeRecords) {
            List<Record> products = record.getDatasetById(DS_PRODUCTS) != null
                    ? record.getDatasetById(DS_PRODUCTS).getAllRecords()
                    : null;
            for (Record product : products) {
                JsonObject accountHolderjson = new JsonObject();
                String accountId = CommonUtils.getParamValue(product, "accountId");
                try {
                    if (newAcntCoreCustomerId.containsKey(accountId)) {
                        product.addStringParam("actions",
                                coreCustomerActions.get(newAcntCoreCustomerId.get(accountId)).toString());
                        product.addParam("isNew", "true");
                    }
                    else {
                        product.addParam("isNew", "false");
                    }
                } catch (Exception e) {
                    alert.prepareError("Exception occured while fetching the corecustomer account level default actions"
                            + e.getMessage()).log();
                }
                String accountHolder = CommonUtils.getParamValue(product, PARAM_ACCOUNT_HOLDER);
                accountHolderjson.addProperty(PARAM_USERNAME, accountHolder);
                accountHolderjson.addProperty(PARAM_FULLNAME, accountHolder);
                product.addStringParam(PARAM_ACCOUNT_HOLDER, accountHolderjson.toString());

                if (StringUtils.isNotBlank(product.getParamValueByName("portfolioId")))
                    product.addStringParam("isPortFolioAccount", Boolean.TRUE.toString());
                else
                    product.addStringParam("isPortFolioAccount", Boolean.FALSE.toString());

                String accountType = product.getParamValueByName(PARAM_ACCOUNT_TYPE);
                if (temenosUtils.accountTypesMap.containsKey(accountType)) {
                    accountType = temenosUtils.accountTypesMap.get(accountType);// getDBXAccountType(accountType);
                }

                product.addStringParam("productGroup", accountType);
                String customerReference = CommonUtils.getParamValue(product, CUSTOMER_REFERENCE);
                if (!"".equalsIgnoreCase(customerReference)) {
                    product.addStringParam(NICKNAME, customerReference);
                    product.addStringParam(DISPLAY_NAME, customerReference);
                } else {
                    String accountName = product.getParamValueByName(PARAM_ACC_NAME) != ""
                            ? product.getParamValueByName(PARAM_ACC_NAME)
                            : "";
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
        if (!accounts.isEmpty() && StringUtils.isNotBlank(loginUserId)) {
            Gson gson = new Gson();
            String gsonAccounts = gson.toJson(accounts);
            temenosUtils.insertIntoSession(SESSION_ATTRIB_ACCOUNT, gsonAccounts, request);
        }
        result.removeDatasetById(DS_ACCOUNTS);
        Result finalResult = new Result();
        Dataset ds = new Dataset(DS_ACCOUNTS);
        ds.addAllRecords(accountFinals);
        finalResult.addDataset(ds);
        finalResult.addOpstatusParam(0);
        finalResult.addHttpStatusCodeParam(200);
        return finalResult;
    }

    @SuppressWarnings("rawtypes")
    public Map<String, Set> fetchDefaultAccountActions(DataControllerRequest request, String customerId,
            HashSet<String> coreCustomerIdList) {
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Set> coreCustomerActions = new HashMap<>();
        try {
            for (String coreCustomerId : coreCustomerIdList) {
                inputParams = new HashMap<>();
                inputParams.put("_userId", customerId);
                inputParams.put("_coreCustomerId", coreCustomerId);
                request.addRequestParam_("_userId", customerId);
                request.addRequestParam_("_coreCustomerId", coreCustomerId);
                Result defaultactions = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
                        "dbpRbLocalServicesdb", "dbxdb_fetch_default_account_actions_proc", true);
                String actions = defaultactions.getDatasetById("records").getAllRecords().get(0)
                        .getParamValueByName("defaultAccountActions");
                coreCustomerActions.put(coreCustomerId, new HashSet<>(Arrays.asList(StringUtils.split(actions, ","))));
            }
            return coreCustomerActions;
        } catch (Exception e) {
        	alert.prepareError(e.toString()).log();
        }
        return null;
    }
}
