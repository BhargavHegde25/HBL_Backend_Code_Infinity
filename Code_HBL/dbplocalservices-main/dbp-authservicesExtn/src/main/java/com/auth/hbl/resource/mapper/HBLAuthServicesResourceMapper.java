package com.auth.hbl.resource.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;

public class HBLAuthServicesResourceMapper implements DBPAPIMapper<Resource>{

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		//map.put(InfinityUserManagementResource.class, InfinityUserManagementResourceImplExtn.class);
		return map;
	}

}
