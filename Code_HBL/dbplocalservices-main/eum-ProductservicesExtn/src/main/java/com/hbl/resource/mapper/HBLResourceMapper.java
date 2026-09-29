package com.hbl.resource.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.hbl.resource.impl.ContractResourceImplExtn;
import com.hbl.resource.impl.InfinityUserManagementResourceImplExtn;
import com.hbl.resource.impl.UserManagementResourceImplExtn;
import com.temenos.dbx.eum.product.contract.resource.api.ContractResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.UserManagementResource;

public class HBLResourceMapper implements DBPAPIMapper<Resource>{

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(InfinityUserManagementResource.class, InfinityUserManagementResourceImplExtn.class);
		map.put(ContractResource.class, ContractResourceImplExtn.class);
		map.put(UserManagementResource.class, UserManagementResourceImplExtn.class);
		return map;
	}

}
