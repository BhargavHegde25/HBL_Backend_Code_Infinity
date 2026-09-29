package com.temenos.infinity.api.visaintegrationapi.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.visaintegrationapi.resource.api.VisaIntegrationApiResource;
import com.temenos.infinity.api.visaintegrationapi.resource.impl.VisaIntegrationApiResourceImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */

public class VisaIntegrationApiResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		// Add Mapping of Business Delegates interface and their implementation
		map.put(VisaIntegrationApiResource.class, VisaIntegrationApiResourceImpl.class);

		return map;
	}
}
