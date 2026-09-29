package com.kony.adminconsole.core.config;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.delegate.ServicePermissionsBusinessDelegate;
import com.kony.adminconsole.core.security.ServicePermissionMapRegister;

public class ServicePermissionMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(ServicePermissionsBusinessDelegate.class, ServicePermissionMapRegister.class);
        return map;
    }

}
