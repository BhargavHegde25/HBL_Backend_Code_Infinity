package com.kony.dbputilities.objpreprocessors;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

public class PasswordPolicyPreprocessor implements ObjectServicePreProcessor {

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
			FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();

		new HashMap<>();
		Map<String, String> inputMap = new HashMap<>();
		responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
				ContentType.APPLICATION_JSON.getMimeType());
		String password = "";

		PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
		PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();

		JsonObject resultJson = new JsonObject();
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
		HelperMethods.getRecordMap(rules.get("usernamerules").toString());
		Map<String, String> passwordRules = HelperMethods.getRecordMap(rules.get("passwordrules").toString());

		if (StringUtils.isNotBlank(password)) {
			if (ValidatePassword(password, passwordRules, resultJson)) {
				requestChain.execute();
			} else {
				responsePayloadHandler.updatePayloadAsJson(resultJson);
			}
		}
	}

	private boolean ValidatePassword(String password, Map<String, String> passwordRules, JsonObject resultJson) {
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

		supportedSymbols = supportedSymbols.replaceAll(",", "");
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
		if (lowerCase) {
			Pattern pattern = Pattern.compile("^(?=[^a-z]*[a-z])");
			Matcher m = pattern.matcher(password);
			if (!m.lookingAt()) {
				ErrorCodeEnum.ERR_10127.setErrorCode(resultJson);
				return false;
			}
		}
		if (upperCase) {
			Pattern pattern = Pattern.compile("^(?=[^A-Z]*[A-Z])");
			Matcher m = pattern.matcher(password);
			if (!m.lookingAt()) {
				ErrorCodeEnum.ERR_10128.setErrorCode(resultJson);
				return false;
			}
		}
		if (number) {
			Pattern pattern = Pattern.compile("^(?=\\D*\\d)");
			Matcher m = pattern.matcher(password);
			if (!m.lookingAt()) {
				ErrorCodeEnum.ERR_10129.setErrorCode(resultJson);
				return false;
			}
		}
		if (symbol) {
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
