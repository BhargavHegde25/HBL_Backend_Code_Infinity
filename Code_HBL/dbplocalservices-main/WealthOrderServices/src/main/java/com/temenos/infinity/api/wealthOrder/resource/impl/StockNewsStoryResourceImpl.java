package com.temenos.infinity.api.wealthOrder.resource.impl;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.StockNewsStoryBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.resource.api.StockNewsStoryResource;

public class StockNewsStoryResourceImpl implements StockNewsStoryResource {

	@Override
	public Result getStockNewsStory(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();
		if (headers == null) {
			headers = new HashMap<String, Object>();
		}

		StockNewsStoryBusinessDelegate stockNewsStoryBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(StockNewsStoryBusinessDelegate.class);
		result = stockNewsStoryBusinessDelegate.getStockNewsStory(methodID, inputArray, request, response, headers);
		return result;
	}

}
