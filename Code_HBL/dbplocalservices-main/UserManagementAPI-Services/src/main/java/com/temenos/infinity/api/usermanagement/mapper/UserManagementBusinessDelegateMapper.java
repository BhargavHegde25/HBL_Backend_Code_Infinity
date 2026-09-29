package com.temenos.infinity.api.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.infinity.api.usermanagement.businessdelegate.impl.UserManagementBusinessDelegateImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */
public class UserManagementBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        // Add Mapping of Business Delegates interface and their implementation
        map.put(UserManagementBusinessDelegate.class, UserManagementBusinessDelegateImpl.class);

        return map;
    }

}
