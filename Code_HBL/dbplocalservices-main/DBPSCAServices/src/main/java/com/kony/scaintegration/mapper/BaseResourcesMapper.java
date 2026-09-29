package com.kony.scaintegration.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.scaintegration.resource.api.SCAIntegrationResource;
import com.kony.scaintegration.resource.api.UpdateVerfifyFlagResource;
import com.kony.scaintegration.resource.impl.SCAIntegrationResourceImpl;
import com.kony.scaintegration.resource.impl.UpdateVerifyFlagResourceImpl;

public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		map.put(SCAIntegrationResource.class, SCAIntegrationResourceImpl.class);
		map.put(UpdateVerfifyFlagResource.class, UpdateVerifyFlagResourceImpl.class);
		return map;
	}

}
