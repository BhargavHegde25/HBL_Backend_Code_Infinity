package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.InternationalFundTransferDTO;

/**
 *  Handles all the operations on International Fund transaction
 *  @author kh1755
 *  extends {@link BusinessDelegate}
 */
public interface InternationalFundTransferBusinessDelegate extends BusinessDelegate {

	/**
     * Creates a transaction entry into internationalfund transfers table with appropriate status
     * @param {@link InternationalFundTransferDTO}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO createTransactionAtDBX(InternationalFundTransferDTO internationalfundtransferdto);

	/**
	 * Updates a transaction entry into internationalfund transfers table with appropriate status
	 * @param internationalfundtransferdto
	 * @return
	 */
	public  InternationalFundTransferDTO updateTransactionAtDBX(InternationalFundTransferDTO internationalfundtransferdto);
	
	/**
	 *  Updates the status and confirmation number for a given transactionId in the table
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return
	 */
	public InternationalFundTransferDTO updateStatus(String transactionId, String status, String confirmationNumber);
	
	/**
	 * Updates the requestId for the given transactionId
	 * @param transactionId
	 * @param requestId
	 * @return true is successful else false
	 */
	public boolean updateRequestId(String transactionId, String requestId);
	
	/**
     * fetches transaction entry from internationalfundtransfers table for the given transactionId
     * @param requestParameters
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO fetchTranscationEntry(String transactionId);
	
	/**
     * Deletes a transaction entry from international transfers table
     * @param transactionId
     * @return return true/false
     */
	public boolean deleteTransactionAtDBX(String transactionId);
	
	/**
     * fetches transaction entry from internationalfundtransfers table for the given confirmationNumber
     * @param transactionId
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds, String createdby , String fetchExecutedTranscationEntry);
	
	/**
	 * executes the international fund transaction after approval
	 * @param transactionId
	 * @param featureActionId 
	 * @param request
	 */
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request);
	
	/**
	 * Fetches the international funds transfers based on List of BBRequestDTOs and the returned value contains the merged information
	 * @param requests
	 * @param dcr
	 * @return List<ApprovalRequestDTO>
	 */
	public List<ApprovalRequestDTO> fetchInternationalTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);
	
	 /**
     * Invokes line of business service for international transfer to create at the Core with pending(INAU) status
     * @param {@link InternationalFundTransferDTO}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO createPendingTransaction(InternationalFundTransferDTO ownaccountfundtransferdto, DataControllerRequest request);
	
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
	public InternationalFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for international to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link InternationalFundTransferDTO}
	 */
	public InternationalFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for international to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link InternationalFundTransferDTO}
	 */
	public InternationalFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);

	/**
	 * fetches the transactions based on filter
	 * @param filter
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchInternationalTransactionsForApprovalInfo(String filter, DataControllerRequest dcr);
	
	/**
     * Invokes line of business service for international transfer to edit at the Core with pending(INAU) status
     * @param {@link InternationalFundTransferDTO}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO editPendingTransaction(InternationalFundTransferDTO input, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international transfer to cancel with approval
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO cancelTransactionWithApproval(String referenceId,  String transactionId,  DataControllerRequest request);
	
	/**
     * Invokes line of business service for international transfer to approve cancellation
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international transfer to reject cancellation
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO rejectCancellation(String referenceId,  DataControllerRequest request);
	
	/**
	 * Invokes line of business service for international transfer to withdraw cancellation
	 * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
	 * @return {@link InternationalFundTransferDTO}
	 */
	public InternationalFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international transfer transfer to delete with approval
	 * @param frequencyType 
     * @param {@link transactionId}
     * @param {@link transactionType}
     * @param {@link request}
	 * @return return InternationalFundTransferDTO
     */
	public InternationalFundTransferDTO deleteTransactionWithApproval(String confirmationNumber, String transactionType, String frequencyType, String transactionId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international transfer transfer to approve deletion
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request);
	
	/**
     * Invokes line of business service for international transfer transfer to reject deletion
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link InternationalFundTransferDTO}
     */
	public InternationalFundTransferDTO rejectDeletion(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for international transfer to withdraw deletion
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link InternationalFundTransferDTO}
	 */
	public InternationalFundTransferDTO withdrawDeletion(String referenceId, DataControllerRequest request);
	
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
	 * @return {@link InternationalFundTransferDTO}
	 */
	public InternationalFundTransferDTO updateStatusUsingTransactionId(String transactionId, String status,
			String confirmationNumber);
		
}
