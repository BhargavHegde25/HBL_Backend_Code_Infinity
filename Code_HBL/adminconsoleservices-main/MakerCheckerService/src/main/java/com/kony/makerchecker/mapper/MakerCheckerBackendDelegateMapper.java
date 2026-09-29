package com.kony.makerchecker.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.makerchecker.backenddelegate.impl.MakerCheckerBackendDelegateImpl;
import com.kony.makerchecker.backenddelegate.api.MakerCheckerBackendDelegate;

public class MakerCheckerBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(MakerCheckerBackendDelegate.class, MakerCheckerBackendDelegateImpl.class);
        return map;
	}

}
