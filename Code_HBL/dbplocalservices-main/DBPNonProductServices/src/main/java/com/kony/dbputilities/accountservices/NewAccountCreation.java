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

public class NewAccountCreation implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_CREATE);
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;
        String accountId = (String) inputParams.get(DBPUtilitiesConstants.ACCOUNT_ID);
        String typeId = (String) inputParams.get(DBPUtilitiesConstants.TYPE_ID);
        String username = (String) inputParams.get(DBPUtilitiesConstants.USER_NAME_A);
        String accountHolder = (String) inputParams.get(DBPUtilitiesConstants.ACCOUNT_HOLDER);

        if (!StringUtils.isNotBlank(accountId) || !StringUtils.isNotBlank(typeId) || !StringUtils.isNotBlank(username)
                || !StringUtils.isNotBlank(accountHolder)) {
            HelperMethods.setValidationMsg(
                    "Please provide " + DBPUtilitiesConstants.ACCOUNT_ID + ", " + DBPUtilitiesConstants.USER_NAME_A
                            + ", " + DBPUtilitiesConstants.TYPE_ID + " and " + DBPUtilitiesConstants.ACCOUNT_HOLDER,
                    dcRequest, result);
            status = false;
        }

        return status;
    }
}
