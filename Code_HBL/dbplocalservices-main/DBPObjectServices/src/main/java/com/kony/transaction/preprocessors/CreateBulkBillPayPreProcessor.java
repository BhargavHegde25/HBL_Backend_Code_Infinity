package com.kony.transaction.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.dbputilities.customersecurityservices.preprocessor.CreateBulkBillPaymentsObjectServicePreProcessor;
import com.kony.task.datavalidation.BulkBillPayValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.dbx.object.businessdelegate.impl.BulkBillPayTransactionActionValidationBDImpl;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * used in : createBulkBillPay
 *
 */
public class CreateBulkBillPayPreProcessor implements ObjectServicePreProcessor {

    @SuppressWarnings("unchecked")
	@Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] { 
        		BulkBillPayTransactionActionValidationBDImpl.class,
        		BulkBillPayValidationTask.class,
        		CreateBulkBillPaymentsObjectServicePreProcessor.class };
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }

}