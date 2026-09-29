package com.kony.adminconsole.service.signatorygroup.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.signatorygroup.resource.api.SignatoryGroupResource;
import com.kony.adminconsole.service.signatorygroup.resource.impl.SignatoryGroupResourceImpl;

public class SignatoryGroupResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(SignatoryGroupResource.class, SignatoryGroupResourceImpl.class);
        return map;
    }
}

