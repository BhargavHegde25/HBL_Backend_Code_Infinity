package com.temenos.auth.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.auth.usermanagement.businessdelegate.api.AuthUserManagementBusinessDelegate;
import com.temenos.auth.usermanagement.businessdelegate.impl.AuthUserManagementBusinessDelegateImpl;

public class UsermanagementBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(AuthUserManagementBusinessDelegate.class, AuthUserManagementBusinessDelegateImpl.class);
		return map;
	}
}
