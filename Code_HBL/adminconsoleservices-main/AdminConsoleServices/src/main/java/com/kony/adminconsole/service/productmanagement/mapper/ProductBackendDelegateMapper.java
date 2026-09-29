package com.kony.adminconsole.service.productmanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.productmanagement.backenddelegate.api.ProductBackendDelegate;
import com.kony.adminconsole.service.productmanagement.backenddelegate.impl.ProductBackendDelegateImpl;

public class ProductBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        map.put(ProductBackendDelegate.class, ProductBackendDelegateImpl.class);
        return map;
    }
}