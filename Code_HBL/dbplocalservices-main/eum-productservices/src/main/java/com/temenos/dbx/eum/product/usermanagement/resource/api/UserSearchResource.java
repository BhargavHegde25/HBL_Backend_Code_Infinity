package com.temenos.dbx.eum.product.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface UserSearchResource extends Resource {

	public Result verifyDbxUserName(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
