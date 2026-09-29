package com.temenos.infinity.api.srmstransactions.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.srmstransactions.resource.api.SrmsTransactionsResource;
import com.temenos.infinity.api.srmstransactions.resource.impl.SrmsTransactionsResourceImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */

public class SrmsTransactionsResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        // Add Mapping of Business Delegates interface and their implementation
        map.put(SrmsTransactionsResource.class, SrmsTransactionsResourceImpl.class);

        return map;
    }
}
