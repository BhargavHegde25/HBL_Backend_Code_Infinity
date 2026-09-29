package com.kony.postprocessors;

import com.kony.dbputilities.mfa.preprocessors.TransactionsMFAPreProcessor;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

public class TransactionsPreProcessor implements ObjectServicePreProcessor {

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();
        new TransactionsMFAPreProcessor().execute(requestManager, responseManager, requestChain);

    }

}
