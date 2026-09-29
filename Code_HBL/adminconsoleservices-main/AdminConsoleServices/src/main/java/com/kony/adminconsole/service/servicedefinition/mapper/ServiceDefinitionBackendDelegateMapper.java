package com.kony.adminconsole.service.servicedefinition.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.servicedefinition.backenddelegate.api.ServiceDefinitionBackendDelegate;
import com.kony.adminconsole.service.servicedefinition.backenddelegate.impl.ServiceDefinitionBackendDelegateImpl;

public class ServiceDefinitionBackendDelegateMapper implements DBPAPIMapper<BackendDelegate>{
	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {


        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

       /* mappings of backend delegate interface and implementation to be added */
        map.put(ServiceDefinitionBackendDelegate.class, ServiceDefinitionBackendDelegateImpl.class);
        
        return map;
	}

}
