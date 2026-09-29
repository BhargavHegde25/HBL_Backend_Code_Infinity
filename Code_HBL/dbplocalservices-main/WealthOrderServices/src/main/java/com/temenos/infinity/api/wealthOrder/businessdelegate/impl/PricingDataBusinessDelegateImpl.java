package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.PricingDataBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.PricingDataBusinessDelegate;

public class PricingDataBusinessDelegateImpl implements PricingDataBusinessDelegate {

	@Override
	public Result getPricingData(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		PricingDataBackendDelegate pricingDataBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(PricingDataBackendDelegate.class);
		result = pricingDataBackendDelegate.getPricingData(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
