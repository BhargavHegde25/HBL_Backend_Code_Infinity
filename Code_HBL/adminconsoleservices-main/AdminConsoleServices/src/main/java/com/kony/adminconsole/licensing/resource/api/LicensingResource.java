package com.kony.adminconsole.licensing.resource.api;

import com.dbp.core.api.Resource;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface LicensingResource extends Resource {
	
	public Result getCountOfUsers(String method, Object[] inputArray, DataControllerRequest dataControllerRequest,
    		DataControllerResponse dataControllerResponse) throws DBPApplicationException;

}



