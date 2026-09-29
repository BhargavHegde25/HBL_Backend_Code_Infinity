package com.temenos.dbx.product.transactionservices.backenddelegate.api;


import java.util.List;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;

/**
 * Handles all backend operations on Bill pay transaction
 * @author KH2624
 * extends {@link BackendDelegate}
 */

public interface BillPayTransactionBackendDelegate extends BackendDelegate {

	/**
	 * Invokes line of business service for Bill pay to create at the backend
	 * @param {@link BillPayTransactionBackendDTO}
	 * @return {@link BillPayTransactionDTO}
	 */	
	public BillPayTransactionDTO createTransactionWithoutApproval(BillPayTransactionBackendDTO billpayTransactionBackendDTO, DataControllerRequest request);
    
	/**
     * Invokes line of business service for Billpay to edit at the backend
     * @param {@link BillPayTransactionBackendDTO}
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO editTransactionWithoutApproval(BillPayTransactionBackendDTO billpayTransactionBackendDTO, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for Billpay to delete at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link BillPayTransactionDTO}
	 */
	public BillPayTransactionDTO deleteTransaction( String referenceId, String transactionType,DataControllerRequest dataControllerRequest);
	
	/**
	 * Invokes line of business service for Billpay to cancel occurance at the backend
	 * @param referenceId
	 * @param dataControllerRequest
	 * @return {@link BillPayTransactionDTO}
	 */
	public BillPayTransactionDTO cancelTransactionOccurrence( String referenceId, DataControllerRequest dataControllerRequest);
	
	 /**
     * Invokes line of business service for billpay transfer to create at the Core with pending(INAU) status
     * @param {@link BillPayTransactionDTO}
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO createPendingTransaction(BillPayTransactionBackendDTO billpaytransferdto, DataControllerRequest request);
	
	/**
     * Invokes line of business service for billpay transfer to approve at the Core
     * @param referenceId
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency);
	
	/**
     * Invokes line of business service for billpay transfer to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for billpay to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link BillPayTransactionDTO}
	 */
	public BillPayTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for billpay to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link BillPayTransactionDTO}
	 */
	public BillPayTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);
		
	/**
     * Invokes line of business service for billpay to edit at the backend
     * @param {@link BillPayTransactionDTO}
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO validateTransaction(BillPayTransactionBackendDTO input, DataControllerRequest request);

	/**
	 * fetches the transactions based on input set of ids
	 * @param billPayTransIds
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> billPayTransIds,
			DataControllerRequest dcr);
	
	/**
	 * updates status of transaction from LIVE to Pending
	 * @param BillPayTransactionBackendDTO
	 * @param dcr
	 * @return BillPayTransactionDTO
	 */
	public BillPayTransactionDTO editTransactionWithApproval(BillPayTransactionBackendDTO billpayTransactionBackendDTO, DataControllerRequest request);
	

}
