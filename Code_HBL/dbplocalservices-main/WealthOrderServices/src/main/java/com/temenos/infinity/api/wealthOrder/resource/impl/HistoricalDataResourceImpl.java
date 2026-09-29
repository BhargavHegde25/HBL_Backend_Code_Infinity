package com.temenos.infinity.api.wealthOrder.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.HistoricalDataBusinessDelegate;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthOrder.resource.api.HistoricalDataResource;

public class HistoricalDataResourceImpl implements HistoricalDataResource {

	@SuppressWarnings("unchecked")
	@Override
	public Result getHistoricalData(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		Map<String, Object> headers = request.getHeaderMap();
		if (headers == null) {
			headers = new HashMap<String, Object>();
		}
// we check for the permission only if currencyPairs is given as input parameter.		
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object currencyPairsObj = inputParams.get(TemenosConstants.CURRENCYPAIRS);
		if (currencyPairsObj != null && !currencyPairsObj.equals("")) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE")) {
				ErrorCodeEnum.ERR_12001.setErrorCode(result);
				return result;
			}
		}

		HistoricalDataBusinessDelegate historicalDataBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(HistoricalDataBusinessDelegate.class);
		result = historicalDataBusinessDelegate.getHistoricalData(methodID, inputArray, request, response, headers);
		return result;
	}

}
