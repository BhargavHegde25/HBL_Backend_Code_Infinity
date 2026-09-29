package com.temenos.dbx.product.bulkpaymentservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentFileBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentPOBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.impl.BulkPaymentFileBackendDelegateImpl;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.impl.BulkPaymentPOBackendDelegateImpl;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.impl.BulkPaymentRecordBackendDelegateImpl;

public class BulkPaymentServicesBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Add Mapping of Backend Delegates interface and their implementation
		map.put(BulkPaymentFileBackendDelegate.class, BulkPaymentFileBackendDelegateImpl.class);
		map.put(BulkPaymentPOBackendDelegate.class, BulkPaymentPOBackendDelegateImpl.class);
		map.put(BulkPaymentRecordBackendDelegate.class, BulkPaymentRecordBackendDelegateImpl.class);
		return map;
	}

}
