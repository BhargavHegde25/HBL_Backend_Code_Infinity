/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.PortfolioDetailsBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.resource.api.PortfolioDetailsResource;

public class PortfolioDetailsResourceImpl implements PortfolioDetailsResource {

	@Override
	public Result getInstrumentTotal(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW")) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}

		PortfolioDetailsBusinessDelegate portfolioDetailsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PortfolioDetailsBusinessDelegate.class);

		result = portfolioDetailsBusinessDelegate.getInstrumentTotal(methodID, inputArray, request, response,
				headersMap);
		return result;
	}

	@Override
	public Result getAssetAllocation(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW")) {
			// ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}

		PortfolioDetailsBusinessDelegate portfolioDetailsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PortfolioDetailsBusinessDelegate.class);

		result = portfolioDetailsBusinessDelegate.getAssetAllocation(methodID, inputArray, request, response,
				headersMap);
		return result;
	}

	public Result getCashBalance(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW")) {
			// ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}

		PortfolioDetailsBusinessDelegate portfolioDetailsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PortfolioDetailsBusinessDelegate.class);
		result = portfolioDetailsBusinessDelegate.getCashBalance(methodID, inputArray, request, response, headersMap);
		return result;
	}

	public Result getPortfolioDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW")
				&& !userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW")
				&& !userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW")) {
			// ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}

		PortfolioDetailsBusinessDelegate portfolioDetailsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PortfolioDetailsBusinessDelegate.class);
		result = portfolioDetailsBusinessDelegate.getPortfolioDetails(methodID, inputArray, request, response,
				headersMap);
		return result;
	}

}
