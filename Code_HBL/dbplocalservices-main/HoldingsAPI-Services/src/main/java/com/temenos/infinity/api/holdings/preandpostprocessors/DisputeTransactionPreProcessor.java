package com.temenos.infinity.api.holdings.preandpostprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.temenos.infinity.api.holdings.datavalidation.DisputeFromAccountValidation;
import com.temenos.infinity.api.holdings.datavalidation.CancelTransactionValidationTask;
import com.temenos.infinity.api.holdings.datavalidation.DisputeTransactionValidationTask;
import com.kony.task.datavalidation.AddOrderManageMentRequestBody;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;



public class DisputeTransactionPreProcessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] {
        		DisputeFromAccountValidation.class,
        		CancelTransactionValidationTask.class,
        		AddOrderManageMentRequestBody.class,
        		DisputeTransactionValidationTask.class
        		};
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }

}