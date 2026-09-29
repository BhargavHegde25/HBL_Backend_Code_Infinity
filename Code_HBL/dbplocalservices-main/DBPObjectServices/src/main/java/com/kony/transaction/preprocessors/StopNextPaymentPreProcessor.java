package com.kony.transaction.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.task.datavalidation.AddOrderManageMentRequestBody;
import com.kony.task.datavalidation.CancelDirectDebitOrderManagementTask;
import com.kony.task.datavalidation.CancelDirectDebitServiceValidation;
import com.kony.task.datavalidation.StopNextPaymentOrderManagementTask;
import com.kony.task.datavalidation.StopNextPaymentServiceValidation;
import com.kony.task.datavalidation.UpdateCDPConsentOrderManagementTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;



public class StopNextPaymentPreProcessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] {
        		StopNextPaymentServiceValidation.class,
        		AddOrderManageMentRequestBody.class,
        		StopNextPaymentOrderManagementTask.class
        		};
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }

}