package com.dbp.batchprocessengine.javaservice;

import com.dbp.batchprocessengine.resource.api.AccountsResource;
import com.dbp.batchprocessengine.resource.impl.AccountsResourceImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class GetAccounts implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		AccountsResource accountsResource = new AccountsResourceImpl();

		return accountsResource.getAccounts(methodID, inputArray, dcRequest, dcResponse);
	}

}
