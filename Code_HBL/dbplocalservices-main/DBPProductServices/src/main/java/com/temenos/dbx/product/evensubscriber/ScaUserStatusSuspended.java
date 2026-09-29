package com.temenos.dbx.product.evensubscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.api.events.EventData;
import com.konylabs.middleware.api.events.EventSubscriber;
import com.konylabs.middleware.api.events.IntegrationEventSubscriber;

@IntegrationEventSubscriber(topics = { "/events/userstatussuspended" })
public class ScaUserStatusSuspended implements EventSubscriber {
    private LoggerUtil logger;
    private Alert alert;
    private Diagnostic diagnostic;

    @Override
    public void onEvent(EventData eventData) {
        logger = new LoggerUtil(ScaActivationCodeSubscriber.class);
        diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        alert.prepareError("eventData.getData()" + eventData.getData()).log();
        alert.prepareError("eventData.getAdditionalMetadata()" + eventData.getAdditionalMetadata()).log();
        JsonObject data;
        try {
            data = new JsonParser().parse(eventData.getData().toString()).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("error in parsing Event Data", e).log();
            return;
        }
        try {
            diagnostic.prepareDebug("eventData.userProfile() : " + (logger.isDebugModeEnabled()
                    ? new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(eventData.getUserProfile())
                    : null)).log();
        } catch (JsonProcessingException e) {
            alert.prepareError("error in printing Event Data", e).log();
        }
        if (data.has("events")) {
            data = data.get("events").getAsJsonObject();
            if (data.has("eventData")) {
                data = data.get("eventData").getAsJsonObject();
                String userId = data.has("userId") ? data.get("userId").getAsString() : "";
                diagnostic.prepareDebug(data.toString()).log();
                diagnostic.prepareDebug("userId value is :" + userId).log();
            }
        }
    }

}
