package com.temenos.dbx.eum.product.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.PushExternalEventBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.UserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.BackendIdentifiersBackendDelegateimpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.PushExternalEventBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.UserManagementBackendDelegateImpl;

public class ProductBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        map.put(UserManagementBackendDelegate.class, UserManagementBackendDelegateImpl.class);
        map.put(BackendIdentifiersBackendDelegate.class, BackendIdentifiersBackendDelegateimpl.class);
        map.put(PushExternalEventBackendDelegate.class, PushExternalEventBackendDelegateImpl.class);
        return map;
    }

}
