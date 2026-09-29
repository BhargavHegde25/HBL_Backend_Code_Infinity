package com.temenos.dbx.product.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CoreCustomerResource extends Resource {

	public Result saveFromParty(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result updateFromParty(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result get(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result saveFromDBX(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result updateFromDBX(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

    public Result activateCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);
	
}
