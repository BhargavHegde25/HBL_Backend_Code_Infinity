package com.kony.adminconsole.service.approvalmatrix.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.approvalmatrix.resource.api.ApprovalMatrixResource;
import com.kony.adminconsole.service.approvalmatrix.resource.impl.ApprovalMatrixResourceImpl;

public class ApprovalMatrixResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ApprovalMatrixResource.class, ApprovalMatrixResourceImpl.class);
        return map;
    }
}