package com.kony.transaction.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.dbputilities.customersecurityservices.preprocessor.UpdateTransactionObjectServicePreProcessor;
import com.kony.task.datavalidation.SkipValidationTask;
import com.kony.task.datavalidation.UpdateFeatureActionIdTask;
import com.kony.task.datavalidation.UpdateTransactionValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.dbx.object.businessdelegate.impl.CreateTransactionActionValidationBDImpl;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * used in: patch
 *
 */

public class UpdateTransactionPreProcessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
         Class<? extends ObjectProcessorTask>[] tasks = new Class[] {
        		 SkipValidationTask.class,
        		 UpdateFeatureActionIdTask.class,
        		 CreateTransactionActionValidationBDImpl.class, 
        		 UpdateTransactionValidationTask.class,
        		 UpdateTransactionObjectServicePreProcessor.class
         };
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }

}