package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.TopMarketNewsBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.TopMarketNewsResource;

public class TopMarketNewsResourceImpl implements TopMarketNewsResource {

	@Override
	public Result getTopMarketNews(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
        if (!userPermissions.contains("WEALTH_MARKET_AND_NEWS_TOP_NEWS_VIEW")) {
//            ErrorCodeEnum.ERR_12001.setErrorCode(result);
            return result;
        }
        
		Map<String, Object> headersMap = request.getHeaderMap();
		TopMarketNewsBusinessDelegate topMarketNewsBusinessDelegate = DBPAPIAbstractFactoryImpl.
				getBusinessDelegate(TopMarketNewsBusinessDelegate.class);
				
		result= topMarketNewsBusinessDelegate.getTopMarketNews(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
