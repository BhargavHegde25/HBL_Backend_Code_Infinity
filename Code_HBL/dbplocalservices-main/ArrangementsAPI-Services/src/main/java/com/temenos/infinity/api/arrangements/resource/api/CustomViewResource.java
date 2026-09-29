package com.temenos.infinity.api.arrangements.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CustomViewResource extends Resource{

	 /**
     * fetches custom views 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    public Result getCustomView(String methodID, Object[] inputArray, DataControllerRequest request,
                               DataControllerResponse response);

    /**
     * creates custom view 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
	public Result createCustomView(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
     * deletes custom view 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
	public Result deleteCustomView(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
     * edits custom view 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
	public Result editCustomView(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
