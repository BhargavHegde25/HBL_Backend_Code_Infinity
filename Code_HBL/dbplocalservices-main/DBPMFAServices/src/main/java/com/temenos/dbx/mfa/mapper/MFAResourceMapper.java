package com.temenos.dbx.mfa.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.mfa.resource.api.MFAServiceResource;
import com.temenos.dbx.mfa.resource.impl.MFAServiceResourceImpl;

public class MFAResourceMapper implements DBPAPIMapper<Resource> {

	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		map.put(MFAServiceResource.class, MFAServiceResourceImpl.class);

		return map;
	}
}
