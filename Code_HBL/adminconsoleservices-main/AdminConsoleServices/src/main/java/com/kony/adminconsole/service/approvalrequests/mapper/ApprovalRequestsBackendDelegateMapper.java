package com.kony.adminconsole.service.approvalrequests.mapper;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.approvalrequests.backenddelegate.api.ApprovalRequestsBackendDelegate;
import com.kony.adminconsole.service.approvalrequests.backenddelegate.impl.ApprovalRequestsBackendDelegateImpl;

import java.util.HashMap;
import java.util.Map;


public class ApprovalRequestsBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ApprovalRequestsBackendDelegate.class, ApprovalRequestsBackendDelegateImpl.class);
        return map;
    }
}