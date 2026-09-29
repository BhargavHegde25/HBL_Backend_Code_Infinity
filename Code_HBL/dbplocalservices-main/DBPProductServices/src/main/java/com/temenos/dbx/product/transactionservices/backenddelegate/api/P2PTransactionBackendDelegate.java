package com.temenos.dbx.product.transactionservices.backenddelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionDTO;

/**
 * Handles all backend operations on P2P Fund transaction
 * @author KH2175
 * extends {@link BackendDelegate}
 */

public interface P2PTransactionBackendDelegate extends BackendDelegate {

	/**
     * Invokes line of business service for p2p transfer to create transaction at the Core
     * @param {@link P2PTransactionBackendDTO}
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO createTransactionWithoutApproval(P2PTransactionBackendDTO p2pTransactionBackendDTO, DataControllerRequest request);

	/**
     * Invokes line of business service for p2p to edit at the backend
     * @param {@link P2PTransactionBackendDTO}
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO editTransactionWithoutApproval(P2PTransactionBackendDTO transferBackendDTO, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for p2p to delete at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param dataControllerRequest
	 * @return {@link P2PTransactionDTO}
	 */
	public P2PTransactionDTO deleteTransaction(String referenceId, String transactionType, DataControllerRequest dataControllerRequest);
	
	/**
	 * Invokes line of business service for p2p to cancel occurrence at the backend
	 * @param referenceId
	 * @param dataControllerRequest
	 * @return {@link P2PTransactionDTO}
	 */
	public P2PTransactionDTO cancelTransactionOccurrence(String referenceId, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for p2p transfer to create at the Core with pending(INAU) status
     * @param {@link P2PTransactionBackendDTO}
     * @param dataControllerRequest
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO createPendingTransaction(P2PTransactionBackendDTO backendObj, DataControllerRequest request);
	
	/**
     * Invokes line of business service for p2p transfer to approve at the Core
     * @param referenceId
     * @param dataControllerRequest
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency);
	
	/**
     * Invokes line of business service for p2p transfer to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for p2p to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link P2PTransactionDTO}
	 */
	public P2PTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for p2p to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link P2PTransactionDTO}
	 */
	public P2PTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);
		
	/**
     * Invokes line of business service for p2p to edit at the backend
     * @param {@link P2PTransactionBackendDTO}
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO validateTransaction(P2PTransactionBackendDTO input, DataControllerRequest request);

	/**
	 * fetches the transactions based on input set of ids
	 * @param billPayTransIds
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> p2pTransIds,
			DataControllerRequest dcr);
	
	/**
	 * updates status of transaction from LIVE to Pending
	 * @param P2PTransactionBackendDTO
	 * @param dcr
	 * @return OwnAccountFundTransferDTO
	 */
	public P2PTransactionDTO editTransactionWithApproval(P2PTransactionBackendDTO transferBackendDTO, DataControllerRequest request);
	
}