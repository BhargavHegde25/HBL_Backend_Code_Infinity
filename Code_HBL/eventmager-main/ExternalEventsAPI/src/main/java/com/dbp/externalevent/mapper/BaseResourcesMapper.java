package com.dbp.externalevent.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.dbp.externalevent.resource.api.PushExternalCampaignResource;
import com.dbp.externalevent.resource.api.UpdateEventConfigResource;
import com.dbp.externalevent.resource.impl.PushExternalCampaignResourceImpl;
import com.dbp.externalevent.resource.impl.UpdateEventConfigResourceImpl;

public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(PushExternalCampaignResource.class, PushExternalCampaignResourceImpl.class);
		map.put(UpdateEventConfigResource.class, UpdateEventConfigResourceImpl.class);
		return map;
	}
}
