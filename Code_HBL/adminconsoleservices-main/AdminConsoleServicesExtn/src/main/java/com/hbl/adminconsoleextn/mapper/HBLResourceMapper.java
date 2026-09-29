package com.hbl.adminconsoleextn.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.hbl.adminconsoleextn.impl.ContractResourceImplExtn;
import com.hbl.adminconsoleextn.impl.CustomerRoleResourceImplExtn;
import com.hbl.adminconsoleextn.impl.FeaturesAndActionsResourceImplExtn;
import com.hbl.adminconsoleextn.impl.ServiceDefinitionResourceImplExtn;
import com.kony.adminconsole.service.contract.resource.api.ContractResource;
import com.kony.adminconsole.service.customerrole.resource.api.CustomerRoleResource;
import com.kony.adminconsole.service.featuresandactions.resource.api.FeaturesAndActionsResource;
import com.kony.adminconsole.service.servicedefinition.resource.api.ServiceDefinitionResource;

public class HBLResourceMapper implements DBPAPIMapper<Resource>{

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(FeaturesAndActionsResource.class, FeaturesAndActionsResourceImplExtn.class);
		map.put(ServiceDefinitionResource.class, ServiceDefinitionResourceImplExtn.class);
		map.put(CustomerRoleResource.class, CustomerRoleResourceImplExtn.class);
		map.put(ContractResource.class, ContractResourceImplExtn.class);
		return map;
	}
}
