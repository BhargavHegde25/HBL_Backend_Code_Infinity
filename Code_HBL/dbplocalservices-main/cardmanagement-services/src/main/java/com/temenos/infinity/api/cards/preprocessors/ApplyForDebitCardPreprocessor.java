package com.temenos.infinity.api.cards.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.dbputilities.mfa.preprocessors.ApplyForDebitCardMFAPreProcessor;
import com.temenos.infinity.api.cards.datavalidation.ActivateCardAddTaskValidation;
import com.temenos.infinity.api.cards.datavalidation.ActivateCardValidationTask;
import com.temenos.infinity.api.cards.datavalidation.AddOrderManageMentRequestBody;
import com.temenos.infinity.api.cards.datavalidation.ApplyForDebitCardAddTaskValidation;
import com.temenos.infinity.api.cards.datavalidation.ApplyForDebitCardValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

public class ApplyForDebitCardPreprocessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] { 
        		ApplyForDebitCardMFAPreProcessor.class,
        		ApplyForDebitCardValidationTask.class,AddOrderManageMentRequestBody.class,ApplyForDebitCardAddTaskValidation.class };
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }
}
