package com.temenos.dbx.product.approvalmatrixservices.mapper;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.approvalmatrixservices.backenddelegate.api.ApprovalMatrixBackendDelegate;
import com.temenos.dbx.product.approvalmatrixservices.backenddelegate.impl.ApprovalMatrixBackendDelegateImpl;

import java.util.HashMap;
import java.util.Map;

public class ApprovalMatrixBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        map.put(ApprovalMatrixBackendDelegate.class, ApprovalMatrixBackendDelegateImpl.class);

        return map;
    }
}
