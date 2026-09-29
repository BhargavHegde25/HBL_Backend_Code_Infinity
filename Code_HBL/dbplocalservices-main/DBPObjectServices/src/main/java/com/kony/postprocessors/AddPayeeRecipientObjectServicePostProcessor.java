package com.kony.postprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class AddPayeeRecipientObjectServicePostProcessor
        implements ObjectServicePostProcessor, ObjectServicesConstants, ObjectProcessorTask {

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
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = PARAM_TRANSFER_RECIPIENT;
            String eventSubType = "";
            String producer = "RBObjects/operations/Payee/addRecipient";
            String statusId = PARAM_SID_EVENT_FAILURE;
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);

            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            }

            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }

            diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {

                if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_WIRE_ACCOUNT_TYPE)) {
                    String wireAccountType = HelperMethods.getStringFromJsonObject(requestPayload, "wireAccountType",
                            true);
                    if (wireAccountType.equalsIgnoreCase(PARAM_INTERNATIONAL)) {
                        eventSubType = PARAM_INT_WIRE_RECIPIENT_ADDED;
                    } else if (wireAccountType.equalsIgnoreCase(PARAM_DOMESTIC)) {
                        eventSubType = PARAM_DOM_WIRE_RECIPIENT_ADDED;
                    }
                }
                customParams = addPayeeNickName(requestPayload, customParams);
                try {

                    ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responseManager,
                            eventType, eventSubType, producer, statusId, null, customerid, customParams));
                } catch (Exception e2) {
                    alert.prepareError("Exception Occured while invoking objectServiceHelperMethods").log();
                }
            }

        } catch (Exception ex) {
            diagnostic.prepareDebug("exception occured in ObjectService PostProcessor while Adding Wire Payee Recipient=", ex).log();
        }

    }

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        try {
            execute(fabricRequestManager, fabricResponseManager);
        } catch (Exception e) {
		Log4j2Configurator.getInstance();
            alert.prepareError("Exception occured in add payee recipient post processor", e).log();

        }
        return true;
    }

    JsonObject addPayeeNickName(JsonObject requestPayload, JsonObject customParams) {
        if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PAYEE_NICKNAME)) {
            String payeeNickName = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_PAYEE_NICKNAME, true);
            customParams.addProperty("RecipientNickName", payeeNickName);
        }
        return customParams;
    }

}
