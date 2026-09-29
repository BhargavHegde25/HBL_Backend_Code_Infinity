package com.bct.cantsignin.mfa;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.ServiceId;

public class ValidateCantsigninOTP implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(ValidateCantsigninOTP.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		String customerid = request.getParameter("customerid");
		String securityKey = request.getParameter("securityKey");
		String serviceKey = request.getParameter("serviceKey");
		
		LOG.debug("customerid:"+ customerid);
		LOG.debug("securityKey:"+ securityKey);
		LOG.debug("serviceKey:"+ serviceKey);
		String otp = request.getParameter("OTP");
		
		Dataset otpDataset = getOTPFromDB(request, securityKey, serviceKey);
		String otpFromDB = otpDataset.getRecord(0).getParamValueByName("Otp");
		String createdts = otpDataset.getRecord(0).getParamValueByName("createdts");
		String InvalidAttempt = otpDataset.getRecord(0).getParamValueByName("InvalidAttempt");
		int maxFailedAttempts = Integer.parseInt(getOTPMaxFailedAttempts());
		
		LOG.debug("otpFromDB:"+ otpFromDB);
		LOG.debug("InvalidAttempt:"+ InvalidAttempt);
		
		if(StringUtils.isNotBlank(otpFromDB) && StringUtils.isNotBlank(otp)) {
			
			if(!isAttemptAllowed(InvalidAttempt, maxFailedAttempts)) {
				int remainingAttempts = maxFailedAttempts - (Integer.parseInt(InvalidAttempt));
				result.setParam(new Param("maxFailedAttemptsAllowed", maxFailedAttempts+""));
				result.setParam(new Param("remainingFailedAttempts", remainingAttempts+""));
				result.setParam(new Param("failedAttempts", InvalidAttempt));
				result.setParam(new Param("isOtpVerified", "false"));
				result.setParam(new Param("dbpErrMsg", "You have exceeded the maximum number of retry attempts. Please wait for some time and try again."));
				//lock user not required as it is prelogin
			}else if(isOTPExpired(createdts)) {
				result.setParam(new Param("isOTPExpired", "true"));
				int remainingAttempts = maxFailedAttempts - (Integer.parseInt(InvalidAttempt));
				int remainingAttemptsIncr = maxFailedAttempts - ((Integer.parseInt(InvalidAttempt) + 1));
				result.setParam(new Param("maxFailedAttemptsAllowed", maxFailedAttempts+""));
				result.setParam(new Param("remainingFailedAttempts", remainingAttemptsIncr+""));
				result.setParam(new Param("failedAttempts", (Integer.parseInt(InvalidAttempt) + 1)+""));
				result.setParam(new Param("isOtpVerified", "false"));
				result.setParam(new Param("dbpErrMsg", "The OTP has expired. Please wait for some time and try again."));
				
				updateAttemptCount(request, InvalidAttempt, securityKey);
			}else if(validateOTP(otp, otpFromDB)) {
				result.setParam(new Param("isOtpVerified", "true"));
				deleteOTP(request, securityKey);
			}else {
				updateAttemptCount(request, InvalidAttempt, securityKey);
				int remainingAttempts = maxFailedAttempts - (Integer.parseInt(InvalidAttempt));
				int remainingAttemptsIncr = maxFailedAttempts - ((Integer.parseInt(InvalidAttempt) + 1));
				result.setParam(new Param("maxFailedAttemptsAllowed", maxFailedAttempts+""));
				result.setParam(new Param("remainingFailedAttempts", remainingAttemptsIncr+""));
				result.setParam(new Param("failedAttempts", (Integer.parseInt(InvalidAttempt) + 1)+""));
				result.setParam(new Param("dbpErrMsg", "Secure Access Code didn't match. Recheck"));
				result.setParam(new Param("dbpErrCode", "10517"));
				result.setParam(new Param("isOtpVerified", "false"));
			}
		}else {
			result.setParam(new Param("dbpErrMsg", "Secure Access Code didn't match. Recheck"));
			result.setParam(new Param("dbpErrCode", "10517"));
			result.setParam(new Param("isOtpVerified", "false"));
		}
		result.setParam(new Param("opstatus", "0"));
		return result;
	}

	public static Dataset getOTPFromDB(DataControllerRequest request, String securityKey, String serviceKey) {
		Dataset otpDataset = null;
		try {

			String filter = "securityKey" + DBPUtilitiesConstants.EQUAL + securityKey + DBPUtilitiesConstants.AND
					+ "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey;
			LOG.debug("filter##:" + filter);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, "dbpRbLocalServicesdb",
					"dbxdb_OTP_get", false);
			otpDataset = result.getDatasetById("OTP");
			if (null != otpDataset) {
				return otpDataset;
			} else {
				LOG.debug("Else getOTPFromDB:");
				return otpDataset;
			}
		} catch (Exception e) {
			LOG.error("Error in exception getOTPFromDB" + e);
		}
		return otpDataset;
	}

	public static String getOTPMaxFailedAttempts() {
		String maxAttempts = "0";
		JSONArray configurations = new JSONArray();

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER,
				"MFA_id" + DBPUtilitiesConstants.EQUAL + "SECURE_ACCESS_CODE" + DBPUtilitiesConstants.AND + "MFAKey_id"
						+ DBPUtilitiesConstants.EQUAL + "MAX_FAILED_ATTEMPTS_ALLOWED");
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId("dbxdb_mfaconfigurations_get")
					.withRequestParameters(inputParams).build().getResponse();

			JSONObject responseJSON = new JSONObject(response);
			configurations = responseJSON.getJSONArray("mfaconfigurations");

			LOG.debug("configurations" + configurations);
			JSONObject conf = configurations.getJSONObject(0);
			maxAttempts = conf.getString("value");
			LOG.debug("MAX_FAILED_ATTEMPTS_ALLOWED VALUE:::" + maxAttempts);

		} catch (Exception e) {
			LOG.error("Exception caught while getPinMaxFailedAttempts", e);
		}
		return maxAttempts;
	}

	public static String GET_SAC_CODE_EXPIRES_AFTER() {
		String maxAttempts = "0";
		JSONArray configurations = new JSONArray();

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "MFA_id" + DBPUtilitiesConstants.EQUAL + "SECURE_ACCESS_CODE"
				+ DBPUtilitiesConstants.AND + "MFAKey_id" + DBPUtilitiesConstants.EQUAL + "SAC_CODE_EXPIRES_AFTER");
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId("dbxdb_mfaconfigurations_get")
					.withRequestParameters(inputParams).build().getResponse();

			JSONObject responseJSON = new JSONObject(response);
			configurations = responseJSON.getJSONArray("mfaconfigurations");

			LOG.debug("configurations" + configurations);
			JSONObject conf = configurations.getJSONObject(0);
			maxAttempts = conf.getString("value");
			LOG.debug("MAX_FAILED_ATTEMPTS_ALLOWED VALUE:::" + maxAttempts);

		} catch (Exception e) {
			LOG.error("Exception caught while getPinMaxFailedAttempts", e);
		}
		return maxAttempts;
	}

	public static boolean isAttemptAllowed(String InvalidAttempt, int maxFailedAttempts) {
		int InvalidAttempts = Integer.parseInt(InvalidAttempt);
		return InvalidAttempts < maxFailedAttempts;
	}

	public static boolean isOTPExpired(String date) {
		Date createdts = HelperMethods.getFormattedTimeStamp(date);
		Calendar generatedCal = Calendar.getInstance();
		generatedCal.setTime(createdts);

		Date verifyDate = new Date();
		Calendar verifyingCal = Calendar.getInstance();
		verifyingCal.setTime(verifyDate);

		int otpValidityTime = getSACCodeExpiretime();
		generatedCal.add(Calendar.MINUTE, otpValidityTime);

		long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
		long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

		if (GeneratedMilliSeconds < verifyingMilliSeconds) {
			return true;
		}
		return false;
	}

	public static boolean validateOTP(String otp, String otpFromDB) {
		return otp.equalsIgnoreCase(otpFromDB);
	}

	public static void deleteOTP(DataControllerRequest dcRequest, String securityKey) {
		HashMap<String, String> input = new HashMap<>();
		input.put("securityKey", securityKey);
		HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_DELETE);
	}

	public static void updateAttemptCount(DataControllerRequest dcRequest, String InvalidAttempts, String securityKey) {

		int InvalidAttempt = 0;
		if (StringUtils.isNotBlank(InvalidAttempts)) {
			InvalidAttempt = Integer.parseInt(InvalidAttempts);
		}
		Map<String, String> input = new HashMap<>();
		input.put("securityKey", securityKey);
		input.put("InvalidAttempt", String.valueOf(InvalidAttempt + 1));
		HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_UPDATE);

	}

	public static int getSACCodeExpiretime() {
		String value = GET_SAC_CODE_EXPIRES_AFTER();
		if (value != null)
			return Integer.parseInt(value);
		return 3;
	}
}
