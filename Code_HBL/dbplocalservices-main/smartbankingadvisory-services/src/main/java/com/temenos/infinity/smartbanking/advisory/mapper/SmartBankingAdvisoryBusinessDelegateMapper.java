package com.temenos.infinity.smartbanking.advisory.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.smartbanking.advisory.businessdelegate.api.SmartBankingAdvisoryBusinessDelegate;
import com.temenos.infinity.smartbanking.advisory.businessdelegate.impl.SmartBankingAdvisoryBusinessDelegateImpl;

/**
 * @author shubham.ahuja
 *
 */
public class SmartBankingAdvisoryBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{
	
	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(SmartBankingAdvisoryBusinessDelegate.class, SmartBankingAdvisoryBusinessDelegateImpl.class);
		return map;
	}

}
