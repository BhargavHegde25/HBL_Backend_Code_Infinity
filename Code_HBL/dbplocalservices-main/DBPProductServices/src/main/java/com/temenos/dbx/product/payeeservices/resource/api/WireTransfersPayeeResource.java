package com.temenos.dbx.product.payeeservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface WireTransfersPayeeResource extends Resource{
	

    /**
     * fetches wire transfers payees 
     *  @author KH2660
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch all wire transfer payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains a list of all the payees
     */
    public Result fetchAllMyPayees(String methodID, Object[] inputArray, DataControllerRequest request,
                               DataControllerResponse response);

    /**
     * This method creates an wire transfer payee
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to create a new intra bank payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains newly created payeeId
     */
    public Result createPayee(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);


    /**
     * This method edits details of an existing wire transfer payee
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit a new wire transfer payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains edited payeeId
     */
    public Result editPayee(String methodId, Object[] inputArray, DataControllerRequest request,
                            DataControllerResponse response);


    /**
     *  This method deletes an wire transfer payee
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to delete an existing wire transfer payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains acknowledgement for deleted payee
     */

    public Result deletePayee(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);


    
}
