package com.hbl.resource.api;


import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;

public interface InfinityUserManagementResourceExtn extends InfinityUserManagementResource {

	public Result enrollRetailUserOperation(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;
	
}
