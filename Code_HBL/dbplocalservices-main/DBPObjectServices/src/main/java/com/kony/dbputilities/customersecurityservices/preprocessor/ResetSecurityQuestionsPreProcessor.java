package com.kony.dbputilities.customersecurityservices.preprocessor;

import java.util.Calendar;
import java.util.Date;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
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

public class ResetSecurityQuestionsPreProcessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	PayloadHandler requestPayloadHandler = null;
	PayloadHandler responsePayloadHandler = null;

	FabricRequestChain requestChain = null;

	JsonObject mfaAttributes = null;

	String serviceKeycreatedTime = null;

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
			FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();

		JsonObject resultJson = new JsonObject();
		responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
				ContentType.APPLICATION_JSON.getMimeType());

		this.requestPayloadHandler = requestManager.getPayloadHandler();
		this.responsePayloadHandler = responseManager.getPayloadHandler();

		if (requestPayloadHandler.getPayloadAsJson() == null || requestPayloadHandler.getPayloadAsJson().isJsonNull()) {
			ErrorCodeEnum.ERR_10624.setErrorCode(resultJson);
			responsePayloadHandler.updatePayloadAsJson(resultJson);
			return;
		}

		JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
		boolean isMFAPresent = false;

		try {

			JsonElement mfaElement = requestpayload.get(MFAConstants.MFA_ATTRIBUTES);
			if (mfaElement.isJsonObject()) {
				mfaAttributes = requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
			} else {
				JsonParser parser = new JsonParser();
				mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
			}

			mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, MFAConstants.SERVICE_ID_PRELOGIN);
			requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
			requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
			isMFAPresent = true;

		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}

		if (!isMFAPresent) {
			ErrorCodeEnum.ERR_10624.setErrorCode(resultJson);
			responsePayloadHandler.updatePayloadAsJson(resultJson);
			return;
		}

		String serviceKey = new String();
		String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
		if (isMFAPresent) {
			if (mfaAttributes.has(MFAConstants.SERVICE_KEY)
					&& !mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull()) {
				serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
			}
		}

		if ((StringUtils.isBlank(serviceKey) && !serviceName.equals(MFAConstants.SERVICE_ID_PRELOGIN))) {
			ErrorCodeEnum.ERR_10625.setErrorCode(resultJson);
			responsePayloadHandler.updatePayloadAsJson(resultJson);
			return;
		}

		if (!isStateVerified(requestManager, serviceKey, serviceName)) {
			resultJson = new JsonObject();
			ErrorCodeEnum.ERR_10626.setErrorCode(resultJson);
			responsePayloadHandler.updatePayloadAsJson(resultJson);
			return;
		}
		requestChain.execute();
	}

	public boolean isStateVerified(FabricRequestManager requestManager, String serviceKey, String serviceName) {

		Result result = new Result();

		boolean status = false;
		String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
				+ MFAConstants.SERVICE_NAME + DBPUtilitiesConstants.EQUAL + serviceName;
		try {
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

			long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
			long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

			if (GeneratedMilliSeconds > verifyingMilliSeconds) {
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
}
