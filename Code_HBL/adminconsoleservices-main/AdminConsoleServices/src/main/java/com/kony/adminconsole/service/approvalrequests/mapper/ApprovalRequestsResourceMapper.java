package com.kony.adminconsole.service.approvalrequests.mapper;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.approvalrequests.resource.api.ApprovalRequestsResource;
import com.kony.adminconsole.service.approvalrequests.resource.impl.ApprovalRequestsResourceImpl;

import java.util.HashMap;
import java.util.Map;

public class ApprovalRequestsResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ApprovalRequestsResource.class, ApprovalRequestsResourceImpl.class);
        return map;
    }
}
