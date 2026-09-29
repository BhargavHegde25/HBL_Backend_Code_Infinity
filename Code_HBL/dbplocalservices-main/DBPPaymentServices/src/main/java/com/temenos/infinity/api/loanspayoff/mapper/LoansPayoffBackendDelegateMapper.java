package com.temenos.infinity.api.loanspayoff.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.loanspayoff.backenddelegate.api.LoansPayoffAPIBackendDelegate;
import com.temenos.infinity.api.loanspayoff.backenddelegate.impl.LoansPayoffAPIBackendDelegateImpl;

/**
 * Mapper between Backend Delegate Interfaces & corresponding Backend Delegate Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class LoansPayoffBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        // Register your BackendDelegate Delegate Implementation classes here
        map.put(LoansPayoffAPIBackendDelegate.class, LoansPayoffAPIBackendDelegateImpl.class);
        return map;
    }

}
