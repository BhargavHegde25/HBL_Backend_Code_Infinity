package com.temenos.infinity.api.chequemanagement.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.dto.ApprovalRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface ChequeManagementBackendDelegate extends BackendDelegate{
	/**
     * method to reject cheque book
     * 
     * @param request
     */
	ChequeBookAction rejectChequeBook(DataControllerRequest request)
            throws ApplicationException;
	
	ChequeBookAction withdrawCheque( DataControllerRequest request) throws ApplicationException;
	
	List<ApprovalRequestDTO> fetchChequeDetails( DataControllerRequest request) throws ApplicationException;
}
