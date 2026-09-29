package com.temenos.infinity.api.accountsweeps.task;

import java.util.Set;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.constants.DBPConstants;
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
import com.konylabs.middleware.dataobject.Param;

public class DeleteweepAuditTask
        implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");


    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        try {

            PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
            PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
            JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
            
            JsonObject queryParamjsonObject = new JsonObject();
            QueryParamsHandler queryParamsHandler = requestManager.getQueryParamsHandler();
            Set<String> parameterNames = queryParamsHandler.getParameterNames();
            JsonObject customParams = new JsonObject();
            
            String primaryAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "primaryAccountNumber", false);
            String secondaryAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "secondaryAccountNumber", false);
            String frequency = HelperMethods.getStringFromJsonObject(requestPayload, "frequency", false);
            String sweepType = HelperMethods.getStringFromJsonObject(requestPayload, "sweepType", false);
            String aboveSweepAmount = HelperMethods.getStringFromJsonObject(requestPayload, "aboveSweepAmount", false);
            String belowSweepAmount = HelperMethods.getStringFromJsonObject(requestPayload, "belowSweepAmount", false);
            String startDate = HelperMethods.getStringFromJsonObject(requestPayload, "startDate", false);
            String endDate = HelperMethods.getStringFromJsonObject(requestPayload, "endDate", false);
            
            
       //     customParams.addProperty("MaskedFromAccount",primaryAccountNumber);
            customParams.addProperty("primaryAccountNumber",primaryAccountNumber);
       //     customParams.addProperty("MaskedToAccount",secondaryAccountNumber);
            customParams.addProperty("secondaryAccountNumber",secondaryAccountNumber);
            customParams.addProperty("frequency",frequency);
       //     customParams.addProperty("sweepType",sweepType);
            customParams.addProperty("aboveSweepAmount",aboveSweepAmount);
            customParams.addProperty("belowSweepAmount",belowSweepAmount);
            customParams.addProperty("startDate",startDate);
            customParams.addProperty("endDate",endDate); 
            String opstatus = "";
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters("ENABLE_EVENTS",
                    requestManager);
            String statusId = "SID_EVENT_FAILURE";
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = "ACCOUNT_ACTION";;
            String eventSubType = "ACC_SWEEP_DELETE";
            String producer = "AccountSweeps/deleteAccountSweep";
            for (String queryParamName : parameterNames) {
                queryParamjsonObject.addProperty(queryParamName, queryParamsHandler.getParameter(queryParamName));
            }
            requestPayloadHandler.updatePayloadAsJson(queryParamjsonObject);

            if (ObjectServiceHelperMethods.hasKey(responsePayload, Param.OPSTATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, Param.OPSTATUS, true);
            }

            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, DBPConstants.DBP_ERROR_CODE_KEY)) {
                statusId = "SID_EVENT_SUCCESS";
            }

            if (enableEvents != null && enableEvents.equalsIgnoreCase("true")) {
             /*   JsonArray transactionsArray = responsePayload.getAsJsonArray(PARAM_TRANSACTIONS);
                responsePayload.addProperty(PARAM_NO_OF_TRANSACTIONS, Integer.toString(transactionsArray.size()));
                responsePayload.remove(PARAM_TRANSACTIONS); */
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
