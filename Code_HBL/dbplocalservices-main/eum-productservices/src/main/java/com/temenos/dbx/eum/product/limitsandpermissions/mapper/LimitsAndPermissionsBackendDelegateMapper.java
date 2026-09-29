package com.temenos.dbx.eum.product.limitsandpermissions.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.api.LimitsAndPermissionsBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.impl.LimitsAndPermissionsBackendDelegateImpl;

public class LimitsAndPermissionsBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
	
	@Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        map.put(LimitsAndPermissionsBackendDelegate.class,LimitsAndPermissionsBackendDelegateImpl.class);
        return map; 
	}

}
