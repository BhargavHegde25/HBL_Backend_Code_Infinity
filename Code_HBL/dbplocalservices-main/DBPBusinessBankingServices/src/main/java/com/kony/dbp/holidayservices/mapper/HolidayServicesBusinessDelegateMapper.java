package com.kony.dbp.holidayservices.mapper;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.dbp.holidayservices.businessdelegate.api.HolidayServicesBusinessDelegate;
import com.kony.dbp.holidayservices.businessdelegate.impl.HolidayServicesBusinessDelegateImpl;


/**
 * 
 * @author KH2394
 * version 1.0
 * implements {@link DBPAPIMapper}
 */
public class HolidayServicesBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		//Add Mapping of Business Delegates interface and their implementation
		map.put(HolidayServicesBusinessDelegate.class, HolidayServicesBusinessDelegateImpl.class);

		return map;
	}
}
