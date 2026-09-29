package com.temenos.infinity.api.QRPayments.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.QRPayments.resource.api.QRPaymentResource;
import com.temenos.infinity.api.QRPayments.resource.impl.QRPaymentResourceImpl;

public class QRPaymentResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		// Add Mapping of Business Delegates interface and their implementation
		map.put(QRPaymentResource.class, QRPaymentResourceImpl.class);

		return map;
	}
}