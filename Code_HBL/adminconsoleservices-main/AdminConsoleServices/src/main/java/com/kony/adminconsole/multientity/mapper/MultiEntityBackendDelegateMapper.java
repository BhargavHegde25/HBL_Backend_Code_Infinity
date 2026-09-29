package com.kony.adminconsole.multientity.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.multientity.backenddelegate.api.MultiEntityBackendDelegate;
import com.kony.adminconsole.multientity.backenddelegate.impl.MultiEntityBackendDelegateImpl;

public class MultiEntityBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        /* all backend delegate interface to implementation mappings are done here */
        map.put(MultiEntityBackendDelegate.class, MultiEntityBackendDelegateImpl.class);
        return map;
	}

}
