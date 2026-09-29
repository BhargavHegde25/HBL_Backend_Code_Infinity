package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.AddCurrencyBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.AddCurrencyBusinessDelegate;

public class AddCurrencyBusinessDelegateImpl implements AddCurrencyBusinessDelegate {

	@Override
	public Result getAddCurrency(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		AddCurrencyBackendDelegate addCurrencyBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(AddCurrencyBackendDelegate.class);
		result = addCurrencyBackendDelegate.getAddCurrency(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
