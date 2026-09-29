package com.kony.dbputilities.preprocessors;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.validations.NumberRangeValidations;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class UpdateNumberRangePreProcessor {

    @SuppressWarnings("rawtypes")
    public boolean execute(Map paramMap, DataControllerRequest dcRequest, Result result) throws Exception {
		Log4j2Configurator.getInstance();
        boolean status = true;
        verifyAndSetCurrentValue(paramMap, dcRequest);

        StringBuilder validationMsg = new StringBuilder();
        if (!isObjectIdFound(dcRequest, validationMsg)
                || !NumberRangeValidations.validateCurrentValue(paramMap, validationMsg)) {
            HelperMethods.setValidationMsg(validationMsg.toString(), dcRequest, result);
            status = false;
        }
        return status;
    }

    private boolean isObjectIdFound(DataControllerRequest dcRequest, StringBuilder validationMsg) {
        String count = dcRequest.getParameter(DBPUtilitiesConstants.RECORD_COUNT);
        if ((null != count) && !DBPUtilitiesConstants.ZERO.equals(count)) {
            return true;
        }
        String objectId = dcRequest.getParameter(DBPUtilitiesConstants.OBJECT_ID);
        validationMsg.append("objectId " + objectId + " is not present in db.");
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void verifyAndSetCurrentValue(Map paramMap, DataControllerRequest dcRequest) {
        String currentValue = dcRequest.getParameter(DBPUtilitiesConstants.CURRENT_VALUE);
        if (StringUtils.isNotBlank(currentValue)) {
            paramMap.put(DBPUtilitiesConstants.CURRENT_VALUE, currentValue);
        }
    }
}