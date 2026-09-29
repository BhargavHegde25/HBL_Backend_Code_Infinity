/**
 * 
 */
package com.temenos.infinity.api.wealth.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.OrdersListBusinessDelegate;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.resource.api.OrdersListResource;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * @author himaja.sridhar
 *
 */
public class OrdersListResourceImpl implements OrdersListResource {

	@Override
	public Result getOrdersDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String ordersViewType = inputParams.get(TemenosConstants.ORDERSVIEW_TYPE).toString();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if ((!userPermissions.contains("WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW"))
				&& (!userPermissions.contains("WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW"))) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}

		if ((userPermissions.contains("WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW")
				&& ordersViewType.equalsIgnoreCase(TemenosConstants.ORDERS_TYPE_OPEN))
				|| (userPermissions.contains("WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW")
						&& ordersViewType.equalsIgnoreCase(TemenosConstants.ORDERS_TYPE_HISTORY))) {
			OrdersListBusinessDelegate ordersListBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(OrdersListBusinessDelegate.class);
			result = ordersListBusinessDelegate.getOrdersDetails(methodID, inputArray, request, response, headersMap);
			return result;
		}
		else {
			ErrorCodeEnum.ERR_29023.setErrorCode(result, ordersViewType);
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDERSVIEW_TYPE);
		}
	}
}
