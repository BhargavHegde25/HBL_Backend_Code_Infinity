package com.temenos.dbx.product.approvalsframework.approvalrequest.mapper;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.approvalsframework.approvalrequest.backenddelegate.api.ApprovalRequestBackendDelegate;
import com.temenos.dbx.product.approvalsframework.approvalrequest.backenddelegate.impl.ApprovalRequestBackendDelegateImpl;

import java.util.HashMap;
import java.util.Map;

public class ApprovalRequestBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ApprovalRequestBackendDelegate.class, ApprovalRequestBackendDelegateImpl.class);
        return map;
    }
}
