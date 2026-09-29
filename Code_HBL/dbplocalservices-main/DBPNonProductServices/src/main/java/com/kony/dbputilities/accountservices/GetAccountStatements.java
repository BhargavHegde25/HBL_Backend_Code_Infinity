package com.kony.dbputilities.accountservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountStatements implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_STATEMENT_GET);
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;
        String accountId = (String) inputParams.get("accountID");

        if (StringUtils.isBlank(accountId)) {
            HelperMethods.setValidationMsg("Please provide accountID", dcRequest, result);
            status = false;
        }

        if (status) {
            StringBuilder filter = new StringBuilder();
            filter.append("Account_id").append(DBPUtilitiesConstants.EQUAL).append(accountId);
            inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
        }

        return status;
    }
}