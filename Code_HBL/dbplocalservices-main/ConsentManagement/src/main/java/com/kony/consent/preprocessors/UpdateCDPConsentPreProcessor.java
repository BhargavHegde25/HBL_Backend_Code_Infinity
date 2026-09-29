package com.kony.consent.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.consent.task.datavalidation.UpdateCDPConsentOrderManagementTask;
import com.kony.consent.task.datavalidation.UpdateCDPConsentServiceValidation;
import com.kony.task.datavalidation.AddOrderManageMentRequestBody;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;



public class UpdateCDPConsentPreProcessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] {
        		UpdateCDPConsentServiceValidation.class,
        		AddOrderManageMentRequestBody.class,
        		UpdateCDPConsentOrderManagementTask.class
        		};
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }

}