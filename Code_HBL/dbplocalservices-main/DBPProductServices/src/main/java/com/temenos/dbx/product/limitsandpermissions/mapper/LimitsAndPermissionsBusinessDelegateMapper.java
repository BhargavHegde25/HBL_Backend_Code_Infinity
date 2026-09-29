package com.temenos.dbx.product.limitsandpermissions.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.limitsandpermissions.businessdelegate.api.LimitsAndPermissionsBusinessDelegate;
import com.temenos.dbx.product.limitsandpermissions.businessdelegate.impl.LimitsAndPermissionsBusinessDelegateImpl;

public class LimitsAndPermissionsBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
	
	@Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(LimitsAndPermissionsBusinessDelegate.class,LimitsAndPermissionsBusinessDelegateImpl.class);
        return map; 
	}

}
