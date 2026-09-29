package com.temenos.infinity.api.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.usermanagement.backenddelegate.api.UserManagementAPIBackendDelegate;
import com.temenos.infinity.api.usermanagement.backenddelegate.impl.UserManagementAPIBackendDelegateImpl;

/**
 * Mapper between Backend Delegate Interfaces & corresponding Backend Delegate Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class UserManagementBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        // Register your BackendDelegate Delegate Implementation classes here
        map.put(UserManagementAPIBackendDelegate.class, UserManagementAPIBackendDelegateImpl.class);
        return map;
    }

}
