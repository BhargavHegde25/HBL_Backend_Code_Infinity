/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.TransactionsListBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.TransactionsListBusinessDelegate;

/**
 * @author himaja.sridhar
 *
 */
public class TransactionsListBusinessDelegateImpl implements TransactionsListBusinessDelegate {

	@Override
	public Result getTransactionDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		TransactionsListBackendDelegate transactionsListBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(TransactionsListBackendDelegate.class);
		result = transactionsListBackendDelegate.getTransactionDetails(methodID, inputArray, request, response, headersMap);
		return result;
	}
	
	@Override
	public Result viewInstrumentTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Result result = new Result();
		TransactionsListBackendDelegate transactionsListBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(TransactionsListBackendDelegate.class);
		result = transactionsListBackendDelegate.viewInstrumentTransactions(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
