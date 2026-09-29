package com.temenos.auth.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.auth.usermanagement.resource.api.AuthUserManagementResource;
import com.temenos.auth.usermanagement.resource.impl.AuthUserManagementResourceImpl;

public class UsermanagementResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        map.put(AuthUserManagementResource.class, AuthUserManagementResourceImpl.class);
        return map;
	}

}
