package com.kony.adminconsole.service.customerrole.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.customerrole.resource.api.CustomerRoleResource;
import com.kony.adminconsole.service.customerrole.resource.impl.CustomerRoleResourceImpl;

public class CustomerRoleResourceMapper implements DBPAPIMapper<Resource>{

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		 map.put(CustomerRoleResource.class,CustomerRoleResourceImpl.class);
		return map;
	}

}
