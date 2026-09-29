package com.dbp.reminderengine.mapper;

import java.util.HashMap;
import java.util.Map;


import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.dbp.reminderengine.resource.api.CustomerDetailsResource;
import com.dbp.reminderengine.resource.api.ProcessAlertsResource;
import com.dbp.reminderengine.resource.impl.CustomerDetailsResourceImpl;
import com.dbp.reminderengine.resource.impl.ProcessAlertsResourceImpl;


public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		map.put(CustomerDetailsResource.class,CustomerDetailsResourceImpl.class);
		map.put(ProcessAlertsResource.class,ProcessAlertsResourceImpl.class);
		return map;
	}
}
