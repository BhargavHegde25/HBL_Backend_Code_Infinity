package com.kony.dbputilities.objpreprocessors;

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

public class UsernamePolicyPreprocessor implements ObjectServicePreProcessor {
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
		String userName = "";

		PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
		PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();

		JsonObject resultJson = new JsonObject();
		JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

		if (requestpayload.get("UserName") != null) {
			userName = requestpayload.get("UserName").getAsString();
		}

		if (requestpayload.get("newUserName") != null) {
			userName = requestpayload.get("newUserName").getAsString();
		}

		JsonObject rules = AdminUtil.invokeAPIAndGetJson(inputMap, URLConstants.ADMIN_USERNAME_PASSWORD_RULES,
				requestManager);
		Map<String, String> usernameRules = HelperMethods.getRecordMap(rules.get("usernamerules").toString());

		if (StringUtils.isNotBlank(userName)) {
			if (validateUsername(userName, usernameRules, resultJson)) {
				requestChain.execute();
			} else {
				responsePayloadHandler.updatePayloadAsJson(resultJson);
			}
		}
	}

	private boolean validateUsername(String userName, Map<String, String> usernameRules, JsonObject resultJson) {

		int userMinLength = (usernameRules.get("minLength") != null ? Integer.parseInt((usernameRules.get("minLength")))
				: 8);
		Boolean symbolsAllowed = (usernameRules.get("symbolsAllowed") != null
				? Boolean.parseBoolean((usernameRules.get("symbolsAllowed")))
				: true);
		int userMaxLength = (usernameRules.get("maxLength") != null ? Integer.parseInt((usernameRules.get("maxLength")))
				: 64);
		String supportedSymbols = (usernameRules.get("supportedSymbols") != null
				? (usernameRules.get("supportedSymbols"))
				: ".,-,_,@,!,#,$");
		String givenSupportedSymbols = supportedSymbols;

		alert.prepareError("supported symbols before: " + supportedSymbols).log();
		supportedSymbols = supportedSymbols.replaceAll(",", "");
		if (supportedSymbols.contains("-")) {
			supportedSymbols = supportedSymbols.replace("-", "\\-");
		}
		alert.prepareError("supported symbols after: " + supportedSymbols).log();

		if (StringUtils.isBlank(userName)) {
			ErrorCodeEnum.ERR_10121.setErrorCode(resultJson);
			return false;
		}
		if (!(userName.length() >= userMinLength && userName.length() <= userMaxLength)) {
			ErrorCodeEnum.ERR_10122.setErrorCode(resultJson,
					" Username length should be in between the limits " + userMinLength + " and " + userMaxLength);
			return false;
		}
		if (symbolsAllowed) {
			Pattern newpattern = Pattern.compile("^(?=.*[^\\s\\w\\d" + supportedSymbols + "])");
			alert.prepareError("special chars other than these" + "^(?=.*[^\\s\\w\\d" + supportedSymbols + "])").log();
			Matcher newm = newpattern.matcher(userName);
			if (newm.find()) {
				ErrorCodeEnum.ERR_10124.setErrorCode(resultJson, "special characters other than these "
						+ givenSupportedSymbols + " are not allowed for username");
				return false;
			}
		} else {
			Pattern pattern = Pattern.compile("[$&+,:;=?@#|'<>.^*()%!-]");
			Matcher m = pattern.matcher(userName);
			if (m.find()) {
				ErrorCodeEnum.ERR_10153.setErrorCode(resultJson,
						"Special Characters should not be included in your Username.");
				return false;
			}

		}

		return true;

	}

}
