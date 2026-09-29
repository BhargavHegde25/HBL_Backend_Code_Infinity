package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.AccountActivityBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.AccountActivityResource;

public class AccountActivityResourceImpl implements AccountActivityResource {

	@Override
	public Result getAccountActivity(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
				
			Result result = new Result();
			Map<String, Object> headersMap = request.getHeaderMap();
			AccountActivityBusinessDelegate accountActivityBusinessDelegate= DBPAPIAbstractFactoryImpl.
					getBusinessDelegate(AccountActivityBusinessDelegate.class);
			result= accountActivityBusinessDelegate.getAccountActivity(methodID, inputArray, request, response, headersMap);
			return result;
	}

	@Override
	public Result getAccountActivityOperations(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();
		
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW")) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}
		
		AccountActivityBusinessDelegate accountActivityBusinessDelegate= DBPAPIAbstractFactoryImpl.
				getBusinessDelegate(AccountActivityBusinessDelegate.class);
		result= accountActivityBusinessDelegate.getAccountActivityOperations(methodID, inputArray, request, response, headersMap);
		return result;
	}
}
