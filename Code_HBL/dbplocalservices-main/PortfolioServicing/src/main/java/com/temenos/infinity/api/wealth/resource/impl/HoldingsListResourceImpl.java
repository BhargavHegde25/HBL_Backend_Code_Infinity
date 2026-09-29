package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.HoldingsListBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.HoldingsListResource;

public class HoldingsListResourceImpl implements HoldingsListResource {

	@Override
	public Result getHoldingsList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();
		
		

		 Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
         if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW")) {
             ErrorCodeEnum.ERR_12001.setErrorCode(result);
             return result;
         }
		HoldingsListBusinessDelegate holdingsListBusinessDelegate = DBPAPIAbstractFactoryImpl.
				getBusinessDelegate(HoldingsListBusinessDelegate.class);
				
		result= holdingsListBusinessDelegate.getHoldingsList(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
