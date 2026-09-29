package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.StockNewsStoryBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.StockNewsStoryBusinessDelegate;

public class StockNewsStoryBusinessDelegateImpl implements StockNewsStoryBusinessDelegate {

	@Override
	public Result getStockNewsStory(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		StockNewsStoryBackendDelegate stockNewsStoryBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(StockNewsStoryBackendDelegate.class);
		result = stockNewsStoryBackendDelegate.getStockNewsStory(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
