package com.kony.postprocessors;

import java.util.Set;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.QueryParamsHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class DeleteBillPayeeObjectServicePostProcessor
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
            JsonObject customParams = new JsonObject();

            String opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = PARAM_BILL_PAYEE;
            String eventSubType = PARAM_BILL_PAYEE_DELETED;
            String producer = "";
            String statusId = PARAM_SID_EVENT_FAILURE;
            String isWiredRecepient = "";
            JsonObject queryParamjsonObject = new JsonObject();
            QueryParamsHandler queryParamsHandler = requestManager.getQueryParamsHandler();
            Set<String> parameterNames = queryParamsHandler.getParameterNames();
            for (String queryParamName : parameterNames) {
                queryParamjsonObject.addProperty(queryParamName, queryParamsHandler.getParameter(queryParamName));
            }
            requestPayloadHandler.updatePayloadAsJson(queryParamjsonObject);
            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }

            diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {

                if (ObjectServiceHelperMethods.hasKey(responsePayload, "isWiredRecepient")) {
                    isWiredRecepient = HelperMethods.getStringFromJsonObject(responsePayload, "isWiredRecepient");
                    if (isWiredRecepient.equalsIgnoreCase("true")) {
                        eventSubType = PARAM_WIRE_RECIPIENT_DELETED;
                    }
                }
                try {

                    ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responseManager,
                            eventType, eventSubType, producer, statusId, null, customerid, customParams));
                } catch (Exception e2) {
                    alert.prepareError("Exception Occured while invoking objectServiceHelperMethods").log();
                }

            }

        } catch (Exception ex) {
            diagnostic.prepareDebug("exception occured in ObjectService PostProcessor while Adding Bill Payee Recipient=", ex).log();
        }

    }

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        try {
            execute(fabricRequestManager, fabricResponseManager);
        } catch (Exception e) {
		Log4j2Configurator.getInstance();
            alert.prepareError("exception occured in execute method of delete bill payee", e).log();

        }
        return true;
    }

}
