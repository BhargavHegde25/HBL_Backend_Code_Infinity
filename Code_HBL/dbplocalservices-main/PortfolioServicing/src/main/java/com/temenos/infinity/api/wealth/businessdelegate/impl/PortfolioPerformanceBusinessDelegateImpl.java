package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.PortfolioPerformanceBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.PortfolioPerformanceBusinessDelegate;

public class PortfolioPerformanceBusinessDelegateImpl implements PortfolioPerformanceBusinessDelegate {

	@Override
	public Result getPortfolioPerformance(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		PortfolioPerformanceBackendDelegate portfolioPerformanceBackendDelegate=DBPAPIAbstractFactoryImpl.
				getBackendDelegate(PortfolioPerformanceBackendDelegate.class);
		
				result = portfolioPerformanceBackendDelegate.getPortfolioPerformance(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
