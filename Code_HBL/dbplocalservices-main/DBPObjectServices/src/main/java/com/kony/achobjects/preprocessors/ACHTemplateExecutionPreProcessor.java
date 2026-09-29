package com.kony.achobjects.preprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

public class ACHTemplateExecutionPreProcessor implements ObjectServicePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
            FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] { ACHTransactionsCompanyValidationTask.class,
                ACHTransactionsAccountValidationTask.class, ACHTransactionsMFAPreProcessorTask.class };

        diagnostic.prepareDebug("Entered into ACHTemplateExecutionPreProcessor").log();
        if (ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager, tasks)) {
            diagnostic.prepareDebug("All preprocessors are passed").log();
            fabricRequestChain.execute();
        }
    }

}
