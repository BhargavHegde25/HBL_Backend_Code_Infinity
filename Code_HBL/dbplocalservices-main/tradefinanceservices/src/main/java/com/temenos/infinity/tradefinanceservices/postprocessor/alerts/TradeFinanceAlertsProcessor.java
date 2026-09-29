/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.postprocessor.alerts;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import static com.kony.postprocessors.ObjectServicesConstants.*;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_ALERT_DATA;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_ALERT_NAME;

/**
 * @author k.meiyazhagan
 */
public class TradeFinanceAlertsProcessor implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
        JsonObject responsePayload = new JsonObject();
        try {
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS, requestManager);
            responsePayload = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
            diagnostic.prepareInfo("Response payload: " + responsePayload).log();
            if (StringUtils.isBlank(enableEvents)
                    || !StringUtils.equalsIgnoreCase(enableEvents, PARAM_TRUE)
                    || !responsePayload.has(PARAM_ALERT_DATA)) {
                diagnostic.prepareInfo("Request failed or Alerts are Disabled").log();
                return true;
            }

            JsonObject customParams = JsonParser.parseString(responsePayload.get(PARAM_ALERT_DATA).getAsString()).getAsJsonObject();
            String alertName = customParams.get(PARAM_ALERT_NAME).getAsString();
            OperationData operation = requestManager.getServicesManager().getOperationData();

            EventsDispatcher.dispatch(requestManager, responseManager,
                    AlertsEnum.valueOf(alertName).getType(), AlertsEnum.valueOf(alertName).getSubType(),
                    StringUtils.join(operation.getServiceId(), "/operations/", operation.getObjectId(), "/", operation.getOperationId()),
                    PARAM_SID_EVENT_SUCCESS, null, HelperMethods.getCustomerIdFromSession(requestManager), customParams);

        } catch (Exception e) {
            alert.prepareError("Error occurred while processing TradeFinance alerts. ", e).log();
        } finally {
            if (responsePayload.has(PARAM_ALERT_DATA)) {
                responsePayload.remove(PARAM_ALERT_DATA);
                responseManager.getPayloadHandler().updatePayloadAsJson(responsePayload);
            }
        }

        return true;
    }
}
