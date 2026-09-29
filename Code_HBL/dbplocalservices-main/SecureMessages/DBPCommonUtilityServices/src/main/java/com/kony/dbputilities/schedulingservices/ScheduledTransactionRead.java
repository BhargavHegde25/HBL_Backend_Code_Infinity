package com.kony.dbputilities.schedulingservices;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ScheduledTransactionRead implements JavaService2 {
    @Override
    public Object invoke(String paramString, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws HttpCallException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (callPreProcessor(inputParams, dcRequest, result)) {
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                    dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION_VALUE));
            result = HelperMethods.callApi(dcRequest, convertMap(inputParams), headerParams,
                    URLConstants.SCHEDULEDTRANS_GET);
        }
        return result;
    }

    private static Map<String, Object> convertMap(Map<String, String> map) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Entry<String, String> entry : map.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }

    private boolean callPreProcessor(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        ScheduledTransactionPreProcessor preprocessor = new ScheduledTransactionPreProcessor();
        return preprocessor.execute(inputParams, dcRequest, result);
    }
}
