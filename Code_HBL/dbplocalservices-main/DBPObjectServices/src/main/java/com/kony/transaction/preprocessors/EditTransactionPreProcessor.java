package com.kony.transaction.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.customersecurityservices.preprocessor.CreateTransferObjectServicePreProcessor;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.scaintegration.task.ProcessSCA;
import com.kony.task.datavalidation.*;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.dbx.object.businessdelegate.impl.CreateTransactionActionValidationBDImpl;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Objects;

/**
 * 
 * used in: patch, BillPaytransferEdit, InternationalFundTransferEdit, TransferToOwnAccountsEdit
 *
 */

public class EditTransactionPreProcessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        String isSCAEnabled = EnvironmentConfigurationsHandler.getValue("IS_SCA_ENABLED");
        if (!Boolean.parseBoolean(isSCAEnabled)) {
            //base infinity flow to be executed if server property IS_SCA_ENABLED is set to false
            Class<? extends ObjectProcessorTask>[] tasks = new Class[]{
                    SkipValidationTask.class,
                    UpdateFeatureActionIdTask.class,
                    CreateTransactionActionValidationBDImpl.class,
                    UpdateTransactionValidationTask.class,
                    //UpdateTransactionObjectServicePreProcessor.class
            };

            if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
                fabricReqChain.execute();
            }
        }else{
            //sca flow to be executed if server property IS_SCA_ENABLED is set to true
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
                Class[] arrayOfClass = { UpdateFeatureActionIdTask.class, CreateTransactionActionValidationBDImpl.class, UploadAttachmentsValidationTask.class, CreateTransactionValidationTask.class, CreateTransferObjectServicePreProcessor.class };
                if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, arrayOfClass))
                    fabricReqChain.execute();
            } else {
                Class[] arrayOfClass = { UpdateFeatureActionIdTask.class, CreateTransactionActionValidationBDImpl.class, UploadAttachmentsValidationTask.class, CreateTransactionValidationTask.class, ProcessSCA.class, CreateTransferObjectServicePreProcessor.class };
                if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, arrayOfClass))
                    fabricReqChain.execute();
            }
        }
    }
}

