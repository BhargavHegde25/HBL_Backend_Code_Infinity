package com.bct.custom.resource.api;

import org.json.JSONArray;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface BranchDetailsResource extends Resource {
	public Result branchDetailsCRUDOperations(String methodId, Object[] inputArray, DataControllerRequest dcRequest) throws ApplicationException;

}
