package com.kony.adminconsole.service.servicedefinition.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.impl.ServiceDefinitionBusinessDelegateImpl;

public class ServiceDefinitionBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
	
	@Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(ServiceDefinitionBusinessDelegate.class,ServiceDefinitionBusinessDelegateImpl.class);
        return map;
    }
}
