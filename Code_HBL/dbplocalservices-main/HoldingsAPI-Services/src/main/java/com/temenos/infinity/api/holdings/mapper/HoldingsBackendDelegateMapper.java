package com.temenos.infinity.api.holdings.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.holdings.backenddelegate.api.HoldingsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.holdings.backenddelegate.impl.HoldingsExperienceAPIBackendDelegateImpl;

/**
 * Mapper between Backend Delegate Interfaces & corresponding Backend Delegate Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class HoldingsBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        // Register your BackendDelegate Delegate Implementation classes here
        map.put(HoldingsExperienceAPIBackendDelegate.class, HoldingsExperienceAPIBackendDelegateImpl.class);
        return map;
    }

}
