package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;

/**
 *  Handles all the operations on Inter Bank transaction
 *  @author kh1755
 *  extends {@link BusinessDelegate}
 */
public interface InterBankFundTransferBusinessDelegate extends BusinessDelegate {
	
	/**
     * Creates a transaction entry into interbankfundtransfers table with appropriate status
     * @param {@link InterBankFundTransferDTO}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO createTransactionAtDBX(InterBankFundTransferDTO interBankFundTransferDTO);
	
	/**
	 * Updates a transaction entry into interbankfundtransfers table with appropriate status
	 * @param interbankfundtransferdto
	 * @return
	 */
	public  InterBankFundTransferDTO updateTransactionAtDBX(InterBankFundTransferDTO interbankfundtransferdto);
	
	/**
	 *  Updates the status and confirmation number for a given transactionId in the table
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return
	 */
	public InterBankFundTransferDTO updateStatus(String transactionId, String status, String confirmationNumber);
	
	/**
	 * Updates the requestId for the given transactionId
	 * @param transactionId
	 * @param requestId
	 * @return true is successful else false
	 */
	public boolean updateRequestId(String transactionId, String requestId);
	
	/**
     * fetches transaction entry from interbankfundtransfers table for the given transactionId
     * @param requestParameters
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO fetchTranscationEntry(String transactionId);
	
	/**
     * Deletes a transaction entry from interbank transfers table
     * @param transactionId
     * @return return true/false
     */
	public boolean deleteTransactionAtDBX(String transactionId);
	
	/**
     * fetches transaction entry from interbanktransfers table for the given confirmationNumber
     * @param transactionId
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds, String createdby, String legalEntityId);
	
	/**
	 * executes the inter bank transaction after approval
	 * @param transactionId
	 * @param featureActionId 
	 * @param request
	 */
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request);
	
	/**
	 * Fetches the inter bank transactions based on List of BBRequestDTOs and the returned value contains the merged information
	 * @param requests
	 * @param dcr
	 * @return List<ApprovalRequestDTO>
	 */
	public List<ApprovalRequestDTO> fetchInterBankTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);


	/**
	 * Invokes line of business service for interbank transfer to cancel occurrence at the backend
	 * @param {@link InterBankFundTransferDTO} 
	 * @param dataControllerRequest
	 * @return {@link InterBankFundTransferDTO} 
	 */
	public InterBankFundTransferDTO createPendingTransaction( InterBankFundTransferDTO input , DataControllerRequest dataControllerRequest);

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
	public InterBankFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest dcr);

	/**
	 * Invokes line of business service for interbank transfer to withdrawTransaction at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param dataControllerRequest
	 * @return {@link InterBankFundTransferDTO} 
	 */
	public InterBankFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dcr);

	/**
	 * Invokes line of business service for interbank transfer to fetchTransactionById at the backend
	 * @param referenceId
	 * @param dataControllerRequest
	 * @return {@link InterBankFundTransferDTO} 
	 */
	public InterBankFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest dcr);

	/**
	 * fetches the transactions based on filter
	 * @param filter
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchInterBankTransactionsForApprovalInfo(String filter, DataControllerRequest dcr);
	
	/**
     * Invokes line of business service for inter bank transfer to edit at the Core with pending(INAU) status
     * @param {@link InterBankFundTransferDTO}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO editPendingTransaction(InterBankFundTransferDTO input, DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer to cancel with approval
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO cancelTransactionWithApproval(String referenceId,  String transactionId,  DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer to approve cancellation
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer to reject cancellation
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO rejectCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for interbank transfer to withdraw cancellation
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link InterBankFundTransferDTO}
	 */
	public InterBankFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer transfer to delete with approval
	 * @param frequencyType 
     * @param {@link transactionId}
     * @param {@link transactionType}
     * @param {@link request}
	 * @return return InterBankFundTransferDTO
     */
	public InterBankFundTransferDTO deleteTransactionWithApproval(String confirmationNumber, String transactionType, String frequencyType, String transactionId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer transfer to approve deletion
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request);
	
	/**
     * Invokes line of business service for interbank transfer transfer to reject deletion
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InterBankFundTransferDTO}
     */
	public InterBankFundTransferDTO rejectDeletion(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for interbank transfer to withdraw deletion
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link InterBankFundTransferDTO}
	 */
	public InterBankFundTransferDTO withdrawDeletion(String referenceId, DataControllerRequest request);
	
	/**
	 * cancels the transaction after approval
	 * @param transactionId
	 * @param featureActionId 
	 * @param request
	 */
	public void cancelTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request);

	/**
	 * Method to update status in dbx infinity tables using transactionId
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return {@link InterBankFundTransferDTO}
	 */
	public InterBankFundTransferDTO updateStatusUsingTransactionId(String transactionId, String status,
			String confirmationNumber);

}
