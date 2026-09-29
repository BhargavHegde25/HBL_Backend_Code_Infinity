package com.bct.cantsignin.mfa;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.Utils;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.common.net.HttpHeaders;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

public class getCantsigninMFAConfigurations implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(getCantsigninMFAConfigurations.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();

		String customerid = request.getParameter("customerid");
		LOG.debug("customerid: " + customerid);
		Dataset mfaConfigurations = spotlightMfaConfigurations(request);
		String Status_id = mfaConfigurations.getRecord(0).getParamValueByName("Status_id");
		String PrimaryMFAType = mfaConfigurations.getRecord(0).getParamValueByName("PrimaryMFAType");
		String SecondaryMFAType = mfaConfigurations.getRecord(0).getParamValueByName("SecondaryMFAType");
		String EmailSubject = mfaConfigurations.getRecord(0).getParamValueByName("EmailSubject");
		String EmailBody = mfaConfigurations.getRecord(0).getParamValueByName("EmailBody");
		String SMSText = mfaConfigurations.getRecord(0).getParamValueByName("EmailSubject");

		LOG.debug("Status_id: " + Status_id);
		LOG.debug("PrimaryMFAType: " + PrimaryMFAType);
		LOG.debug("SecondaryMFAType: " + SecondaryMFAType);

		String customerEmail = Utils.getCustomerEmialfromCore(request, customerid);
		String customerPhone = Utils.getCustomerPhonefromCore(request, customerid);
		boolean isOTPCreated = false;

		LOG.debug("customerEmail: " + customerEmail);
		LOG.debug("customerPhone: " + customerPhone);

		Dataset otpLength = getOTPLength(request);
		String otpLengthVal = otpLength.getRecord(0).getParamValueByName("value");
		
		int OTPLength = 6;
		
		if (StringUtils.isNotBlank(otpLengthVal)) {
			OTPLength = Integer.parseInt(otpLengthVal);
		}
		
		int otp = generateOtp(OTPLength);

		LOG.debug("OTPLength: " + OTPLength);
		LOG.debug("OTP Value: " + otp);

		if (Status_id.equalsIgnoreCase("SID_ACTIVE")) {
			String securityKey = getRandomKey();
			String serviceKey = getRandomKey();

			LOG.debug("securityKey: " + securityKey);
			LOG.debug("serviceKey: " + serviceKey);

			// send OTP to customer email and phone and store same otp in OTP table
			isOTPCreated = createOTP(request, securityKey, Integer.toString(otp), customerPhone, customerid, serviceKey,
					customerEmail, "0");

			if (isOTPCreated) {
				// send email with OTP
				// send sms with otp
				String mobileNumber = customerPhone.substring(customerPhone.length() - 10);
				String smsBody = SMSText + " " + otp;
				/***
				 * Send SMS service is taking time, so executing this service in background
				 ***/
				CompletableFuture.runAsync(() -> {
					backgroundTask(smsBody, mobileNumber);
				});
				sendEmailWithOTP(request, Integer.toString(otp), customerEmail);
			}

			result.setParam(new Param("OTPLength", Integer.toString(OTPLength)));
			result.setParam(new Param("OTP", Integer.toString(otp)));
			result.setParam(new Param("securityKey", securityKey));
			result.setParam(new Param("serviceKey", serviceKey));
		}

		result.setParam(new Param("Status_id", Status_id));
		result.setParam(new Param("customerEmail", customerEmail));
		result.setParam(new Param("customerPhone", customerPhone));
		result.setParam(new Param("PrimaryMFAType", PrimaryMFAType));
		result.setParam(new Param("SecondaryMFAType", SecondaryMFAType));

		result.setParam(new Param("opstatus", "0"));
		return result;
	}

	 static void backgroundTask(String smsBody, String mobileNumber) {
		sendSMSWithOTP(smsBody, mobileNumber);
	    }
	 
	private Dataset spotlightMfaConfigurations(DataControllerRequest request) {
		Dataset mfa = null;
		try {
			String currentAppId = "RETAIL_AND_BUSINESS_BANKING";
			String currentActionId = "CANT_SIGN_IN_ACTIVATE";

			String filter = "Action_id" + DBPUtilitiesConstants.EQUAL + currentActionId + DBPUtilitiesConstants.AND
					+ "App_id" + DBPUtilitiesConstants.EQUAL + currentAppId;
			LOG.debug("filter##:" + filter);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, "dbpRbLocalServicesdb",
					"dbxdb_mfa_get", false);
			mfa = result.getDatasetById("mfa");
			if (null != mfa) {
				return mfa;
			} else {
				LOG.debug("Else spotlightMfaConfigurations:");
				return mfa;
			}
		} catch (Exception e) {
			LOG.error("Error in spotlightMfaConfigurations");
		}
		return mfa;
	}

	
	private Dataset getOTPLength(DataControllerRequest request) {
		Dataset mfa = null;
		try {
			String MFAId = "SECURE_ACCESS_CODE";
			String MFAKey_id = "SAC_CODE_LENGTH";

			String filter = "MFA_id" + DBPUtilitiesConstants.EQUAL + MFAId + DBPUtilitiesConstants.AND
					+ "MFAKey_id" + DBPUtilitiesConstants.EQUAL + MFAKey_id;
			LOG.debug("filter##:" + filter);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, "dbpRbLocalServicesdb",
					"dbxdb_mfaconfigurations_get", false);
			mfa = result.getDatasetById("mfaconfigurations");
			if (null != mfa) {
				return mfa;
			} else {
				LOG.debug("Else spotlightMfaConfigurations:");
				return mfa;
			}
		} catch (Exception e) {
			LOG.error("Error in spotlightMfaConfigurations");
		}
		return mfa;
	}
	
	
	public static int generateOtp(int length) {
		int floor = (int) Math.pow(10, length - 1);
		int ceil = floor * 9;
		SecureRandom rand = new SecureRandom();
		return (int) (floor + (rand.nextFloat() * ceil));
	}

	public static String getRandomKey() {
		String randomKey = UUID.randomUUID().toString();
		return randomKey;
	}

	public static boolean createOTP(DataControllerRequest request, String securityKey, String Otp, String Phone,
			String User_id, String serviceKey, String Email, String NumberOfRetries) throws HttpCallException {
		boolean isinvalidattemptEntryMade = false;
		try {
			Map<String, Object> inputParams = new HashMap<>();

			LOG.debug("recordApiAuditLog ApiName" + securityKey);
			LOG.debug("recordApiAuditLog PlainPayload" + Otp);
			LOG.debug("recordApiAuditLog EncryptedRequest" + Phone);
			LOG.debug("recordApiAuditLog EncryptedResponse" + User_id);
			LOG.debug("recordApiAuditLog ValidatedResponse" + serviceKey);
			LOG.debug("recordApiAuditLog ValidatedResponse" + Email);
			LOG.debug("recordApiAuditLog ValidatedResponse" + NumberOfRetries);

			inputParams.put("securityKey", securityKey);
			inputParams.put("Otp", Otp);
			inputParams.put("OtpType", "");
			inputParams.put("InvalidAttempt", "0");
			inputParams.put("createdts", getTimestamp());
			inputParams.put("Phone", Phone);
			inputParams.put("User_id", User_id);
			inputParams.put("serviceKey", serviceKey);
			inputParams.put("Email", Email);
			inputParams.put("NumberOfRetries", NumberOfRetries);

			LOG.debug("BCT::createOTP: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CANTSIGNIN_OTP_CREATE).withRequestParameters(inputParams)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(request.getHeaderMap())
					.build().getResponse();
			LOG.debug("BCT::createOTP: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
				LOG.debug("BCT::createOTP failure:");
			} else {
				isinvalidattemptEntryMade = true;
				LOG.debug("BCT::createOTP success:");
			}
		} catch (Exception e) {
			LOG.debug("Couldn't create createOTP");
			return false;
		}

		return isinvalidattemptEntryMade;
	}

	public static String getTimestamp() {
		String localDateTime;
		if (LocalDateTime.now().getSecond() == 0) {
			localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
		} else {
			localDateTime = LocalDateTime.now().withNano(0).toString();
		}
		return localDateTime;
	}

	private static void sendSMSWithOTP(String body, String phonenumber) {
		try {
			LOG.debug("sendSms.phonenumber:" + phonenumber);
			LOG.debug("sendSms.body:" + body);
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("contact", phonenumber);
			inputParams.put("body", body);
			String smsResponse = DBPServiceExecutorBuilder.builder().withServiceId("sendHBLSMS")
					.withOperationId("sendHBLSMS").withRequestParameters(inputParams).withRequestHeaders(headerParams)
					.withDataControllerRequest(null).build().getResponse();
			LOG.debug("sendSms.response:" + smsResponse);
		} catch (Exception e) {
			LOG.debug("Error occured while sending SMS." + e);
		}
	}

	public static void sendEmailWithOTP(DataControllerRequest request, String otp, String email)
			throws HttpCallException, AppRegistryException {
		LOG.debug("email :###" + email);

		String emailTemplate = "CANTSIGININ_MFA";

		LOG.debug("triggerEmail value:" + email);
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("EmailType", emailTemplate);
		JSONObject addContext = new JSONObject();
		addContext.put("OTP", otp);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
	}
}
