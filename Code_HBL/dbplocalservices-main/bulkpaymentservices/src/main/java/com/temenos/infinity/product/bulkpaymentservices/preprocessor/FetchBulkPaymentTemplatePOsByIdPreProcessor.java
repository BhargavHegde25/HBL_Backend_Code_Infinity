package com.temenos.infinity.product.bulkpaymentservices.preprocessor;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.product.bulkpaymentservices.validations.BulkPaymentTemplatePOsByIdValidation;
import com.kony.dbputilities.util.Log4j2Configurator;

public class FetchBulkPaymentTemplatePOsByIdPreProcessor implements ObjectServicePreProcessor {
    @Override
    public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager, FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();

        Class<? extends ObjectProcessorTask>[] tasks = new Class[]{BulkPaymentTemplatePOsByIdValidation.class};

        if (ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager, tasks)) {
            fabricRequestChain.execute();
        }
    }
}