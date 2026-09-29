package com.temenos.infinity.api.accountsweeps.postproccesor;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.infinity.api.accountsweeps.task.AccountSweepUpdateTask;
import com.temenos.infinity.api.accountsweeps.task.DeleteAccountSweepTask;
import com.temenos.infinity.api.accountsweeps.task.EditAccountSweepTask;
import com.temenos.infinity.api.accountsweeps.task.EditSweepAuditTask;

/**
 * @author naveen.yerra
 */
public class EditAccountSweepPostProcessor implements ObjectServicePostProcessor {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(FabricRequestManager fabricReqManager, FabricResponseManager fabricResManager)
            throws Exception {
        Class<? extends ObjectProcessorTask>[] tasks = new Class[]{EditAccountSweepTask.class, AccountSweepUpdateTask.class,EditSweepAuditTask.class};
        ObjectProcessorTaskManager.invokeAll(fabricReqManager, fabricResManager, tasks);
    }

}
