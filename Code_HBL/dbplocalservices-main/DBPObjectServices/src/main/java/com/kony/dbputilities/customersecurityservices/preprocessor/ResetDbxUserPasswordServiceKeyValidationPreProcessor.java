package com.kony.dbputilities.customersecurityservices.preprocessor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.konylabs.middleware.dataobject.Result;

public class ResetDbxUserPasswordServiceKeyValidationPreProcessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
			FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();

		new HashMap<>();
		Map<String, String> inputMap = new HashMap<>();
		responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
				ContentType.APPLICATION_JSON.getMimeType());
		String password = "";
		JsonObject resultJson = new JsonObject();

		PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
		PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();

		JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

		if (requestpayload.has("Password") && !requestpayload.get("Password").isJsonNull()
				&& !requestpayload.get("Password").getAsString().isEmpty()) {
			password = requestpayload.get("Password").getAsString();
		}

		if (requestpayload.has("newPassword") && !requestpayload.get("newPassword").isJsonNull()
				&& !requestpayload.get("newPassword").getAsString().isEmpty()) {
			password = requestpayload.get("newPassword").getAsString();
		}

		JsonObject rules = AdminUtil.invokeAPIAndGetJson(inputMap, URLConstants.ADMIN_USERNAME_PASSWORD_RULES,
				requestManager);
		Map<String, String> passwordRules = HelperMethods.getRecordMap(rules.get("passwordrules").toString());

		if (StringUtils.isNotBlank(password)) {
			if (validatePassword(password, passwordRules, resultJson)) {

			} else {
				responsePayloadHandler.updatePayloadAsJson(resultJson);
				return;
			}
		}

		if (resultJson.has(DBPConstants.DBP_ERROR_CODE_KEY) || resultJson.has(DBPConstants.DBP_ERROR_MESSAGE_KEY)) {
			return;
		}

		JsonObject jsonObject = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

		String serviceKey = null;
		String userName = null;

		if (jsonObject.has("serviceKey")) {
			serviceKey = jsonObject.get("serviceKey").getAsString();
		}

		if (StringUtils.isBlank(serviceKey)) {
			resultJson = new JsonObject();
			ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
			responsePayloadHandler.updatePayloadAsJson(resultJson);
			return;
		}

		String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;

		if (jsonObject.has("UserName")) {
			userName = jsonObject.get("UserName").getAsString();
		}

		if (StringUtils.isBlank(userName)) {
			resultJson = new JsonObject();
			ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
			responsePayloadHandler.updatePayloadAsJson(resultJson);
			return;
		}

		String userid = getUserId(requestManager, userName);

		if (StringUtils.isBlank(userid) || !isStateVerified(requestManager, serviceKey, serviceName, userid)) {
			resultJson = new JsonObject();
			ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
			responsePayloadHandler.updatePayloadAsJson(resultJson);
			//return;
			//Commented above line to by pass service key validation google authenticator instead of MFA in can't sign in flow
		}
		requestChain.execute();

	}

	public boolean isStateVerified(FabricRequestManager requestManager, String serviceKey, String serviceName,
			String userid) {

		Result result = new Result();

		boolean status = false;
		String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
				+ MFAConstants.SERVICE_NAME + DBPUtilitiesConstants.EQUAL + serviceName;
		try {
			filter += DBPUtilitiesConstants.AND + "User_id" + DBPUtilitiesConstants.EQUAL + userid;
			result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
					URLConstants.MFA_SERVICE_GET);
			if (HelperMethods.hasRecords(result) && !isServiceKeyExpired(requestManager, result)) {
				status = "true".equals(HelperMethods.getFieldValue(result, MFAConstants.IS_VERIFIED));
			}
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
			status = false;
		}
		return status;
	}

	private boolean isServiceKeyExpired(FabricRequestManager requestManager, Result result) {

		String string = HelperMethods.getFieldValue(result, "Createddts");

		if (StringUtils.isNotBlank(string)) {
			Date createdts = HelperMethods.getFormattedTimeStamp(string);
			Calendar generatedCal = Calendar.getInstance();
			generatedCal.setTime(createdts);

			Date verifyDate = new Date();
			Calendar verifyingCal = Calendar.getInstance();
			verifyingCal.setTime(verifyDate);

			int otpValidityTime = getServiceKeyExpiretime(requestManager);
			generatedCal.add(Calendar.MINUTE, otpValidityTime);

			long generatedMilliSeconds = generatedCal.getTimeInMillis();
			long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

			if (generatedMilliSeconds > verifyingMilliSeconds) {
				return false;
			}
		}

		return true;
	}

	private int getServiceKeyExpiretime(FabricRequestManager requestManager) {
		try {
			return Integer.parseInt(URLFinder.getPathUrl(URLConstants.SERVICEKEY_EXPIRE_TIME, requestManager));
		} catch (Exception e) {
		}
		return 10;
	}

	private String getUserId(FabricRequestManager requestManager, String userName) {

		String filterQuery = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
		Result result = HelperMethods.callGetApi(requestManager, filterQuery, HelperMethods.getHeaders(requestManager),
				URLConstants.CUSTOMERVERIFY_GET);

		if (HelperMethods.hasError(result) || !HelperMethods.hasRecords(result)) {
			return null;
		}

		return HelperMethods.getFieldValue(result, "id");

	}

	private boolean validatePassword(String password, Map<String, String> passwordRules, JsonObject resultJson) {
		int passMinLength = (passwordRules.get("minLength") != null ? Integer.parseInt((passwordRules.get("minLength")))
				: 8);
		int passMaxLength = (passwordRules.get("maxLength") != null ? Integer.parseInt((passwordRules.get("maxLength")))
				: 64);
		Boolean lowerCase = (passwordRules.get("atleastOneLowerCase") != null
				? Boolean.parseBoolean((passwordRules.get("maxLength")))
				: true);
		Boolean upperCase = (passwordRules.get("atleastOneUpperCase") != null
				? Boolean.parseBoolean((passwordRules.get("atleastOneUpperCase")))
				: true);
		Boolean number = (passwordRules.get("atleastOneNumber") != null
				? Boolean.parseBoolean((passwordRules.get("atleastOneNumber")))
				: true);
		Boolean symbol = (passwordRules.get("atleastOneSymbol") != null
				? Boolean.parseBoolean((passwordRules.get("atleastOneSymbol")))
				: true);
		int charRepeatCount = (passwordRules.get("charRepeatCount") != null
				? Integer.parseInt((passwordRules.get("charRepeatCount")))
				: 4);
		String supportedSymbols = (passwordRules.get("supportedSymbols") != null
				? (passwordRules.get("supportedSymbols"))
				: ".,-,_,@,!,#,$");
		String givenSupportedSymbols = supportedSymbols;

		supportedSymbols = supportedSymbols.replace(",", "");
		if (supportedSymbols.contains("-")) {
			supportedSymbols = supportedSymbols.replace("-", "\\-");
		}

		if (StringUtils.isBlank(password)) {
			ErrorCodeEnum.ERR_10125.setErrorCode(resultJson);
			return false;
		}
		if (!(password.length() >= passMinLength && password.length() <= passMaxLength)) {
			ErrorCodeEnum.ERR_10126.setErrorCode(resultJson,
					" password length should be in between the limits " + passMinLength + " and " + passMaxLength);
			return false;
		}
		if (Boolean.TRUE.equals(lowerCase)) {
			Pattern pattern = Pattern.compile("^(?=[^a-z]*[a-z])");
			Matcher m = pattern.matcher(password);
			if (!m.lookingAt()) {
				ErrorCodeEnum.ERR_10127.setErrorCode(resultJson);
				return false;
			}
		}
		if (Boolean.TRUE.equals(upperCase)) {
			Pattern pattern = Pattern.compile("^(?=[^A-Z]*[A-Z])");
			Matcher m = pattern.matcher(password);
			if (!m.lookingAt()) {
				ErrorCodeEnum.ERR_10128.setErrorCode(resultJson);
				return false;
			}
		}
		if (Boolean.TRUE.equals(number)) {
			Pattern pattern = Pattern.compile("^(?=\\D*\\d)");
			Matcher m = pattern.matcher(password);
			if (!m.lookingAt()) {
				ErrorCodeEnum.ERR_10129.setErrorCode(resultJson);
				return false;
			}
		}
		if (Boolean.TRUE.equals(symbol)) {
			Pattern pattern = Pattern.compile("^(?=.*[" + supportedSymbols + "])");
			Matcher m = pattern.matcher(password);
			if (!m.lookingAt()) {
				ErrorCodeEnum.ERR_10130.setErrorCode(resultJson);
				return false;
			}
			Pattern newpattern = Pattern.compile("^(?=.*[^\\s\\w\\d" + supportedSymbols + "])");
			Matcher newm = newpattern.matcher(password);
			if (newm.find()) {
				ErrorCodeEnum.ERR_10131.setErrorCode(resultJson, "special characters other than these "
						+ givenSupportedSymbols + " are not allowed for password");
				return false;
			}
		}
		if (charRepeatCount > 0) {
			Pattern pattern = Pattern.compile("(.)\\1{" + charRepeatCount + ",}");
			Matcher m = pattern.matcher(password);
			if (m.find()) {
				ErrorCodeEnum.ERR_10132.setErrorCode(resultJson,
						"Maximum number of times a character can be repeated consecutively in the password is "
								+ charRepeatCount);
				return false;
			}
		}
		return true;
	}
}
