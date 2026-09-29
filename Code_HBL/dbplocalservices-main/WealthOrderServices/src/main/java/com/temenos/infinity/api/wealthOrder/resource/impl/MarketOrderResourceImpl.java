package com.temenos.infinity.api.wealthOrder.resource.impl;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.MarketOrderBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.resource.api.MarketOrderResource;

public class MarketOrderResourceImpl implements MarketOrderResource {

	@Override
	public Result createMarketOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		/*
		 * String refenitivTokenref =
		 * EnvironmentConfigurationsHandler.getValue(TemenosConstants.
		 * WEALTH_REFINITV_TOKEN, request);
		 */
		Map<String, Object> headers = request.getHeaderMap();
		if (headers == null) {
			headers = new HashMap<String, Object>();
		}

		MarketOrderBusinessDelegate marketOrderBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(MarketOrderBusinessDelegate.class);
		result = marketOrderBusinessDelegate.createMarketOrder(methodID, inputArray, request, response, headers);
		return result;
	}

	@Override
	public Result modifyOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		// String transactToken =
		// EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_T24_TOKEN,
		// request);
		Map<String, Object> headers = request.getHeaderMap();
		if (headers == null) {
			headers = new HashMap<String, Object>();
		}
		// headers.put("Authorization", transactToken);
		Map<String, Object> headersMap = request.getHeaderMap();
		MarketOrderBusinessDelegate marketOrderBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(MarketOrderBusinessDelegate.class);
		result = marketOrderBusinessDelegate.modifyOrder(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
