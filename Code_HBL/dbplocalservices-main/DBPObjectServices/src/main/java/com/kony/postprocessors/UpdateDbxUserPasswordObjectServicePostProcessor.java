package com.kony.postprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.google.gson.JsonObject;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class UpdateDbxUserPasswordObjectServicePostProcessor

		implements ObjectServicePostProcessor, ObjectServicesConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

		try {
			PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
			PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
			JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
			JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
			JsonObject customParams = new JsonObject();

			String opstatus = "";
			String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
					requestManager);
			String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
			String eventType = PARAM_CREDENTIAL_CHANGE;
			String eventSubType = PARAM_PSWD_CHANGE;
			String producer = "DbxUser/UpdateDbxUserPassword";
			String statusId = PARAM_SID_EVENT_FAILURE;
			String oldPassword = null;
			String newPassword = null;

			if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
				opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
			}
			if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
				statusId = PARAM_SID_EVENT_SUCCESS;
			}

			diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

			if (requestPayload.has(PARAM_OLD_PSWD)) {
				oldPassword = requestPayload.get(PARAM_OLD_PSWD).getAsString();
				requestPayload.remove("oldPassword");
				requestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
			}
			if (requestPayload.has(PARAM_NEW_PSWD)) {
				newPassword = requestPayload.get(PARAM_NEW_PSWD).getAsString();
				requestPayload.remove(PARAM_NEW_PSWD);
				requestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
			}

			if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {
				try {

					ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responseManager,
							eventType, eventSubType, producer, statusId, null, customerid, customParams));
				} catch (Exception e2) {
					alert.prepareError("Exception Occured while invoking objectServiceHelperMethods").log();
				}

			}

			if (oldPassword != null) {
				requestPayload.addProperty("oldPassword", oldPassword);
				requestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
			}

			if (newPassword != null) {
				requestPayload.addProperty("newPassword", newPassword);
				requestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
			}

		} catch (Exception ex) {
			diagnostic.prepareDebug("exception occured in ObjectService PostProcessor while resetting DBX User Password =", ex).log();
		}

	}

}
