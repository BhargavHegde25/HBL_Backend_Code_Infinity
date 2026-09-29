package com.dbp.transactionslimitengine.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.dbp.transactionslimitengine.resource.api.TransactionsLimitResource;
import com.dbp.transactionslimitengine.resource.impl.TransactionsLimitResourceImpl;

public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(TransactionsLimitResource.class, TransactionsLimitResourceImpl.class);
		return map;
	}
}
