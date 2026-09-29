package com.temenos.msArrangement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author smugesh
 * @version 1.0
 * Interface for AccountsResource extends {@link Resource}
 *
 */

public interface ArrangementsResource extends Resource {
    
 
    /**
     *  method to get the Arrangement Accounts
     *  @param methodID operationName
     *  @param inputArray input parameters 
     *  @param request DataControllerRequest
     *  @param response DataControllerResponse
     *  @return array of JSON containing all the approval rules
     *  
     */
    Result getArrangementAccounts(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    

}  
