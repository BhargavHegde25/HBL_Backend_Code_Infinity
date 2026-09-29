package com.auth.hbl.resource.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;

public class HBLAuthServicesBackendDelegateMapper implements DBPAPIMapper<BackendDelegate>{

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
		//map.put(ContractBackendDelegate.class, ContractBackendDelegateImplExtn.class);
		
		return map;
	}

}
