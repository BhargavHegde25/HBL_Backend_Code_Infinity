package com.temenos.infinity.api.visaintegrationapi.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.visaintegrationapi.backenddelegate.api.VisaIntegrationApiAPIBackendDelegate;
import com.temenos.infinity.api.visaintegrationapi.backenddelegate.impl.VisaIntegrationApiAPIBackendDelegateImpl;

/**
 * Mapper between Backend Delegate Interfaces & corresponding Backend Delegate
 * Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class VisaIntegrationApiBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Register your BackendDelegate Delegate Implementation classes here
		map.put(VisaIntegrationApiAPIBackendDelegate.class, VisaIntegrationApiAPIBackendDelegateImpl.class);
		return map;
	}

}
