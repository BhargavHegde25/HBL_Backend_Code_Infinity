package com.kony.eum.dbputilities.mfa;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.mfa.utils.OTPUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class VerifyMFAOTP implements JavaService2 {

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

        if (!OTPUtil.isValidRequest(fetchMFAAndOTP)) {
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

    private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
            String otpBypass, LoginMFAUtil mfaUtil) throws HttpCallException {
        Result retRes = new Result();

        if (HelperMethods.hasRecords(result)) {
            if (!OTPUtil.isAttemptAllowed(dcRequest, result, mfaUtil)) {
                OTPUtil.setVerifyAttributes(dcRequest, result, retRes, mfaUtil);
            } else if (OTPUtil.isOTPExpired(dcRequest, result, mfaUtil)) {
                retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_EXPIRED, "true", "String"));
                OTPUtil.updateAttemptCount(dcRequest, result, retRes, mfaUtil);
                OTPUtil.setVerifyAttributes(dcRequest, result, retRes, mfaUtil);
                int retryCount = Integer.parseInt(HelperMethods.getFieldValue(result, "NumberOfRetries"));
                retRes = mfaUtil.addRequestAttributes(retRes, retryCount - 1);
            } else if (OTPUtil.validateOTP(dcRequest, result)) {
                HelperMethods.setSuccessMsg("Secure Access Code has been successfully verified", retRes);
                retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "true", "String"));
                mfaUtil.createEntryInStateChecker();
                OTPUtil.deleteOTP(dcRequest, HelperMethods.getFieldValue(result, "securityKey"));
            } else {
                retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "false", "String"));
                ErrorCodeEnum.ERR_10517.setErrorCode(retRes);
                OTPUtil.updateAttemptCount(dcRequest, result, retRes, mfaUtil);
                return mfaUtil.addVerifyAttributes(result, retRes);
            }
        } else {
            ErrorCodeEnum.ERR_10518.setErrorCode(retRes);
        }

        return retRes;
    }

    

}
