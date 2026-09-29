package com.temenos.dbx.product.forexservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ForexResource extends Resource{

	Result fetchAllCurrencies(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	Result fetchBaseCurrency(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	Result fetchCurrencyRates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	Result fetchPopularCurrencies(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	Result fetchRecentCurrencies(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	Result fetchDashboardCurrencyList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result fetchDashboardCurrencyRates(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	Result updateRecentCurrencies(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
