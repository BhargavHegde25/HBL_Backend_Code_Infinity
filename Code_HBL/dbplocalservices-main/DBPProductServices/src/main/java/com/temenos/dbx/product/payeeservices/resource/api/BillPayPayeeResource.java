package com.temenos.dbx.product.payeeservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface BillPayPayeeResource extends Resource {

    /**
     * fetches bill pay payees 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    public Result fetchAllMyPayees(String methodID, Object[] inputArray, DataControllerRequest request,
                               DataControllerResponse response);

    /**
     * creates bill pay payees 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
	public Result createPayee(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
     * deletes bill pay payee 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
	public Result deletePayee(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
     * edits bill pay payee 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
	public Result editPayee(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
    
}
