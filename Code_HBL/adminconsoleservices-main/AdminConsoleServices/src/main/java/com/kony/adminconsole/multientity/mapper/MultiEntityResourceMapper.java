package com.kony.adminconsole.multientity.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.multientity.resource.api.MultiEntityResource;
import com.kony.adminconsole.multientity.resource.impl.MultiEntityResourceImpl;


public class MultiEntityResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(MultiEntityResource.class, MultiEntityResourceImpl.class);
        return map;
	}

}
