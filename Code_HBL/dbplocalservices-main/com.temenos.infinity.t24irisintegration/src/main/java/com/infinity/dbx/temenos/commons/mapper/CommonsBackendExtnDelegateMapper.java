package com.infinity.dbx.temenos.commons.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.infinity.dbx.temenos.commons.backenddelegate.impl.CancellationReasonBackendDelegateImplExtn;
import com.infinity.dbx.temenos.commons.backenddelegate.impl.TransactionLimitsBackendDelegateImplExtn;
import com.temenos.dbx.product.commons.backenddelegate.api.CancellationReasonBackendDelegate;
import com.temenos.dbx.product.commons.backenddelegate.api.TransactionLimitsBackendDelegate;


public class CommonsBackendExtnDelegateMapper implements DBPAPIMapper<BackendDelegate>{
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Add Mapping of Backend Delegates interface and their implementation
		map.put(CancellationReasonBackendDelegate.class, CancellationReasonBackendDelegateImplExtn.class);
		map.put(TransactionLimitsBackendDelegate.class, TransactionLimitsBackendDelegateImplExtn.class);
		return map;
	}
}
