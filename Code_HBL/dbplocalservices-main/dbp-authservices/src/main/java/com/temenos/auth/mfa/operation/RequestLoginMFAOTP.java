package com.temenos.auth.mfa.operation;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.auth.mfa.util.LoginMFAUtil;
import com.temenos.auth.mfa.util.MFAConstants;

public class RequestLoginMFAOTP implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        LoginMFAUtil mfaUtil = null;

        String userId = null;

        Result otpCountResult = null;
        Result result = new Result();
        mfaUtil = new LoginMFAUtil(dcRequest);

        String serviceName = dcRequest.getParameter("serviceName");
        String serviceKey = dcRequest.getParameter("serviceKey");

        mfaUtil.getMFaModeforRequestfromDB(serviceKey, serviceName);
        if (mfaUtil.mfaConfigurationUtil == null || !mfaUtil.mfaConfigurationUtil.isValidMFA()) {
			ErrorCodeEnum.ERR_10525.setErrorCode(result);
			return result;
		}
        
        if (!mfaUtil.isAppActionValid() || !mfaUtil.isAppActionPresent()) {
            ErrorCodeEnum.ERR_10515.setErrorCode(result);
            return result;
        }

        if (!validate(dcRequest)) {
            result = new Result();
            ErrorCodeEnum.ERR_10508.setErrorCode(result);
            return result;
        }

        userId = HelperMethods.getCustomerIdFromKnownUserToken(dcRequest);

        int otpCount = getOTPCount(dcRequest, userId, otpCountResult);
        int allowedLimit = mfaUtil.getMaximumOTPsperDay();
        if (otpCount >= allowedLimit) {
            result = new Result();
            ErrorCodeEnum.ERR_10509.setErrorCode(result);
            return result;
        }

        int retryCount = -1;
        Result Otpresult = getOTPResult(dcRequest);
        String securityKey = null;
        if (HelperMethods.hasRecords(Otpresult)) {
            if (StringUtils.isNotBlank(HelperMethods.getFieldValue(Otpresult, "NumberOfRetries"))) {
                retryCount = Integer.parseInt(HelperMethods.getFieldValue(Otpresult, "NumberOfRetries"));
            }

            if (StringUtils.isNotBlank(HelperMethods.getFieldValue(Otpresult, "securityKey"))) {
                securityKey = HelperMethods.getFieldValue(Otpresult, "securityKey");
            }
        }

        if (mfaUtil.isRetryAllowed(retryCount)) {
            result = new Result();
            ErrorCodeEnum.ERR_10510.setErrorCode(result);
            return result;
        }

        Result otpresult = createOTP(methodID, inputArray, dcRequest, dcResponse, retryCount, otpCount, securityKey,
                userId, otpCountResult, mfaUtil);
        String otp = HelperMethods.getParamValue(otpresult.getParamByName("Otp"));
        securityKey = HelperMethods.getParamValue(otpresult.getParamByName("securityKey"));
        result.addParam(new Param("securityKey", securityKey, MWConstants.STRING));
        mfaUtil.addRequestAttributes(result, retryCount);
        if (StringUtils.isBlank(otp) || StringUtils.isBlank(securityKey)) {
            return otpresult;
        }

        String phone = dcRequest.getParameter("Phone");
        String email = dcRequest.getParameter("Email");

        if (StringUtils.isNotBlank(otp) && StringUtils.isNotBlank(phone)) {

            Map<String, String> input = new HashMap<>();
            input.put("Subscribe", "true");
            input.put("SendToMobiles", dcRequest.getParameter("Phone").trim().replace("+", ""));
            String smsContent = mfaUtil.getSMSText(otp);
            input.put(MFAConstants.SMS_TEXT, smsContent);
            if (StringUtils.isNotBlank(smsContent)) {
                Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
                headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
                HelperMethods.callApiAsync(dcRequest, input, headers, URLConstants.DBX_SEND_SMS_ORCH);
            }
        }

        if (StringUtils.isNotBlank(otp) && StringUtils.isNotBlank(email)) {

            Map<String, String> input = new HashMap<>();
            input.put("Subscribe", "true");
            input.put("FirstName", "firstName");
            input.put("LastName", "lastName");
            String emailContent = mfaUtil.getEmailBody(otp);
            input.put(MFAConstants.EMAIL_BODY, emailContent);
            input.put(MFAConstants.EMAIL_SUBJECT, mfaUtil.mfaConfigurationUtil.getEmailSubject());
            if (StringUtils.isNotBlank(emailContent)) {
                input.put("Email", dcRequest.getParameter("Email"));
                Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
                headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
                HelperMethods.callApiAsync(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
            }
        }

        updateErrorCode(result);

        return result;

    }

    private Result createOTP(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse, int retryCount, int otpCount, String securityKey, String user_id,
            Result otpCountResult, LoginMFAUtil mfaUtil) {

        Result result = new Result();
        int otp = generateOtp(mfaUtil);
        String serviceKey = dcRequest.getParameter("serviceKey");

        boolean valid = true;

        if (valid) {
            Map<String, String> inputParams = new HashMap<>();
            inputParams.put("securityKey", securityKey);

            user_id = HelperMethods.getCustomerIdFromKnownUserToken(dcRequest);

            if (StringUtils.isNotBlank(user_id)) {
                inputParams.put("User_id", user_id);
            }

            if (StringUtils.isNotBlank(dcRequest.getParameter("Phone"))) {
                inputParams.put("Phone", dcRequest.getParameter("Phone"));
            }

            if (StringUtils.isNotBlank(dcRequest.getParameter("Email"))) {
                inputParams.put("Email", dcRequest.getParameter("Email"));
            }

            inputParams.put("Otp", String.valueOf(otp));

            inputParams.put("OtpType", dcRequest.getParameter("OtpType"));

            inputParams.put("NumberOfRetries", "" + (retryCount + 1));

            inputParams.put("createdts", HelperMethods.getCurrentTimeStamp());

            String url = URLConstants.OTP_CREATE;

            if (StringUtils.isBlank(securityKey) || retryCount == -1) {
                securityKey = HelperMethods.getNewId();
                inputParams.put("serviceKey", serviceKey);
            } else {
                url = URLConstants.OTP_UPDATE;
            }
            inputParams.put("securityKey", securityKey);

            try {
                result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), url);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

        if (valid && !HelperMethods.hasError(result)) {
            result = postProcess(securityKey, otp, dcRequest, otpCount, user_id, otpCountResult);
        }
        return result;
    }

    private void updateErrorCode(Result result) {
        HelperMethods.setSuccessMsg("OTP request sent successfully.", result);
    }

    private boolean validate(DataControllerRequest dcRequest) {
        return StringUtils.isNotBlank(dcRequest.getParameter("Phone"))
                || StringUtils.isNotBlank(dcRequest.getParameter("Email"));
    }

    private Result postProcess(String securityKey, int otp, DataControllerRequest dcRequest, int otpCount,
            String user_id, Result otpCountResult) {
        Result result = new Result();
        Param sKeyParam = new Param("securityKey", securityKey, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        Param otpParam = new Param("Otp", String.valueOf(otp), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        result.addParam(sKeyParam);
        result.addParam(otpParam);

        updateOTPCount(dcRequest, otpCount, user_id, otpCountResult);

        return result;
    }

    private int generateOtp(LoginMFAUtil mfaUtil) {
        int count = mfaUtil.mfaConfigurationUtil.getOTPLength();
        int floor = (int) Math.pow(10, count - 1);
        int ceil = floor * 9;
        SecureRandom rand = new SecureRandom();
        return (int) (floor + (rand.nextFloat() * ceil));
    }

    private Result getOTPResult(DataControllerRequest dcRequest) {

        String securityKey = dcRequest.getParameter("securityKey");
        String serviceKey = dcRequest.getParameter("serviceKey");

        String filter = "";

        if (StringUtils.isNotBlank(securityKey)) {
            filter += "securityKey" + DBPUtilitiesConstants.EQUAL + securityKey;
        }

        if (StringUtils.isNotBlank(serviceKey)) {
            if (!filter.isEmpty()) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey;
        }

        Result result = null;
        try {
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.OTP_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        return result;
    }

    public static int getOTPCount(DataControllerRequest dcRequest, String userId, Result otpCountResult) {
        String date = HelperMethods.getCurrentDate();

        String filter = "";

        if (StringUtils.isNotBlank(userId)) {
            filter += "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        }

        int count = 0;
        if (!filter.isEmpty()) {
            filter += DBPUtilitiesConstants.AND;

            filter += "Date" + DBPUtilitiesConstants.EQUAL + date;
            try {
                otpCountResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.OTPCOUNT_GET);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }

            if (HelperMethods.hasRecords(otpCountResult)) {
                count = Integer.parseInt(HelperMethods.getFieldValue(otpCountResult, "Count"));
            }
        }

        return count;
    }

    public static String getOTPCountKey(DataControllerRequest dcRequest, Result otpCountResult) {

        String key = HelperMethods.getNewId();

        if (StringUtils.isNotBlank(HelperMethods.getFieldValue(otpCountResult, "key"))) {
            key = HelperMethods.getFieldValue(otpCountResult, "key");
        }

        return key;

    }

    public static void updateOTPCount(DataControllerRequest dcRequest, int otpCount, String userId,
            Result otpCountResult) {
        HashMap<String, String> hashMap = new HashMap<>();

        if (StringUtils.isNotBlank(userId)) {
            hashMap.put("User_id", userId);
        }

        hashMap.put("key", getOTPCountKey(dcRequest, otpCountResult));
        hashMap.put("Date", HelperMethods.getCurrentDate());

        int count = otpCount;

        hashMap.put("Count", "" + (++count));

        String url = URLConstants.OTPCOUNT_UPDATE;
        if (count == 1) {
            url = URLConstants.OTPCOUNT_CREATE;
        }
        HelperMethods.callApiAsync(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest), url);

    }

}
