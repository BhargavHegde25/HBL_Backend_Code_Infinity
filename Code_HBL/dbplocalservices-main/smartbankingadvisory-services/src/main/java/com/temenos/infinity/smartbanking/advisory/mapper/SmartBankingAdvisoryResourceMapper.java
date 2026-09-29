package com.temenos.infinity.smartbanking.advisory.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.smartbanking.advisory.resource.api.SmartBankingAdvisoryResource;
import com.temenos.infinity.smartbanking.advisory.resource.impl.SmartBankingAdvisoryResourceImpl;

/**
 * @author shubham.ahuja
 *
 */
public class SmartBankingAdvisoryResourceMapper implements DBPAPIMapper<Resource> {
	
	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(SmartBankingAdvisoryResource.class, SmartBankingAdvisoryResourceImpl.class);
		return map;
		
	}

}
