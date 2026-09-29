package com.kony.adminconsole.service.approvalworkflow.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;

import com.kony.adminconsole.service.approvalworkflow.resource.api.MigrateDataResource;
import com.kony.adminconsole.service.approvalworkflow.resource.impl.MigrateDataResourceImpl;

/**
 *
 * @author This class maps resource interface to its implementation and it can be changed whenever required
 *
 */

public class ApprovalWorkflowResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(MigrateDataResource.class, MigrateDataResourceImpl.class);
        return map;
    }
}