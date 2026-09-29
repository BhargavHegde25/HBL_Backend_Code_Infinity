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
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.TransactionsListBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.resource.api.TransactionsListResource;

/**
 * @author himaja.sridhar
 *
 */
public class TransactionsListResourceImpl implements TransactionsListResource {

	@Override
	public Result getTransactionDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW")) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}

		TransactionsListBusinessDelegate transactionsListBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(TransactionsListBusinessDelegate.class);
		result = transactionsListBusinessDelegate.getTransactionDetails(methodID, inputArray, request, response,
				headersMap);
		return result;
	}
	
	@Override
	public Result viewInstrumentTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> headersMap = request.getHeaderMap();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PRODUCT_DETAILS_INSTRUMENT_VIEW")) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}

		TransactionsListBusinessDelegate transactionsListBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(TransactionsListBusinessDelegate.class);
		result = transactionsListBusinessDelegate.viewInstrumentTransactions(methodID, inputArray, request, response,
				headersMap);
		return result;
	}

}
