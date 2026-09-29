package com.kony.transaction.postprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.task.sessionmgmt.SavePostedTransactionsInSessionTask;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.dbx.object.businessdelegate.impl.ViewTransactionActionValidationBDImpl;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * used in : getPostedTransferAndP2pTransactions
 */

public class GetPostedTransactionsPostProcessor implements ObjectServicePostProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
			throws Exception {
		Log4j2Configurator.getInstance();
		Class<? extends ObjectProcessorTask>[] tasks = new Class[] { ViewTransactionActionValidationBDImpl.class,
				SavePostedTransactionsInSessionTask.class };
		ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
	}

}
