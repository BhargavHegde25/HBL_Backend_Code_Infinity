/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.preprocessor;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.tradefinanceservices.preprocessor.validations.ClauseValidation;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CreateClausesPreprocessor implements ObjectServicePreProcessor {
    @Override
    public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager, FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();

        Class<? extends ObjectProcessorTask>[] tasks = new Class[]{ClauseValidation.class};

        if (ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager, tasks)) {
            fabricRequestChain.execute();
        }
    }
}