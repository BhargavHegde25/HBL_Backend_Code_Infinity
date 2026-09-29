package com.temenos.infinity.api.holdings.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.kony.postprocessors.ObjectServicesConstants;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class GetAccountTransactionByTypeObjectServicePostProcessor
        implements ObjectServicePostProcessor, ObjectServicesConstants, ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {

        try {

            PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
            PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();

            JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonObject customParams = new JsonObject();
            String opstatus = "";
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);
            String statusId = PARAM_SID_EVENT_FAILURE;
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);

            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            }

            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {

                String transactionType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_TRANSACTION_TYPE,
                        true);
                String eventType = PARAM_ACCOUNT_ACTION;
                String eventSubType = "";
                String producer = "Transactions/getRecent";

                switch (transactionType) {
                    case PARAM_TRANSFERS:
                        eventSubType = PARAM_ACC_TRANS_TRANSFERS_FETCH;
                        break;

                    case PARAM_DEPOSITS:
                        eventSubType = PARAM_ACC_TRANS_DEPOSITS_FETCH;
                        break;

                    case PARAM_CHECKS:
                        eventSubType = PARAM_ACC_TRANS_CHECKS_FETCH;
                        break;

                    case PARAM_WITHDRAWLS:
                        eventSubType = PARAM_ACC_TRANS_WITHDRAWALS_FETCH;
                        break;

                    case PARAM_ALL:
                        eventSubType = PARAM_ACC_ALL_TRANSACTIONS_FETCH;
                        break;

                    default:
                        break;
                }

                JsonArray transactionsArray = responsePayload.getAsJsonArray("Transactions");
                responsePayload.addProperty("noOfTransactions", Integer.toString(transactionsArray.size()));
                responsePayload.remove("Transactions");
                try {

                    ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responsePayload,
                            eventType, eventSubType, producer, statusId, null, customerid, customParams));
                } catch (Exception e2) {
                    alert.prepareError("Exception Occured while invoking objectServiceHelperMethods").log();
                }
            }

        } catch (Exception ex) {
            alert.prepareError("exception occured in GetAccountTransactionByTypeObjectServicePostProcessor ", ex).log();
        }

    }

    @Override
    public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
            throws Exception {
        execute(requestManager, responseManager);
        return true;
    }

}
