package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.WealthDashboardBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.WealthDashboardBusinessDelegate;

public class WealthDashboardBusinessDelegateImpl implements WealthDashboardBusinessDelegate {
	@Override
	public Result getPortfolioList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		WealthDashboardBackendDelegate wealthDashboardBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(WealthDashboardBackendDelegate.class);
		result = wealthDashboardBackendDelegate.getPortfolioList(methodID, inputArray, request, response, headersMap);
		return result;
	}
	
	@Override
	public Result getAssetList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		WealthDashboardBackendDelegate wealthDashboardBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(WealthDashboardBackendDelegate.class);
		result = wealthDashboardBackendDelegate.getAssetList(methodID, inputArray, request, response, headersMap);
		return result;
	}
	
	@Override
	public Result getDashboardRecentActivity(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		WealthDashboardBackendDelegate wealthDashboardBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(WealthDashboardBackendDelegate.class);
		result = wealthDashboardBackendDelegate.getDashboardRecentActivity(methodID, inputArray, request, response, headersMap);
		return result;
	}
	@Override
	public Result getDashboardGraphData(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		WealthDashboardBackendDelegate wealthDashboardBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(WealthDashboardBackendDelegate.class);
		result = wealthDashboardBackendDelegate.getDashboardGraphData(methodID, inputArray, request, response, headersMap);
		return result;
	}
}
