package com.temenos.infinity.api.savingspot.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.savingspot.backenddelegate.api.SavingsPotBackendDelegate;
import com.temenos.infinity.api.savingspot.backenddelegate.impl.SavingsPotBackendDelegateImpl;

public class SavingsPotBackendDelegateMapper implements DBPAPIMapper<BackendDelegate>{
   @Override
	    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
	        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

	        // Register your BackendDelegate Delegate Implementation classes here
	        map.put(SavingsPotBackendDelegate.class, SavingsPotBackendDelegateImpl.class);
	        return map;
	    }

}
