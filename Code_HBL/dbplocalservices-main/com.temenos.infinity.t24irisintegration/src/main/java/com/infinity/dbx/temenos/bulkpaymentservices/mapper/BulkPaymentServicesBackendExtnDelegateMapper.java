package com.infinity.dbx.temenos.bulkpaymentservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.infinity.dbx.temenos.bulkpaymentservices.backenddelegate.api.impl.BulkPaymentFileBackendDelegateImplExtn;
import com.infinity.dbx.temenos.bulkpaymentservices.backenddelegate.api.impl.BulkPaymentPOBackendDelegateImplExtn;
import com.infinity.dbx.temenos.bulkpaymentservices.backenddelegate.api.impl.BulkPaymentRecordBackendDelegateImplExtn;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentFileBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentPOBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;

public class BulkPaymentServicesBackendExtnDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Add Mapping of Backend Delegates interface and their implementation
		map.put(BulkPaymentFileBackendDelegate.class, BulkPaymentFileBackendDelegateImplExtn.class);
		map.put(BulkPaymentPOBackendDelegate.class, BulkPaymentPOBackendDelegateImplExtn.class);
		map.put(BulkPaymentRecordBackendDelegate.class, BulkPaymentRecordBackendDelegateImplExtn.class);		
		return map;
	}

}
