package com.kony.dbputilities.numberrangeservices;

import java.util.Map;

import com.kony.dbputilities.preprocessors.CreateNumberRangePreProcessor;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateNumberRangeService implements JavaService2 {

    @Override
    @SuppressWarnings("rawtypes")
    public Object invoke(String paramString, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (callPreProcessor(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.NUMBER_RANGE_CREATE);
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    private boolean callPreProcessor(Map inputParams, DataControllerRequest dcRequest, Result result) {
        CreateNumberRangePreProcessor preprocessor = new CreateNumberRangePreProcessor();
        return preprocessor.execute(inputParams, dcRequest, result);
    }
}