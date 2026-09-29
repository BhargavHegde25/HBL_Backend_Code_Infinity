package com.kony.dbputilities.extaccountservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetOtherBankAccount implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.EXT_ACCOUNTS_GET);
        }

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        StringBuilder sb = new StringBuilder();
        sb.append(DBPUtilitiesConstants.USER_ID).append(DBPUtilitiesConstants.EQUAL).append(userId);
        sb.append(DBPUtilitiesConstants.AND);
        sb.append(DBPUtilitiesConstants.SOFT_DEL).append(DBPUtilitiesConstants.EQUAL).append("0");
        sb.append(DBPUtilitiesConstants.AND);
        sb.append(DBPUtilitiesConstants.IS_SAME_BANK_ACCNT).append(DBPUtilitiesConstants.EQUAL).append("0");
        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        return true;
    }
}
