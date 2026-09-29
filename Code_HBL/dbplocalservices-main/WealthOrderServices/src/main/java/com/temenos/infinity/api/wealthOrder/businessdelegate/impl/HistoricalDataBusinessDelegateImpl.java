package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.HistoricalDataBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.HistoricalDataBusinessDelegate;

public class HistoricalDataBusinessDelegateImpl implements HistoricalDataBusinessDelegate {

	@Override
	public Result getHistoricalData(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		HistoricalDataBackendDelegate historicalDataBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(HistoricalDataBackendDelegate.class);
		result = historicalDataBackendDelegate.getHistoricalData(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
