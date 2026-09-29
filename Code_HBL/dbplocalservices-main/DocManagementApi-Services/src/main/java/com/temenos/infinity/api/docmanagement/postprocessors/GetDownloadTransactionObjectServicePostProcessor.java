package com.temenos.infinity.api.docmanagement.postprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.google.gson.JsonObject;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.kony.postprocessors.ObjectServicesConstants;

public class GetDownloadTransactionObjectServicePostProcessor
        implements ObjectServicePostProcessor, ObjectServicesConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        try {

            JsonObject responsePayload = new JsonObject();
            JsonObject customParams = new JsonObject();
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);
            String statusId = PARAM_SID_EVENT_SUCCESS;

            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                statusId = PARAM_SID_EVENT_FAILURE;
            }

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {

                String eventType = PARAM_ACCOUNT_ACTION;
                String eventSubType = PARAM_INTACC_TRANSACTIONS_DOWNLOAD;
                String producer = "DownloadTransaction/GET";
                try {

                    ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responseManager,
                            eventType, eventSubType, producer, statusId, null, "", customParams));
                } catch (Exception e2) {
                    alert.prepareError("Exception Occured while invoking objectServiceHelperMethods", e2).log();
                }
            }

        } catch (Exception ex) {
            alert.prepareError("exception occured in GetDownloadTransactionObjectServicePostProcessor ", ex).log();
        }

    }

}
