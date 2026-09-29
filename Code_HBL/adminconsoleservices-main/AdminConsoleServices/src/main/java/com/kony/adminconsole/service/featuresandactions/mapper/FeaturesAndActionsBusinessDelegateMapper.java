package com.kony.adminconsole.service.featuresandactions.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.featuresandactions.businessdelegate.api.FeaturesAndActionsBusinessDelegate;
import com.kony.adminconsole.service.featuresandactions.businessdelegate.impl.FeaturesAndActionsBusinessDelegateImpl;

public class FeaturesAndActionsBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
	
	@Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(FeaturesAndActionsBusinessDelegate.class,FeaturesAndActionsBusinessDelegateImpl.class);
        return map;
    }

}
