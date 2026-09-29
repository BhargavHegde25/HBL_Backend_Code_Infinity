package com.kony.makerchecker.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.makerchecker.resource.api.MakerCheckerResource;
import com.kony.makerchecker.resource.impl.MakerCheckerResourceImpl;

public class MakerCheckerResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(MakerCheckerResource.class, MakerCheckerResourceImpl.class);
        return map;
	}

}
