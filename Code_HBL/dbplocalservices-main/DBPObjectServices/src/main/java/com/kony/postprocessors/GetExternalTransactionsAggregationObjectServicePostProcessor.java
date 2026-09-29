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

public class GetExternalTransactionsAggregationObjectServicePostProcessor
        implements ObjectServicePostProcessor, ObjectServicesConstants {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        try {
            PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
            JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonObject customParams = new JsonObject();

            String opstatus = "";
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters("ENABLE_EVENTS", requestManager);
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = PARAM_ACCOUNT_ACTION;
            String eventSubType = PARAM_EXTACC_TRANSACTIONS_FETCH;
            String producer = "GetExternalTransactionsAggregation/GET";
            String statusId = PARAM_SID_EVENT_FAILURE;

            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            }

            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {
                try {
                    ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responseManager,
                            eventType, eventSubType, producer, statusId, null, customerid, customParams));
                } catch (Exception e2) {
                    alert.prepareError("Exception Occured while invoking objectServiceHelperMethods").log();
                }
            }
        } catch (Exception ex) {
            alert.prepareError("exception occured in GetExternalTransactionsAggregationObjectServicePostProcessor ", ex).log();
        }

    }

}
