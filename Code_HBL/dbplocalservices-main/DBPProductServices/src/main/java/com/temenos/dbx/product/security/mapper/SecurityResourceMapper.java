package com.temenos.dbx.product.security.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.security.resource.api.CaptchaResource;
import com.temenos.dbx.product.security.resource.impl.CaptchaResourceImpl;

public class SecurityResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        map.put(CaptchaResource.class, CaptchaResourceImpl.class);
        return map;
    }
}
