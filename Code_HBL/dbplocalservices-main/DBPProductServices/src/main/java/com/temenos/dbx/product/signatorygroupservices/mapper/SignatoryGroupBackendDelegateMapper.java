package com.temenos.dbx.product.signatorygroupservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.signatorygroupservices.backenddelegate.api.SignatoryGroupBackendDelegate;
import com.temenos.dbx.product.signatorygroupservices.backenddelegate.impl.SignatoryGroupBackendDelegateImpl;

/**
 * 
 * @author KH2624
 * @version 1.0
 * implements {@link DBPAPIMapper}
 */
public class SignatoryGroupBackendDelegateMapper implements DBPAPIMapper<BackendDelegate>{

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		//Add Mapping of Backend Delegates interface and their implementation
		
		map.put(SignatoryGroupBackendDelegate.class, SignatoryGroupBackendDelegateImpl.class);
		return map;
	}
}