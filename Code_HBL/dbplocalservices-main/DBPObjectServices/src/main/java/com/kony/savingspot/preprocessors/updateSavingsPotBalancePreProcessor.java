package com.kony.savingspot.preprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.task.datavalidation.SavingsPotClosedValidationTask;
import com.kony.task.datavalidation.SavingsPotUpdateBalanceValidation;
import com.kony.task.datavalidation.SavingsPotValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

public class updateSavingsPotBalancePreProcessor implements ObjectServicePreProcessor{

	@SuppressWarnings("unchecked")
	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
			FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();
		Class<? extends ObjectProcessorTask>[] tasks = new Class[] {SavingsPotValidationTask.class,SavingsPotClosedValidationTask.class,SavingsPotUpdateBalanceValidation.class };
        if (ObjectProcessorTaskManager.invokeAll(fabricRequestManager, fabricResponseManager,
                tasks)){
        	fabricRequestChain.execute();
        }      
  }

}
