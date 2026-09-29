package com.temenos.infinity.api.savingspot.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.savingspot.resource.api.SavingsPotResource;
import com.temenos.infinity.api.savingspot.resource.impl.SavingsPotResourceImpl;

public class SavingsPotResourceMapper implements DBPAPIMapper<Resource> { 
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(SavingsPotResource.class, SavingsPotResourceImpl.class);
        return map;
	}
        
}
