package com.temenos.dbx.product.approvalservices.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.scaintegration.task.ProcessSCA;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

public class ApprovalRequestObjectServicePreProcessor implements ObjectServicePreProcessor {
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
			FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();
		Class<? extends ObjectProcessorTask>[] tasks = new Class[] { 				
				  ProcessSCA.class};
      
      if (ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager, tasks)) {
    	  fabricRequestChain.execute();
      }
		
	}

}
