/**
 * 
 */
package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.DailyMarketBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.DailyMarketResource;

/**
 * @author himaja.sridhar
 *
 */
public class DailyMarketResourceImpl implements DailyMarketResource {

	@Override
	public Result getDailyMarket(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
        if (!userPermissions.contains("WEALTH_MARKET_AND_NEWS_MARKET_VIEW")) {
//            ErrorCodeEnum.ERR_12001.setErrorCode(result);
            return result;
        }
		
		Map<String, Object> headersMap = request.getHeaderMap();
		DailyMarketBusinessDelegate dailyMarketBusinessDelegate = DBPAPIAbstractFactoryImpl.
				getBusinessDelegate(DailyMarketBusinessDelegate.class);
				
		result= dailyMarketBusinessDelegate.getDailyMarket(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
