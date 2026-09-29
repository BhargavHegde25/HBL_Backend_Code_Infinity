package com.temenos.dbx.transaction.mapper;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.transaction.resource.api.SwiftCodeResource;
import com.temenos.dbx.transaction.resource.impl.SwiftCodeResourceImpl;


public class SwiftCodeResourceMapper implements DBPAPIMapper<Resource> {


	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		//Add Mapping of Business Delegates interface and their implementation
		map.put(SwiftCodeResource.class, SwiftCodeResourceImpl.class);

		return map;
	}

}
