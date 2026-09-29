package com.kony.adminconsole.service.servicedefinition.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.servicedefinition.resource.api.ServiceDefinitionResource;
import com.kony.adminconsole.service.servicedefinition.resource.impl.ServiceDefinitionResourceImpl;

/**
 * 
 * @author KH2660
 * This class maps resource interface to its implementation and it can be
 * changed whenever required
 *
 */

public class ServiceDefinitionResourceMapper implements DBPAPIMapper<Resource>{

	 @Override
	    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
	        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
	        /* all resource interface to implementation mappings are done here */
	        map.put(ServiceDefinitionResource.class,ServiceDefinitionResourceImpl.class);
	        return map;
	    }
}
