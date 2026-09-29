package com.kony.dbputilities.preprocessors;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.validations.NumberRangeValidations;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class CreateNumberRangePreProcessor {

    @SuppressWarnings("rawtypes")
    public boolean execute(Map paramMap, DataControllerRequest dcRequest, Result result) throws Exception {
		Log4j2Configurator.getInstance();
        boolean status = true;
        StringBuilder validationMsg = new StringBuilder();
        boolean hasError = !validateMandatoryInputParam(paramMap, validationMsg)
                || (!NumberRangeValidations.validateLengthField(paramMap, validationMsg)
                        || !NumberRangeValidations.validateStartValue(paramMap, validationMsg)
                        || !NumberRangeValidations.validateEndValue(paramMap, validationMsg)
                        || !NumberRangeValidations.validateCurrentValue(paramMap, validationMsg));
        if (hasError) {
            HelperMethods.setValidationMsg(validationMsg.toString(), dcRequest, result);
            status = false;
        }
        if (status) {
            setStartAndEndValue(paramMap);
            updateCurrentValue(paramMap);
        }

        return status;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void updateCurrentValue(Map paramMap) {
        String currentValue = (String) paramMap.get(DBPUtilitiesConstants.CURRENT_VALUE);
        if (!StringUtils.isNotBlank(currentValue)) {
            String startValue = (String) paramMap.get(DBPUtilitiesConstants.START_VALUE);
            long start = Long.parseLong(startValue);
            paramMap.put(DBPUtilitiesConstants.CURRENT_VALUE, String.valueOf(start - 1));
        } else {
            long curr = Long.parseLong(currentValue);
            paramMap.put(DBPUtilitiesConstants.CURRENT_VALUE, String.valueOf(curr - 1));
        }
    }

    @SuppressWarnings("rawtypes")
    private boolean validateMandatoryInputParam(Map paramMap, StringBuilder validationMsg) {
        String objectId = (String) paramMap.get(DBPUtilitiesConstants.OBJECT_ID);
        String objectName = (String) paramMap.get(DBPUtilitiesConstants.OBJECT_NAME);
        String length = (String) paramMap.get(DBPUtilitiesConstants.LENGTH);

        boolean valid = StringUtils.isNotBlank(length) && StringUtils.isNotBlank(objectId)
                && StringUtils.isNotBlank(objectName);
        if (!valid) {
            validationMsg.append("Mandatory fileds are missing.");
        }
        return valid;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setStartAndEndValue(Map paramMap) {
        String startValue = (String) paramMap.get(DBPUtilitiesConstants.START_VALUE);
        String endValue = (String) paramMap.get(DBPUtilitiesConstants.END_VALUE);
        String length = (String) paramMap.get(DBPUtilitiesConstants.LENGTH);

        if (!StringUtils.isNotBlank(startValue)) {
            paramMap.put(DBPUtilitiesConstants.START_VALUE, DBPUtilitiesConstants.DEFAULT_START_VALUE);
        }

        if (!StringUtils.isNotBlank(endValue)) {
            int lenIntValue = Integer.parseInt(length);
            StringBuilder retInt = new StringBuilder();
            while (lenIntValue > 0) {
                retInt.append("9");
                lenIntValue--;
            }
            paramMap.put(DBPUtilitiesConstants.END_VALUE, retInt.toString());
        }
    }
}