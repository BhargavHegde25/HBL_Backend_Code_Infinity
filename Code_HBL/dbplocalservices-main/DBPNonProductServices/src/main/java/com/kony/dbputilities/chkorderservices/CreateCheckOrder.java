package com.kony.dbputilities.chkorderservices;

import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateCheckOrder implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CHECK_ORDER_CREATE);
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        Record account = getAccount(dcRequest, (String) inputParams.get("accountID"));
        inputParams.put("account_id", inputParams.get("accountID"));
        inputParams.put("accountName", HelperMethods.getFieldValue(account, "accountName"));
        inputParams.put("accountNickName", HelperMethods.getFieldValue(account, "nickName"));
        inputParams.put("postBoxNumber", inputParams.get("postboxNumber"));
        inputParams.put("zipCode", inputParams.get("zipcode"));
        inputParams.put("orderTime", HelperMethods.getCurrentTimeStamp());
        inputParams.put("status", "Ordered");
        removeUnrequiredInput(inputParams);
        return true;
    }

    private Record getAccount(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        Result account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        return account.getAllDatasets().get(0).getRecord(0);
    }

    @SuppressWarnings("rawtypes")
    private void removeUnrequiredInput(Map inputParams) {
        inputParams.remove("accountID");
        inputParams.remove("postboxNumber");
        inputParams.remove("zipcode");
    }
}
