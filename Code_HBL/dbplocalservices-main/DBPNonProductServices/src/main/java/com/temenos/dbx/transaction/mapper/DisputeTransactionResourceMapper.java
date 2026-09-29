package com.temenos.dbx.transaction.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;

import com.temenos.dbx.transaction.resource.api.DisputeTransactionResource;
import com.temenos.dbx.transaction.resource.impl.DisputeTransactionResourceImpl;
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
