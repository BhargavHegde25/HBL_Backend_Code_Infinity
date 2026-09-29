package com.temenos.dbx.party.javaservice;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.api.events.EventData;
import com.konylabs.middleware.api.events.EventSubscriber;
import com.konylabs.middleware.api.events.IntegrationEventSubscriber;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;


@IntegrationEventSubscriber(topics = { "/events/updateParty" })
public class UpdatePartyEventSubscriber implements EventSubscriber {
    private LoggerUtil logger;
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    public void onEvent(EventData eventData) {
        logger = new LoggerUtil(CreatePartyEventSubscriber.class);
        alert.prepareError("eventData.getData()" + eventData.getData()).log();
        alert.prepareError("eventData.getAdditionalMetadata()" + eventData.getAdditionalMetadata()).log();
        JsonObject data;
        try {
        data= new JsonParser().parse(eventData.getData().toString()).getAsJsonObject();
        }catch (Exception e) {
            alert.prepareError("error in parsing Event Data").log();
            return;
        }
        try {
            diagnostic.prepareDebug("eventData.userProfile() : "+ (logger.isDebugModeEnabled()? new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(eventData.getUserProfile()) : null)).log();
        } catch (JsonProcessingException e) {
        	alert.prepareError("Exception", e).log();
        }
        Map<String, Object> inputParams = new HashMap<String, Object>();
        
        if(data.has("events")) {
            data = data.get("events").getAsJsonObject() ;
            if(data.has("eventData")) {
                data = data.get("eventData").getAsJsonObject() ;
                inputParams.put("partyEventData", data.toString());
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, null, URLConstants.DBX_PROSPECT_UPDATE);
                return;
            }
        }
        
        alert.prepareError("Party Information not found in event data.").log();        
    }

}

