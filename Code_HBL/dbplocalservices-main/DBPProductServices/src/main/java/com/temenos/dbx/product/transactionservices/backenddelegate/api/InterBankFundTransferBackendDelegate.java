package com.temenos.dbx.product.transactionservices.backenddelegate.api;


import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;

/**
 * Handles all backend operations on InterBank Fund transaction
 * @author KH2624
 * extends {@link BackendDelegate}
 */

public interface InterBankFundTransferBackendDelegate extends BackendDelegate {
	
   /**
     * Invokes line of business service for interbankfundtransfer to create at the Backend
     * @param {@link InterBankFundTransferBackendDTO}
     * @param headerParams
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO createTransactionWithoutApproval(InterBankFundTransferBackendDTO interBankFundTransferBackendDTO, DataControllerRequest request);

   /**
     * Invokes line of business service for interbank to edit at the backend
     * @param {@link InterBankFundTransferBackendDTO}
     * @param headerParams
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO editTransactionWithoutApproval(InterBankFundTransferBackendDTO interbankTransactionBackendDTO, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for interbank transfer to cancel occurrence at the backend
	 * @param {@link InterBankFundTransferDTO} 
	 * @param dataControllerRequest
	 * @return {@link InterBankFundTransferDTO} 
	 */
	public InterBankFundTransferDTO createPendingTransaction( InterBankFundTransferBackendDTO input , DataControllerRequest dataControllerRequest);

	/**
	 * Invokes line of business service for interbank transfer to approve at the Core
	 * @param referenceId
	 * @param dataControllerRequest
	 * @return {@link InterBankFundTransferDTO}     
	 */
	public InterBankFundTransferDTO approveTransaction(String referenceId, DataControllerRequest dcr, String frequency);

	/**
	 * Invokes line of business service for interbank transfer to rejectTransaction at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param dataControllerRequest
	 * @return {@link InterBankFundTransferDTO} 
	 */
	public InterBankFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest dcr, String frequency);

	/**
	 * Invokes line of business service for interbank transfer to withdrawTransaction at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param dataControllerRequest
	 * @return {@link InterBankFundTransferDTO} 
	 */
	public InterBankFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dcr, String frequency);

	/**
	 * Invokes line of business service for interbank transfer to fetchTransactionById at the backend
	 * @param referenceId
	 * @param dataControllerRequest
	 * @return {@link InterBankFundTransferDTO} 
	 */
	public InterBankFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest dcr);
	
	/**
	 * Invokes line of business service for interbank transfer to validate transaction at the backend
	 * @param {@link InterBankFundTransferDTO} 
	 * @param dataControllerRequest
	 * @return {@link InterBankFundTransferDTO}
	 */
	public InterBankFundTransferDTO validateTransaction( InterBankFundTransferBackendDTO input , DataControllerRequest dataControllerRequest);

	/**
	 * fetches the transactions based on input set of ids
	 * @param billPayTransIds
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> interBankTransIds,
			DataControllerRequest dcr);
	
	/**
	 * updates status of transaction from LIVE to Pending
	 * @param InterBankFundTransferBackendDTO
	 * @param dcr
	 * @return InterBankFundTransferDTO
	 */
	public InterBankFundTransferDTO editTransactionWithApproval(InterBankFundTransferBackendDTO interBankFundTransferBackendDTO, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for interbank transfer to cancel without approval at the backend
	 * @param {@link referenceId}
	 * @param {@link request}
	 * @return {@link InterBankFundTransferDTO}
	 */	
	public InterBankFundTransferDTO cancelTransactionWithoutApproval(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer to cancel at the Core with approval
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO cancelTransactionWithApproval(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer to approve cancellation at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer to reject cancellation at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO rejectCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for interbank transfer to withdraw cancellation at the backend
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link InterBankFundTransferDTO}
	 */
	public InterBankFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for interbank transfer to cancel without delete at the backend
	 * @param frequencyType 
	 * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
	 * @return {@link InterBankFundTransferDTO}
	 */	
	public InterBankFundTransferDTO deleteTransactionWithoutApproval(String transactionId, String transactionType,	String frequencyType, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for interbank transfer transfer to delete at the Core with approval
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO deleteTransactionWithApproval(String referenceId, String transactionType, String frequencyType, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for interbank transfer transfer to approve deletion at the Core
	 * @param frequencyType 
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer transfer to reject deletion at the Core
	 * @param frequencyType 
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO rejectDeletion(String referenceId, String frequencyType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for interbank transfer to withdraw deletion at the backend
	 * @param frequencyType 
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link InterBankFundTransferDTO}
	 */
	public InterBankFundTransferDTO withdrawDeletion(String referenceId, String frequencyType, DataControllerRequest request);
	
	/**
	 * To populate beneficiary bank address into transactions
	 * @param {@link transactions}
     * @param {@link request}
	 * @return {@link List<ApprovalRequestDTO>}
	 */
	public List<ApprovalRequestDTO> getBeneBankAddress(List<ApprovalRequestDTO> transactions, DataControllerRequest request);

	JSONObject updatePayeeVerificationStatus(String payeeId, String payeeVerificationstatus);

	JSONObject getPayeeIDToUpdatePayeeVerificationStatus(String accountNumber, boolean isSameBankAccount,
			boolean isInternationalAccount, String userId);

}
