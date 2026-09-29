package com.kony.scaintegration.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface UpdateVerfifyFlagResource extends Resource {
	
	public Result updateFlag(DataControllerRequest request);

}
