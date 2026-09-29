package com.temenos.dbx.nonproduct.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.nonproduct.resource.api.SavingsPotResource;
import com.temenos.dbx.nonproduct.resource.impl.SavingsPotResourceImpl;

public class SavingsPotResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		//Add Mapping of Resource interface and their implementation
		map.put(SavingsPotResource.class, SavingsPotResourceImpl.class);

		return map;
	}

}
