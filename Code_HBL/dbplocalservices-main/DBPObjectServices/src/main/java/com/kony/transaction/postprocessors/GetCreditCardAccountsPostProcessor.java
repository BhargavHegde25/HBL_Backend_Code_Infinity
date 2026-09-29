package com.kony.transaction.postprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.postprocessors.GetCreditCardsObjectPostProcessor;
import com.kony.task.sessionmgmt.SaveCreditCardsInSessionTask;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetCreditCardAccountsPostProcessor implements ObjectServicePostProcessor {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
            throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] { 
                SaveCreditCardsInSessionTask.class,
                GetCreditCardsObjectPostProcessor.class};
        ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
    }

}