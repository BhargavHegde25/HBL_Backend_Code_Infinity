package com.temenos.dbx.product.forexservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.forexservices.resource.api.ForexResource;
import com.temenos.dbx.product.forexservices.resource.impl.ForexResourceImpl;

public class ForexServicesResourceMapper implements DBPAPIMapper<Resource> {
	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Resource interface and their implementation
		map.put(ForexResource.class, ForexResourceImpl.class);
		return map;
	}

}
