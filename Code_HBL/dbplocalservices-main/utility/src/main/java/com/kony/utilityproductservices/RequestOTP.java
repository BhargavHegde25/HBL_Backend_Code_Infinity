package com.kony.utilityproductservices;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.MFAConstants;
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

public class RequestOTP implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		String securityKey = null;
		int otp = generateOtp();
		String userid = HelperMethods.getCustomerIdFromSession(dcRequest);
		boolean valid = validateMandatoryFields(dcRequest, result) && isRequestAllowed(dcRequest, result);
		if (valid) {
			Map<String, String> inputParams = new HashMap<>();
			securityKey = generateSecurityKey();
			otp = generateOtp();
			inputParams.put("Phone", dcRequest.getParameter("Phone"));
			inputParams.put(MFAConstants.SECURITY_KEY, securityKey);
			inputParams.put("Otp", String.valueOf(otp));
			inputParams.put("OtpType", dcRequest.getParameter("OtpType"));
			if (StringUtils.isNotBlank(userid)) {
				inputParams.put("User_id", userid);
			}
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.OTP_CREATE);
		}
		if (valid && !HelperMethods.hasError(result)) {
			result = postProcess(securityKey, otp);
		}

		/*
		 * ResultCache cache = dcRequest.getServiceManager().getResultCache();
		 * cache.insertIntoCache(securityKey, String.valueOf(otp),
		 * Integer.parseInt(URLConstants.OTP_VALIDITY_TIME));
		 */
		return result;
	}

	private boolean isRequestAllowed(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		String phone = dcRequest.getParameter("Phone");
		if (StringUtils.isNotBlank(phone)) {
			String today = HelperMethods.getCurrentDate();
			String startDate = today + "T00:00:00";
			String endDate = today + "T23:59:59";
			String filter = "Phone" + DBPUtilitiesConstants.EQUAL + phone + DBPUtilitiesConstants.AND + "("
					+ "createdts" + DBPUtilitiesConstants.GREATER_EQUAL + startDate + DBPUtilitiesConstants.AND
					+ "createdts" + DBPUtilitiesConstants.LESS_EQUAL + endDate + ")";
			Result otpCount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.OTP_GET);
			if (HelperMethods.hasRecords(otpCount)) {
				int count = otpCount.getAllDatasets().get(0).getAllRecords().size();
				int allowedCount = getAllowedCount(dcRequest);
				if (count > allowedCount) {
//					HelperMethods
//							.setValidationMsgwithCode(
//									"OTP request limit is excceeded than allowed number.",
//									ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS,
//									result);
					ErrorCodeEnum.ERR_10022.setErrorCode(result);
					return false;
				}
			}
		}
		return true;
	}

	private int getAllowedCount(DataControllerRequest dcRequest) {
		String requestLimit = URLFinder.getPathUrl(URLConstants.OTP_REQUEST_LIMIT, dcRequest);
		if (StringUtils.isNotBlank(requestLimit)) {
			return Integer.parseInt(requestLimit);
		}
		return 6;
	}

	private boolean validateMandatoryFields(DataControllerRequest dcRequest, Result result) {
//		if(StringUtils.isBlank(dcRequest.getParameter("Phone"))){
//			HelperMethods.setValidationMsgwithCode("Mandatory params are missing.", 
//					ErrorCodes.ERROR_CREATING_RECORD_MANDATORY_INFORMATION_MISS, result);
//			return false;
//		}
		return true;
	}

	private Result postProcess(String securityKey, int otp) {
		Result result = new Result();
		Param sKeyParam = new Param(MFAConstants.SECURITY_KEY, securityKey, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		Param otpParam = new Param("Otp", String.valueOf(otp), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		result.addParam(sKeyParam);
		result.addParam(otpParam);
		return result;
	}

	private int generateOtp() {
		SecureRandom rand = new SecureRandom();
		return (int) (100000 + (rand.nextFloat() * 900000));
	}

	private String generateSecurityKey() {
		return UUID.randomUUID().toString();
	}
}