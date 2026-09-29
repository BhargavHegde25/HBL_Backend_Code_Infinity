package com.kony.campaignsmanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.campaignsmanagement.businessdelegate.api.CampaignsManagementBusinessDelegate;
import com.kony.campaignsmanagement.businessdelegate.impl.CampaignsManagementBusinessDelegateImpl;

public class CampaignsManagementBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(CampaignsManagementBusinessDelegate.class, CampaignsManagementBusinessDelegateImpl.class);
        return map;
	}

}
