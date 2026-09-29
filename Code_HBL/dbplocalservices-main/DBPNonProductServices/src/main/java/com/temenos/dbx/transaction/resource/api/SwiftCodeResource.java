package com.temenos.dbx.transaction.resource.api;
import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


public interface SwiftCodeResource extends Resource{

	public Result getSwiftBICcode(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
