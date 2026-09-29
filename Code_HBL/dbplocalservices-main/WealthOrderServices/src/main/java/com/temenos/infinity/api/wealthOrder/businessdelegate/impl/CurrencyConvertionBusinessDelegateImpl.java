/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.CurrencyConvertionBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.CurrencyConvertionBusinessDelegate;

/**
 * @author balaji.kk
 *
 */
public class CurrencyConvertionBusinessDelegateImpl implements CurrencyConvertionBusinessDelegate {

	@Override
	public Result createCurrencyConvertion(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		CurrencyConvertionBackendDelegate currencyConvertionBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CurrencyConvertionBackendDelegate.class);
		result = currencyConvertionBackendDelegate.createCurrencyConvertion(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
