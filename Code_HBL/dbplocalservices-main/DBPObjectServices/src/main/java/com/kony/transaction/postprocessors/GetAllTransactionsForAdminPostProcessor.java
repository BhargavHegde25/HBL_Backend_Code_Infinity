package com.kony.transaction.postprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.dbx.object.businessdelegate.impl.ViewTransactionActionValidationBDImpl;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetAllTransactionsForAdminPostProcessor implements ObjectServicePostProcessor {

	@Override
	public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
			throws Exception {
		Log4j2Configurator.getInstance();
		Class<? extends ObjectProcessorTask>[] tasks = new Class[] { ViewTransactionActionValidationBDImpl.class };
		ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
	}

}
