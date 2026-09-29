package com.temenos.infinity.api.cards.postprocessors;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.temenos.infinity.api.cards.datavalidation.SaveCardsInSessionTask;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * Used in : get
 *
 */
public class GetCardsPostProcessor implements ObjectServicePostProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
			throws Exception {
		Log4j2Configurator.getInstance();
		Class<? extends ObjectProcessorTask>[] tasks = new Class[] { SaveCardsInSessionTask.class,
				GetCreditCardsObjectPostProcessor.class };
		ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
	}

}
