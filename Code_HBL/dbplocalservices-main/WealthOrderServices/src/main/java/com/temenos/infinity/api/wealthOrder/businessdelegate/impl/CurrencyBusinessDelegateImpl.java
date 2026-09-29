package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.CurrencyBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.CurrencyBusinessDelegate;

public class CurrencyBusinessDelegateImpl implements CurrencyBusinessDelegate {

	@Override
	public Result getMarketRates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		CurrencyBackendDelegate currencyBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CurrencyBackendDelegate.class);
		result = currencyBackendDelegate.getMarketRates(methodID, inputArray, request, response, headersMap);
		return result;
	}

	@Override
	public Result createForexOrders(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		CurrencyBackendDelegate currencyBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CurrencyBackendDelegate.class);
		result = currencyBackendDelegate.createForexOrders(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
