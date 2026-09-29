package com.temenos.infinity.api.QRPayments.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.QRPayments.backenddelegate.api.QRPaymentBackendDelegate;
import com.temenos.infinity.api.QRPayments.backenddelegate.impl.QRPaymentBackendDelegateImpl;

// Mapper between Backend Delegate Interfaces & corresponding Backend Delegate Implementation classes

public class QRPaymentBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		// Register your BackendDelegate Delegate Implementation classes here
		map.put(QRPaymentBackendDelegate.class, QRPaymentBackendDelegateImpl.class);
		return map;
	}

}
