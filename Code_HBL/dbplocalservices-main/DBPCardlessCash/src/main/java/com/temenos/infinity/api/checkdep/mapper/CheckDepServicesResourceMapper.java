package com.temenos.infinity.api.checkdep.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;

import com.temenos.infinity.api.checkdep.resource.api.RDCResource;

import com.temenos.infinity.api.checkdep.resource.impl.RDCResourceImpl;

/**
 * 
 * @author eivanov
 * @version 1.0
 * implements {@link DBPAPIMapper}
 */
public class CheckDepServicesResourceMapper implements DBPAPIMapper<Resource> {
	

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
		
		map.put(RDCResource.class, RDCResourceImpl.class);
		
        return map;
	}

}
