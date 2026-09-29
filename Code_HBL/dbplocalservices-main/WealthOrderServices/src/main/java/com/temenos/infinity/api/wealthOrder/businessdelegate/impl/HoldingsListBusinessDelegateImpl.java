package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.HoldingsListBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.HoldingsListBusinessDelegate;

public class HoldingsListBusinessDelegateImpl implements HoldingsListBusinessDelegate {

	@Override
	public Result getHoldingsList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		HoldingsListBackendDelegate holdingsListBackendDelagate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(HoldingsListBackendDelegate.class);
		result = holdingsListBackendDelagate.getHoldingsList(methodID, inputArray, request, response, headersMap);

		return result;
	}

	@Override
	public Result getSearchInstrumentList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		HoldingsListBackendDelegate holdingsListBackendDelagate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(HoldingsListBackendDelegate.class);
		result = holdingsListBackendDelagate.getSearchInstrumentList(methodID, inputArray, request, response,
				headersMap);
		return result;
	}

}
