package com.temenos.infinity.api.arrangements.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.api.arrangements.task.AccountNumberValidationTask;
import com.kony.dbputilities.util.Log4j2Configurator;

public class closeAccountPreprocessor implements ObjectServicePreProcessor {
	@SuppressWarnings("unchecked")
	public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();


		Class<? extends ObjectProcessorTask>[] tasks = new Class[] { AccountNumberValidationTask.class};
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager,
                tasks)) {
            fabricReqChain.execute();
        }
    }

}
