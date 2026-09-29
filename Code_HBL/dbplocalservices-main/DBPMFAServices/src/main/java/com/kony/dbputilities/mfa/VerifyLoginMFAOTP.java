package com.kony.dbputilities.mfa;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class VerifyLoginMFAOTP implements JavaService2 {
	 private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	//private static final Logger LOG = LogManager.getLogger(VerifyLoginMFAOTP.class);
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String serviceKey = inputParams.get(MFAConstants.SERVICE_KEY);
        String serviceName = inputParams.get(MFAConstants.SERVICE_NAME);

        if (StringUtils.isBlank(serviceName) || StringUtils.isBlank(serviceKey)) {
            ErrorCodeEnum.ERR_10514.setErrorCode(result);
            return result;
        }

        Result fetchMFAAndOTP = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.LOGIN_OTP_CONCURRENT_ORCHESTRATION);
        diagnostic.prepareDebug("VerifyLoginMFAOTP ###"+ ResultToJSON.convert(fetchMFAAndOTP));
        if (!isValidRequest(fetchMFAAndOTP)) {
            ErrorCodeEnum.ERR_10518.setErrorCode(result);
            return result;
        }

        Dataset dataset = fetchMFAAndOTP.getDatasetById("OTP");
        Result otpResult = new Result();
        otpResult.addDataset(dataset);

        fetchMFAAndOTP.removeDatasetById("OTP");

        LoginMFAUtil mfaUtil = null;
        mfaUtil = new LoginMFAUtil(dcRequest);
        mfaUtil.setMFaModeforRequest(fetchMFAAndOTP);

        if (mfaUtil.mfaConfigurationUtil==null || !mfaUtil.mfaConfigurationUtil.isValidMFA() || !mfaUtil.isAppActionValid() || !mfaUtil.isAppActionPresent()) {
            ErrorCodeEnum.ERR_10515.setErrorCode(result);
            return result;
        } else {
            String otpBypass = "";
            result = postProcess(inputParams, dcRequest, otpResult, otpBypass, mfaUtil);
        }

        return result;
    }

    private boolean isValidRequest(Result fetchMFAAndOTP) {
    	diagnostic.prepareDebug("isValidRequest ###").log();
        Dataset dataset = fetchMFAAndOTP.getDatasetById("OTP");
        diagnostic.prepareDebug("isValidRequest dataset###"+ dataset.toString()).log();
        Record otp = new Record();
        if (dataset != null) {
            if (dataset.getAllRecords().size() > 0) {
                otp = dataset.getAllRecords().get(0);
            } else {
                otp = null;
            }
        }

        Dataset dataset1 = fetchMFAAndOTP.getDatasetById("mfaservice");
        Record mfaService = new Record();
        if (dataset1 != null) {
            if (dataset1.getAllRecords().size() > 0) {
                mfaService = dataset1.getAllRecords().get(0);
            } else {
                mfaService = null;
            }
        }

        diagnostic.prepareDebug("otp dataset###"+ otp).log();
        //LOG.debug("mfaService dataset###"+ mfaService.toString());
        if (otp != null && mfaService != null) {
        	diagnostic.prepareDebug("Inside mfaService dataset###").log();
            String customerId = fetchMFAAndOTP.getParamValueByName("customerId");
            diagnostic.prepareDebug("Inside mfaService dataset customerId###"+ customerId).log();
            diagnostic.prepareDebug("Inside mfaService dataset User_id###"+ mfaService.getParamValueByName("User_id")).log();
            
            if (StringUtils.isNotBlank(customerId) && customerId.equals(mfaService.getParamValueByName("User_id"))) {
                return true;
            }
        }
        return false;
    }

    private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
            String otpBypass, LoginMFAUtil mfaUtil) throws HttpCallException {
        Result retRes = new Result();

        if (HelperMethods.hasRecords(result)) {
            if (!isAttemptAllowed(dcRequest, result, mfaUtil)) {
                setVerifyAttributes(dcRequest, result, retRes, mfaUtil);
            } else if (isOTPExpired(dcRequest, result, mfaUtil)) {
                retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_EXPIRED, "true", "String"));
                updateAttemptCount(dcRequest, result, retRes, mfaUtil);
                setVerifyAttributes(dcRequest, result, retRes, mfaUtil);
                int retryCount = Integer.parseInt(HelperMethods.getFieldValue(result, "NumberOfRetries"));
                retRes = mfaUtil.addRequestAttributes(retRes, retryCount - 1);
            } else if (validateOTP(dcRequest, result)) {
                HelperMethods.setSuccessMsg("Secure Access Code has been successfully verified", retRes);
                retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "true", "String"));
                mfaUtil.createEntryInStateChecker();
                deleteOTP(dcRequest, result);
            } else {
                retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "false", "String"));
                ErrorCodeEnum.ERR_10517.setErrorCode(retRes);
                updateAttemptCount(dcRequest, result, retRes, mfaUtil);
                return mfaUtil.addVerifyAttributes(result, retRes);
            }
        } else {
            ErrorCodeEnum.ERR_10518.setErrorCode(retRes);
        }

        return retRes;
    }

    private void deleteOTP(DataControllerRequest dcRequest, Result result) {
        HashMap<String, String> input = new HashMap<>();
        input.put("securityKey", HelperMethods.getFieldValue(result, "securityKey"));
        HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_DELETE);
    }

    private void updateAttemptCount(DataControllerRequest dcRequest, Result result, Result retRes,
            LoginMFAUtil mfaUtil) {

        int InvalidAttempt = 0;
        String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
        if (StringUtils.isNotBlank(str)) {
            InvalidAttempt = Integer.parseInt(str);
        }
        Map<String, String> input = new HashMap<>();
        input.put("securityKey", HelperMethods.getFieldValue(result, "securityKey"));
        input.put("InvalidAttempt", String.valueOf(InvalidAttempt + 1));
        HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_UPDATE);

        int allowedAttempt = mfaUtil.mfaConfigurationUtil.maxFailedAttemptsAllowed();
        retRes.addParam(new Param("maxFailedAttemptsAllowed", allowedAttempt + "", DBPUtilitiesConstants.STRING_TYPE));
        retRes.addParam(new Param("remainingFailedAttempts", (allowedAttempt - (InvalidAttempt + 1)) + "",
                DBPUtilitiesConstants.STRING_TYPE));
        retRes.addParam(new Param("failedAttempts", (InvalidAttempt + 1) + "", DBPUtilitiesConstants.STRING_TYPE));
    }

    private void setVerifyAttributes(DataControllerRequest dcRequest, Result result, Result retRes,
            LoginMFAUtil mfaUtil) throws HttpCallException {
        int InvalidAttempt = 0;
        String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
        if (StringUtils.isNotBlank(str)) {
            InvalidAttempt = Integer.parseInt(str);
        }

        int allowedAttempt = mfaUtil.mfaConfigurationUtil.maxFailedAttemptsAllowed();
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

            if (mfaUtil.shouldLogoutUser()) {
                retRes.addParam(new Param("logoutUser", "true", "String"));
                retRes.addParam(new Param("lockoutTime", lockoutTime, "int"));
            } else {
                retRes.addParam(new Param("logoutUser", "false", "String"));
            }
            ErrorCodeEnum.ERR_10519.setErrorCode(retRes);
        }
    }

    private boolean isAttemptAllowed(DataControllerRequest dcRequest, Result result, LoginMFAUtil mfaUtil) {
        String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
        if (StringUtils.isNotBlank(str)) {
            int allowedAttempt = mfaUtil.mfaConfigurationUtil.maxFailedAttemptsAllowed();
            int InvalidAttempt = Integer.parseInt(str);
            return allowedAttempt > InvalidAttempt;
        }

        return true;
    }

    private boolean isOTPExpired(DataControllerRequest dcRequest, Result result, LoginMFAUtil mfaUtil) {
        Date createdts = HelperMethods.getFormattedTimeStamp(HelperMethods.getFieldValue(result, "createdts"));
        Calendar generatedCal = Calendar.getInstance();
        generatedCal.setTime(createdts);

        Date verifyDate = new Date();
        Calendar verifyingCal = Calendar.getInstance();
        verifyingCal.setTime(verifyDate);

        int otpValidityTime = mfaUtil.mfaConfigurationUtil.getSACCodeExpiretime();
        generatedCal.add(Calendar.MINUTE, otpValidityTime);

        long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
        long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

        if (GeneratedMilliSeconds < verifyingMilliSeconds) {
            return true;
        }

        return false;
    }

    private boolean validateOTP(DataControllerRequest dcRequest, Result result) {
        String Otp = HelperMethods.getFieldValue(result, "Otp");
        String serviceKey = HelperMethods.getFieldValue(result, "serviceKey");
        boolean status = true;

        status = StringUtils.isBlank(serviceKey) ? false : serviceKey.equals(dcRequest.getParameter("serviceKey"));

        return (Otp.equals(dcRequest.getParameter("OTP")) || Otp.equals(dcRequest.getParameter("Otp"))) && status;
    }

}
