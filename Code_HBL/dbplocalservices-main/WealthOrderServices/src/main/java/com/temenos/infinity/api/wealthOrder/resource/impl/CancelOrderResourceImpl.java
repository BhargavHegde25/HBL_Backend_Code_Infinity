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
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.CancelOrderBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.resource.api.CancelOrderResource;

/**
 * @author himaja.sridhar
 *
 */
public class CancelOrderResourceImpl implements CancelOrderResource {

	@Override
	public Result cancelOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();
		
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_ORDER_MGMT_ORDER_CANCEL")) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}
		
		CancelOrderBusinessDelegate cancelOrderBusinessDelegate = DBPAPIAbstractFactoryImpl.
				getBusinessDelegate(CancelOrderBusinessDelegate.class);
		result= cancelOrderBusinessDelegate.cancelOrder(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
