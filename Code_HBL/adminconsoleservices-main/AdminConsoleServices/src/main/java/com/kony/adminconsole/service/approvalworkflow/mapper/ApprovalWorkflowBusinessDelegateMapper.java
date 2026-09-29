package com.kony.adminconsole.service.approvalworkflow.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.approvalworkflow.businessdelegate.api.MigrateDataBusinessDelegate;
import com.kony.adminconsole.service.approvalworkflow.businessdelegate.impl.MigrateDataBusinessDelegateImpl;

public class ApprovalWorkflowBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(MigrateDataBusinessDelegate.class, MigrateDataBusinessDelegateImpl.class);
        return map;
    }
}