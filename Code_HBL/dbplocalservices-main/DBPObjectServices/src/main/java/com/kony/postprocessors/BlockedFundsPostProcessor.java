package com.kony.postprocessors;

import java.util.Set;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.QueryParamsHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class BlockedFundsPostProcessor
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
            JsonObject queryParamjsonObject = new JsonObject();
            QueryParamsHandler queryParamsHandler = requestManager.getQueryParamsHandler();
            Set<String> parameterNames = queryParamsHandler.getParameterNames();
            JsonObject customParams = new JsonObject();
            String opstatus = "";
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);
            String statusId = PARAM_SID_EVENT_FAILURE;
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = PARAM_ACCOUNT_ACTION;
            String eventSubType = PARAM_BLOCKFUNDS_FETCH;
            String producer = "BlockFunds/getList";
            for (String queryParamName : parameterNames) {
                queryParamjsonObject.addProperty(queryParamName, queryParamsHandler.getParameter(queryParamName));
            }
            requestPayloadHandler.updatePayloadAsJson(queryParamjsonObject);
           
            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            }

            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {
                JsonArray transactionsArray = responsePayload.getAsJsonArray(PARAM_TRANSACTIONS);
                responsePayload.addProperty(PARAM_NO_OF_TRANSACTIONS, Integer.toString(transactionsArray.size()));
                responsePayload.remove(PARAM_TRANSACTIONS);
                EventsDispatcher.dispatch(requestManager, responsePayload, eventType, eventSubType, producer, statusId,
                        null, customerid, customParams);
            }

        } catch (Exception ex) {
            alert.prepareError("Exception occured in Search Transaction ObjectService PostProcessor ", ex).log();
        }

    }

    @Override
    public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
            throws Exception {
        try {
            execute(requestManager, responseManager);
        } catch (Exception e) {
		Log4j2Configurator.getInstance();

        }
        return true;
    }

}
