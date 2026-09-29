package com.kony.externalalerts.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.externalalerts.resource.api.ExternalAlertsResource;
import com.kony.externalalerts.resource.impl.ExternalAlertsResourceImpl;

public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		map.put(ExternalAlertsResource.class, ExternalAlertsResourceImpl.class);

		return map;
	}
}
