package com.auth.hbl.resource.mapper;

import java.util.HashMap;
import java.util.Map;

import com.auth.hbl.businessdelegate.AuthUserManagementBusinessDelegateImplExtn;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.auth.usermanagement.businessdelegate.api.AuthUserManagementBusinessDelegate;
public class HBLAuthServicesBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(AuthUserManagementBusinessDelegate.class, AuthUserManagementBusinessDelegateImplExtn.class);
		
		return map;
	}
 

}
