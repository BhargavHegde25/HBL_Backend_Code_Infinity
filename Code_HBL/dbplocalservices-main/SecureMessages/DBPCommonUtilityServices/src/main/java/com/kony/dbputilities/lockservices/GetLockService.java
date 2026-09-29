package com.kony.dbputilities.lockservices;

import java.util.Map;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.postprocessors.ReadLockPostProcessor;
import com.kony.dbputilities.preprocessors.ReadLockPreProcessor;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetLockService implements JavaService2 {

    @Override
    @SuppressWarnings("rawtypes")
    public Object invoke(String paramString, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (callPreProcessor(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, null, URLConstants.LOCK_GET);
        }

        if (DBPUtilitiesConstants.ZERO.equals(result.getParamByName(DBPConstants.FABRIC_OPSTATUS_KEY).getValue())) {
            callPostProcessor(result, dcRequest);
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    private boolean callPreProcessor(Map inputParams, DataControllerRequest dcRequest, Result result) {
        ReadLockPreProcessor preprocessor = new ReadLockPreProcessor();
        return preprocessor.execute(inputParams, dcRequest, result);
    }

    private void callPostProcessor(Result result, DataControllerRequest dcRequest) {
        ReadLockPostProcessor postProcessor = new ReadLockPostProcessor();
        postProcessor.execute(result, dcRequest);
    }
}
