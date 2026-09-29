package com.temenos.dbx.product.achservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
/**
 * This Class takes in the request payload from JavaService(Integration Layer)
 * and massages the data as per required by Business Delegate contracts to fetch
 * ACH Transaction Sub Records
 * @author KH2624
 * @version 1.0
 * **/

public interface ACHTransactionSubRecordResource extends Resource {

    /**
     * This method takes user input and delegates fetching ACH Transaction Sub Record
     * @param methodName has service name
     * @param inputArray contains the input parameter to fetch ach transaction record
     * @param dataControllerRequest contains request handler
     * @param dataControllerResponse contains response handler
     * @author KH2624
     * @version 1.0
     * @return
     */

    public Result fetchACHTransactionSubRecords(String methodName, Object[] inputArray, DataControllerRequest dataControllerRequest,
                                                DataControllerResponse dataControllerResponse);
}