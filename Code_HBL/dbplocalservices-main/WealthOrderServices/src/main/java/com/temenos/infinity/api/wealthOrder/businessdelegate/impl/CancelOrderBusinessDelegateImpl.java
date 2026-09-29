/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.CancelOrderBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.CancelOrderBusinessDelegate;

/**
 * @author balaji.kk
 *
 */
public class CancelOrderBusinessDelegateImpl implements CancelOrderBusinessDelegate {

	@Override
	public Result cancelOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		CancelOrderBackendDelegate cancelOrderBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(CancelOrderBackendDelegate.class);
		result = cancelOrderBackendDelegate.cancelOrder(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
