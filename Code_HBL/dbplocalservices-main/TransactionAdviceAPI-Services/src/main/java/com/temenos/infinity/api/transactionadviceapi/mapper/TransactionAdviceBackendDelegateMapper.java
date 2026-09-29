package com.temenos.infinity.api.transactionadviceapi.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.transactionadviceapi.backenddelegate.api.TransactionAdviceAPIBackendDelegate;
import com.temenos.infinity.api.transactionadviceapi.backenddelegate.impl.TransactionAdviceAPIBackendDelegateImpl;

/**
 * Mapper between Backend Delegate Interfaces & corresponding Backend Delegate Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class TransactionAdviceBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        // Register your BackendDelegate Delegate Implementation classes here
        map.put(TransactionAdviceAPIBackendDelegate.class, TransactionAdviceAPIBackendDelegateImpl.class);
        return map;
    }

}
