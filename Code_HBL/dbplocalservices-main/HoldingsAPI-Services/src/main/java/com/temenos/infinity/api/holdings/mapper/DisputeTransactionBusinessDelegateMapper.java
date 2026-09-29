package com.temenos.infinity.api.holdings.mapper;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.holdings.businessdelegate.api.DisputeTransactionBusinessDelegate;
import com.temenos.infinity.api.holdings.businessdelegate.impl.DisputeTransactionBusinessDelegateImpl;


public class DisputeTransactionBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		//Add Mapping of Business Delegates interface and their implementation
	   	map.put(DisputeTransactionBusinessDelegate.class, DisputeTransactionBusinessDelegateImpl.class);

		return map;
	}
}
