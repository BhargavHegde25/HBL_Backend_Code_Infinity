package com.dbp.batchprocessengine.resource.api;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface TransactionResource {

    Result getTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

   

}