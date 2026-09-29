package com.temenos.infinity.api.wealth.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.wealth.resource.api.ReportAndDownloadTypesResource;
import com.temenos.infinity.api.wealth.resource.impl.ReportAndDownloadTypesResourceImpl;

public class ReportAndDownloadTypesResourceMapper implements DBPAPIMapper<Resource> {
	
	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(ReportAndDownloadTypesResource.class, ReportAndDownloadTypesResourceImpl.class);
        return map;
	}
}
