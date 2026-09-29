package com.kony.cards.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.dbputilities.mfa.preprocessors.ApplyForDebitCardMFAPreProcessor;
import com.kony.task.datavalidation.ActivateCardAddTaskValidation;
import com.kony.task.datavalidation.ActivateCardValidationTask;
import com.kony.task.datavalidation.AddOrderManageMentRequestBody;
import com.kony.task.datavalidation.ApplyForCreditCardValidationTask;
import com.kony.task.datavalidation.ApplyForDebitCardAddTaskValidation;
import com.kony.task.datavalidation.ApplyForDebitCardValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

public class ApplyForCreditCardPreprocessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] {
        		ApplyForCreditCardValidationTask.class};
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }
}
