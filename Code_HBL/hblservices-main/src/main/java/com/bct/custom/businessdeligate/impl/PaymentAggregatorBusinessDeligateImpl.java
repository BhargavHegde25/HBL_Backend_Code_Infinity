package com.bct.custom.businessdeligate.impl;

import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.PaymentAggregatorBackendDeligate;
import com.bct.custom.businessdeligate.api.PaymentAggregatorBusinessDeligate;
import com.bct.custom.dto.PaymentAggregatorDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class PaymentAggregatorBusinessDeligateImpl implements PaymentAggregatorBusinessDeligate {

	@Override
	public JSONArray getAllPaymentAggregator(Map<String, Object> headerMap) throws ApplicationException {
		PaymentAggregatorBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(PaymentAggregatorBackendDeligate.class);
		return backendDelegate.getAllPaymentAggregator(headerMap);
	}

	@Override
	public JSONArray getPaymentAggregator(String code, Map<String, Object> headerMap) throws ApplicationException {
		PaymentAggregatorBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(PaymentAggregatorBackendDeligate.class);
		return backendDelegate.getPaymentAggregator(code, headerMap);
	}

	@Override
	public Result updatePaymentAggregator(PaymentAggregatorDTO dto, Map<String, Object> headerMap,
			Map<String, Object> inputs) throws ApplicationException {
		PaymentAggregatorBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(PaymentAggregatorBackendDeligate.class);
		return backendDelegate.updatePaymentAggregator(dto, headerMap, inputs);
	}

	@Override
	public Result createPaymentAggregator(PaymentAggregatorDTO dto, Map<String, Object> headerMap,
			Map<String, Object> inputs) throws ApplicationException {
		PaymentAggregatorBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(PaymentAggregatorBackendDeligate.class);
		return backendDelegate.createPaymentAggregator(dto, headerMap, inputs);
	}
	
}
