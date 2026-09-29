package com.temenos.dbx.transaction.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.transaction.resource.api.DirectDebitsResource;
import com.temenos.dbx.transaction.resource.impl.DirectDebitsResourceImpl;

/**
 * @author sribarani.vasthan
 */
public class DirectDebitsResourceMapper implements DBPAPIMapper<Resource> {
	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		// Add Mapping of Business Delegates interface and their implementation
		map.put(DirectDebitsResource.class, DirectDebitsResourceImpl.class);

		return map;
	}
}
