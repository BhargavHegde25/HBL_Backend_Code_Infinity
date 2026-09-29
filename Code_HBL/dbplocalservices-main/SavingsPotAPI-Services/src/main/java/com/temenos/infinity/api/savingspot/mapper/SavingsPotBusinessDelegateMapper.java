package com.temenos.infinity.api.savingspot.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.savingspot.businessdelegate.api.SavingsPotBusinessDelegate;
import com.temenos.infinity.api.savingspot.businessdelegate.impl.SavingsPotBusinessDelegateImpl;

public class SavingsPotBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	    @Override
	    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
	        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

	        // Add Mapping of Business Delegates interface and their implementation
	        map.put(SavingsPotBusinessDelegate.class, SavingsPotBusinessDelegateImpl.class);

	        return map;
	    }

}
