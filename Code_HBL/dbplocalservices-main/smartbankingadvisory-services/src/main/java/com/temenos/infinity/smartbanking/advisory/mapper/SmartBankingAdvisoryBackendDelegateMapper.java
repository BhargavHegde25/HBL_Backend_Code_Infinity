package com.temenos.infinity.smartbanking.advisory.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.smartbanking.advisory.backenddelegate.api.SmartBankingAdvisoryBackendDelegate;
import com.temenos.infinity.smartbanking.advisory.backenddelegate.impl.SmartBankingAdvisoryBackendDelegateImpl;

/**
 * @author shubham.ahuja
 *
 */
public class SmartBankingAdvisoryBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
	
	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
		map.put(SmartBankingAdvisoryBackendDelegate.class, SmartBankingAdvisoryBackendDelegateImpl.class);
		return map;

	}
}
