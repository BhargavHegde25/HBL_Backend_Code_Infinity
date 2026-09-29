package com.temenos.dbx.core.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.core.resource.impl.CoreCustomerResourceImpl;
import com.temenos.dbx.product.usermanagement.resource.api.CoreCustomerResource;

public class CoreResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        map.put(CoreCustomerResource.class, CoreCustomerResourceImpl.class);
        map.put(com.temenos.dbx.eum.product.usermanagement.resource.api.CoreCustomerResource.class, CoreCustomerResourceImpl.class);
        return map;
    }
}
