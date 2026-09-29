package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class VerifyExternalBankAccount implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        process(inputParams, dcRequest, result);
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void process(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String username = (String) inputParams.get("userName");
        String password = (String) inputParams.get("password");
        Param response = new Param(DBPUtilitiesConstants.RESULT_MSG, "Failed", DBPUtilitiesConstants.STRING_TYPE);
        
        result.addParam(response);
    }
}