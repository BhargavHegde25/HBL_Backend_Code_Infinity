package com.temenos.dbx.product.achservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * This Class takes in the request payload from JavaService(Integration Layer)
 * and massages the data as per required by Business Delegate contracts to fetch
 * ACH Transaction Records
 * @author KH2624
 * @version 1.0
 * **/

public interface ACHTransactionRecordResource extends Resource {

    /**
     * This method takes user input and delegates fetching ACHTransactionRecords
     * @param methodName has service name
     * @param inputArray contains the input parameter to fetch ach transaction record
     * @param dataControllerRequest contains request handler
     * @param dataControllerResponse contains response handler
     * @author KH2624
     * @version 1.0
     * @return
     */

    public Result fetchACHTransactionRecords(String methodName, Object[] inputArray, DataControllerRequest dataControllerRequest,
                                             DataControllerResponse dataControllerResponse);
}
