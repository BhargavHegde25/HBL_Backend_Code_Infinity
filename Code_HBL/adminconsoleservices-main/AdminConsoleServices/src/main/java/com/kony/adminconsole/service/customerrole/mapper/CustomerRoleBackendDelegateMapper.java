package com.kony.adminconsole.service.customerrole.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.customerrole.backenddelegate.api.CustomerRoleBackendDelegate;
import com.kony.adminconsole.service.customerrole.backenddelegate.impl.CustomerRoleBackendDelegateImpl;
public class CustomerRoleBackendDelegateMapper implements DBPAPIMapper<BackendDelegate>{

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

       /* mappings of backend delegate interface and implementation to be added */
        map.put(CustomerRoleBackendDelegate.class, CustomerRoleBackendDelegateImpl.class);
        
        return map;
	}

}
