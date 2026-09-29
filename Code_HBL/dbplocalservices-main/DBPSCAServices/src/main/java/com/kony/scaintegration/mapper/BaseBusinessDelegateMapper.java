package com.kony.scaintegration.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.scaintegration.businessdelegate.api.SCAIntegrationBusinessDelegate;
import com.kony.scaintegration.businessdelegate.api.UpdateVerifyFlagBusinessDelegate;
import com.kony.scaintegration.businessdelegate.impl.SCAIntegrationBusinessDelegateImpl;
import com.kony.scaintegration.businessdelegate.impl.UpdateVerifyFlagBusinessDelegateImpl;

public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		map.put(SCAIntegrationBusinessDelegate.class, SCAIntegrationBusinessDelegateImpl.class);
		map.put(UpdateVerifyFlagBusinessDelegate.class, UpdateVerifyFlagBusinessDelegateImpl.class);
		return map;
	}

}
