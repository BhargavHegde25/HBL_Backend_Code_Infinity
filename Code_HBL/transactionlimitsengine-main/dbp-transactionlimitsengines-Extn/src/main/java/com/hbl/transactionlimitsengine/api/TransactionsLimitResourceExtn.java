package com.hbl.transactionlimitsengine.api;

import com.dbp.transactionslimitengine.resource.api.TransactionsLimitResource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface TransactionsLimitResourceExtn extends TransactionsLimitResource{
	Result getTransactionLimits(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
}
