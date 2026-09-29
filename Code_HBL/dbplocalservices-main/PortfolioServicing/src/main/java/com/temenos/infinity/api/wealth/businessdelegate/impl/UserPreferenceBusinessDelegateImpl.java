package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.UserPreferenceBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.UserPreferenceBusinessDelegate;

public class UserPreferenceBusinessDelegateImpl implements UserPreferenceBusinessDelegate {

	@Override
	public Result getHoldingsOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		Result result = new Result();
		UserPreferenceBackendDelegate userPreferenceBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(UserPreferenceBackendDelegate.class);
				result = userPreferenceBackendDelegate.getHoldingsOrder(methodID, inputArray, request, response, headersMap);
		return result;
	}

	@Override
	public Result modifyHoldingsOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		Result result = new Result();
		UserPreferenceBackendDelegate userPreferenceBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(UserPreferenceBackendDelegate.class);
				result = userPreferenceBackendDelegate.modifyHoldingsOrder(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
