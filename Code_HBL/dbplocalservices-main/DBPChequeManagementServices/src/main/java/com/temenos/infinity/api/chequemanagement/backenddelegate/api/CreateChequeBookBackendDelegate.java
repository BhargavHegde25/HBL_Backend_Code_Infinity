package com.temenos.infinity.api.chequemanagement.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface CreateChequeBookBackendDelegate extends BackendDelegate {

    /**
     * method to create cheque book request
     * 
     * @param request
     */
    ChequeBook createChequeBookOrder(ChequeBook chequeBook, DataControllerRequest request)
            throws ApplicationException;
    
    /**
     * method to validate cheque book request
     * 
     * @param request 
     */
    ChequeBook validateChequeBookOrder(ChequeBook chequeBook, DataControllerRequest request)
            throws ApplicationException;
}
