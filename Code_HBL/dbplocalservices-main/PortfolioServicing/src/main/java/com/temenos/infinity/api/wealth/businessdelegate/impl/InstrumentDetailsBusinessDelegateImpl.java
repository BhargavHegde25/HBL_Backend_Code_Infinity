package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.InstrumentDetailsBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.InstrumentDetailsBusinessDelegate;

public class InstrumentDetailsBusinessDelegateImpl implements InstrumentDetailsBusinessDelegate{



	@Override
	public Result getInstrumentDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		InstrumentDetailsBackendDelegate instrumentDetailsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InstrumentDetailsBackendDelegate.class);
		result = instrumentDetailsBackendDelegate.getInstrumentDetails(methodID, inputArray, request, response,headersMap);
		return result;
	}

}
