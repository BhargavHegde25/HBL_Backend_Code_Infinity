package com.temenos.dbx.party.javaservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.api.events.EventData;
import com.konylabs.middleware.api.events.EventSubscriber;
import com.konylabs.middleware.api.events.IntegrationEventSubscriber;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

@IntegrationEventSubscriber(topics = { "/events/scaactivationcode" })
public class ScaActivationCodeSubscriber implements EventSubscriber {
    private LoggerUtil logger;
private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public void onEvent(EventData eventData) {
        logger = new LoggerUtil(ScaActivationCodeSubscriber.class);
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
                String activationCode = data.has("activationCode") ? data.get("activationCode").getAsString() : "";
                diagnostic.prepareDebug(data.toString()).log();
                diagnostic.prepareDebug("userId value is :" + userId).log();
                diagnostic.prepareDebug("activationCode value is :" + activationCode).log();
            }
        }
    }

}
