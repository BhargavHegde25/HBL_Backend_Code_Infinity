package com.temenos.dbx.core.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.core.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.core.backenddelegate.impl.CoreCustomerBackendDelegateImpl;

public class CoreBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        map.put(CoreCustomerBackendDelegate.class, CoreCustomerBackendDelegateImpl.class);
        
        return map;
    }

}
