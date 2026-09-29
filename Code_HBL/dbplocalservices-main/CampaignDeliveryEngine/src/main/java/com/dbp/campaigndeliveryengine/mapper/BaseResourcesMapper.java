package com.dbp.campaigndeliveryengine.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.campaigndeliveryengine.resource.api.DeliverCampaignResource;
import com.dbp.campaigndeliveryengine.resource.api.ServeEventResource;
import com.dbp.campaigndeliveryengine.resource.impl.DeliverCampaignResourceImpl;
import com.dbp.campaigndeliveryengine.resource.impl.ServeEventResourceImpl;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;

public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(DeliverCampaignResource.class, DeliverCampaignResourceImpl.class);
		map.put(ServeEventResource.class, ServeEventResourceImpl.class);
		return map;
	}
}
