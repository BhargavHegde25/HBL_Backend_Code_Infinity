package com.temenos.infinity.api.accountaggregation.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.accountaggregation.resource.api.AccountAggregationResource;
import com.temenos.infinity.api.accountaggregation.resource.impl.AccountAggregationResourceImpl;

/**
 * Mapper between Resource Interfaces & corresponding Resource Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class AccountAggregationResourcesMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        // Register your Resource Implementation classes here
        map.put(AccountAggregationResource.class, AccountAggregationResourceImpl.class);

        return map;
    }
}
