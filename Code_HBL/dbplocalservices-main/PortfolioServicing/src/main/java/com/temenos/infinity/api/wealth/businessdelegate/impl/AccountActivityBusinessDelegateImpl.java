package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.AccountActivityBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.AccountActivityBusinessDelegate;

public class AccountActivityBusinessDelegateImpl implements AccountActivityBusinessDelegate {

	@Override
	public Result getAccountActivity(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		AccountActivityBackendDelegate accountActivityBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(AccountActivityBackendDelegate.class);
				result = accountActivityBackendDelegate.getAccountActivity(methodID, inputArray, request, response, headersMap);
		return result;
	}

	@Override
	public Result getAccountActivityOperations(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		AccountActivityBackendDelegate accountActivityBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(AccountActivityBackendDelegate.class);
				result = accountActivityBackendDelegate.getAccountActivityOperations(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
