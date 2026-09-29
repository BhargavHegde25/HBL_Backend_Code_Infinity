package com.temenos.infinity.api.holdings.preandpostprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.objectserviceutils.EventLogPostProcessorTask;
import com.kony.task.sessionmgmt.SaveScheduledTransactionsInSessionTask;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.dbx.object.businessdelegate.impl.ViewTransactionActionValidationBDImpl;

/**
 * used in :getScheduledTransferAndP2pTransactions
 */

public class GetScheduledTransactionsPostProcessor implements ObjectServicePostProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
			throws Exception {
		Class<? extends ObjectProcessorTask>[] tasks = new Class[] { ViewTransactionActionValidationBDImpl.class,
				SaveScheduledTransactionsInSessionTask.class, EventLogPostProcessorTask.class };
		ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
	}

}
