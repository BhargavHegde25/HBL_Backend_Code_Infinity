package com.kony.adminconsole.licensing.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.licensing.backenddelegate.api.LicensingBackendDelegate;
import com.kony.adminconsole.licensing.backenddelegate.impl.LicensingBackendDelegateImpl;

public class LicensingBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        /* all backend delegate interface to implementation mappings are done here */
        map.put(LicensingBackendDelegate.class, LicensingBackendDelegateImpl.class);
        return map;
	}
}
