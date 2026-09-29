package com.kony.utilityproductservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.communicationservices.KMSUtil;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SmsOTP implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		if (!validate(dcRequest)) {
			result = new Result();
			ErrorCodeEnum.ERR_10058.setErrorCode(result);
			return result;
		}
		try {
			RequestOTP requestOTP = new RequestOTP();
			Result otpresult = (Result) requestOTP.invoke(methodID, inputArray, dcRequest, dcResponse);
			String otp = HelperMethods.getParamValue(otpresult.getParamByName("Otp"));
			String securityKey = HelperMethods.getParamValue(otpresult.getParamByName(MFAConstants.SECURITY_KEY));

			if (StringUtils.isBlank(otp) || StringUtils.isBlank(securityKey)) {
				return otpresult;
			}

			String phone = dcRequest.getParameter("Phone");
			String email = dcRequest.getParameter("Email");
			boolean emailSend = false;
			boolean smsSend = false;
			if (StringUtils.isNotBlank(otp) && StringUtils.isNotBlank(phone)) {
				Map<String, String> input = new HashMap<>();
				input.put("Subscribe", "true");
				input.put("MessageType", "registrationMessage");
				input.put("SendToMobiles", dcRequest.getParameter("Phone").trim().replace("+", ""));
				input.put("Content", KMSUtil.getOTPContent(otp, dcRequest.getParameter("Content"), null));
				if (StringUtils.isNotBlank(dcRequest.getParameter("MessageType"))) {
					input.put("MessageType", dcRequest.getParameter("MessageType"));
				}

				Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
				headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
				result = HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_SMS_ORCH);
				if (!HelperMethods.hasDBPErrorMSG(result) && !HelperMethods.hasError(result)) {
					smsSend = true;
					result.addParam(new Param(MFAConstants.SECURITY_KEY, securityKey, MWConstants.STRING));
				}
			}
			if (StringUtils.isNotBlank(otp) && StringUtils.isNotBlank(email)) {
				Map<String, String> input = new HashMap<>();
				input.put("Subscribe", dcRequest.getParameter("subscribe"));
				input.put("FirstName", "firstName");
				input.put("EmailType", "registrationMessage");
				input.put("LastName", "lastName");
				input.put("AdditionalContext", KMSUtil.getOTPContent(otp, dcRequest.getParameter("Content"), null));
				input.put("Email", dcRequest.getParameter("Email"));
				if (StringUtils.isNotBlank(dcRequest.getParameter("MessageType"))) {
					input.put("EmailType", dcRequest.getParameter("MessageType"));
				}
				Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
				headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
				Result response = HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
				if (!HelperMethods.hasDBPErrorMSG(response) && !HelperMethods.hasError(response)) {
					emailSend = true;
					result.addParam(new Param(MFAConstants.SECURITY_KEY, securityKey, MWConstants.STRING));
					result.addParam(new Param("emailRequestId",
							HelperMethods.getParamValue(response.getParamByName("requestId")), MWConstants.STRING));
				}
			}
			updateErrorCode(result, emailSend, smsSend);
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
			result = new Result();
			ErrorCodeEnum.ERR_10059.setErrorCode(result);

		}
		return result;
	}

	private void updateErrorCode(Result result, boolean emailSend, boolean smsSend) {
		if (emailSend && smsSend) {
			HelperMethods.setSuccessMsg("Both Sms and email sent successfully.", result);
		} else if (emailSend) {
			HelperMethods.setSuccessMsg("Only email sent successfully.", result);
		} else if (smsSend) {
			HelperMethods.setSuccessMsg("Only Sms sent successfully.", result);
		} else {
			ErrorCodeEnum.ERR_10063.setErrorCode(result);

		}
	}

	private boolean validate(DataControllerRequest dcRequest) {
		return StringUtils.isNotBlank(dcRequest.getParameter("Phone"));
	}

}
