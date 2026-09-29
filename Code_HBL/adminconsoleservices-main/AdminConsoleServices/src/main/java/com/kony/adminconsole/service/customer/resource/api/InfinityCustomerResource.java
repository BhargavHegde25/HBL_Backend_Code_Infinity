package com.kony.adminconsole.service.customer.resource.api;

import com.dbp.core.api.Resource;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface InfinityCustomerResource extends Resource {

	public Result getInfinityAccounts(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;
	
}
