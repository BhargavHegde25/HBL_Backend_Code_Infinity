package com.kony.accounts.postprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.postprocessors.GetAccountsPostLoginObjectServicePostProcessor;
import com.kony.postprocessors.GetNickNameObjectServicePostProcessor;
import com.kony.task.sessionmgmt.SaveAccountsInSessionTask;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
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
		Class<? extends ObjectProcessorTask>[] tasks = new Class[] { GetAccountsPostLoginObjectServicePostProcessor.class,GetNickNameObjectServicePostProcessor.class, SaveAccountsInSessionTask.class };
		ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
	}

}
