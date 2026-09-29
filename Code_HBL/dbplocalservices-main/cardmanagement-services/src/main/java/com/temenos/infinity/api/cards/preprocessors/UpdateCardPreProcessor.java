package com.temenos.infinity.api.cards.preprocessors;

import java.util.HashMap;
import java.util.Objects;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.scaintegration.task.ProcessSCA;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.temenos.infinity.api.cards.datavalidation.AddOrderManageMentRequestBody;
import com.temenos.infinity.api.cards.datavalidation.CardMngtValidationTask;
import com.temenos.infinity.api.cards.datavalidation.CardValidationTask;
import com.temenos.infinity.api.cards.datavalidation.UpdateCardValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * used in : cancelcard, reportlost, unlockcard, patch, replacecard, changepin, lockcard
 *
 */
public class UpdateCardPreProcessor implements ObjectServicePreProcessor {

    @Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Boolean scaEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getValue("IS_SCA_ENABLED"));
        if (!scaEnabled) {
            Class<? extends ObjectProcessorTask>[] tasks = new Class[]{
                    CardValidationTask.class,
                    ProcessSCA.class,
                    CardUpdatePushAuditPreProcessor.class,
                    UpdateCardValidationTask.class,
                    AddOrderManageMentRequestBody.class, CardMngtValidationTask.class};
            if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
                fabricReqChain.execute();
            }
        }
        else{
            PayloadHandler requestPayloadHandler = fabricReqManager.getPayloadHandler();
            JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson() == null
                    || requestPayloadHandler.getPayloadAsJson().isJsonNull() ? new JsonObject()
                    : requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

            JsonElement isMFARequired = requestPayload.get("isMFARequired");
            JsonElement serviceKey = requestPayload.get("serviceKey");
            String isMFARequiredStr = "true";
            String isserviceKey = "false";
            if (isMFARequired  != null) {
                isMFARequiredStr = isMFARequired.getAsString();
            }
            if (serviceKey  != null) {
                isserviceKey = "true";
            }

            String stepUpval = isMFARequired != null ? isMFARequired.getAsString():"";
            String stepUpCache = "";
            stepUpCache = Objects.toString( fabricReqManager.getServicesManager().getResultCache().retrieveFromCache("stepUp"))!=null?Objects.toString(fabricReqManager.getServicesManager().getResultCache().retrieveFromCache("stepUp")):"";
            if(stepUpCache.equalsIgnoreCase("null"))
                stepUpCache = "";
            if(stepUpval!=null && !stepUpval.isEmpty() && stepUpCache!=null && !stepUpCache.isEmpty()){
                fabricReqManager.getServicesManager().getResultCache().removeFromCache("stepUp");
                if (!stepUpCache.equalsIgnoreCase(stepUpval)) {
                    return;
                }
            }
            if (!"true".equalsIgnoreCase(isMFARequiredStr) && !"true".equalsIgnoreCase(isserviceKey)){
                Class<? extends ObjectProcessorTask>[] tasks = new Class[] {
                        CardValidationTask.class,
                        CardUpdatePushAuditPreProcessor.class ,
                        UpdateCardValidationTask.class,
                        AddOrderManageMentRequestBody.class,CardMngtValidationTask.class};
                if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
                    fabricReqChain.execute();
                }
            }
            else{

                Class<? extends ObjectProcessorTask>[] tasks = new Class[] {
                        CardValidationTask.class,
                        ProcessSCA.class,
                        CardUpdatePushAuditPreProcessor.class ,
                        UpdateCardValidationTask.class,
                        AddOrderManageMentRequestBody.class,CardMngtValidationTask.class};
                if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
                    fabricReqChain.execute();
                }
            }
        }
    }

	

}