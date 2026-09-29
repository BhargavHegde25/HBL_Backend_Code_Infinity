package com.temenos.dbx.product.achservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.achservices.backenddelegate.api.ACHFileBackendDelegate;
import com.temenos.dbx.product.achservices.backenddelegate.api.ACHTransactionBackendDelegate;
import com.temenos.dbx.product.achservices.backenddelegate.impl.ACHFileBackendDelegateImpl;
import com.temenos.dbx.product.achservices.backenddelegate.impl.ACHTransactionBackendDelegateImpl;

public class ACHServicesBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Add Mapping of Backend Delegates interface and their implementation
		map.put(ACHFileBackendDelegate.class, ACHFileBackendDelegateImpl.class);
		map.put(ACHTransactionBackendDelegate.class, ACHTransactionBackendDelegateImpl.class);
		return map;
	}

}
