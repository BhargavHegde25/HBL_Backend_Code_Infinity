package com.temenos.dbx.product.transactionservices.backenddelegate.api;


import java.util.List;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.InternationalFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.InternationalFundTransferDTO;

/**
 * Handles all backend operations on International Fund transaction
 * @author KH2624
 * extends {@link BackendDelegate}
 */

public interface InternationalFundTransferBackendDelegate extends BackendDelegate {

    /**
     * Invokes line of business service for international fund transfer to create at the Backend
     * @param {@link InternationalFundTransferBackendDTO}
     * @param headerParams
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO createTransactionWithoutApproval(InternationalFundTransferBackendDTO internationalfundtransferbackenddto, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international to edit at the backend
     * @param {@link InternationalFundTransferBackendDTO}
     * @param headerParams
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO editTransactionWithoutApproval(InternationalFundTransferBackendDTO internationalTransactionBackendDTO, DataControllerRequest request);
	
	 /**
     * Invokes line of business service for international transfer to create at the Core with pending(INAU) status
     * @param {@link InternationalFundTransferDTO}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO createPendingTransaction(InternationalFundTransferBackendDTO internationalFundTransferBackendDTO, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international transfer to approve at the Core
     * @param referenceId
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency);
	
	/**
     * Invokes line of business service for international transfer to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for international to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link InternationalFundTransferDTO}
	 */
	public InternationalFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for international to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link InternationalFundTransferDTO}
	 */
	public InternationalFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);
		
	/**
     * Invokes line of business service for international to edit at the backend
     * @param {@link InternationalFundTransferDTO}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO validateTransaction(InternationalFundTransferBackendDTO input, DataControllerRequest request);

	/**
	 * fetches the transactions based on input set of ids
	 * @param billPayTransIds
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> internationalTransIds,
			DataControllerRequest dcr);
	
	/**
	 * updates status of transaction from LIVE to Pending
	 * @param InternationalFundTransferBackendDTO
	 * @param dcr
	 * @return InternationalFundTransferDTO
	 */
	public InternationalFundTransferDTO editTransactionWithApproval(InternationalFundTransferBackendDTO internationalfundtransferbackenddto, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for international fund transfer to cancel without approval at the backend
	 * @param {@link referenceId}
	 * @param {@link request}
	 * @return {@link InternationalFundTransferDTO}
	 */	
	public InternationalFundTransferDTO cancelTransactionWithoutApproval(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international fund transfer to cancel at the Core with approval
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO cancelTransactionWithApproval(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international fund transfer to approve cancellation at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international fund transfer to reject cancellation at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO rejectCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for international fund transfer to withdraw cancellation at the backend
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link InternationalFundTransferDTO}
	 */
	public InternationalFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for international fund transfer to cancel without delete at the backend
	 * @param frequencyType 
	 * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
	 * @return {@link InternationalFundTransferDTO}
	 */	
	public InternationalFundTransferDTO deleteTransactionWithoutApproval(String transactionId, String transactionType,	String frequencyType, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for international fund transfer transfer to delete at the Core with approval
	 * @param frequencyType 
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO deleteTransactionWithApproval(String referenceId, String transactionType, String frequencyType, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for international fund transfer transfer to approve deletion at the Core
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international fund transfer transfer to reject deletion at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO rejectDeletion(String referenceId, String frequencyType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for international fund transfer to withdraw deletion at the backend
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link InternationalFundTransferDTO}
	 */
	public InternationalFundTransferDTO withdrawDeletion(String referenceId, String frequencyType, DataControllerRequest request);
	
	/**
	 * To populate beneficiary bank address into transactions
	 * @param {@link transactions}
     * @param {@link request}
	 * @return {@link List<ApprovalRequestDTO>}
	 */
	public List<ApprovalRequestDTO> getBeneBankAddress(List<ApprovalRequestDTO> transactions, DataControllerRequest request);
}