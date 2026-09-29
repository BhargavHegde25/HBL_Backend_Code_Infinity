package com.kony.eum.dbputilities.mfa;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class VerifyPreLoginOTP implements JavaService2 {
	
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        Result result = new Result();
       
        PreLoginUtil mfaUtil = null;

        mfaUtil = new PreLoginUtil(dcRequest);
        if (StringUtils.isNotBlank(dcRequest.getParameter("userId"))) {
            String userId = dcRequest.getParameter("userId");
            mfaUtil.setUserID(userId);
        }

        if (!mfaUtil.isValidMFA()) {
            ErrorCodeEnum.ERR_10550.setErrorCode(result);
            return result;
        }

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String otpBypass = URLFinder.getOTPByPassState(dcRequest);
        GetOTP getOTP = new GetOTP();
        result = (Result) getOTP.invoke(methodID, inputArray, dcRequest, dcResponse);
        result = postProcess(inputParams, dcRequest, result, otpBypass, mfaUtil);
        return result;
    }

    private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
            String otpBypass, PreLoginUtil mfaUtil) throws HttpCallException {
        Result retRes = new Result();

        if (HelperMethods.hasRecords(result)) {
            if (!isAttemptAllowed(dcRequest, result, mfaUtil)) {
                setVerifyAttributes(dcRequest, result, retRes, mfaUtil);
            } else if (isOTPExpired(dcRequest, result, mfaUtil)) {
                ErrorCodeEnum.ERR_10551.setErrorCode(retRes);
                retRes.addParam(new Param("isOTPExpired", "true", "String"));
                updateAttemptCount(dcRequest, result, retRes, mfaUtil);
                setVerifyAttributes(dcRequest, result, retRes, mfaUtil);
                int retryCount = Integer.parseInt(HelperMethods.getFieldValue(result, "NumberOfRetries"));
                retRes = mfaUtil.addRequestAttributes(retRes, retryCount - 1);
            } else if (validateOTP(dcRequest, result)) {
                HelperMethods.setSuccessMsg("Secure Access Code has been sent", retRes);
                retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "true", "String"));
                mfaUtil.createEntryInStateChecker();
                deleteOTP(dcRequest, result);
            } else {
                retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "false", "String"));
                ErrorCodeEnum.ERR_10552.setErrorCode(retRes);
                updateAttemptCount(dcRequest, result, retRes, mfaUtil);
                return mfaUtil.addVerifyAttributes(result, retRes);
            }
        } else {
            ErrorCodeEnum.ERR_10553.setErrorCode(retRes);
        }

        return retRes;
    }

    private void deleteOTP(DataControllerRequest dcRequest, Result result) throws HttpCallException {
        HashMap<String, String> input = new HashMap<>();
        input.put("securityKey", HelperMethods.getFieldValue(result, "securityKey"));
        HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_DELETE);
    }

    private void updateAttemptCount(DataControllerRequest dcRequest, Result result, Result retRes, PreLoginUtil mfaUtil)
            throws HttpCallException {
        int InvalidAttempt = 0;
        String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
        if (StringUtils.isNotBlank(str)) {
            InvalidAttempt = Integer.parseInt(str);
        }

        Map<String, String> input = new HashMap<>();
        input.put("securityKey", HelperMethods.getFieldValue(result, "securityKey"));
        input.put("InvalidAttempt", String.valueOf(InvalidAttempt + 1));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_UPDATE);

        int allowedAttempt = mfaUtil.maxFailedAttemptsAllowed();
        retRes.addParam(new Param("maxFailedAttemptsAllowed", allowedAttempt + "", DBPUtilitiesConstants.STRING_TYPE));
        retRes.addParam(new Param("remainingFailedAttempts", (allowedAttempt - (InvalidAttempt + 1)) + "",
                DBPUtilitiesConstants.STRING_TYPE));
        retRes.addParam(new Param("failedAttempts", (InvalidAttempt + 1) + "", DBPUtilitiesConstants.STRING_TYPE));
    }

    private void setVerifyAttributes(DataControllerRequest dcRequest, Result result, Result retRes,
            PreLoginUtil mfaUtil) throws HttpCallException {
        int InvalidAttempt = 0;
        String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
        if (StringUtils.isNotBlank(str)) {
            InvalidAttempt = Integer.parseInt(str);
        }

        int allowedAttempt = mfaUtil.maxFailedAttemptsAllowed();
        int attemptsLeft = (allowedAttempt - (InvalidAttempt + 1));
        attemptsLeft = attemptsLeft < 0 ? 0 : attemptsLeft;
        retRes.addParam(new Param("maxFailedAttemptsAllowed", allowedAttempt + "", DBPUtilitiesConstants.STRING_TYPE));
        retRes.addParam(new Param("remainingFailedAttempts", attemptsLeft + "", DBPUtilitiesConstants.STRING_TYPE));
        retRes.addParam(new Param("failedAttempts", (InvalidAttempt + 1) + "", DBPUtilitiesConstants.STRING_TYPE));

        if (attemptsLeft <= 0) {
            String lockoutTime = mfaUtil.getLockoutTime();
            retRes.addParam(new Param("remainingFailedAttempts", "0", DBPUtilitiesConstants.STRING_TYPE));
            if (mfaUtil.shouldLockUser()) {
                retRes.addParam(new Param("lockUser", "true", "String"));
                retRes.addParam(new Param("lockoutTime", lockoutTime, "int"));
            } else {
                retRes.addParam(new Param("lockUser", "false", "String"));
            }

            ErrorCodeEnum.ERR_10554.setErrorCode(retRes);
        }
    }

    private boolean isAttemptAllowed(DataControllerRequest dcRequest, Result result, PreLoginUtil mfaUtil) {
        String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
        if (StringUtils.isNotBlank(str)) {
            int allowedAttempt = mfaUtil.maxFailedAttemptsAllowed();
            int InvalidAttempt = Integer.parseInt(str);
            return allowedAttempt > InvalidAttempt;
        }

        return true;
    }

    private boolean isOTPExpired(DataControllerRequest dcRequest, Result result, PreLoginUtil mfaUtil) {
        Date createdts = HelperMethods.getFormattedTimeStamp(HelperMethods.getFieldValue(result, "createdts"));
        Calendar generatedCal = Calendar.getInstance();
        generatedCal.setTime(createdts);

        Date verifyDate = new Date();
        Calendar verifyingCal = Calendar.getInstance();
        verifyingCal.setTime(verifyDate);

        int otpValidityTime = mfaUtil.getSACCodeExpiretime();
        generatedCal.add(Calendar.MINUTE, otpValidityTime);

        long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
        long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

        if (GeneratedMilliSeconds < verifyingMilliSeconds) {
            return true;
        }

        return false;
    }

    private boolean validateOTP(DataControllerRequest dcRequest, Result result) throws HttpCallException {
        String Otp = HelperMethods.getFieldValue(result, "Otp");
        String serviceKey = HelperMethods.getFieldValue(result, "serviceKey");
        String userId = HelperMethods.getFieldValue(result, "User_id");
        boolean status = true;
        status = StringUtils.isBlank(serviceKey) ? false : serviceKey.equals(dcRequest.getParameter("serviceKey"));
        return (Otp.equals(dcRequest.getParameter("OTP")) || Otp.equals(dcRequest.getParameter("Otp"))) && status
        		&& userId.equals(CommonUtils.getUserId(dcRequest));
    }

}
