package com.kony.dbputilities.payeeservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetBillerCompany implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_GET);
        }

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;
        String company = "";
        if (!StringUtils.isNotBlank(company)) {
            HelperMethods.setValidationMsg("Please provide company.", dcRequest, result);
            status = false;
        }
        if (status) {
            StringBuilder sb = new StringBuilder();
            inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        }
        return status;
    }
}
