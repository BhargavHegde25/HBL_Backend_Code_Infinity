package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.StockNewsWebBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.StockNewsWebBusinessDelegate;

public class StockNewsWebBusinessDelegateImpl implements StockNewsWebBusinessDelegate {

	@Override
	public Result getStockNewsWeb(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		StockNewsWebBackendDelegate stockNewsWebBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(StockNewsWebBackendDelegate.class);
		result = stockNewsWebBackendDelegate.getStockNewsWeb(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
