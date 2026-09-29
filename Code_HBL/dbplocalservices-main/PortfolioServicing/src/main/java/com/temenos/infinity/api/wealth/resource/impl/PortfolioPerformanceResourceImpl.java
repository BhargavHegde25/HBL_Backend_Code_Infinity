package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.PortfolioPerformanceBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.PortfolioPerformanceResource;

public class PortfolioPerformanceResourceImpl implements PortfolioPerformanceResource {

	@Override
	public Result getPortfolioPerformance(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW")) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}
		PortfolioPerformanceBusinessDelegate portfolioPerformanceBusinessDelegate= DBPAPIAbstractFactoryImpl.
				getBusinessDelegate(PortfolioPerformanceBusinessDelegate.class);
		result= portfolioPerformanceBusinessDelegate.getPortfolioPerformance(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
