package com.temenos.dbx.product.commons.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.commons.resource.api.CancellationReasonsResource;
import com.temenos.dbx.product.commons.resource.impl.CancellationReasonsResourceImpl;


public class CommonsResourceMapper implements DBPAPIMapper<Resource> {
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
		map.put(CancellationReasonsResource.class, CancellationReasonsResourceImpl.class);
		return map;
	}
}
