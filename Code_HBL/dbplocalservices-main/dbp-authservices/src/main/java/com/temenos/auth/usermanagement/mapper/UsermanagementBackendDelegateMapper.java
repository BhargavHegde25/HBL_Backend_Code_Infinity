package com.temenos.auth.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.auth.usermanagement.backenddelegate.api.AuthUserManagementBackendDelegate;
import com.temenos.auth.usermanagement.backenddelegate.impl.AuthUserManagementBackendDelegateImpl;

public class UsermanagementBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
		map.put(AuthUserManagementBackendDelegate.class, AuthUserManagementBackendDelegateImpl.class);
		return map;
	}

}
