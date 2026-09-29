package com.temenos.infinity.api.loanspayoff.eventlogs;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.api.loanspayoff.config.ServerConfigurations;
import com.temenos.infinity.api.loanspayoff.constants.EventLogConstants;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class EventLogPostProcessorTask implements ObjectProcessorTask, EventLogConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        // Check events enabled globally
        String enableEvents = ServerConfigurations.ENABLE_EVENTS.getValue();
        if (enableEvents == null || enableEvents.equalsIgnoreCase(PARAM_FALSE)) {
            alert.prepareError("Events not enabled globally").log();
            return true;
        }

        try {

            EventLogUtils EventLogUtils = new EventLogUtils();
            JsonObject request = (JsonObject) fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
            JsonObject EventLog = EventLogUtils.buildEventData(fabricRequestManager, request, response);

            // Initialize Variables
            JsonObject requestPayload = new JsonObject();
            JsonObject responsePayload = new JsonObject();
            JsonObject customParams = new JsonObject();
            String eventType = StringUtils.EMPTY;
            String eventSubType = StringUtils.EMPTY;
            String producer = StringUtils.EMPTY;
            String customerName = StringUtils.EMPTY;
            String StatusId = PARAM_SID_EVENT_FAILURE;

            if (EventLog == null || EventLog.entrySet().isEmpty()) {
                alert.prepareError("Audit Log Process aborted").log();
                return true;
            }

            if (!EventLog.get("ProcessError").getAsString().equalsIgnoreCase(PARAM_FALSE)) {
                alert.prepareError("Error Occured while processing the logs. Audit Log Process aborted").log();
                return true;
            }

            // Set Event Type
            if (EventLog.has(PARAM_EVENT_TYPE))
                eventType = EventLog.get(PARAM_EVENT_TYPE).getAsString();

            // Set Event Sub Type
            if (EventLog.has(PARAM_EVENT_SUBTYPE))
                eventSubType = EventLog.get(PARAM_EVENT_SUBTYPE).getAsString();

            // Set Producer
            if (EventLog.has(PARAM_PRODUCER))
                producer = EventLog.get(PARAM_PRODUCER).getAsString();

            // Set Producer
            if (EventLog.has(PARAM_PRODUCER))
                producer = EventLog.get(PARAM_PRODUCER).getAsString();

            // Set Customer Name
            if (EventLog.has(PARAM_CUSTOMER_NAME))
                customerName = EventLog.get(PARAM_CUSTOMER_NAME).getAsString();

            // Set Request Payload - Should not be logged for Post Processor

            if (EventLog.has("request") && !EventLog.get("request").isJsonNull())
                requestPayload = EventLog.get("request").getAsJsonObject();

            // Set Request Payload
            if (EventLog.has("response") && !EventLog.get("response").isJsonNull())
                responsePayload = EventLog.get("response").getAsJsonObject();

            // Set Event Status
            if (EventLog.has("StatusID"))
                StatusId = EventLog.get("StatusID").getAsString();

            // Set Request Payload
            if (EventLog.has("customParams"))
                customParams = EventLog.get("customParams").getAsJsonObject();

            customParams.addProperty("Producer", producer);

            // Push the event via event dispatcher
            EventsDispatcher.dispatch(fabricRequestManager, requestPayload, responsePayload, eventType, eventSubType,
                    producer, StatusId, null, null, customParams);

        } catch (Exception e) {
            alert.prepareError("Error while pushing to Audit Engine.").log();
        }
        return true;
    }
}
