package com.infinity.dbx.temenos.accounts;

import java.util.ArrayList;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAllAccountsPostProcessor extends BasePostProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private final String RESULT_PARAM_ACCOUNTNAME = "AccountName";
    private final String RESULT_PARAM_ACCOUNTTYPE = "accountType";
    private final String RESULT_PARAM_TYPEID = "Type_id";
    private final String RESULT_PARAM_ACCOUNTHOLDER = "AccountHolder";

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        Dataset accountTypeDS = result.getDatasetById(DS_ACCOUNTS);
        List<Record> accountTypeRecords = accountTypeDS != null ? accountTypeDS.getAllRecords() : null;
        List<Record> accountFinals = new ArrayList<Record>();
        if (accountTypeRecords.isEmpty()) {
            alert.prepareError("Accounts empty return result").log();
            result.removeParamByName(PARAM_ERR_MSG);
            result.removeParamByName(DBP_ERROR_MSG);
            result.removeParamByName(PARAM_ERR_CODE);
            result.removeParamByName(DBP_ERR_CODE);
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(200);
            return result;
        }

        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);
        for (Record product : accountTypeRecords) {
            JsonObject accountHolderjson = new JsonObject();
            String accountHolder = CommonUtils.getParamValue(product, PARAM_ACCOUNT_HOLDER);
            accountHolderjson.addProperty(PARAM_USERNAME, accountHolder);
            accountHolderjson.addProperty(PARAM_FULLNAME, accountHolder);
            product.addStringParam(RESULT_PARAM_ACCOUNTHOLDER, accountHolderjson.toString());
            String accountType = product.getParamValueByName(PARAM_ACCOUNT_TYPE);
            accountType = temenosUtils.accountTypesMap.get(accountType);// getDBXAccountType(accountType);
            product.addParam(RESULT_PARAM_ACCOUNTNAME, accountType);
            product.addParam(RESULT_PARAM_ACCOUNTTYPE, accountType);
            if (accountType != null && !"".equalsIgnoreCase(accountType)) {
                switch (accountType) {
                    case "Savings":
                        product.addParam(RESULT_PARAM_TYPEID, "2");
                        break;
                    case "Checking":
                        product.addParam(RESULT_PARAM_TYPEID, "1");
                        break;
                    case "Deposit":
                        product.addParam(RESULT_PARAM_TYPEID, "4");
                        break;
                    case "Loan":
                        product.addParam(RESULT_PARAM_TYPEID, "6");
                        break;
                }
                product.addParam("IsOrganizationAccount", "true");
                accountFinals.add(product);
            }
        }

        result.removeDatasetById(DS_ACCOUNTS);
        Result finalResult = new Result();
        Dataset ds = new Dataset(DS_ACCOUNTS);
        ds.addAllRecords(accountFinals);
        finalResult.addDataset(ds);
        finalResult.addOpstatusParam(0);
        finalResult.addHttpStatusCodeParam(200);
        finalResult.addStringParam("success", "Record found in DBX. ");

        return finalResult;
    }

}