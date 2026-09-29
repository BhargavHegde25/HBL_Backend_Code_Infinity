package com.temenos.infinity.api.holdings.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.holdings.resource.api.AccountTransactionsResource;
import com.temenos.infinity.api.holdings.resource.impl.AccountTransactionsResourceImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */

public class HoldingsResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        // Add Mapping of Business Delegates interface and their implementation
        map.put(AccountTransactionsResource.class, AccountTransactionsResourceImpl.class);

        return map;
    }
}
