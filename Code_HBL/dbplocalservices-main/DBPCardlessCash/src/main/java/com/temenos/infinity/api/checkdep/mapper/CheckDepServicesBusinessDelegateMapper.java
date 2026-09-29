package com.temenos.infinity.api.checkdep.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.checkdep.businessdelegate.api.RDCBusinessDelegate;
import com.temenos.infinity.api.checkdep.businessdelegate.impl.RDCBusinessDelegateImpl;


/**
 * 
 * @author KH1755
 * @version 1.0
 * implements {@link DBPAPIMapper}
 */
public class CheckDepServicesBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
		map.put(RDCBusinessDelegate.class, RDCBusinessDelegateImpl.class);
		
        return map;
	}

}
