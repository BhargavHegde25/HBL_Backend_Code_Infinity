package com.kony.objectserviceutils;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.postprocessors.ObjectServicesConstants;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class ObjectBasePreProcessor implements ObjectServicePreProcessor, ObjectServicesConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
            FabricRequestChain fabricReqChain) throws Exception {
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] { EventLogPreProcessorTask.class }; 
        
        if (ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager,
                tasks)) {
            fabricReqChain.execute(); 
        }
    }

}
