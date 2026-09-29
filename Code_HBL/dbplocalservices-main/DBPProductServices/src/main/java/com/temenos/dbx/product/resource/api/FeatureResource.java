package com.temenos.dbx.product.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 * Interface for fetching the feature actions
 */
public interface FeatureResource extends Resource {
	
	/**
     * method to fetch list of features at FI Level
     * 
     * @param methodID
     *            operationName
     * @param inputArray
     *            input parameters
     * @param request
     *            DataControllerRequest
     * @param response
     *            DataControllerResponse
     * @return Result containing the list of features at FI Level
     * 
     */
	
	public Result getFeatures(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

}
