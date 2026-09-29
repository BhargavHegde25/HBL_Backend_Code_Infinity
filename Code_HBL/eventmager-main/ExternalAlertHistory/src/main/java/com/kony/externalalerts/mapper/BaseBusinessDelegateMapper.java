package com.kony.externalalerts.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.externalalerts.businessdelegate.api.ExternalAlertsBusinessDelegate;
import com.kony.externalalerts.businessdelegate.impl.ExternalAlertsBusinessDelegateImpl;

public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		map.put(ExternalAlertsBusinessDelegate.class, ExternalAlertsBusinessDelegateImpl.class);

		return map;
	}

}
