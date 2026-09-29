package com.temenos.dbx.product.payeeservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface P2PPayeeResource extends Resource {



    /**
     * This method edits details of an existing P2p payee
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit a new P2p payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains edited payeeId
     */
    public Result editPayee(String methodId, Object[] inputArray, DataControllerRequest request,
                            DataControllerResponse response);


    /**
     *  This method deletes an P2p payee
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to delete an existing p2p payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains acknowledgement for deleted payee
     */

    public Result deletePayee(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);


  

    /**
     * This method creates an p2p payee
     *  @author KH2659
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to create a new p2p payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains newly created payeeId
     */
    public Result createPayee(String methodId, Object[] inputArray, DataControllerRequest request,
                                    DataControllerResponse response);


    /**
     * This method fetches all p2p payees for a user or organization
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch all p2p payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains a list of all the payees
     */

    public Result fetchAllMyPayees(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);


}
