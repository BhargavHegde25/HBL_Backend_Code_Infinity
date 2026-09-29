package com.temenos.infinity.api.arrangements.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.arrangements.backenddelegate.api.DocumentStorageBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.impl.DocumentStorageBackendDelegateImpl;


public class BaseBackendDelegatesMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
		map.put(DocumentStorageBackendDelegate.class, DocumentStorageBackendDelegateImpl.class);
		return map;
	}

}
