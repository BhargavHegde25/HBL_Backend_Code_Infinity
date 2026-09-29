package com.temenos.infinity.api.wealthOrder.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.PricingDataBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.resource.api.PricingDataResource;

public class PricingDataResourceImpl implements PricingDataResource {

	@Override
	public Result getPricingData(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();
		if (headers == null) {
			headers = new HashMap<String, Object>();
		}

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW")) {
			return result;
		}

		PricingDataBusinessDelegate pricingDataBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PricingDataBusinessDelegate.class);
		result = pricingDataBusinessDelegate.getPricingData(methodID, inputArray, request, response, headers);
		return result;
	}

}
