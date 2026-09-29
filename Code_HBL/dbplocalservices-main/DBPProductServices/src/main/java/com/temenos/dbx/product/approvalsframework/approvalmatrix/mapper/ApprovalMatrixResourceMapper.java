package com.temenos.dbx.product.approvalsframework.approvalmatrix.mapper;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.resource.api.ApprovalMatrixResource;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.resource.impl.ApprovalMatrixResourceImpl;

import java.util.HashMap;
import java.util.Map;

public class ApprovalMatrixResourceMapper implements DBPAPIMapper<Resource> {
    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ApprovalMatrixResource.class, ApprovalMatrixResourceImpl.class);
        return map;
    }
}
