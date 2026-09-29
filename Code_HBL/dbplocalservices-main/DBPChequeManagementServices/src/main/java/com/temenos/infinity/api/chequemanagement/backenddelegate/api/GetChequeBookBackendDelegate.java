package com.temenos.infinity.api.chequemanagement.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface GetChequeBookBackendDelegate  extends BackendDelegate {

    /**
     * method to get the Blocked Funds return result Object
     * 
     * @param request
     */
    List<ChequeBook> getChequeBookOrdersFromOMS(ChequeBook chequeBook, DataControllerRequest request)
            throws ApplicationException;
}
