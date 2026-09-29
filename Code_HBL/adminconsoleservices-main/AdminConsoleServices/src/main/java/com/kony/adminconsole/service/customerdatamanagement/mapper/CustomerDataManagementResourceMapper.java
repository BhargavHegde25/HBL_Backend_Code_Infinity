package com.kony.adminconsole.service.customerdatamanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.customerdatamanagement.resource.api.CustomerDataManagementResource;
import com.kony.adminconsole.service.customerdatamanagement.resource.impl.CustomerDataManagementResourceImpl;

public class CustomerDataManagementResourceMapper implements DBPAPIMapper<Resource> {
	@Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(CustomerDataManagementResource.class, CustomerDataManagementResourceImpl.class);
        return map;
    }
}
