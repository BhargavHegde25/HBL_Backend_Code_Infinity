package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.InstrumentMinimalBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.InstrumentMinimalBusinessDelegate;

public class InstrumentMinimalBusinessDelegateImpl implements InstrumentMinimalBusinessDelegate {

	@Override
	public Result getInstrumentMinimal(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		InstrumentMinimalBackendDelegate instrumentMinimalBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InstrumentMinimalBackendDelegate.class);
		result = instrumentMinimalBackendDelegate.getInstrumentMinimal(methodID, inputArray, request, response,
				headersMap);
		return result;
	}

}
