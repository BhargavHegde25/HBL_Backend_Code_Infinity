package com.kony.transaction.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.scaintegration.task.ProcessSCA;
import com.kony.task.datavalidation.CreateOneTimeTransferServiceValidation;
import com.kony.task.datavalidation.UploadAttachmentsValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import java.util.Objects;

public class CreateOneTimeTransferPreProcessor implements ObjectServicePreProcessor {
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager, FabricRequestChain fabricReqChain) throws Exception {
        Log4j2Configurator.getInstance();
        String isSCAEnabled = EnvironmentConfigurationsHandler.getValue("IS_SCA_ENABLED");
        if (!Boolean.parseBoolean(isSCAEnabled)) {
            Class[] arrayOfClass = { CreateOneTimeTransferServiceValidation.class, UploadAttachmentsValidationTask.class };
            if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, arrayOfClass))
                fabricReqChain.execute();
        } else {
            PayloadHandler requestPayloadHandler = fabricReqManager.getPayloadHandler();
            PayloadHandler responsePayloadHandler = fabricResManager.getPayloadHandler();
            JsonObject requestPayload = (requestPayloadHandler.getPayloadAsJson() == null || requestPayloadHandler.getPayloadAsJson().isJsonNull()) ? new JsonObject() : requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonElement isMFARequired = requestPayload.get("isMFARequired");
            JsonElement serviceKey = requestPayload.get("serviceKey");
            String isMFARequiredStr = "true";
            String isserviceKey = "false";
            if (isMFARequired != null)
                isMFARequiredStr = isMFARequired.getAsString();
            if (serviceKey != null)
                isserviceKey = "true";
            String stepUpval = (isMFARequired != null) ? isMFARequired.getAsString() : "";
            String stepUpCache = "";
            stepUpCache = (Objects.toString(fabricReqManager.getServicesManager().getResultCache().retrieveFromCache("stepUp")) != null) ? Objects.toString(fabricReqManager.getServicesManager().getResultCache().retrieveFromCache("stepUp")) : "";
            if (stepUpCache.equalsIgnoreCase("null"))
                stepUpCache = "";
            if (stepUpval != null && !stepUpval.isEmpty() && stepUpCache != null && !stepUpCache.isEmpty()) {
                fabricReqManager.getServicesManager().getResultCache().removeFromCache("stepUp");
                if (!stepUpCache.equalsIgnoreCase(stepUpval))
                    return;
            }
            if (!"true".equalsIgnoreCase(isMFARequiredStr) && !"true".equalsIgnoreCase(isserviceKey)) {
                Class[] arrayOfClass = { CreateOneTimeTransferServiceValidation.class, UploadAttachmentsValidationTask.class };
                if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, arrayOfClass))
                    fabricReqChain.execute();
            } else {
                Class[] arrayOfClass = { CreateOneTimeTransferServiceValidation.class, UploadAttachmentsValidationTask.class, ProcessSCA.class };
                if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, arrayOfClass))
                    fabricReqChain.execute();
            }
        }
    }
}
