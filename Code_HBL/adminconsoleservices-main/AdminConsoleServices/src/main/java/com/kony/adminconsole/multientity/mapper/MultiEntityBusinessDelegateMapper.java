package com.kony.adminconsole.multientity.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.multientity.businessdelegate.api.MultiEntityBusinessDelegate;
import com.kony.adminconsole.multientity.businessdelegate.impl.MultiEntityBusinessDelegateImpl;

public class MultiEntityBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		 Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
	        /* all business delegate interface to implementation mappings are done here */
	        map.put(MultiEntityBusinessDelegate.class, MultiEntityBusinessDelegateImpl.class);
	        return map;
	}

}
