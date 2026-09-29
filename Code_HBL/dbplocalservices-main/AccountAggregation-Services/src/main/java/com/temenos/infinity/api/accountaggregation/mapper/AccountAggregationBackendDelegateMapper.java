package com.temenos.infinity.api.accountaggregation.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.accountaggregation.backenddelegate.api.AccountAggregationBackendDelegate;
import com.temenos.infinity.api.accountaggregation.backenddelegate.impl.AccountAggregationBackendDelegateImpl;;

/**
 * Mapper between Backend Delegate Interfaces & corresponding Backend Delegate Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class AccountAggregationBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        map.put(AccountAggregationBackendDelegate.class, AccountAggregationBackendDelegateImpl.class);
        return map;
    }

}
