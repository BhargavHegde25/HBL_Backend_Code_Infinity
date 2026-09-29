package com.kony.cards.preprocessors;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.kony.scaintegration.task.ProcessSCA;
import com.kony.task.datavalidation.AddOrderManageMentRequestBody;
import com.kony.task.datavalidation.CardMngtValidationTask;
import com.kony.task.datavalidation.CardValidationTask;
import com.kony.task.datavalidation.UpdateCardValidationTask;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * used in : cancelcard, reportlost, unlockcard, patch, replacecard, changepin, lockcard
 *
 */
public class UpdateCardPreProcessor implements ObjectServicePreProcessor {

    @Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager,
            FabricRequestChain fabricReqChain) throws Exception {
		Log4j2Configurator.getInstance();
        Class<? extends ObjectProcessorTask>[] tasks = new Class[] { 
        		CardValidationTask.class,
        		ProcessSCA.class,
                CardUpdatePushAuditPreProcessor.class ,
                UpdateCardValidationTask.class,
                AddOrderManageMentRequestBody.class,CardMngtValidationTask.class};
        if (ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks)) {
            fabricReqChain.execute();
        }
    }

	

}