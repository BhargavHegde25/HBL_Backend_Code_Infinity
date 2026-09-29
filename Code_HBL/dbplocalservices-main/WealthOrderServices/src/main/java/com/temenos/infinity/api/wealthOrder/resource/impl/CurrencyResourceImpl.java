package com.temenos.infinity.api.wealthOrder.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.CurrencyBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.resource.api.CurrencyResource;

public class CurrencyResourceImpl implements CurrencyResource {

	@Override
	public Result getMarketRates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE")) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}
		CurrencyBusinessDelegate currencyBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CurrencyBusinessDelegate.class);
		result = currencyBusinessDelegate.getMarketRates(methodID, inputArray, request, response, headers);
		return result;
	}

	@Override
	public Result createForexOrders(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE")) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}

		CurrencyBusinessDelegate currencyBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CurrencyBusinessDelegate.class);
		result = currencyBusinessDelegate.createForexOrders(methodID, inputArray, request, response, headers);
		return result;
	}
}
