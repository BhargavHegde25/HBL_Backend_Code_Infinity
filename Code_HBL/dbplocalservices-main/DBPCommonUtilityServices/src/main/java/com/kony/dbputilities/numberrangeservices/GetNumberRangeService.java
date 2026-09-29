package com.kony.dbputilities.numberrangeservices;

import java.util.Map;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.postprocessors.GetNumberRangePostProcessor;
import com.kony.dbputilities.preprocessors.GetNumberRangePreProcessor;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetNumberRangeService implements JavaService2 {

    @Override
    @SuppressWarnings("rawtypes")
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcReponse) throws Exception {

        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (callPreProcessor(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.NUMBER_RANGE_GET);
        }

        if (!hasError(result)) {
            callPostProcessor(result, dcRequest);
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    private boolean callPreProcessor(Map inputParams, DataControllerRequest dcRequest, Result result) throws Exception {
        GetNumberRangePreProcessor preprocessor = new GetNumberRangePreProcessor();
        return preprocessor.execute(inputParams, dcRequest, result);
    }

    private void callPostProcessor(Result result, DataControllerRequest dcRequest) throws Exception {
        GetNumberRangePostProcessor postProcessor = new GetNumberRangePostProcessor();
        postProcessor.execute(result, dcRequest);
    }

    private boolean hasError(Result result) {
        boolean preprocessorFailed = null != result.getParamByName(DBPUtilitiesConstants.VALIDATION_ERROR);
        boolean apicallFailed = (null != result.getParamByName(DBPConstants.FABRIC_OPSTATUS_KEY))
                ? !(DBPUtilitiesConstants.ZERO
                        .equals(result.getParamByName(DBPConstants.FABRIC_OPSTATUS_KEY).getValue()))
                : false;
        return preprocessorFailed || apicallFailed;
    }
}