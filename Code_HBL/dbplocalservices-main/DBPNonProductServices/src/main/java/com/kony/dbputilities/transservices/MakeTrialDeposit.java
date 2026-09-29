package com.kony.dbputilities.transservices;

import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class MakeTrialDeposit implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            process(inputParams, dcRequest, result);
        }
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void process(Map inputParams, DataControllerRequest dcRequest, Result result) throws HttpCallException {
        double amount = 0.2;
        for (int i = 0; i <= 1; i++) {
            inputParams.put("amount", String.valueOf(amount));
            HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_CREATE);
            amount = amount + 0.21;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String extAccount = (String) inputParams.get("ExternalAccountNumber");
        inputParams.put("toExternalAccountNumber", extAccount);
        inputParams.put("Type_id", "3");
        inputParams.put("notes", "For Verification");
        inputParams.put("transactionDate", HelperMethods.getCurrentTimeStamp());
        inputParams.put("createdDate", HelperMethods.getCurrentTimeStamp());
        inputParams.put("IsScheduled", "true");
        return true;
    }
}
