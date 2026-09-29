package com.kony.adminconsole.licensing.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.licensing.businessdelegate.api.LicensingBusinessDelegate;
import com.kony.adminconsole.licensing.businessdelegate.impl.LicensingBusinessDelegateImpl;


public class LicensingBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
	
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		 Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
	        /* all business delegate interface to implementation mappings are done here */
	        map.put(LicensingBusinessDelegate.class, LicensingBusinessDelegateImpl.class);
	        return map;
	}

}
