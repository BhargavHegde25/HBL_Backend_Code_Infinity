package com.kony.dbputilities.mfa.utils;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.LoginMFAUtil;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.PostLoginMFAUtil;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class OTPUtil {
	public static LoggerUtil logger = new LoggerUtil(OTPUtil.class);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public static int generateOtp(int length) {
		int floor = (int) Math.pow(10, length - 1);
		int ceil = floor * 9;
		SecureRandom rand = new SecureRandom();
		return (int) (floor + (rand.nextFloat() * ceil));
	}

	public static boolean isValidRequest(Result fetchMFAAndOTP) {
		Dataset dataset = fetchMFAAndOTP.getDatasetById("OTP");
		Record otp = new Record();
		if (dataset != null) {
			if (dataset.getAllRecords().size() > 0) {
				otp = dataset.getAllRecords().get(0);
			} else {
				otp = null;
			}
		}

		Dataset dataset1 = fetchMFAAndOTP.getDatasetById("mfaservice");
		Record mfaService = new Record();
		if (dataset1 != null) {
			if (dataset1.getAllRecords().size() > 0) {
				mfaService = dataset1.getAllRecords().get(0);
			} else {
				mfaService = null;
			}
		}

		if (otp != null && mfaService != null) {
			String customerId = fetchMFAAndOTP.getParamValueByName("customerId");
			if (StringUtils.isNotBlank(customerId) && customerId.equals(mfaService.getParamValueByName("User_id"))) {
				return true;
			}
		}
		return false;
	}

	public	static void deleteOTP(DataControllerRequest dcRequest, String securityKey) {
		HashMap<String, String> input = new HashMap<>();
		input.put("securityKey", securityKey);
		HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_DELETE);
	}

	public	static void setVerifyAttributes(DataControllerRequest dcRequest, Result result, Result retRes,
			LoginMFAUtil mfaUtil) throws HttpCallException {
		int InvalidAttempt = 0;
		String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
		if (StringUtils.isNotBlank(str)) {
			InvalidAttempt = Integer.parseInt(str);
		}

		int allowedAttempt = mfaUtil.mfaConfigurationUtil.maxFailedAttemptsAllowed();
		int attemptsLeft = (allowedAttempt - (InvalidAttempt + 1));
		attemptsLeft = attemptsLeft < 0 ? 0 : attemptsLeft;
		retRes.addParam(new Param("maxFailedAttemptsAllowed", allowedAttempt + "", DBPUtilitiesConstants.STRING_TYPE));
		retRes.addParam(new Param("remainingFailedAttempts", attemptsLeft + "", DBPUtilitiesConstants.STRING_TYPE));
		retRes.addParam(new Param("failedAttempts", (InvalidAttempt + 1) + "", DBPUtilitiesConstants.STRING_TYPE));

		if (attemptsLeft <= 0) {
			String lockoutTime = mfaUtil.getLockoutTime();
			retRes.addParam(new Param("remainingFailedAttempts", "0", DBPUtilitiesConstants.STRING_TYPE));
			if (mfaUtil.shouldLockUser()) {
				retRes.addParam(new Param("lockUser", "true", "String"));
				retRes.addParam(new Param("lockoutTime", lockoutTime, "int"));
			} else {
				retRes.addParam(new Param("lockUser", "false", "String"));
			}

			if (mfaUtil.shouldLogoutUser()) {
				retRes.addParam(new Param("logoutUser", "true", "String"));
				retRes.addParam(new Param("lockoutTime", lockoutTime, "int"));
			} else {
				retRes.addParam(new Param("logoutUser", "false", "String"));
			}
			ErrorCodeEnum.ERR_10519.setErrorCode(retRes);
		}
	}

	public	static void updateAttemptCount(DataControllerRequest dcRequest, Result result, Result retRes,
			LoginMFAUtil mfaUtil) {

		int InvalidAttempt = 0;
		String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
		if (StringUtils.isNotBlank(str)) {
			InvalidAttempt = Integer.parseInt(str);
		}
		Map<String, String> input = new HashMap<>();
		input.put("securityKey", HelperMethods.getFieldValue(result, "securityKey"));
		input.put("InvalidAttempt", String.valueOf(InvalidAttempt + 1));
		HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_UPDATE);

		int allowedAttempt = mfaUtil.mfaConfigurationUtil.maxFailedAttemptsAllowed();
		retRes.addParam(new Param("maxFailedAttemptsAllowed", allowedAttempt + "", DBPUtilitiesConstants.STRING_TYPE));
		retRes.addParam(new Param("remainingFailedAttempts", (allowedAttempt - (InvalidAttempt + 1)) + "",
				DBPUtilitiesConstants.STRING_TYPE));
		retRes.addParam(new Param("failedAttempts", (InvalidAttempt + 1) + "", DBPUtilitiesConstants.STRING_TYPE));
	}

	public	static boolean isAttemptAllowed(DataControllerRequest dcRequest, Result result, LoginMFAUtil mfaUtil) {
		String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
		if (StringUtils.isNotBlank(str)) {
			int allowedAttempt = mfaUtil.mfaConfigurationUtil.maxFailedAttemptsAllowed();
			int InvalidAttempt = Integer.parseInt(str);
			return allowedAttempt > InvalidAttempt;
		}
		return true;
	}
	
	public	static boolean isOTPExpired(DataControllerRequest dcRequest, Result result, LoginMFAUtil mfaUtil) {
        Date createdts = HelperMethods.getFormattedTimeStamp(HelperMethods.getFieldValue(result, "createdts"));
        Calendar generatedCal = Calendar.getInstance();
        generatedCal.setTime(createdts);

        Date verifyDate = new Date();
        Calendar verifyingCal = Calendar.getInstance();
        verifyingCal.setTime(verifyDate);

        int otpValidityTime = mfaUtil.mfaConfigurationUtil.getSACCodeExpiretime();
        generatedCal.add(Calendar.MINUTE, otpValidityTime);

        long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
        long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

        if (GeneratedMilliSeconds < verifyingMilliSeconds) {
            return true;
        }

        return false;
    }

    public static boolean validateOTP(DataControllerRequest dcRequest, Result result) {
        String Otp = HelperMethods.getFieldValue(result, "Otp");
        String serviceKey = HelperMethods.getFieldValue(result, "serviceKey");
        boolean status = true;

        status = StringUtils.isBlank(serviceKey) ? false : serviceKey.equals(dcRequest.getParameter("serviceKey"));

        return (Otp.equals(dcRequest.getParameter("OTP")) || Otp.equals(dcRequest.getParameter("Otp"))) && status;
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
            	alert.prepareError("Caught exception while Getting OTP count : ",  e).log();
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
    
    public static String getMaskedPhoneNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder(phoneNumber);
        for (int i = 3; i < sb.length() - 2; ++i) {
            sb.setCharAt(i, 'X');
        }
        return sb.toString();
    }

    public static String getMaskedEmail(String email) {
        StringBuilder sb = new StringBuilder(email);
        for (int i = 3; i < sb.length() && sb.charAt(i) != '@'; ++i) {
            sb.setCharAt(i, 'X');
        }
        return sb.toString();
    }

    public static void removeServiceKey(String serviceKey, DataControllerRequest dcRequest) {

    	diagnostic.prepareDebug("Removing Service Key for Login : "+serviceKey).log();
        Map<String, String> map = new HashMap<>();
        map.put(MFAConstants.SERVICE_KEY, serviceKey);
        HelperMethods.callApiAsync(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                URLConstants.MFA_SERVICE_DELETE);
        diagnostic.prepareDebug("Removed ServiceKey : "+ serviceKey).log();
        
    }

    public static String processMobile(String mobile) {

        if (StringUtils.isBlank(mobile)) {
            return mobile;
        }

        String mobilenumber = "";

        if (!mobile.contains("+")) {
            mobilenumber = "+";
        }
        String[] strings = mobile.split("-");
        for (int i = 0; i < strings.length; i++) {
            mobilenumber += strings[i];
        }

        return mobilenumber;
    }
    
    
    public  static Record requestOTP(JsonObject OTP, Record mfa_meta, String serviceKey, DataControllerRequest dcRequest) {
        Record mfaAttributes = new Record();

        boolean isPhoneValid = true;
        boolean isEmailValid = true;

        Result result = null;

        if (isPhoneValid || isEmailValid) {

            Map<String, String> map = new HashMap<>();
            map.put(MFAConstants.SERVICE_KEY, serviceKey);
            if (OTP.has(MFAConstants.PHONE)) {
                map.put("Phone", OTP.get(MFAConstants.PHONE).getAsString());
            }
            if (OTP.has(MFAConstants.EMAIL)) {
                map.put("Email", OTP.get(MFAConstants.EMAIL).getAsString());
            }

            map.put(MFAConstants.SERVICE_NAME, dcRequest.getParameter(MFAConstants.SERVICE_NAME));

            if (OTP.has(MFAConstants.SECURITY_KEY)) {
                map.put(MFAConstants.SECURITY_KEY, OTP.get(MFAConstants.SECURITY_KEY).getAsString());
            }

            try {
                result = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeadersWithReportingParams(dcRequest),
                        URLConstants.REQUEST_MFA_OTP);
            } catch (HttpCallException e) {

            	alert.prepareError("Caught exception while Requesting OTP : ",  e).log();
            }
            if (result != null && result.getNameOfAllParams().contains(DBPUtilitiesConstants.SUCCESS)) {
                mfaAttributes = new Record();
                mfaAttributes.addParam(new Param(MFAConstants.SECURITY_KEY,
                        result.getParamByName(MFAConstants.SECURITY_KEY).getValue()));

                mfaAttributes.addParam(new Param("sacMaxResendRequestsAllowed",
                        result.getParamByName("sacMaxResendRequestsAllowed").getValue()));
                if (result.getNameOfAllParams().contains("remainingResendAttempts")) {
                    mfaAttributes.addParam(new Param("remainingResendAttempts",
                            result.getParamByName("remainingResendAttempts").getValue()));
                    mfaAttributes
                            .addParam(new Param("sacCodeLength", result.getParamByName("sacCodeLength").getValue()));
                }
                mfaAttributes.setId(MFAConstants.MFA_ATTRIBUTES);
                mfa_meta.addRecord(mfaAttributes);
                mfa_meta.addParam(new Param(DBPUtilitiesConstants.SUCCESS,
                        result.getParamByName(DBPUtilitiesConstants.SUCCESS).getValue()));
            } else {
                if (result != null) {
                    if (result.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
                        mfa_meta.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY,
                                result.getParamByName(ErrorCodeEnum.ERROR_MESSAGE_KEY).getValue()));
                    }
                    if (result.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_CODE_KEY)) {
                        mfa_meta.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY,
                                result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY).getValue()));
                    }
                    if (result.getNameOfAllParams().contains(DBPUtilitiesConstants.VALIDATION_ERROR)) {
                        mfa_meta.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY,
                                result.getParamByName(DBPUtilitiesConstants.VALIDATION_ERROR).getValue()));
                    }
                    return mfa_meta;
                }

                ErrorCodeEnum.ERR_10063.setErrorCode(mfa_meta);

            }
        } else {
            ErrorCodeEnum.ERR_10058.setErrorCode(mfa_meta);
        }
        return mfa_meta;
    }
    
    
    public static boolean isValidPhone(JsonObject OTP, boolean isPrimary, JsonObject referenceIdMappings) {
        return referenceIdMappings.has(OTP.get("phone").getAsString());
    }

    public static boolean isValidEmail(JsonObject OTP, boolean isPrimary, JsonObject referenceIdMappings) {
        return referenceIdMappings.has(OTP.get("email").getAsString());
    }

    public static void updateSecurityQuestionsRetryCount(int retryCount, JsonArray securityQuestionsArray, FabricRequestManager requestManager) {

        JsonObject mfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

        String serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();

        Map<String, String> map = new HashMap<>();

        map.put(MFAConstants.SERVICE_KEY, serviceKey);
        map.put(MFAConstants.RETRY_COUNT, (retryCount + 1) + "");
        try {
            map.put(MFAConstants.SECURITY_QUESTIONS_DB, CryptoText.encrypt(securityQuestionsArray.toString()));
        } catch (Exception e) {

        	alert.prepareError("Caught exception while Updating SecurityQuestions Retry count : ",  e).log();
        }
        HelperMethods.callApiAsync(requestManager, map, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_UPDATE);
    }
    
}
