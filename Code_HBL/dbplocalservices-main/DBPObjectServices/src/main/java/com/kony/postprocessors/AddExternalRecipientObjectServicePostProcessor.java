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

public class AddExternalRecipientObjectServicePostProcessor
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

            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = PARAM_TRANSFER_RECIPIENT;
            String eventSubType = "";
            String producer = "RBObjects/objects/ExternalAccounts/POST";
            String statusId = PARAM_SID_EVENT_FAILURE;
            String opstatus = "";
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);

            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            }

            diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();
            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {

                if ((ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_IS_INTERNATIONAL_ACCOUNT))
                        && (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_IS_SAME_BANK_ACCOUNT))) {

                    String isInternationalAccount = HelperMethods.getStringFromJsonObject(requestPayload,
                            PARAM_IS_INTERNATIONAL_ACCOUNT, true);

                    String isSameBankAccount = HelperMethods.getStringFromJsonObject(requestPayload,
                            PARAM_IS_SAME_BANK_ACCOUNT, true);

                    eventSubType = deriveSubTypeForAddRecipient(isInternationalAccount, isSameBankAccount);

                    if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_NICK_NAME)) {
                        customParams.addProperty("RecipientNickName",
                                HelperMethods.getStringFromJsonObject(requestPayload, PARAM_NICK_NAME, true));
                    }
                }
                try {

                    ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responseManager,
                            eventType, eventSubType, producer, statusId, null, customerid, customParams));
                } catch (Exception e2) {
                    alert.prepareError("Exception Occured while invoking objectServiceHelperMethods", e2).log();
                }
            }

        } catch (Exception ex) {
            alert.prepareError("exception occured in ObjectService PostProcessor while Adding External Recipient=", ex).log();
        }

    }

    String deriveSubTypeForAddRecipient(String isInternationalAccount, String isSameBankAccount) {
        if (isInternationalAccount.equalsIgnoreCase(PARAM_TRUE) && isSameBankAccount.equalsIgnoreCase(PARAM_FALSE)) {
            return PARAM_INT_TRANSFER_RECIPIENT_ADDED;
        }
        if (isInternationalAccount.equalsIgnoreCase(PARAM_FALSE) && isSameBankAccount.equalsIgnoreCase(PARAM_FALSE)) {
            return PARAM_OTHER_BANK_RECIPIENT_ADDED;
        }
        if (isInternationalAccount.equalsIgnoreCase(PARAM_FALSE) && isSameBankAccount.equalsIgnoreCase(PARAM_TRUE)) {
            return PARAM_SAME_BANK_RECIPIENT_ADDED;
        }
        return "";
    }

}
