package com.dbp.externalevent.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.externalevent.businessdelegate.api.UpdateEventConfigBusinessDelegate;
import com.dbp.externalevent.businessdelegate.impl.UpdateEventConfigBusinessDelegateImpl;

public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(UpdateEventConfigBusinessDelegate.class, UpdateEventConfigBusinessDelegateImpl.class);
		return map;
	}

}
