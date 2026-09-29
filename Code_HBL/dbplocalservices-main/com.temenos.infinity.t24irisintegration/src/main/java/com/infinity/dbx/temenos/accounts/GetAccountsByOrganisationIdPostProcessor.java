package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsByOrganisationIdPostProcessor extends BasePostProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        final String SERVICE_BACKEND_CERTIFICATE = "dbpRbLocalServicesdb";
        final String OP_ACCOUNTS_GET = "dbxdb_accounts_get";
        Dataset accountTypeDS = result.getDatasetById(DS_ORGINAZTION_ACCOUNTS);
        HashMap<String, Account> accounts = new HashMap<String, Account>();
        List<Record> accountTypeRecords = accountTypeDS != null ? accountTypeDS.getAllRecords() : null;
        if (accountTypeDS == null || accountTypeRecords == null) {
            alert.prepareError("Accounts empty return result").log();
            Result emptyResult = new Result();
            emptyResult.addDataset(new Dataset(DS_ORGINAZTION_ACCOUNTS));
            emptyResult.addOpstatusParam(0);
            emptyResult.addHttpStatusCodeParam(200);
            return emptyResult;
        }

        String organizationId = request.getParameter(AccountsConstants.PARAM_ORGANISATION_ID);
        HashMap<String, String> membershiIds = new HashMap<String, String>();
        HashMap<String, String> membershipNames = new HashMap<String, String>();

        if (StringUtils.isNotBlank(organizationId)) {

            HashMap<String, Object> inputParams = new HashMap<String, Object>();
            inputParams.put(AccountsConstants.FILTER, AccountsConstants.ORGANISATION_FILTER + organizationId);
            request.addRequestParam_(AccountsConstants.FILTER, AccountsConstants.ORGANISATION_FILTER + organizationId);
            Result userAccounts = CommonUtils.callIntegrationService(request, inputParams, null,
                    SERVICE_BACKEND_CERTIFICATE, OP_ACCOUNTS_GET, true);
            Dataset customerAccounts = userAccounts.getDatasetById(AccountsConstants.DS_DB_ACCOUNTS);
            if (customerAccounts != null && customerAccounts.getAllRecords().size() > 0) {
                for (Record rec : customerAccounts.getAllRecords()) {
                    membershiIds.put(rec.getParamValueByName(DB_ACCOUNTID),
                            rec.getParamValueByName(DB_MEMBERSHIP_ID));
                    membershipNames.put(rec.getParamValueByName(DB_ACCOUNTID),
                            rec.getParamValueByName(DB_MEMBERSHIP_NAME));
                }
            }
        }

        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);

        for (Record record : accountTypeRecords) {
            JsonObject accountHolderjson = new JsonObject();
            String accountHolder = CommonUtils.getParamValue(record, PARAM_ACCOUNT_HOLDER);
            accountHolderjson.addProperty(PARAM_USERNAME, accountHolder);
            accountHolderjson.addProperty(PARAM_FULLNAME, accountHolder);
            record.addStringParam(PARAM_ACCOUNT_HOLDER, accountHolderjson.toString());

            String accountType = record.getParamValueByName(PARAM_ACCOUNT_TYPE);
            accountType = temenosUtils.accountTypesMap.get(accountType);// getDBXAccountType(accountType);
            if (accountType != null && !"".equalsIgnoreCase(accountType)) {
                String principalBalance = record.getParamValueByName(PRINCIPAL_BALANCE);
                if (ACCOUNT_TYPE_DEPOSIT.equalsIgnoreCase(accountType)) {
                    record.addStringParam(AVAILABLE_BALANCE, principalBalance);
                }
                if (accountType != null && !"".equalsIgnoreCase(accountType)) {
                    switch (accountType) {
                        case ACCOUNT_TYPE_SAVINGS:
                            record.addParam(KONY_DBX_ACCOUNT_TYPE, DBX_ACCOUNT_TYPE_2);
                            break;
                        case ACCOUNT_TYPE_CHECKING:
                            record.addParam(KONY_DBX_ACCOUNT_TYPE, DBX_ACCOUNT_TYPE_1);
                            break;
                        case ACCOUNT_TYPE_DEPOSIT:
                            record.addParam(KONY_DBX_ACCOUNT_TYPE, DBX_ACCOUNT_TYPE_4);
                            break;
                        case ACCOUNT_TYPE_LOAN:
                            record.addParam(KONY_DBX_ACCOUNT_TYPE, AccountsConstants.DBX_ACCOUNT_TYPE_6);
                            break;
                    }
                    record.addParam(AccountsConstants.PARAM_IS_ORGANIZATION_ACCOUNT, Constants.TRUE);
                }
                record.addParam(PARAM_ACCOUNT_TYPE, accountType);
                record.addParam(new Param(AccountsConstants.PARAM_STATUS_DESC, AccountsConstants.STATUS_ACTIVE));
                record.addParam(new Param(AccountsConstants.PARAM_ORGANISATION_ID,
                        request.getParameter(AccountsConstants.PARAM_ORGANISATION_ID)));
                record.addStringParam(PARAM_SUPPORT_BILLPAY, YES);
                record.addStringParam(PARAM_SUPPORT_CHECKS, YES);
                record.addStringParam(PARAM_DEPOSIT_DESTINATION_ACCOUNT, YES);
                record.addStringParam(PARAM_TRANSFER_SOURCE_ACCOUNT, YES);
                record.addStringParam(PARAM_TRANSFER_DESTINATION_ACCOUNT, YES);

                String membershipId = membershiIds.get(record.getParamValueByName("accountId")) != null
                        ? membershiIds.get(record.getParamValueByName("accountId"))
                        : "";
                String membershipName = membershipNames.get(record.getParamValueByName("accountId")) != null
                        ? membershipNames.get(record.getParamValueByName("accountId"))
                        : "";
                if (StringUtils.isNotBlank(membershipId)) {
                    record.addStringParam(MEMBERSHIP_ID, membershipId);
                }
                if (StringUtils.isNotBlank(membershipName)) {
                    record.addStringParam(MEMBERSHIP_NAME, membershipName);
                }
            }
        }
        return result;
    }

}