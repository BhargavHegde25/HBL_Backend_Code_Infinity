package com.kony.dbputilities.p2pregservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class RegisterP2P implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.P2PREGISTRATION_CREATE);
        }
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = validateMandatoryFields(inputParams, dcRequest, result);
        if (status) {
            inputParams.put("user_id", HelperMethods.getUserIdFromSession(dcRequest));
            inputParams.put("account_id", inputParams.get("accountId"));
        }
        return status;
    }

    @SuppressWarnings("rawtypes")
    private boolean validateMandatoryFields(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String phone = (String) inputParams.get("phone");
        String email = (String) inputParams.get("email");
        String accountId = (String) inputParams.get("accountId");
        String displayName = (String) inputParams.get("displayName");
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(email) || StringUtils.isBlank(accountId)
                || StringUtils.isBlank(displayName)) {
            HelperMethods.setValidationMsg("Please pass all the mandatory fields", dcRequest, result);
            return false;
        }
        return true;
    }
}
