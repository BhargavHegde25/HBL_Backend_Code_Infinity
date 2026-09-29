package com.temenos.infinity.api.cards.preprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.preprocessors.CardUpdateMFAPreProcessor;
import com.kony.postprocessors.ObjectServicesConstants;
import com.kony.scaintegration.helper.Constants;
import com.kony.scaintegration.helper.ErrorCodeEnum;
import com.kony.scaintegration.helper.GetConfigParams;
import com.kony.scaintegration.helper.Helper;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class CardUpdatePushAuditPreProcessor implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
			throws Exception {
		Log4j2Configurator.getInstance();
		if (GetConfigParams.getIsScaEnabled() == null)
			GetConfigParams.setIsScaEnabled(Helper.getConfigProperty(Constants.ISSCAENABLED));
		if (GetConfigParams.getIsScaEnabled() == null) {
			alert.prepareError("IS_SCA_ENABLED runtime param must be set").log();
		}
		if (Boolean.parseBoolean(GetConfigParams.getIsScaEnabled()))
			return true;
		
		diagnostic.prepareDebug("MFA").log();
		boolean status = new CardUpdateMFAPreProcessor().process(requestManager, responseManager);
		String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters("ENABLE_EVENTS", requestManager);

		try {

			JsonObject requestPayload = new JsonObject();
			JsonObject responsePayload = new JsonObject();
			PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
			PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();

			try {
				responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
			} catch (Exception e) {
				alert.prepareError("response is null").log();
			}
			try {
				requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
			} catch (Exception e) {
				alert.prepareError("request is null").log();
			}

			JsonObject customParams = new JsonObject();

			String statusId = ObjectServicesConstants.PARAM_SID_EVENT_SUCCESS;
			String eventType = ObjectServicesConstants.CREDIT_CARD;
			String eventSubType = getEventSubType(requestPayload);
			String producer = "RBObjects/Cards/updateCard";

			if (responsePayload != null && responsePayload.has(ObjectServicesConstants.PARAM_DBP_ERR_CODE)) {
				statusId = ObjectServicesConstants.PARAM_SID_EVENT_FAILURE;
			}

			String customerid = null;
			JsonObject mfaAttr = new JsonObject();
			if (responsePayload != null && responsePayload.has(ObjectServicesConstants.PARAM_MFA_ATRIBUTES)
					&& responsePayload.get(ObjectServicesConstants.PARAM_MFA_ATRIBUTES) != null) {
				mfaAttr = requestPayload.get(ObjectServicesConstants.PARAM_MFA_ATRIBUTES).getAsJsonObject();
			}

			try {
				if (mfaAttr != null && mfaAttr.has("lockUser") && mfaAttr.get("lockUser") != null
						&& mfaAttr.get("lockUser").getAsString().equals("true")) {
					eventType = ObjectServicesConstants.PARAM_LOGIN;
					eventSubType = ObjectServicesConstants.PARAM_ACCOUNT_LOCKED;
					statusId = ObjectServicesConstants.PARAM_SID_EVENT_SUCCESS;
				}
			} catch (Exception e) {
				alert.prepareError("Error while setting eventsubtype as ACCOUNT_LOCKED", e).log();
			}
			String newPin = "";
			alert.prepareError("requestPayload" + requestPayload.toString()).log();
			if (eventSubType.equals(ObjectServicesConstants.CARD_PIN_CHANGE_MFA) && requestPayload.has("newPin")) {
				newPin = requestPayload.get("newPin").getAsString();
				requestPayload.remove("newPin");
			}
			requestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
			if (enableEvents != null && enableEvents.equalsIgnoreCase("true")) {

				try {
					ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responseManager,
							eventType, eventSubType, producer, statusId, null, customerid, customParams));
				} catch (Exception e) {
					alert.prepareError("Error while pushing to Audit Engine", e).log();
				}
			}
			if (eventSubType.equals(ObjectServicesConstants.CARD_PIN_CHANGE_MFA)) {
				requestPayload.addProperty("newPin", newPin);
				requestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
			}
		} catch (Exception e) {
			alert.prepareError("error while pushing alert for cards mfa", e).log();
		}

		return status;
	}

	private static String getEventSubType(JsonObject requestPayload) {

		String eventsubtype = ObjectServicesConstants.CARD_UPDATE_MFA;
		try {
			if (requestPayload != null && requestPayload.has("Action") && requestPayload.get("Action") != null) {
				if (requestPayload.get("Action").getAsString().equals("Activate")) {
					eventsubtype = ObjectServicesConstants.CARD_ACTIVATE_MFA;
				} else if (requestPayload.get("Action").getAsString().equals("Lock")) {
					eventsubtype = ObjectServicesConstants.CARD_LOCK_MFA;
				} else if (requestPayload.get("Action").getAsString().equals("Replace Request")) {
					eventsubtype = ObjectServicesConstants.CARD_REPLACE_MFA;
				} else if (requestPayload.get("Action").getAsString().equals("Lost")) {
					eventsubtype = ObjectServicesConstants.CARD_LOST_MFA;
				} else if (requestPayload.get("Action").getAsString().equals("Cancel")) {
					eventsubtype = ObjectServicesConstants.CARD_CANCEL_MFA;
				} else if (requestPayload.get("Action").getAsString().equals("PinChange")) {
					eventsubtype = ObjectServicesConstants.CARD_PIN_CHANGE_MFA;
				}
			}
		} catch (Exception e) {
			alert.prepareError("unable to fetch action from request", e).log();
		}

		return eventsubtype;
	}
}
