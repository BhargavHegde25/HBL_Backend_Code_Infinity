package com.temenos.infinity.api.checkdep.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface RDCResource extends Resource{

	/**
	 * @author eivanov
	 * @version 1.0
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return
	 */
	public Result createRDC(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	
}