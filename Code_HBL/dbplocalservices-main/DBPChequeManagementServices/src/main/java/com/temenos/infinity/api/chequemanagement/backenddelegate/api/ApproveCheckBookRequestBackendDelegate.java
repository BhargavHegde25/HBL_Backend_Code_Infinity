
package com.temenos.infinity.api.chequemanagement.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface ApproveCheckBookRequestBackendDelegate extends BackendDelegate {

    /**
     * method to approve cheque book request
     * 
     * @param request
     */
	ChequeBookAction approveChequeBookRequest(ChequeBookAction chequeBook, DataControllerRequest request)
            throws ApplicationException;
}
