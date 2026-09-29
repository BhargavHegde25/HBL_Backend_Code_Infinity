package com.kony.dbputilities.mfa.postprocessors;

import java.util.Map.Entry;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.utils.TokenUtils;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class RequestLoginMFAOTPPostProcessor implements ObjectServicePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();
		String enableEvents = EnvironmentConfigurationsHandler.getValue("ENABLE_EVENTS", requestManager);
		JsonObject jsonObject = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		JsonObject requestPayload = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

		JsonObject response = new JsonObject();
		JsonObject mfaAttributes = new JsonObject();
		String statusId = MFAConstants.SID_EVENT_SUCCESS;
		for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			if (entry.getKey().equals(DBPUtilitiesConstants.SUCCESS)) {
				response.addProperty(DBPUtilitiesConstants.SUCCESS,
						jsonObject.get(DBPUtilitiesConstants.SUCCESS).getAsString());
			} else if (entry.getKey().equals(ErrorCodeEnum.ERROR_CODE_KEY)) {
				response.addProperty(ErrorCodeEnum.ERROR_CODE_KEY,
						jsonObject.get(ErrorCodeEnum.ERROR_CODE_KEY).getAsString());
				statusId = MFAConstants.SID_EVENT_FAILURE;
			} else if (entry.getKey().equals(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
				response.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY,
						jsonObject.get(ErrorCodeEnum.ERROR_MESSAGE_KEY).getAsString());
			} else if (entry.getKey().equals(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY)) {
				response.addProperty(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY,
						jsonObject.get(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY).getAsString());
			} else if (entry.getKey().equals(DBPConstants.FABRIC_OPSTATUS_KEY)) {
				response.addProperty(DBPConstants.FABRIC_OPSTATUS_KEY,
						jsonObject.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString());
			} else if (entry.getKey().equals(DBPUtilitiesConstants.IS_OTP_VERIFIED)) {
				response.addProperty(DBPUtilitiesConstants.IS_OTP_VERIFIED,
						jsonObject.get(DBPUtilitiesConstants.IS_OTP_VERIFIED).getAsString());
			} else {
				mfaAttributes.addProperty(entry.getKey(), entry.getValue().getAsString());
			}
		}

		if (!mfaAttributes.entrySet().isEmpty()) {
			response.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
		}

		responseManager.getPayloadHandler().updatePayloadAsJson(response);
		String eventType = MFAConstants.LOGIN;
		String eventSubType = MFAConstants.LOGIN_ATTEMPT_MFA;
		String producer = "RBObjects/DbxUser/requestLoginMFAOTP";

		JsonObject customParams = new JsonObject();
		customParams.addProperty(MFAConstants.MFA_STATE, getMFAState(requestPayload));
		customParams.addProperty(MFAConstants.MFA_TYPE, MFAConstants.SECURE_ACCESS_CODE);
		String customerid = null;
		String sessionid = null;

		try {
			String authkey = requestManager.getHeadersHandler().getHeader("X-Kony-Authorization");
			if (authkey != null && !authkey.equals("")) {
				TokenUtils tokenogj = new TokenUtils(authkey);
				customerid = tokenogj.getValue(URLConstants.PROVIDER_USER_ID);
				sessionid = tokenogj.getValue(URLConstants.SESSIONID);
			}
		} catch (Exception e) {
			alert.prepareError("Error while fetching cutomerid or sessionid").log();
		}

		customParams.addProperty("sessionId", sessionid);
		if (enableEvents != null && enableEvents.equalsIgnoreCase("true")) {
			try {
				EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer, statusId,
						null, customerid, customParams);
			} catch (Exception e) {
				alert.prepareError("Error while pushing to Audit Engine.").log();
			}
		}

	}

	private static String getMFAState(JsonObject requestPayload) {
		String mfastate = MFAConstants.REQUEST;
		try {
			if (requestPayload.has(MFAConstants.SECURITY_KEY)
					&& !requestPayload.get(MFAConstants.SECURITY_KEY).getAsString().equals("")) {
				mfastate = MFAConstants.RESEND;
			}

		} catch (Exception e) {
			alert.prepareError("error occured for state", e).log();
		}
		return mfastate;
	}
}
