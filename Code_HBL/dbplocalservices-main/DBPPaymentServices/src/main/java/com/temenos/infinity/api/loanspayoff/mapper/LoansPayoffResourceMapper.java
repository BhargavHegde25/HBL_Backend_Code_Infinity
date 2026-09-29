package com.temenos.infinity.api.loanspayoff.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.loanspayoff.resource.api.LoansPayoffResource;
import com.temenos.infinity.api.loanspayoff.resource.impl.LoansPayoffResourceImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */

public class LoansPayoffResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        // Add Mapping of Business Delegates interface and their implementation
        map.put(LoansPayoffResource.class, LoansPayoffResourceImpl.class);

        return map;
    }
}
