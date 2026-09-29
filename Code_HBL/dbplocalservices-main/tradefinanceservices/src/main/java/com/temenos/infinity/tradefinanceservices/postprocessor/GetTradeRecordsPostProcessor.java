/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.postprocessor;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.infinity.tradefinanceservices.sessionmanagement.SaveMessageEligibleRecordsInSession;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetTradeRecordsPostProcessor implements ObjectServicePostProcessor {
    @Override
    public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[]{SaveMessageEligibleRecordsInSession.class};
        ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager, tasks);
    }
}
