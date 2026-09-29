/**
 * 
 */
package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.OrdersListBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.OrdersListBusinessDelegate;

/**
 * @author himaja.sridhar
 *
 */
public class OrdersListBusinessDelegateImpl implements OrdersListBusinessDelegate {

	@Override
	public Result getOrdersDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		OrdersListBackendDelegate transactionsListBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(OrdersListBackendDelegate.class);
		result = transactionsListBackendDelegate.getOrdersDetails(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
