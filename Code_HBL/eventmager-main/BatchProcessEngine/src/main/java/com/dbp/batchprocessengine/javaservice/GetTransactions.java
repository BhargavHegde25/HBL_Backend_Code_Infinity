package com.dbp.batchprocessengine.javaservice;


import com.dbp.batchprocessengine.resource.api.TransactionResource;
import com.dbp.batchprocessengine.resource.impl.TransactionResourceImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class GetTransactions implements JavaService2 {


	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		TransactionResource transactionResource = new TransactionResourceImpl();

		return transactionResource.getTransactions(methodID, inputArray, request, response);
	}

}