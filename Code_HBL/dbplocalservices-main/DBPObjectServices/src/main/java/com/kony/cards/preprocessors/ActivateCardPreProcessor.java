package com.kony.cards.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.scaintegration.task.ProcessSCA;
import com.kony.task.datavalidation.ActivateCardAddTaskValidation;
import com.kony.task.datavalidation.ActivateCardValidationTask;
import com.kony.task.datavalidation.AddOrderManageMentRequestBody;
import com.kony.task.datavalidation.CardValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

public class ActivateCardPreProcessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] {
        		CardValidationTask.class, ProcessSCA.class, CardUpdatePushAuditPreProcessor.class,
				ActivateCardValidationTask.class, AddOrderManageMentRequestBody.class,
				ActivateCardAddTaskValidation.class };
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }
}

