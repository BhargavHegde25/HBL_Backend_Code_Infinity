package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetIsSecurityQuestionsConfigured implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Result result = null;
        Result returnResult = new Result();
        if (preProcess(inputParams)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERSECURITYQUESTION_VIEW);
        }
        boolean isSecurityQuestionsConfigured = HelperMethods.hasRecords(result);
        returnResult.addParam(
                new Param("isSecurityQuestionConfigured", Boolean.toString(isSecurityQuestionsConfigured),
                        "string"));

        return returnResult;

    }

    private boolean preProcess(Map<String, String> inputParams) {

        String customerId = inputParams.get("Customer_id");
        if (StringUtils.isNotBlank(customerId)) {
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
            return true;
        }

        return false;
    }
}
