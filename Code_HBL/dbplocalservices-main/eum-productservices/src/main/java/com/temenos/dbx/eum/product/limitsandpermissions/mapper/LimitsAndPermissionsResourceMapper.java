package com.temenos.dbx.eum.product.limitsandpermissions.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.eum.product.limitsandpermissions.resource.api.LimitsAndPermissionsResource;
import com.temenos.dbx.eum.product.limitsandpermissions.resource.impl.LimitsAndPermissionsResourceImpl;

public class LimitsAndPermissionsResourceMapper implements DBPAPIMapper<Resource>{

	 @Override
	    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
	        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
	        /* all resource interface to implementation mappings are done here */
	        map.put(LimitsAndPermissionsResource.class,LimitsAndPermissionsResourceImpl.class);
	        return map;
	    }
}
