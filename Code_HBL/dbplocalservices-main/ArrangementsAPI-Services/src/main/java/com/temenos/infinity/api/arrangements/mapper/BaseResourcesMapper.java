package com.temenos.infinity.api.arrangements.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.arrangements.resource.api.DocumentStorageResource;
import com.temenos.infinity.api.arrangements.resource.impl.DocumentStorageResourceImpl;



public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(DocumentStorageResource.class, DocumentStorageResourceImpl.class);
		return map;
	}

}
