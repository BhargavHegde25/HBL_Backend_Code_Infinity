package com.temenos.dbx.product.forexservices.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.forexservices.backenddelegate.api.ForexBackendDelegate;
import com.temenos.dbx.product.forexservices.backenddelegate.impl.ForexBackendDelegateImpl;

public class ForexServicesBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
	
	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Add Mapping of Backend Delegates interface and their implementation
		map.put(ForexBackendDelegate.class, ForexBackendDelegateImpl.class);
		return map;
	}

}
