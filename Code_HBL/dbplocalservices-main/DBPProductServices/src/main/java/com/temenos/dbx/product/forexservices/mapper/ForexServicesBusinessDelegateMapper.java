package com.temenos.dbx.product.forexservices.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.forexservices.businessdelegate.api.ForexBusinessDelegate;
import com.temenos.dbx.product.forexservices.businessdelegate.impl.ForexBusinessDelegateImpl;

public class ForexServicesBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{
	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {

		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(ForexBusinessDelegate.class, ForexBusinessDelegateImpl.class);
        return map;
	}
}
