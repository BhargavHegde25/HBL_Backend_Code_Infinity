package com.temenos.infinity.api.QRPayments.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.QRPayments.businessdelegate.api.QRPaymentBusinessDelegate;
import com.temenos.infinity.api.QRPayments.businessdelegate.impl.QRPaymentBusinessDelegateImpl;

// Mapper between Business Delegate Interfaces & corresponding Business Delegate Implementation classes

public class QRPaymentBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		// Register your BusinessDelegate Delegate Implementation classes here
		map.put(QRPaymentBusinessDelegate.class, QRPaymentBusinessDelegateImpl.class);
		return map;
	}

}
