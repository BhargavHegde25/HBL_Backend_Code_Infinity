package com.kony.campaign.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.campaign.resource.api.CampaignResource;
import com.kony.campaign.resource.impl.CampaignResourceImpl;

public class BaseResourcesMapper implements DBPAPIMapper<Resource> {
	
    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        map.put(CampaignResource.class, CampaignResourceImpl.class);
        return map;
    }
}
