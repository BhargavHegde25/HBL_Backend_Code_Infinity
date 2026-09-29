package com.kony.campaignsmanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.campaignsmanagement.resource.api.CampaignsManagementResource;
import com.kony.campaignsmanagement.resource.impl.CampaignsManagementResourceImpl;

public class CampaignsManagementResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(CampaignsManagementResource.class, CampaignsManagementResourceImpl.class);
        return map;
	}

}
