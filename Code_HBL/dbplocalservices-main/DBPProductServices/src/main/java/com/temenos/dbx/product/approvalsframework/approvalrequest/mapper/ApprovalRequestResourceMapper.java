package com.temenos.dbx.product.approvalsframework.approvalrequest.mapper;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.approvalsframework.approvalrequest.resource.api.ApprovalRequestResource;
import com.temenos.dbx.product.approvalsframework.approvalrequest.resource.impl.ApprovalRequestResourceImpl;

import java.util.HashMap;
import java.util.Map;

public class ApprovalRequestResourceMapper implements DBPAPIMapper<Resource> {
    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ApprovalRequestResource.class, ApprovalRequestResourceImpl.class);
        return map;
    }
}
