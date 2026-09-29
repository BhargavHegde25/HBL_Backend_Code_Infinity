package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.MarketOrderBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.MarketOrderBusinessDelegate;

public class MarketOrderBusinessDelegateImpl implements MarketOrderBusinessDelegate {

	@Override
	public Result createMarketOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		MarketOrderBackendDelegate marketOrderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MarketOrderBackendDelegate.class);
		result = marketOrderBackendDelegate.createMarketOrder(methodID, inputArray, request, response, headersMap);
		return result;
	}

	@Override
	public Result modifyOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		MarketOrderBackendDelegate marketOrderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MarketOrderBackendDelegate.class);
		result = marketOrderBackendDelegate.modifyOrder(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
