package com.kony.makerchecker.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.makerchecker.businessdelegate.api.MakerCheckerBusinessDelegate;
import com.kony.makerchecker.businessdelegate.impl.MakerCheckerBusinessDelegateImpl;

public class MakerCheckerBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(MakerCheckerBusinessDelegate.class, MakerCheckerBusinessDelegateImpl.class);
        return map;
	}

}
