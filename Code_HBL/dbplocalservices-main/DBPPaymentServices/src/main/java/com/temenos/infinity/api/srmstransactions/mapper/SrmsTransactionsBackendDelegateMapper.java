package com.temenos.infinity.api.srmstransactions.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.srmstransactions.backenddelegate.api.SrmsTransactionsAPIBackendDelegate;
import com.temenos.infinity.api.srmstransactions.backenddelegate.impl.SrmsTransactionsAPIBackendDelegateImpl;

/**
 * Mapper between Backend Delegate Interfaces & corresponding Backend Delegate Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class SrmsTransactionsBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        // Register your BackendDelegate Delegate Implementation classes here
        map.put(SrmsTransactionsAPIBackendDelegate.class, SrmsTransactionsAPIBackendDelegateImpl.class);
        return map;
    }

}
