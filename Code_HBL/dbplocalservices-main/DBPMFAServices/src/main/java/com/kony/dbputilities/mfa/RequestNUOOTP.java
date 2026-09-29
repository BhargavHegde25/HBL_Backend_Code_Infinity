package com.kony.dbputilities.mfa;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.CryptoText;
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

public class RequestNUOOTP implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private PreLoginUtil mfaUtil = null;
    private static String userId = null;

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        try {
            mfaUtil = new PreLoginUtil(dcRequest);

            if (!validate(dcRequest)) {
                result = new Result();
                ErrorCodeEnum.ERR_10555.setErrorCode(result);
                return result;
            }
            if (!mfaUtil.isValidMFA()) {
                ErrorCodeEnum.ERR_10556.setErrorCode(result);
                return result;
            }
            String serviceKey = dcRequest.getParameter("serviceKey");
            if (StringUtils.isBlank(serviceKey)) {
                serviceKey = generateServiceKey(dcRequest);
            }

            dcRequest.addRequestParam_("serviceKey", serviceKey);

            int phoneOTPcount = getPhoneOTPCount(dcRequest);
            int emailOTPcount = getEmailOTPCount(dcRequest);
            int allowedLimit = mfaUtil.getMaximumOTPsperDay();
            if (phoneOTPcount >= allowedLimit || emailOTPcount >= allowedLimit) {
                result = new Result();
                ErrorCodeEnum.ERR_10543.setErrorCode(result);
                return result;
            }

            int retryCount = -1;
            Result Otpresult = getOTPResult(dcRequest);
            String securityKey = new String();
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
                ErrorCodeEnum.ERR_10544.setErrorCode(result);
                return result;
            }

            Result otpresult = createOTP(methodID, inputArray, dcRequest, dcResponse, retryCount, phoneOTPcount,
                    emailOTPcount, securityKey);
            String otp = HelperMethods.getParamValue(otpresult.getParamByName("Otp"));
            securityKey = HelperMethods.getParamValue(otpresult.getParamByName("securityKey"));
            result.addParam(new Param("securityKey", securityKey, MWConstants.STRING));
            result.addParam(new Param("serviceKey", serviceKey, MWConstants.STRING));
            mfaUtil.addRequestAttributes(result, retryCount);
            if (StringUtils.isBlank(otp) || StringUtils.isBlank(securityKey)) {
                return otpresult;
            }

            String phone = dcRequest.getParameter("Phone");

            if (StringUtils.isNotBlank(otp) && StringUtils.isNotBlank(phone)) {

                Map<String, String> input = new HashMap<>();
                input.put("Subscribe", "true");
                input.put("MessageType", "OnboardingProcess");
                input.put("SendToMobiles", dcRequest.getParameter("Phone").trim().replace("+", ""));
                input.put("Content", addOTPContent(otp));
                Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
                headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
                HelperMethods.callApiAsync(dcRequest, input, headers, URLConstants.DBX_SEND_SMS_ORCH);

            }
            updateErrorCode(result);
        } catch (

        Exception e) {
            alert.prepareError(e.getMessage()).log();
            result = new Result();
            ErrorCodeEnum.ERR_10545.setErrorCode(result);

        }
        return result;
    }

    private String addOTPContent(String otp) {
        return "otp:" + otp;
    }

    private Result createOTP(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse, int retryCount, int phoneOTPcount, int emailOTPcount,
            String securityKey) {

        Result result = new Result();
        int otp = generateOtp();
        String serviceKey = dcRequest.getParameter("serviceKey");

        boolean valid = true;

        if (valid) {
            Map<String, String> inputParams = new HashMap<>();
            otp = generateOtp();
            inputParams.put("securityKey", securityKey);

            if (StringUtils.isNotBlank(userId)) {
                inputParams.put("User_id", userId);
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
            result = postProcess(securityKey, otp, dcRequest, phoneOTPcount, emailOTPcount);
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

    private Result postProcess(String securityKey, int otp, DataControllerRequest dcRequest, int phoneOTPcount,
            int emailOTPcount) {
        Result result = new Result();
        Param sKeyParam = new Param("securityKey", securityKey, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        Param otpParam = new Param("Otp", String.valueOf(otp), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        result.addParam(sKeyParam);
        result.addParam(otpParam);

        updateOTPCount(dcRequest, phoneOTPcount, emailOTPcount);

        return result;

    }

    private int generateOtp() {
        int count = mfaUtil.getOTPLength();
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

        Result result = new Result();

        if (StringUtils.isBlank(securityKey)) {
            return result;
        }
        try {
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.OTP_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        return result;
    }

    public static int getPhoneOTPCount(DataControllerRequest dcRequest) {
        String date = HelperMethods.getCurrentDate();
        String phone = dcRequest.getParameter("Phone");

        String filter = "";

        if (StringUtils.isNotBlank(userId)) {
            filter += "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        } else if (StringUtils.isNotBlank(phone)) {
            filter += "Phone" + DBPUtilitiesConstants.EQUAL + phone;
        }

        int count = 0;
        if (!filter.isEmpty()) {
            filter += DBPUtilitiesConstants.AND;

            filter += "Date" + DBPUtilitiesConstants.EQUAL + date;
            Result result = null;
            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.OTPCOUNT_GET);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }

            if (HelperMethods.hasRecords(result)) {
                count = Integer.parseInt(HelperMethods.getFieldValue(result, "Count"));
            }
        }

        return count;
    }

    public static int getEmailOTPCount(DataControllerRequest dcRequest) {
        String date = HelperMethods.getCurrentDate();

        String email = dcRequest.getParameter("Email");

        String filter = "";

        if (StringUtils.isNotBlank(userId)) {
            filter += "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        } else if (StringUtils.isNotBlank(email)) {
            filter += "Email" + DBPUtilitiesConstants.EQUAL + email;
        }

        int count = 0;
        if (!filter.isEmpty()) {
            filter += DBPUtilitiesConstants.AND;

            filter += "Date" + DBPUtilitiesConstants.EQUAL + date;
            Result result = null;
            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.OTPCOUNT_GET);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }

            if (HelperMethods.hasRecords(result)) {
                count = Integer.parseInt(HelperMethods.getFieldValue(result, "Count"));
            }
        }

        return count;
    }

    public static String getPhoneOTPCountKey(DataControllerRequest dcRequest) {
        String date = HelperMethods.getCurrentDate();

        String phone = dcRequest.getParameter("Phone");

        String filter = "";

        if (StringUtils.isNotBlank(userId)) {
            filter += "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        } else if (StringUtils.isNotBlank(phone)) {
            filter += "Phone" + DBPUtilitiesConstants.EQUAL + phone;
        }

        String key = HelperMethods.getNewId();

        if (!filter.isEmpty()) {

            filter += DBPUtilitiesConstants.AND;

            filter += "Date" + DBPUtilitiesConstants.EQUAL + date;
            Result result = null;
            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.OTPCOUNT_GET);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }

            if (StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "key"))) {
                key = HelperMethods.getFieldValue(result, "key");
            }

        }
        return key;
    }

    public static String getEmailOTPCountKey(DataControllerRequest dcRequest) {
        String date = HelperMethods.getCurrentDate();

        String email = dcRequest.getParameter("Email");

        String filter = "";

        if (StringUtils.isNotBlank(userId)) {
            filter += "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        } else if (StringUtils.isNotBlank(email)) {
            filter += "Email" + DBPUtilitiesConstants.EQUAL + email;
        }
        String key = HelperMethods.getNewId();

        if (!filter.isEmpty()) {

            filter += DBPUtilitiesConstants.AND;

            filter += "Date" + DBPUtilitiesConstants.EQUAL + date;
            Result result = null;
            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.OTPCOUNT_GET);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }

            if (StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "key"))) {
                key = HelperMethods.getFieldValue(result, "key");
            }
        }

        return key;
    }

    public static void updateOTPCount(DataControllerRequest dcRequest, int phoneOTPcount, int emailOTPcount) {
        HashMap<String, String> hashMap = new HashMap<>();

        if (StringUtils.isNotBlank(userId)) {
            hashMap.put("User_id", userId);
        } else {
            updatePhoneOTPCount(dcRequest, phoneOTPcount);
            updateEmailOTPCount(dcRequest, emailOTPcount);
            return;
        }

        hashMap.put("key", getPhoneOTPCountKey(dcRequest));
        hashMap.put("Date", HelperMethods.getCurrentDate());

        int count = phoneOTPcount;

        hashMap.put("Count", "" + (++count));

        String url = URLConstants.OTPCOUNT_UPDATE;
        if (count == 1) {
            url = URLConstants.OTPCOUNT_CREATE;
        }
        HelperMethods.callApiAsync(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest), url);

    }

    public static void updatePhoneOTPCount(DataControllerRequest dcRequest, int count) {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("key", getPhoneOTPCountKey(dcRequest));
        hashMap.put("Date", HelperMethods.getCurrentDate());

        hashMap.put("User_id", userId);
        if (StringUtils.isBlank(hashMap.get("User_id"))) {
            hashMap.put("Phone", dcRequest.getParameter("Phone"));
        }
        hashMap.put("Count", "" + (++count));

        String url = URLConstants.OTPCOUNT_CREATE;
        if (count > 1) {
            url = URLConstants.OTPCOUNT_UPDATE;
        }
        HelperMethods.callApiAsync(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest), url);
    }

    public static void updateEmailOTPCount(DataControllerRequest dcRequest, int count) {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("key", getEmailOTPCountKey(dcRequest));
        hashMap.put("Date", HelperMethods.getCurrentDate());

        hashMap.put("User_id", userId);
        if (StringUtils.isBlank(hashMap.get("User_id"))) {
            hashMap.put("Email", dcRequest.getParameter("Email"));
        }
        hashMap.put("Count", "" + (++count));

        String url = URLConstants.OTPCOUNT_CREATE;
        if (count > 1) {
            url = URLConstants.OTPCOUNT_UPDATE;
        }
        HelperMethods.callApiAsync(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest), url);
    }

    private String generateServiceKey(DataControllerRequest dcRequest) {

        String phone = dcRequest.getParameter("Phone");

        Map<String, String> inputParams = new HashMap<>();

        String serviceKey = HelperMethods.getNewId();
        String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
        JsonObject payload = new JsonObject();
        payload.addProperty(MFAConstants.PHONE, phone);
        payload.addProperty("appId", "KonyOnboarding");

        String data = payload.toString();
        try {
            data = CryptoText.encrypt(data);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return null;
        }
        String Createddts = HelperMethods.getCurrentTimeStamp();

        inputParams.put("serviceKey", serviceKey);
        inputParams.put("serviceName", serviceName);
        inputParams.put("payload", data);
        inputParams.put("Createddts", Createddts);

        Result result = new Result();

        try {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MFA_SERVICE_CREATE);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
            return null;
        }
        if (HelperMethods.hasRecords(result)) {
            return serviceKey;
        }

        return null;
    }

}
