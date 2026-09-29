package com.kony.campaignsmanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.campaignsmanagement.backenddelegate.api.CampaignsManagementBackendDelegate;
import com.kony.campaignsmanagement.backenddelegate.impl.CampaignsManagementBackendDelegateImpl;

public class CampaignsManagementBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(CampaignsManagementBackendDelegate.class, CampaignsManagementBackendDelegateImpl.class);
        return map;
	}

}
