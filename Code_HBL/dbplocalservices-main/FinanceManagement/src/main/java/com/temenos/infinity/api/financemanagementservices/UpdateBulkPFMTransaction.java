package com.temenos.infinity.api.financemanagementservices;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateBulkPFMTransaction implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        process(dcRequest, inputParams);
        return new Result();
    }

    private void process(DataControllerRequest dcRequest, Map<String, String> inputParams) throws HttpCallException {
        JsonArray transactionList = getJsonArray(inputParams.get("pfmtransactionlist"));
        Map<String, String> input = null;
        JsonObject transaction = null;
        for (int i = 0; i < transactionList.size(); i++) {
            input = new HashMap<>();
            transaction = (JsonObject) transactionList.get(i);
            input.put("id", transaction.get("transactionId").getAsString());
            if (null != transaction.get("categoryId")) {
                input.put("categoryId", transaction.get("categoryId").getAsString());
            }
            if (null != transaction.get("isAnalyzed")) {
                input.put("isAnalyzed", transaction.get("isAnalyzed").getAsString());
            }
            if (null != transaction.get("isMappedToMerchant")) {
                input.put("isMappedToMerchant", transaction.get("isMappedToMerchant").getAsString());
            }
            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PFM_TRANSACTIONS_UPDATE);
        }
    }

    private JsonArray getJsonArray(String transactionList) {
        JsonParser jsonParser = new JsonParser();
        return (JsonArray) jsonParser.parse(transactionList);
    }
}
