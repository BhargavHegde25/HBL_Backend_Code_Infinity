/**
 * 
 */
package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.PortfolioDetailsBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.PortfolioDetailsBusinessDelegate;


public class PortfolioDetailsBusinessDelegateImpl implements PortfolioDetailsBusinessDelegate {

	@Override
	public Result getInstrumentTotal(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		PortfolioDetailsBackendDelegate portfolioDetailsBackendDelegate = DBPAPIAbstractFactoryImpl.
				getBackendDelegate(PortfolioDetailsBackendDelegate.class);
		result= portfolioDetailsBackendDelegate.getInstrumentTotal(methodID, inputArray, request, response, headersMap);
		
		return result;
	}

	@Override
	public Result getAssetAllocation(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		PortfolioDetailsBackendDelegate portfolioDetailsBackendDelegate = DBPAPIAbstractFactoryImpl.
				getBackendDelegate(PortfolioDetailsBackendDelegate.class);
		result= portfolioDetailsBackendDelegate.getAssetAllocation(methodID, inputArray, request, response, headersMap);
		return result;
	}
	
	@Override
	public Result getCashBalance(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		PortfolioDetailsBackendDelegate portfolioDetailsBackendDelegate = DBPAPIAbstractFactoryImpl.
				getBackendDelegate(PortfolioDetailsBackendDelegate.class);
		result= portfolioDetailsBackendDelegate.getCashBalance(methodID, inputArray, request, response, headersMap);
		
		return result;
	}

	@Override
	public Result getPortfolioDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		PortfolioDetailsBackendDelegate portfolioDetailsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(PortfolioDetailsBackendDelegate.class);
		result= portfolioDetailsBackendDelegate.getPortfolioDetails(methodID, inputArray, request, response, headersMap);
		
		return result;
	}

	@Override
	public Result getPortfolioHealth(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		PortfolioDetailsBackendDelegate portfolioDetailsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(PortfolioDetailsBackendDelegate.class);
		result= portfolioDetailsBackendDelegate.getPortfolioHealth(methodID, inputArray, request, response, headersMap);
		
		return result;
	}

	@Override
	public Result getAllocation(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		PortfolioDetailsBackendDelegate portfolioDetailsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(PortfolioDetailsBackendDelegate.class);
		result= portfolioDetailsBackendDelegate.getAllocation(methodID, inputArray, request, response, headersMap);
		
		return result;
	}

}
