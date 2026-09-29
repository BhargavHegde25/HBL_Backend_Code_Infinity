package com.temenos.infinity.api.arrangements.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public interface InfinityAccountsResource extends Resource {

    /**
     * 
     * @param methodId
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    Object getInfinityAccounts(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    
}
