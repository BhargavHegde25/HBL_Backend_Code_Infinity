package com.kony.adminconsole.service.termandcondition.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.termandcondition.resource.api.TnCResource;
import com.kony.adminconsole.service.termandcondition.resource.impl.TnCResourceImpl;

public class TnCResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(TnCResource.class, TnCResourceImpl.class);
        return map;
    }
}

