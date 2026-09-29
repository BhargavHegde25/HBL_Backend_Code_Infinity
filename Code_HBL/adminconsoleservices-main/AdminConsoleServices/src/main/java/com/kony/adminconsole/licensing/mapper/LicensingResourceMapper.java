package com.kony.adminconsole.licensing.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.licensing.resource.api.LicensingResource;
import com.kony.adminconsole.licensing.resource.impl.LicensingResourceImpl;


public class LicensingResourceMapper implements DBPAPIMapper<Resource> {

	@Override	
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(LicensingResource.class, LicensingResourceImpl.class);
        return map;
	}

}
