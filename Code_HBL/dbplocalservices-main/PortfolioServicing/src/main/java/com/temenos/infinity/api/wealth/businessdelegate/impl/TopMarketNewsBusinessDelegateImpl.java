package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.TopMarketNewsBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.TopMarketNewsBusinessDelegate;

public class TopMarketNewsBusinessDelegateImpl implements TopMarketNewsBusinessDelegate {

	@Override
	public Result getTopMarketNews(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		TopMarketNewsBackendDelegate topMarketNewsBackendDelegate =  DBPAPIAbstractFactoryImpl.
				getBackendDelegate(TopMarketNewsBackendDelegate.class);
		result = topMarketNewsBackendDelegate.getTopMarketNews(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
