package com.temenos.dbx.product.payeeservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface InternationalExternalPayeeResource extends Resource {

    /**
     * This method creates an International payee
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to create a new International bank payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains newly created payeeId
     */
    public Result createPayee(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);


    /**
     * This method edits details of an existing International bank payee
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit a new International bank payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains edited payeeId
     */
    public Result editPayee(String methodId, Object[] inputArray, DataControllerRequest request,
                            DataControllerResponse response);


    /**
     *  This method deletes an International bank payee
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to delete an existing International bank payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains acknowledgement for deleted payee
     */

    public Result deletePayee(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);


    /**
     * This method fetches all International bank payees for a user or organization
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch all International bank payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains a list of all the payees
     */

    public Result fetchPayees(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);
}
