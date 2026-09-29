package com.temenos.msArrangement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.msArrangement.resource.api.AccountTransactionsResource;
import com.temenos.msArrangement.resource.api.ArrangementsResource;
import com.temenos.msArrangement.resource.impl.AccountTransactionsResourceImpl;
import com.temenos.msArrangement.resource.impl.ArrangementsResourceImpl;


/**
 * 
 * @author KH2281
 * version 1.0
 * implements {@link DBPAPIMapper}
 */

public class HoldingsAndAMSResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
    	
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        //Add Mapping of Business Delegates interface and their implementation
        map.put(AccountTransactionsResource.class, AccountTransactionsResourceImpl.class);
        map.put(ArrangementsResource.class, ArrangementsResourceImpl.class);
        return map; 
    }
}
