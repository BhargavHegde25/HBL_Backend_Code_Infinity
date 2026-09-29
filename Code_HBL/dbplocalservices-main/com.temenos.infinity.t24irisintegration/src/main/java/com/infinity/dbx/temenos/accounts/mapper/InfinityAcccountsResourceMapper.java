package com.infinity.dbx.temenos.accounts.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.infinity.dbx.temenos.accounts.resource.impl.InfinityAccountsResouceImpl;
import com.temenos.infinity.api.arrangements.resource.api.InfinityAccountsResource;

public class InfinityAcccountsResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(InfinityAccountsResource.class, InfinityAccountsResouceImpl.class);
		return map;
	}

}
