package com.temenos.infinity.api.chequemanagement.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.BBRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookStatusDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface CreateChequeBookBusinessDelegate extends BusinessDelegate {

    /**
     * method to Create the cheque Book Request
     * 
     * @param request
     */
    ChequeBook createChequeBook(ChequeBook chequeBook, DataControllerRequest request)
            throws Exception; 
    
    /**
     * method to Validate the cheque Book Request
     * 
     * @param request
     */
    ChequeBook validateChequeBook(ChequeBook chequeBook, DataControllerRequest request)
            throws ApplicationException; 
    
    /**
     * method to approve the cheque Book Request
     * 
     * @param request
     */
    ChequeBookAction executeChequeBookRequestAfterApproval(ChequeBookAction chequeBook, DataControllerRequest request)
            throws ApplicationException;
			
	/**
     * Method to validate the user id has approvals
     * @param chequeBook
     * @param request
     * @return
     */
    public ChequeBookStatusDTO validateForApprovals(ChequeBookStatusDTO chequeBook, DataControllerRequest request);
    
    /**
     * Updates the approval queue with the backend Id
     * @param requestId
     * @param transactionId
     * @param isSelfApproved
     * @param featureActionId
     * @param request
     * @return
     */
    public ChequeBookStatusDTO updateBackendIdInApprovalQueue(String requestId, String transactionId,  boolean isSelfApproved,  String featureActionId, DataControllerRequest request);
    
    /**
     * Deletes the cheque book request order from the approval queue
     * @param requestId
     * @return
     */
    public boolean deleteChequeFromApprovalQueue(String requestId);
    
    /**
     * Fetches the request details
     * @param requestId
     * @return
     */
    public BBRequestDTO getAccountId(String requestId);
}
