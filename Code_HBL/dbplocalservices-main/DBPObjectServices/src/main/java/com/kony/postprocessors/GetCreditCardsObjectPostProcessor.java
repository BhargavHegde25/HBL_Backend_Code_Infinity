package com.kony.postprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class GetCreditCardsObjectPostProcessor
        implements ObjectServicePostProcessor, ObjectServicesConstants, ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static int getNoOfCardsFetched(JsonObject responsePayload) {
        int noOfcards = 0;
        alert.prepareError("inside get getNoOfCardsFetched response=" + responsePayload.toString()).log();

        if (responsePayload.has("records")) {
            JsonElement records = responsePayload.get("records");
            if (records.isJsonArray()) {
                noOfcards = records.getAsJsonArray().size();
            }
        }

        alert.prepareError("getNoOfCardsFetched=" + noOfcards).log();
        return noOfcards;
    }

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        try {

            PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
            PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
            JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
            if(responsePayload != null && responsePayload.toString().equals("{}")) {
            	throw new Exception("Skipping the execution for empty payload");
            }  
            JsonObject customParams = new JsonObject();

            String opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = CREDIT_CARD;
            String eventSubType = FETCH_CREDIT_CARDS;
            String producer = "TransactionObjects/objects/getCreditCardAccounts";
            String statusId = PARAM_SID_EVENT_SUCCESS;
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);

            try {
                customParams.addProperty("NumberOfCards", getNoOfCardsFetched(responsePayload));
            } catch (Exception e) {
                alert.prepareError("Exception occured while fetching the card count", e).log();
            }
            diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();
            if (!opstatus.equals("0") || ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_FAILURE;
            }

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {

                EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer, statusId,
                        null, customerid, customParams);
            }

        } catch (Exception ex) {
            diagnostic.prepareDebug("exception occured in ObjectService PostProcessor while Fetching number of cards", ex).log();
        }

    }

    @Override
    public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
            throws Exception {
        try {
            execute(requestManager, responseManager);
        } catch (Exception e) {
		Log4j2Configurator.getInstance();
            alert.prepareError("exception occured n get credit cards execute method", e).log();
        }
        return true;
    }

}
