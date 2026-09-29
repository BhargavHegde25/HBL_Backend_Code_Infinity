package com.temenos.infinity.api.wealthOrder.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.StockNewsBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.resource.api.StockNewsResource;

public class StockNewsResourceImpl implements StockNewsResource {

	@Override
	public Result getStockNews(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();
		if (headers == null) {
			headers = new HashMap<String, Object>();
		}

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_NEWS_AND_DOCUMENTS_STOCK_NEWS_VIEW")) {
			return result;
		}

		StockNewsBusinessDelegate stockNewsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(StockNewsBusinessDelegate.class);
		result = stockNewsBusinessDelegate.getStockNews(methodID, inputArray, request, response, headers);
		return result;
	}

}
