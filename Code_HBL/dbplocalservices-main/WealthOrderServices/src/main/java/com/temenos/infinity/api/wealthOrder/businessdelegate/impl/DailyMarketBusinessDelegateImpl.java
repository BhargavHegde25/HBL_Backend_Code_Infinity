/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.DailyMarketBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.DailyMarketBusinessDelegate;

/**
 * @author himaja.sridhar
 *
 */
public class DailyMarketBusinessDelegateImpl implements DailyMarketBusinessDelegate {

	@Override
	public Result getDailyMarket(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		DailyMarketBackendDelegate dailyMarketBackendDelegate =  DBPAPIAbstractFactoryImpl.
				getBackendDelegate(DailyMarketBackendDelegate.class);
		result = dailyMarketBackendDelegate.getDailyMarket(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
