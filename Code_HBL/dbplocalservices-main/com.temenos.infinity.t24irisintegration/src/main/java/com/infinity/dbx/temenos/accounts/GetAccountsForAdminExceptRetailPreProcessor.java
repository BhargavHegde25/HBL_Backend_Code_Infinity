package com.infinity.dbx.temenos.accounts;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsForAdminExceptRetailPreProcessor extends TemenosBasePreProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result)
            throws Exception {

        super.execute(params, request);
        String username = params.get(AccountsConstants.PARAM_USERNAME) != null
                ? params.get(AccountsConstants.PARAM_USERNAME).toString()
                : "";

        String customerId = params.get(USER_ID) != null ? params.get(USER_ID).toString() : "";
        if (StringUtils.isBlank(customerId)) {
            customerId = request.getParameter(USER_ID) != null ? request.getParameter(USER_ID).toString() : "";
        }
        if (StringUtils.isBlank(username) && StringUtils.isBlank(customerId)) {
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(200);
            result.addErrMsgParam("Misssing input params");
            return Boolean.FALSE;
        }
        Map<String, String> typeIdMap = new HashMap<>();
        if (StringUtils.isBlank(customerId)) {
            typeIdMap = getCustomerTypeAndId(request, username);
            customerId = typeIdMap.get(Constants.KONY_DBX_USER_LOWERCASE_ID);
        }
        if (StringUtils.isBlank(customerId)) {
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(200);
            result.addErrMsgParam("Customer Doesn't exist for the provided username");
            return Boolean.FALSE;
        }
        /*
         * In case of Business and Combined User Read accounts from DB Append accountId to new string with space
         * separated
         */

        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put("$filter", "Customer_id eq " + customerId);
        request.addRequestParam_("$filter", "Customer_id eq " + customerId);
        Result userAccounts = CommonUtils.callIntegrationService(request, inputParams, null,
                SERVICE_BACKEND_CERTIFICATE, OPERATION_CUSTOMER_ACCOUNTS_GET, true);
        String accounts = "";
        for (Record rec : userAccounts.getDatasetById(AccountsConstants.DS_CUSTOMERACCOUNTS).getAllRecords()) {
            accounts += rec.getParamValueByName(AccountsConstants.DB_ACCOUNTID);
            accounts += " ";
        }
        params.put(AccountsConstants.DB_ACCOUNTID,
                URLEncoder.encode(accounts.substring(0, accounts.length() - 1), "UTF-8"));
        alert.prepareError("Final Params for getAccounts for Admin " + params.toString()).log();
        request.addRequestParam_(PARAM_LOGINUSERID, customerId);

        return Boolean.TRUE;
    }

    public Map<String, String> getCustomerTypeAndId(DataControllerRequest request, String username) {

        String customerId = "";
        String customerType_id = "";
        Map<String, String> typeIdMap = new HashMap<String, String>();

        try {

            String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, username);
            HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
            HashMap<String, Object> svcParams = new HashMap<String, Object>();

            svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
            Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
                    Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
            Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
            if (null != customerDataset) {

                customerId = customerDataset.getRecord(0).getParamValueByName(Constants.KONY_DBX_USER_LOWERCASE_ID);
                customerType_id = customerDataset.getRecord(0).getParamValueByName(AccountsConstants.CUSTOMER_TYPE_ID);

            }

        } catch (Exception e) {

            alert.prepareError("Error while retrieving backendId for  " + username).log();
        }

        typeIdMap.put(Constants.KONY_DBX_USER_LOWERCASE_ID, customerId);
        typeIdMap.put(AccountsConstants.CUSTOMER_TYPE_ID, customerType_id);
        return typeIdMap;
    }

}