package com.temenos.infinity.api.docmanagement.preprocessor;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.temenos.infinity.api.docmanagement.task.TransactionValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * used in: DownloadTransactionReport
 * 
 *
 */
public class DownloadTransactionReportPreProcessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] { 
        		TransactionValidationTask.class };
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }

}