package com.temenos.infinity.api.cards.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.scaintegration.task.ProcessSCA;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.temenos.infinity.api.cards.datavalidation.AddOrderManageMentRequestBody;
import com.temenos.infinity.api.cards.datavalidation.ApplyForDebitCardAddTaskValidation;
import com.temenos.infinity.api.cards.datavalidation.ApplyForDebitCardValidationTask;
import com.temenos.infinity.api.cards.datavalidation.CardLimitsAddTaskValidation;
import com.temenos.infinity.api.cards.datavalidation.CardLimitsValidationTask;
import com.temenos.infinity.api.cards.datavalidation.CardValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Objects;

public class CardLimitsPreProcessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Boolean scaEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getValue("IS_SCA_ENABLED"));
        if (!scaEnabled) {
            Class<? extends ObjectProcessorTask>[] tasks = new Class[]{
                    CardValidationTask.class, CardLimitsValidationTask.class, AddOrderManageMentRequestBody.class, CardLimitsAddTaskValidation.class,
                    ProcessSCA.class};
            if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
                fabricReqChain.execute();
            }
        } else {
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
                        CardValidationTask.class,CardLimitsValidationTask.class,AddOrderManageMentRequestBody.class,CardLimitsAddTaskValidation.class};
                if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
                    fabricReqChain.execute();
                }
            }
            else{

                Class<? extends ObjectProcessorTask>[] tasks = new Class[] {
                        CardValidationTask.class,ProcessSCA.class,CardLimitsValidationTask.class,AddOrderManageMentRequestBody.class,CardLimitsAddTaskValidation.class};
                if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
                    fabricReqChain.execute();
                }
            }
        }
    }

}
