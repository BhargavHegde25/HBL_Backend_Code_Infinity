package com.kony.auditlogservices.core;

import com.kony.auditlogservices.dbutils.AuditLogDataSourceHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public abstract class AbstractLogJavaService implements JavaService2 {

    @Override
    public Object invoke(String methodid, Object[] inputarray, DataControllerRequest requestinstance,
            DataControllerResponse responseinstance)
            throws Exception {
        try {
            // keeping DCR in current thread context
            AuditLogDataSourceHandler.setRequest(requestinstance);

            return execute(methodid, inputarray, requestinstance, responseinstance);
        } finally {
            // removing DCR from current thread context. This is important.
            AuditLogDataSourceHandler.removeRequest();
            
        }
    }

    public abstract Object execute(String methodid, Object[] inputarray, DataControllerRequest requestinstance,
            DataControllerResponse responseinstance);

}
