package com.temenos.infinity.api.chequemanagement.preprocessor;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.scaintegration.task.ProcessSCA;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

public class ChequeBookRequestObjectServicePreProcessor implements ObjectServicePreProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
			FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();
		
		PayloadHandler requestPayloadHandler = fabricRequestManager.getPayloadHandler();
		JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson() == null
				|| requestPayloadHandler.getPayloadAsJson().isJsonNull() ? new JsonObject()
						: requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
				
	    JsonElement validateElement = requestPayload.get("validate");
	    String validate = "false";
		if (validateElement != null) {
			validate = validateElement.getAsString();
		}
        if (!"true".equalsIgnoreCase(validate)) {
            Class<? extends ObjectProcessorTask>[] tasks = new Class[] { ProcessSCA.class };
            if (ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager, tasks)) {
                fabricRequestChain.execute();
            }
        } else {
            Class<? extends ObjectProcessorTask>[] tasks = new Class[] {};
            if (ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager, tasks)) {
                fabricRequestChain.execute();
            }

        }
    }

}
