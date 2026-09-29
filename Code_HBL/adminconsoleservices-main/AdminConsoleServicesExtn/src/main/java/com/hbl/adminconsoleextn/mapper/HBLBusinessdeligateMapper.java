package com.hbl.adminconsoleextn.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.hbl.adminconsoleextn.impl.FeaturesAndActionsBusinessDelegateImplExtn;
import com.hbl.adminconsoleextn.impl.ServiceDefinitionBusinessDelegateImplExtn;
import com.kony.adminconsole.service.featuresandactions.businessdelegate.api.FeaturesAndActionsBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.api.ServiceDefinitionBusinessDelegate;


public class HBLBusinessdeligateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(FeaturesAndActionsBusinessDelegate.class, FeaturesAndActionsBusinessDelegateImplExtn.class);
		map.put(ServiceDefinitionBusinessDelegate.class, ServiceDefinitionBusinessDelegateImplExtn.class);
		
		return map;
	}

}
