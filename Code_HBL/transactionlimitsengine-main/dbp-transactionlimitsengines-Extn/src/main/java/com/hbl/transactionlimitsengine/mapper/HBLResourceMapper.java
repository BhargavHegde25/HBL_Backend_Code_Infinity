package com.hbl.transactionlimitsengine.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.dbp.transactionslimitengine.resource.api.TransactionsLimitResource;
import com.hbl.transactionlimitsengine.impl.TransactionsLimitResourceImplExtn;

public class HBLResourceMapper implements DBPAPIMapper<Resource>{

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(TransactionsLimitResource.class, TransactionsLimitResourceImplExtn.class);
		return map;
	}
}
