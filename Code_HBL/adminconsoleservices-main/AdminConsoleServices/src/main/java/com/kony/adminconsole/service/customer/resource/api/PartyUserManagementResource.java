package com.kony.adminconsole.service.customer.resource.api;

import com.dbp.core.api.Resource;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface PartyUserManagementResource extends Resource {
	
	public Result createPartyUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

    public Result updatePartyUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result searchPartyUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

}
