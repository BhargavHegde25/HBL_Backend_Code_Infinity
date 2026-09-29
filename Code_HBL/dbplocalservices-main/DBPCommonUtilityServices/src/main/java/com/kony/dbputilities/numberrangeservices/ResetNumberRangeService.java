package com.kony.dbputilities.numberrangeservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ResetNumberRangeService implements JavaService2 {

    @Override
    @SuppressWarnings("rawtypes")
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (callPreProcessor(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, null, URLConstants.NUMBER_RANGE_UPDATE);
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    private boolean callPreProcessor(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = validations(inputParams, dcRequest, result);

        if (status) {
            status = processInputs(inputParams, dcRequest, result);
        }

        return status;
    }

    @SuppressWarnings("rawtypes")
    private boolean validations(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;
        String objectId = (String) inputParams.get(DBPUtilitiesConstants.OBJECT_ID);

        if (!StringUtils.isNotBlank(objectId)) {
            Param validionMsg = new Param(DBPUtilitiesConstants.VALIDATION_ERROR,
                    "Please provide " + DBPUtilitiesConstants.OBJECT_ID, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            result.addParam(validionMsg);
            status = false;
        }

        return status;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean processInputs(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;
        String startVal = dcRequest.getParameter(DBPUtilitiesConstants.START_VALUE);
        if (StringUtils.isNotBlank(startVal)) {
            long temp = Long.parseLong(startVal);
            inputParams.put(DBPUtilitiesConstants.CURRENT_VALUE, String.valueOf(temp - 1));
        }

        return status;
    }
}