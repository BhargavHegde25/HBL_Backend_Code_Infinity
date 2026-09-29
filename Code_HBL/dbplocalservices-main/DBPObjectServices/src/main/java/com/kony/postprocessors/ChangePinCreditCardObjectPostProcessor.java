package com.kony.postprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.google.gson.JsonObject;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class ChangePinCreditCardObjectPostProcessor implements ObjectServicePostProcessor, ObjectServicesConstants {

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

            String opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = CREDIT_CARD;
            String eventSubType = CHANGE_PIN;
            String producer = "RBObjects/objects/Cards/changePIN";
            String statusId = PARAM_SID_EVENT_SUCCESS;
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);

            diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();
            if (!opstatus.equals("0") || ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_FAILURE;
            }

            if (requestPayload != null && requestPayload.has(PARAM_NEW_PIN)) {
                requestPayload.remove(PARAM_NEW_PIN);
            }
            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {

                EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer, statusId,
                        null, customerid, customParams);
            }

        } catch (Exception ex) {
            diagnostic.prepareDebug("exception occured in ObjectService PostProcessor while Fetching number of cards", ex).log();
        }

    }

}
