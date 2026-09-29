package com.temenos.msArrangement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2281
 * @version 1.0
 * Interface for AccountTransactionsResource extends {@link Resource}
 *
 */

public interface AccountTransactionsResource extends Resource {
    

	/**
	 *  method to get the Account transactions
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return array of JSON containing all the approval rules
	 *  
	 */
    Result getAccountTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    

}  
