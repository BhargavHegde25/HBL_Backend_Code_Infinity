package com.temenos.infinity.api.chequemanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;

public interface CreateChequeBookResource extends Resource {

    Result createChequeBook(ChequeBook chequeBook, DataControllerRequest request)  throws Exception;

    /**
     * method to execute chequebook request after approval.
     * @param inputArray 
     * @param methodId 
     * @param request
     * @param response 
     * @return
     */
	Result executeChequeBookRequestAfterApproval(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);

}
