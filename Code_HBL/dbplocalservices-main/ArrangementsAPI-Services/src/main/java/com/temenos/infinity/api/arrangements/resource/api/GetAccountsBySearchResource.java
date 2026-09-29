package com.temenos.infinity.api.arrangements.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface GetAccountsBySearchResource extends Resource {

    Result getAccounts(String accountId, String membershipId, String taxId, String authToken,
            DataControllerRequest request) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return arrangements array
     * @throws ApplicationException
     */
    Result getAccountsByCoreCustomerIdSearch(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;
}
