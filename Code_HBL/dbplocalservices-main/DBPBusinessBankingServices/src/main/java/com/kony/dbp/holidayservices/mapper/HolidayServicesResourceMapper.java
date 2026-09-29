package com.kony.dbp.holidayservices.mapper;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.dbp.holidayservices.resource.api.HolidayServicesResource;
import com.kony.dbp.holidayservices.resource.impl.HolidayServicesResourceImpl;


/**
 * 
 * @author KH2394
 * version 1.0
 * implements {@link DBPAPIMapper}
 */
public class HolidayServicesResourceMapper implements DBPAPIMapper<Resource> {


	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		//Add Mapping of Business Delegates interface and their implementation
		map.put(HolidayServicesResource.class, HolidayServicesResourceImpl.class);

		return map;
	}

}
