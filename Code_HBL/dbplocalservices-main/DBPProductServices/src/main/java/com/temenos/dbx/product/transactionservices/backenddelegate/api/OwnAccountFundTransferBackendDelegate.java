package com.temenos.dbx.product.transactionservices.backenddelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferDTO;

/**
 *  Handles all the operations on Own Account Fund transaction
 *  @author kh2715
 *	extends {@link BackendDelegate}
 */

public interface OwnAccountFundTransferBackendDelegate extends BackendDelegate{
	
	 /**
     * Invokes line of business service for Internal transfer to create at the Core
     * @param {@link OwnAccountFundTransferBackendDTO}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO createTransactionWithoutApproval(OwnAccountFundTransferBackendDTO ownaccountfundtransferbackenddto, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount to edit at the backend
     * @param {@link OwnAccountFundTransferBackendDTO}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO editTransactionWithoutApproval(OwnAccountFundTransferBackendDTO transferBackendDTO, DataControllerRequest request);
	
	 /**
     * Invokes line of business service for Internal transfer to create at the Core with pending(INAU) status
     * @param {@link OwnAccountFundTransferBackendDTO}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO createPendingTransaction(OwnAccountFundTransferBackendDTO ownaccountBackendDTO, DataControllerRequest request);
	
	/**
     * Invokes line of business service for Internal transfer to approve at the Core
     * @param referenceId
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency);
	
	/**
     * Invokes line of business service for Internal transfer to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for ownaccount to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link OwnAccountFundTransferDTO}
	 */
	public OwnAccountFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for ownaccount to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link OwnAccountFundTransferDTO}
	 */
	public OwnAccountFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);
		
	/**
     * Invokes line of business service for ownaccount to edit at the backend
     * @param {@link OwnAccountFundTransferBackendDTO}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO validateTransaction(OwnAccountFundTransferBackendDTO input, DataControllerRequest request);

	/**
	 * fetches the transactions based on input set of ids
	 * @param billPayTransIds
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> ownAccountFundTransferIds,
			DataControllerRequest dcr);
	
	/**
	 * updates status of transaction from LIVE to Pending
	 * @param OwnAccountFundTransferBackendDTO
	 * @param dcr
	 * @return OwnAccountFundTransferDTO
	 */
	public OwnAccountFundTransferDTO editTransactionWithApproval(OwnAccountFundTransferBackendDTO transferBackendDTO, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ownaccount transfer to cancel without approval at the backend
	 * @param {@link referenceId}
	 * @param {@link request}
	 * @return {@link OwnAccountFundTransferDTO}
	 */	
	public OwnAccountFundTransferDTO cancelTransactionWithoutApproval(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount transfer to cancel at the Core with approval
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO cancelTransactionWithApproval(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount transfer to approve cancellation at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount transfer to reject cancellation at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO rejectCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ownaccount transfer to withdraw cancellation at the backend
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link OwnAccountFundTransferDTO}
	 */
	public OwnAccountFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ownaccount transfer to cancel without delete at the backend
	 * @param frequencyType 
	 * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
	 * @return {@link OwnAccountFundTransferDTO}
	 */	
	public OwnAccountFundTransferDTO deleteTransactionWithoutApproval(String transactionId, String transactionType,	String frequencyType, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for ownaccount transfer transfer to delete at the Core with approval
	 * @param frequencyType 
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO deleteTransactionWithApproval(String referenceId, String transactionType, String frequencyType, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for ownaccount transfer transfer to approve deletion at the Core
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount transfer transfer to reject deletion at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO rejectDeletion(String referenceId, String frequencyType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ownaccount transfer to withdraw deletion at the backend
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link OwnAccountFundTransferDTO}
	 */
	public OwnAccountFundTransferDTO withdrawDeletion(String referenceId, String frequencyType, DataControllerRequest request);
	
}