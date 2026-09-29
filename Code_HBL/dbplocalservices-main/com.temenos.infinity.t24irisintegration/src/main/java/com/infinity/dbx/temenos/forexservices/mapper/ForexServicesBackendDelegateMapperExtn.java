package com.infinity.dbx.temenos.forexservices.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.forexservices.backenddelegate.api.ForexBackendDelegate;
import com.infinity.dbx.temenos.forexservices.backenddelegate.api.impl.ForexBackendDelegateImplExtn;

public class ForexServicesBackendDelegateMapperExtn implements DBPAPIMapper<BackendDelegate> {
	
	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Add Mapping of Backend Delegates interface and their implementation
		map.put(ForexBackendDelegate.class, ForexBackendDelegateImplExtn.class);
		return map;
	}

}
