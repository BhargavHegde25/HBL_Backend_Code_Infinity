/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.preprocessor;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.tradesupplyfinance.preprocessor.validations.CounterPartyInvoiceValidation;

/**
 * @author k.meiyazhagan
 */
public class CounterPartyInvoicePreProcessor implements ObjectServicePreProcessor {
    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager, FabricRequestChain requestChain) throws Exception {
        Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[]{CounterPartyInvoiceValidation.class};
        if (ObjectProcessorTaskManager.invokeAll(requestManager, responseManager, tasks)) {
            requestChain.execute();
        }
    }
}
