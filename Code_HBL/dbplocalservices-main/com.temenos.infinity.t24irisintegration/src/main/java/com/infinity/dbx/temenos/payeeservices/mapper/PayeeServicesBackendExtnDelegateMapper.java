package com.infinity.dbx.temenos.payeeservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.infinity.dbx.temenos.payeeservices.backenddelegate.api.impl.BulkPaymentPayeeBackendDelegateImplExtn;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.BulkPaymentsPayeeBackendDelegate;


public class PayeeServicesBackendExtnDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Add Mapping of Backend Delegates interface and their implementation
		map.put(BulkPaymentsPayeeBackendDelegate.class, BulkPaymentPayeeBackendDelegateImplExtn.class);
		return map;
	}

}
