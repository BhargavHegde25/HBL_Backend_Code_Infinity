package com.kony.dbputilities.preprocessors;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class DeleteNumberRangePreProcessor {

    @SuppressWarnings("rawtypes")
    public boolean execute(Map paramMap, DataControllerRequest dcRequest, Result result) {
		Log4j2Configurator.getInstance();
        boolean status = true;
        StringBuilder validationMsg = new StringBuilder();
        if (!isObjectIdFound(dcRequest, validationMsg)
                || !validateMandatoryInputParam(paramMap, dcRequest, validationMsg)
                || !validateCurrentValue(paramMap, dcRequest, validationMsg)) {
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

    @SuppressWarnings("rawtypes")
    private boolean validateCurrentValue(Map paramMap, DataControllerRequest dcRequest, StringBuilder validationMsg) {
        boolean retValue = true;
        String currentValue = dcRequest.getParameter(DBPUtilitiesConstants.CURRENT_VALUE);
        String startValue = dcRequest.getParameter(DBPUtilitiesConstants.START_VALUE);
        long curr = Long.parseLong(currentValue);
        long start = Long.parseLong(startValue);
        if (curr > start) {
            validationMsg.append("Current Value is in use and can not be deleted.");
            retValue = false;
        }
        return retValue;
    }

    @SuppressWarnings("rawtypes")
    private boolean validateMandatoryInputParam(Map paramMap, DataControllerRequest dcRequest,
            StringBuilder validationMsg) {
        boolean retValue = true;
        String currentValue = dcRequest.getParameter(DBPUtilitiesConstants.CURRENT_VALUE);
        String objectId = (String) paramMap.get(DBPUtilitiesConstants.OBJECT_ID);

        if (!StringUtils.isNotBlank(currentValue)) {
            validationMsg.append("Could not verify " + DBPUtilitiesConstants.CURRENT_VALUE + ".");
            retValue = false;
        }
        if (!StringUtils.isNotBlank(objectId)) {
            validationMsg.append("Object id is either not provided or not present in db.");
            retValue = false;
        }
        return retValue;
    }
}