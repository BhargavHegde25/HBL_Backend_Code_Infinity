package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.UserPreferenceBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.UserPreferenceResource;

public class UserPreferenceResourceImpl implements UserPreferenceResource {

	@Override
	public Result getHoldingsOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {

		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();
		UserPreferenceBusinessDelegate userPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(UserPreferenceBusinessDelegate.class);
		result = userPreferenceBusinessDelegate.getHoldingsOrder(methodID, inputArray, request, response, headers);
		return result;
	}

	@Override
	public Result modifyHoldingsOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();
		UserPreferenceBusinessDelegate userPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(UserPreferenceBusinessDelegate.class);
		result = userPreferenceBusinessDelegate.modifyHoldingsOrder(methodID, inputArray, request, response, headers);
		return result;
	}
}
