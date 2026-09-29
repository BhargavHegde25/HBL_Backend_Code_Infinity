package com.hbl.adminconsoleextn.api;

import com.kony.adminconsole.service.customerrole.resource.api.CustomerRoleResource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CustomerRoleResourceExtn extends CustomerRoleResource{
	public Result editGroup(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
