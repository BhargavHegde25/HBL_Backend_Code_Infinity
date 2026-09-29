package com.temenos.dbx.product.transactionservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface RDCResource extends Resource{

	/**
	 * @author sharath.prabhala
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