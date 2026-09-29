package com.kony.dbputilities.p2pregservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class RegisterP2PStatus implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.P2PREGISTRATION_GET);
        }
        result = postProcess(result);
        return result;
    }

    private Result postProcess(Result result) {
        Result response = new Result();
        Param isP2pRegistered = new Param("isP2PRegistered", "false", DBPUtilitiesConstants.STRING_TYPE);
        if (HelperMethods.hasRecords(result)) {
            isP2pRegistered.setValue("true");
        }
        response.addParam(isP2pRegistered);
        return response;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String filter = "user_id" + DBPUtilitiesConstants.EQUAL + userId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        return true;
    }
}
