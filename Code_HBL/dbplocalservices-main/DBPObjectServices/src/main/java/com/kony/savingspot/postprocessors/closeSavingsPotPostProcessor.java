package com.kony.savingspot.postprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.task.sessionmgmt.CloseSavingsPotInSessionTask;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

public class closeSavingsPotPostProcessor implements ObjectServicePostProcessor{

	@SuppressWarnings("unchecked")
    @Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
            throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] { CloseSavingsPotInSessionTask.class };
        ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
    }
}
