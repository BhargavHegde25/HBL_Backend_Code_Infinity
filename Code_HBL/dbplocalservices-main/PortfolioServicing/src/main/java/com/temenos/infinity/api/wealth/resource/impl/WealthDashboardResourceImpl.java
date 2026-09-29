package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.WealthDashboardBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.WealthDashboardResource;

public class WealthDashboardResourceImpl implements WealthDashboardResource {

	@Override
	public Result getPortfolioList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
				
			Result result = new Result();
			
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
	        if (!userPermissions.contains("WEALTH_INVESTMENT_DETAILS_INVESTMENT_SUMMARY_VIEW")) {
//	            ErrorCodeEnum.ERR_12001.setErrorCode(result);
	            return result;
	        }
	        
			Map<String, Object> headersMap = request.getHeaderMap();
			WealthDashboardBusinessDelegate wealthDashboardBusinessDelegate = DBPAPIAbstractFactoryImpl.
					getBusinessDelegate(WealthDashboardBusinessDelegate.class);
					
			result= wealthDashboardBusinessDelegate.getPortfolioList(methodID, inputArray, request, response, headersMap);
			return result;
	}
	
	@Override
	public Result getAssetList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
				
			Result result = new Result();
			
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
	        if (!userPermissions.contains("WEALTH_INVESTMENT_DETAILS_TOTAL_ASSETS_VIEW")) {
//	            ErrorCodeEnum.ERR_12001.setErrorCode(result);
	            return result;
	        }
	        
			Map<String, Object> headersMap = request.getHeaderMap();
			WealthDashboardBusinessDelegate wealthDashboardBusinessDelegate = DBPAPIAbstractFactoryImpl.
					getBusinessDelegate(WealthDashboardBusinessDelegate.class);
					
			result= wealthDashboardBusinessDelegate.getAssetList(methodID, inputArray, request, response, headersMap);
			return result;
	}
		@Override
	public Result getDashboardRecentActivity(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
        if (!userPermissions.contains("WEALTH_INVESTMENT_DETAILS_RECENT_ACTIVITY_VIEW")) {
//            ErrorCodeEnum.ERR_12001.setErrorCode(result);
            return result;
        }
        
		Map<String, Object> headersMap = request.getHeaderMap();
		WealthDashboardBusinessDelegate wealthDashboardBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(WealthDashboardBusinessDelegate.class);
		result = wealthDashboardBusinessDelegate.getDashboardRecentActivity(methodID, inputArray, request, response,
				headersMap);
		return result;
	}
		@Override
		public Result getDashboardGraphData(String methodID, Object[] inputArray, DataControllerRequest request,
				DataControllerResponse response) {
			Result result = new Result();
			
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
	        if (!userPermissions.contains("WEALTH_INVESTMENT_DETAILS_INVESTMENT_SUMMARY_VIEW")) {
//	            ErrorCodeEnum.ERR_12001.setErrorCode(result);
	            return result;
	        }
	        
			Map<String, Object> headersMap = request.getHeaderMap();
			WealthDashboardBusinessDelegate wealthDashboardBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(WealthDashboardBusinessDelegate.class);
			result = wealthDashboardBusinessDelegate.getDashboardGraphData(methodID, inputArray, request, response,
					headersMap);
			return result;
		}

}
