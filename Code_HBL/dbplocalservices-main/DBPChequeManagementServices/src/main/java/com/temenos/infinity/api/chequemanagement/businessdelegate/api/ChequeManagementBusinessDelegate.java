package com.temenos.infinity.api.chequemanagement.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.ApprovalRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface ChequeManagementBusinessDelegate extends BusinessDelegate{

	/**
     * method to get the reject chequebook return result Object
     * 
     * @param request
     */
    ChequeBookAction rejectChequeBook(DataControllerRequest request)
            throws ApplicationException; 
    
    ChequeBookAction withdrawCheque( DataControllerRequest request) throws ApplicationException;
    
    List<ApprovalRequestDTO> fetchChequeDetails( DataControllerRequest request) throws ApplicationException;
	
}
