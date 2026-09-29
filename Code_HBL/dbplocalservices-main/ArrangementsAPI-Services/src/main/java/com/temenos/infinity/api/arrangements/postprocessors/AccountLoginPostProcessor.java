package com.temenos.infinity.api.arrangements.postprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.infinity.api.arrangements.postprocessors.GetAccountsPostLoginObjectServicePostProcessor;
import com.temenos.infinity.api.arrangements.postprocessors.SaveAccountsInSessionTask;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * Used in : getAccountsPostLogin
 *
 */
public class AccountLoginPostProcessor implements ObjectServicePostProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
			throws Exception {
		Log4j2Configurator.getInstance();
		Class<? extends ObjectProcessorTask>[] tasks = new Class[] { GetAccountsPostLoginObjectServicePostProcessor.class, SaveAccountsInSessionTask.class };
		ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
	}

}
