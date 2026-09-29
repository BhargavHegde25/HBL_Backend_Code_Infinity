package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.StockNewsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.StockNewsBusinessDelegate;

public class StockNewsBusinessDelegateImpl implements StockNewsBusinessDelegate {

	@Override
	public Result getStockNews(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		StockNewsBackendDelegate stockNewsBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(StockNewsBackendDelegate.class);
		result = stockNewsBackendDelegate.getStockNews(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
