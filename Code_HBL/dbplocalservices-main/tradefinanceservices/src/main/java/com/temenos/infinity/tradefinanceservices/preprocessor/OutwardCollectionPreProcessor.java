package com.temenos.infinity.tradefinanceservices.preprocessor;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.tradefinanceservices.preprocessor.validations.OutwardCollectionValidation;
import com.kony.dbputilities.util.Log4j2Configurator;

public class OutwardCollectionPreProcessor implements ObjectServicePreProcessor{
	@Override
    public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager, FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();

        Class<? extends ObjectProcessorTask>[] tasks = new Class[]{OutwardCollectionValidation.class};

        if (ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager, tasks)) {
            fabricRequestChain.execute();
        }
    }
}
