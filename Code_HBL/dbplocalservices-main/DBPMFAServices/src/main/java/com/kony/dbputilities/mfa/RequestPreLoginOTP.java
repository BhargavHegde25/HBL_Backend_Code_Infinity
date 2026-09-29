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
import org.json.JSONObject;

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

public class RequestPreLoginOTP implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();

        PreLoginUtil mfaUtil = null;
        String userId = null;

        mfaUtil = new PreLoginUtil(dcRequest);

        if (StringUtils.isNotBlank(dcRequest.getParameter("userId"))) {
            userId = dcRequest.getParameter("userId");
        }

        if (!mfaUtil.isValidMFA()) {
            ErrorCodeEnum.ERR_10541.setErrorCode(result);
            return result;
        }

        if (!validate(dcRequest)) {
            result = new Result();
            ErrorCodeEnum.ERR_10542.setErrorCode(result);
            return result;
        }

        int phoneOTPcount = getPhoneOTPCount(dcRequest, userId);
        int emailOTPcount = getEmailOTPCount(dcRequest, userId);
        int allowedLimit = mfaUtil.getMaximumOTPsperDay();
        if (phoneOTPcount >= allowedLimit || emailOTPcount >= allowedLimit) {
            result = new Result();
            ErrorCodeEnum.ERR_10543.setErrorCode(result);
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
            ErrorCodeEnum.ERR_10544.setErrorCode(result);
            return result;
        }

        Result otpresult = createOTP(dcRequest, retryCount, phoneOTPcount, emailOTPcount, securityKey, userId, mfaUtil);
        String otp = HelperMethods.getParamValue(otpresult.getParamByName("Otp"));
        securityKey = HelperMethods.getParamValue(otpresult.getParamByName("securityKey"));
        result.addParam(new Param("securityKey", securityKey, MWConstants.STRING));
        mfaUtil.addRequestAttributes(result, retryCount);
        if (StringUtils.isBlank(otp) || StringUtils.isBlank(securityKey)) {
            return otpresult;
        }

        String phone = dcRequest.getParameter("Phone");
        String email = dcRequest.getParameter("Email");
       
        StringBuilder sb = new StringBuilder();
        sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append(userId);

        Result rs = null;
		try {
			rs = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
			        URLConstants.CUSTOMERVERIFY_GET);
			
		} catch (HttpCallException e) {
			alert.prepareError(e.toString()).log();
			
		}
        String customerType = HelperMethods.getFieldValue(rs, "CustomerType_id");
   
        if ("TYPE_ID_PROSPECT".equalsIgnoreCase(customerType) && StringUtils.isNotBlank(email)) {
        	  Map<String, String> inputParams = new HashMap<>();
        	  String firstname = HelperMethods.getFieldValue(rs, "FirstName");
              String lastname = HelperMethods.getFieldValue(rs, "LastName");
              String username = HelperMethods.getFieldValue(rs, "UserName");
              String  emailTemplate = DBPUtilitiesConstants.PROSPECT_USERNAME_TEMPLATE;
              
              JSONObject additionalContext = new JSONObject();
              
              additionalContext.put("UserName",userId);
              additionalContext.put("FirstName",firstname);
              additionalContext.put("LastName",lastname);
              additionalContext.put("Email",email);
              
              String AdditionalContext = additionalContext.toString().replace("\"", "").replace(",", ";");
              
              inputParams.put("FirstName",firstname);
              inputParams.put("LastName",lastname);
              inputParams.put("UserName",username);
              inputParams.put("AdditionalContext",AdditionalContext.substring(1, AdditionalContext.length() - 1));
              inputParams.put("EmailType", emailTemplate);
              inputParams.put("Email", email);
              inputParams.put("Subscribe", "true");
         
             Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
             headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
           
             HelperMethods.callApiAsync(dcRequest, inputParams, headers, URLConstants.DBX_SEND_EMAIL_ORCH); 	 
        }

        if (StringUtils.isNotBlank(otp) && StringUtils.isNotBlank(phone)) {

            Map<String, String> input = new HashMap<>();
            input.put("Subscribe", "true");
            input.put("SendToMobiles", dcRequest.getParameter("Phone").trim().replace("+", ""));
            String smsContent = "OTP is " + otp;
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
            String emailContent = "OTP is " + otp;
            input.put(MFAConstants.EMAIL_BODY, "OTP is " + otp);
            input.put(MFAConstants.EMAIL_SUBJECT, "OTP");
            if (StringUtils.isNotBlank(emailContent)) {
                input.put("Email", dcRequest.getParameter("Email"));
                Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
                headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
                HelperMethods.callApiAsync(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
            }

            updateErrorCode(result);
        }
        return result;

    }

    private Result createOTP(DataControllerRequest dcRequest, int retryCount, int phoneOTPcount, int emailOTPcount,
            String securityKey, String userId, PreLoginUtil mfaUtil) {

        Result result = new Result();
        int otp = generateOtp(mfaUtil);
        String serviceKey = dcRequest.getParameter("serviceKey");
        Map<String, String> inputParams = new HashMap<>();
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

        if (!HelperMethods.hasError(result)) {
            result = postProcess(securityKey, otp, dcRequest, phoneOTPcount, emailOTPcount, userId);
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
            int emailOTPcount, String userId) {
        Result result = new Result();
        Param sKeyParam = new Param("securityKey", securityKey, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        Param otpParam = new Param("Otp", String.valueOf(otp), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        result.addParam(sKeyParam);
        result.addParam(otpParam);

        updateOTPCount(dcRequest, phoneOTPcount, emailOTPcount, userId);

        return result;
    }

    private int generateOtp(PreLoginUtil mfaUtil) {
        int count = mfaUtil.getOTPLength();
        int floor = (int) Math.pow(10, count - 1);
        int ceil = floor * 9;
        SecureRandom rand = new SecureRandom();
        return (int) (floor + (rand.nextInt(ceil)));
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

    public static int getPhoneOTPCount(DataControllerRequest dcRequest, String userId) {
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

    public static int getEmailOTPCount(DataControllerRequest dcRequest, String userId) {
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

    public static String getPhoneOTPCountKey(DataControllerRequest dcRequest, String userId) {
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

    public static String getEmailOTPCountKey(DataControllerRequest dcRequest, String userId) {
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
                //
                alert.prepareError(e.getMessage()).log();
            }

            if (StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "key"))) {
                key = HelperMethods.getFieldValue(result, "key");
            }
        }

        return key;
    }

    public static void updateOTPCount(DataControllerRequest dcRequest, int phoneOTPcount, int emailOTPcount,
            String userId) {
        HashMap<String, String> hashMap = new HashMap<>();

        if (StringUtils.isNotBlank(userId)) {
            hashMap.put("User_id", userId);
        } else {
            updatePhoneOTPCount(dcRequest, phoneOTPcount, userId);
            updateEmailOTPCount(dcRequest, emailOTPcount, userId);
            return;
        }

        hashMap.put("key", getPhoneOTPCountKey(dcRequest, userId));
        hashMap.put("Date", HelperMethods.getCurrentDate());

        int count = phoneOTPcount;

        hashMap.put("Count", "" + (++count));

        String url = URLConstants.OTPCOUNT_UPDATE;
        if (count == 1) {
            url = URLConstants.OTPCOUNT_CREATE;
        }
        HelperMethods.callApiAsync(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest), url);

    }

    public static void updatePhoneOTPCount(DataControllerRequest dcRequest, int count, String userId) {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("key", getPhoneOTPCountKey(dcRequest, userId));
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

    public static void updateEmailOTPCount(DataControllerRequest dcRequest, int count, String userId) {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("key", getEmailOTPCountKey(dcRequest, userId));
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

}
