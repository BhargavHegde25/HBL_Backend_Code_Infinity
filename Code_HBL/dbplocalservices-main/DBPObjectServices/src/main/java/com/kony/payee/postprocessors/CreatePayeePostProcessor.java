package com.kony.payee.postprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.postprocessors.AddBillPayeeObjectServicePostProcessor;
import com.kony.task.datavalidation.PayeeReloadTask;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * Used id: create
 *
 */
public class CreatePayeePostProcessor implements ObjectServicePostProcessor {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
            throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] { PayeeReloadTask.class,
                AddBillPayeeObjectServicePostProcessor.class };
        ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
    }

}