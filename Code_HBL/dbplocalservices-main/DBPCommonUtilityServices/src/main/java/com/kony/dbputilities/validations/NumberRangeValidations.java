package com.kony.dbputilities.validations;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;

public class NumberRangeValidations {

    private NumberRangeValidations() {
    }

    @SuppressWarnings("rawtypes")
    public static boolean validateLengthField(Map paramMap, StringBuilder validationMsg) {
        String length = (String) paramMap.get(DBPUtilitiesConstants.LENGTH);
        int lenIntValue = StringUtils.isNotBlank(length) ? Integer.parseInt(length) : 0;
        boolean valid = lenIntValue <= DBPUtilitiesConstants.NR_MAX_LENGTH;
        if (!valid) {
            validationMsg.append("Maximum allowed length value is " + DBPUtilitiesConstants.NR_MAX_LENGTH);
        }
        return valid;
    }

    @SuppressWarnings("rawtypes")
    public static boolean validateStartValue(Map paramMap, StringBuilder validationMsg) {
        boolean retValue = true;
        String startValue = (String) paramMap.get(DBPUtilitiesConstants.START_VALUE);
        if (StringUtils.isNotBlank(startValue)) {

            int length = startValue.length();
            int allowedLength = Integer.parseInt((String) paramMap.get(DBPUtilitiesConstants.LENGTH));
            if (length > allowedLength) {
                validationMsg.append("Maximum allowed length for " + DBPUtilitiesConstants.START_VALUE + " value is "
                        + allowedLength);
                retValue = false;
            }

            String endValue = (String) paramMap.get(DBPUtilitiesConstants.END_VALUE);
            if (StringUtils.isNotBlank(endValue)) {
                long start = Long.parseLong(startValue);
                long end = Long.parseLong(endValue);
                if (retValue && (start >= end)) {
                    validationMsg.append("startValue cannt be greater than end value.");
                    retValue = false;
                }
            }
        }
        return retValue;
    }

    @SuppressWarnings("rawtypes")
    public static boolean validateEndValue(Map paramMap, StringBuilder validationMsg) {
        boolean retValue = true;
        String endValue = (String) paramMap.get(DBPUtilitiesConstants.END_VALUE);
        if (StringUtils.isNotBlank(endValue)) {
            int length = endValue.length();
            int allowedLength = Integer.parseInt((String) paramMap.get(DBPUtilitiesConstants.LENGTH));
            if (length > allowedLength) {
                validationMsg.append(
                        "Maximum allowed length for " + DBPUtilitiesConstants.END_VALUE + " value is " + allowedLength);
                retValue = false;
            }
        }
        return retValue;
    }

    @SuppressWarnings("rawtypes")
    public static boolean validateCurrentValue(Map paramMap, StringBuilder validationMsg) {
        boolean retValue = true;
        String currentValue = (String) paramMap.get(DBPUtilitiesConstants.CURRENT_VALUE);
        if (StringUtils.isNotBlank(currentValue)) {
            long current = Long.parseLong(currentValue);
            if (current < 0) {
                validationMsg.append("currentValue is exhausted.");
                retValue = false;
            }

            int allowedLength = Integer.parseInt((String) paramMap.get(DBPUtilitiesConstants.LENGTH));
            String endValue = (String) paramMap.get(DBPUtilitiesConstants.END_VALUE);
            String startValue = (String) paramMap.get(DBPUtilitiesConstants.START_VALUE);

            retValue = isLengthAllowed(currentValue.length(), allowedLength, validationMsg)
                    || isWithInRange(startValue, currentValue, endValue, validationMsg);
        }
        return retValue;
    }

    private static boolean isLengthAllowed(int currLen, int allowedLen, StringBuilder msg) {
        boolean status = true;
        if (currLen > allowedLen) {
            msg.append("Maximum allowed length for " + DBPUtilitiesConstants.CURRENT_VALUE + " value is " + allowedLen);
            status = false;
        }
        return status;
    }

    private static boolean isWithInRange(String startValue, String currentValue, String endValue, StringBuilder msg) {
        boolean status = true;
        long current = Long.parseLong(currentValue);
        if (StringUtils.isNotBlank(endValue)) {
            long end = Long.parseLong(endValue);
            if (current > end) {
                msg.append("currentValue cannt be greater than endValue.");
                status = false;
            }
        }

        if (status && StringUtils.isNotBlank(startValue)) {
            long start = Long.parseLong(startValue);
            if (current < start) {
                msg.append("currentValue cannt be lesser than startValue.");
                status = false;
            }
        }
        return status;
    }
}