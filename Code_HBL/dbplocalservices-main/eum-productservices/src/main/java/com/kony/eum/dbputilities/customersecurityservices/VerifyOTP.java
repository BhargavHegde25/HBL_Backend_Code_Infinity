
package com.kony.eum.dbputilities.customersecurityservices;

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
import com.kony.dbputilities.util.URLFinder;
import com.kony.eum.dbputilities.mfa.GetOTP;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class VerifyOTP implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String otpBypass = URLFinder.getOTPByPassState(dcRequest);
		GetOTP getOTP = new GetOTP();
		result = (Result) getOTP.invoke(methodID, inputArray, dcRequest, dcResponse);
		result = postProcess(inputParams, dcRequest, result, otpBypass);
		return result;
	}

	private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
			String otpBypass) throws HttpCallException {
		Result retRes = new Result();

		if ("123456".equals(dcRequest.getParameter("Otp")) && "true".equalsIgnoreCase(otpBypass)) {
			retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "true", "String"));
			deleteOTP(dcRequest, result);
			return retRes;
		}

		if (HelperMethods.hasRecords(result)) {
			if (!isAttemptAllowed(dcRequest, result)) {
				ErrorCodeEnum.ERR_10072.setErrorCode(retRes);
			} else if (isOTPExpired(dcRequest, result)) {
				ErrorCodeEnum.ERR_10073.setErrorCode(retRes);
			} else if (validateOTP(dcRequest, result)) {
				//				HelperMethods.setSuccessMsg("Secure Access Code has been sent", retRes);
				retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "true", "String"));
				deleteOTP(dcRequest, result);
			} else {
				retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "false", "String"));
				ErrorCodeEnum.ERR_10074.setErrorCode(retRes);
				updateAttemptCount(dcRequest, result);
			}
		} else {
			ErrorCodeEnum.ERR_10075.setErrorCode(retRes);
			retRes.addParam(new Param(DBPUtilitiesConstants.IS_OTP_VERIFIED, "false", "String"));
		}

		return retRes;
	}

	private void deleteOTP(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		String securityKey = HelperMethods.getFieldValue(result, MFAConstants.SECURITY_KEY);
		if (StringUtils.isNotBlank(securityKey)) {
			HashMap<String, String> input = new HashMap<>();
			input.put(MFAConstants.SECURITY_KEY, HelperMethods.getFieldValue(result, MFAConstants.SECURITY_KEY));
			HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_DELETE);
		}
	}

	private void updateAttemptCount(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		int InvalidAttempt = 0;
		String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
		if (StringUtils.isNotBlank(str)) {
			InvalidAttempt = Integer.parseInt(str);
		}

		Map<String, String> input = new HashMap<>();
		input.put(MFAConstants.SECURITY_KEY, HelperMethods.getFieldValue(result, MFAConstants.SECURITY_KEY));
		input.put("InvalidAttempt", String.valueOf(InvalidAttempt + 1));
		HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_UPDATE);
	}

	private boolean isAttemptAllowed(DataControllerRequest dcRequest, Result result) {
		String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
		if (StringUtils.isNotBlank(str)) {
			int InvalidAttempt = Integer.parseInt(str);
			String validAttemptCnt = URLFinder.getPathUrl(URLConstants.OTP_ALLOWED_ATTEMPT, dcRequest);
			if (StringUtils.isNotBlank(validAttemptCnt)) {
				int allowedAttempt = Integer.parseInt(validAttemptCnt);
				return allowedAttempt > InvalidAttempt;
			}
		}

		return true;
	}

	private boolean isOTPExpired(DataControllerRequest dcRequest, Result result) {
		Date createdts = HelperMethods.getFormattedTimeStamp(HelperMethods.getFieldValue(result, "createdts"));
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(createdts);
		String string = URLFinder.getPathUrl(URLConstants.OTP_VALIDITY_TIME, dcRequest);
		int minutes = 10;
		if (StringUtils.isNotBlank(string)) {
			minutes = Integer.parseInt(string);
		}
		cal.add(Calendar.MINUTE, minutes);
		createdts = cal.getTime();
		if (now.after(createdts)) {
			return true;
		}

		return false;
	}

	private boolean validateOTP(DataControllerRequest dcRequest, Result result) {
		String Otp = HelperMethods.getFieldValue(result, "Otp");
		String OtpType = HelperMethods.getFieldValue(result, "OtpType");

		String userid = HelperMethods.getFieldValue(result, "User_id");
		String storedUserid = HelperMethods.getCustomerIdFromSession(dcRequest);
		if (StringUtils.isNotBlank(userid) && StringUtils.isNotBlank(storedUserid) && !storedUserid.equals(userid)) {
			return false;
		}

		boolean status = StringUtils.isBlank(OtpType) ? true : OtpType.equals(dcRequest.getParameter("OtpType"));
		return Otp.equals(dcRequest.getParameter("Otp")) && status;
	}
}
