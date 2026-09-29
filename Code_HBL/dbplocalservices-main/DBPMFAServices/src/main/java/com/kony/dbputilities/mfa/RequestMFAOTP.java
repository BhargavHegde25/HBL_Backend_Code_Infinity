package com.kony.dbputilities.mfa;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.utils.OTPUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class RequestMFAOTP implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        PostLoginMFAUtil mfaUtil = null;

        String userId = null;

        Result otpCountResult = null;
        Result result = new Result();
		mfaUtil = StringUtils.isNotBlank(dcRequest.getParameter("userId")) ? new PostLoginMFAUtil(dcRequest,dcRequest.getParameter("userId"))
				: new PostLoginMFAUtil(dcRequest);

        String serviceKey = dcRequest.getParameter("serviceKey");

        mfaUtil.getMFaModeforRequestfromDB(serviceKey);
        if (!mfaUtil.isAppActionValid()) {
            ErrorCodeEnum.ERR_10515.setErrorCode(result);
            return result;
        }

        if (!validate(dcRequest)) {
            result = new Result();
            ErrorCodeEnum.ERR_10508.setErrorCode(result);
            return result;
        }

		userId = StringUtils.isNotBlank(dcRequest.getParameter("userId")) ? dcRequest.getParameter("userId")
				: HelperMethods.getCustomerIdFromKnownUserToken(dcRequest);

        int otpCount = OTPUtil.getOTPCount(dcRequest, userId, otpCountResult);
        int allowedLimit = mfaUtil.getMaximumOTPsperDay();
        if (otpCount >= allowedLimit) {
            result = new Result();
            ErrorCodeEnum.ERR_10509.setErrorCode(result);
            return result;
        }

        int retryCount = -1;
        Result Otpresult = (Result) new GetOTP().invoke(inputArray, dcRequest, dcResponse);
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
            input.put(MFAConstants.EMAIL_SUBJECT, mfaUtil.getEmailSubject());
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
            Result otpCountResult, PostLoginMFAUtil mfaUtil) {

        Result result = new Result();
        int otp = OTPUtil.generateOtp(mfaUtil.getOTPLength());
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

        OTPUtil.updateOTPCount(dcRequest, otpCount, user_id, otpCountResult);

        return result;
    }
}
