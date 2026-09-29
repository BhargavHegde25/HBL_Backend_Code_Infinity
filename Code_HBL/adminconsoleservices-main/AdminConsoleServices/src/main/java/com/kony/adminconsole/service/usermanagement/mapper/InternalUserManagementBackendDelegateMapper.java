package com.kony.adminconsole.service.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.usermanagement.backenddelegate.api.EmployeeRoleBackendDelegate;
import com.kony.adminconsole.service.usermanagement.backenddelegate.api.InternalUserManagementBackendDelegate;
import com.kony.adminconsole.service.usermanagement.backenddelegate.impl.EmployeeRoleBackendDelegateImpl;
import com.kony.adminconsole.service.usermanagement.backenddelegate.impl.InternalUserManagementBackendDelegateImpl;

public class InternalUserManagementBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        /* mappings of backend delegate interface and implementation to be added */
        map.put(InternalUserManagementBackendDelegate.class, InternalUserManagementBackendDelegateImpl.class);
        map.put(EmployeeRoleBackendDelegate.class, EmployeeRoleBackendDelegateImpl.class);
        return map;
    }

}
