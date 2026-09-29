package com.temenos.infinity.api.holdings.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;

import com.temenos.infinity.api.holdings.resource.api.DisputeTransactionResource;
import com.temenos.infinity.api.holdings.resource.impl.DisputeTransactionResourceImpl;
import com.dbp.core.api.Resource;


public class DisputeTransactionResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		//Add Mapping of Business Delegates interface and their implementation
		map.put(DisputeTransactionResource.class, DisputeTransactionResourceImpl.class);

		return map;
	}
}
