package com.kony.adminconsole.service.productmanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.productmanagement.resource.api.ProductResource;
import com.kony.adminconsole.service.productmanagement.resource.impl.ProductResourceImpl;

public class ProductResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ProductResource.class, ProductResourceImpl.class);
        return map;
    }
}
