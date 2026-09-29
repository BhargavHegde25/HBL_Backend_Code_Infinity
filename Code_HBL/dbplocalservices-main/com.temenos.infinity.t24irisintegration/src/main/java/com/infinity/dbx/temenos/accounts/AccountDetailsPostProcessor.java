package com.infinity.dbx.temenos.accounts;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class AccountDetailsPostProcessor extends BasePostProcessor implements AccountsConstants {

    @SuppressWarnings("null")
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        String[] interestRate = CommonUtils.getParamValue(result, DIVIDEND_RATE).split("%");
        result.getParamByName(DIVIDEND_RATE).setValue(interestRate[0]);
        
        String dividendPaidYTD = CommonUtils.getParamValue(result, DIVIDENDPAIDYTD);
        String dividendLastPaidAmount = CommonUtils.getParamValue(result, DIVIDEND_LAST_PAID_AMOUNT);
        
		if (StringUtils.isNotBlank(dividendPaidYTD))
			result.getParamByName(DIVIDENDPAIDYTD).setValue(dividendPaidYTD.split("\\|")[0]);
		if (StringUtils.isNotBlank(dividendPaidYTD))
			result.getParamByName(DIVIDEND_LAST_PAID_AMOUNT).setValue(dividendLastPaidAmount.split("\\|")[0]);
        
        
        JsonObject accountHolderjson = new JsonObject();
        String accountHolder = CommonUtils.getParamValue(result, PARAM_ACCOUNT_HOLDER);
        accountHolderjson.addProperty(PARAM_USERNAME, accountHolder);
        accountHolderjson.addProperty(PARAM_FULLNAME, accountHolder);
        result.addParam(new Param(PARAM_ACCOUNT_HOLDER, accountHolderjson.toString(), Constants.PARAM_DATATYPE_STRING));

        // Add joint holders in result
        JsonArray accountJointHolders = new JsonArray();
        Dataset jointOwnerDS = result.getDatasetById(PARAM_ACCOUNT_JOINT_OWNER) != null
                ? result.getDatasetById(PARAM_ACCOUNT_JOINT_OWNER) : null;
        List<Record> jointOwnerRecords = jointOwnerDS != null ? jointOwnerDS.getAllRecords() : null;
        if (jointOwnerRecords != null) {
            for (Record record : jointOwnerRecords) {
                String CustomerId = record.getParamValueByName(AccountsConstants.CUSTOMER_ID) != ""
                        ? record.getParamValueByName(AccountsConstants.CUSTOMER_ID) : "";
                String CustomerName = record.getParamValueByName(CUSTOMER_NAME) != ""
                        ? record.getParamValueByName(CUSTOMER_NAME) : "";
                JsonObject accountJointHolder = new JsonObject();
                if (StringUtils.isNotEmpty(CustomerName)) {
                    accountJointHolder.addProperty(PARAM_USERNAME, CustomerName);
                    accountJointHolder.addProperty(PARAM_FULLNAME, CustomerName);
                }
                if (StringUtils.isNotEmpty(CustomerId)) {
                    accountJointHolder.addProperty(AccountsConstants.CUSTOMER_ID, CustomerId);
                }
                accountJointHolders.add(accountJointHolder);
            }
            result.addParam(new Param(PARAM_ACCOUNT_JOINT_HOLDER, accountJointHolders.toString(),
                    Constants.PARAM_DATATYPE_STRING));
        }

        return result;
    }
}
