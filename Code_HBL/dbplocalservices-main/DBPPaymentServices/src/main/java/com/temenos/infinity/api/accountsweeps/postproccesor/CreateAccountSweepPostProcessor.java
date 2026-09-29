package com.temenos.infinity.api.accountsweeps.postproccesor;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.infinity.api.accountsweeps.task.AccountSweepUpdateTask;
import com.temenos.infinity.api.accountsweeps.task.CreateAccountSweepTask;
import com.temenos.infinity.api.accountsweeps.task.CreateSweepAuditTask;

/**
 * @author naveen.yerra
 */
public class CreateAccountSweepPostProcessor implements ObjectServicePostProcessor {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
            throws Exception {
        Class<? extends ObjectProcessorTask>[] tasks = new Class[]{CreateAccountSweepTask.class, AccountSweepUpdateTask.class,CreateSweepAuditTask.class};
        ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
    }

}
