package com.temenos.dbx.product.commons.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.commons.backenddelegate.api.CancellationReasonBackendDelegate;
import com.temenos.dbx.product.commons.backenddelegate.api.TransactionLimitsBackendDelegate;
import com.temenos.dbx.product.commons.backenddelegate.impl.CancellationReasonBackendDelegateImpl;
import com.temenos.dbx.product.commons.backenddelegate.impl.TransactionLimitsBackendDelegateImpl;
import com.temenos.dbx.product.commons.backenddelegate.api.UserBackendDelegate;
import com.temenos.dbx.product.commons.backenddelegate.impl.CancellationReasonBackendDelegateImpl;
import com.temenos.dbx.product.commons.backenddelegate.impl.UserBackendDelegateImpl;

public class CommonsBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Add Mapping of Backend Delegates interface and their implementation
		map.put(CancellationReasonBackendDelegate.class, CancellationReasonBackendDelegateImpl.class);
		map.put(TransactionLimitsBackendDelegate.class, TransactionLimitsBackendDelegateImpl.class);
		map.put(UserBackendDelegate.class, UserBackendDelegateImpl.class);
		
		return map;
	}

}
