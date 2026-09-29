package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.OwnAccountFundTransferDTO;

/**
 *  Handles all the operations on Own Account Fund transaction
 *  @author kh1755
 *	extends {@link BusinessDelegate}
 */
public interface OwnAccountFundTransferBusinessDelegate extends BusinessDelegate {

	/**
     * Invokes line of business service for Internal transfer to create at the Core with pending(INAU) status
     * @param {@link OwnAccountFundTransferDTO}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO createPendingTransaction(OwnAccountFundTransferDTO ownaccountfundtransferdto, DataControllerRequest request);
	
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
	public OwnAccountFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ownaccount to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link OwnAccountFundTransferDTO}
	 */
	public OwnAccountFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ownaccount to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link OwnAccountFundTransferDTO}
	 */
	public OwnAccountFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);
	
	/**
     * Creates a transaction entry into ownaccount transfers table with appropriate status
     * @param {@link OwnAccountFundTransferDTO}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO createTransactionAtDBX(OwnAccountFundTransferDTO ownaccountfundtransferdto);
	
	/**
	 * Updates the transaction at backend
	 * @param ownaccountfundtransferdto
	 * @return
	 */
	public OwnAccountFundTransferDTO updateTransactionAtDBX(OwnAccountFundTransferDTO ownaccountfundtransferdto);
	
	/**
	 *  Updates the status and confirmation number for a given transactionId in the table
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return
	 */
	public OwnAccountFundTransferDTO updateStatus(String transactionId, String status, String confirmationNumber);
	
	/**
	 * Updates the requestId for the given transactionId
	 * @param transactionId
	 * @param requestId
	 * @return true is successful else false
	 */
	public boolean updateRequestId(String transactionId, String requestId);
	
	/**
     * fetches transaction entry from ownaccounttransfers table for the given transactionId
     * @param requestParameters
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO fetchTranscationEntry(String transactionId);
	
	/**
     * Deletes a transaction entry from ownaccount transfers table
     * @param transactionId
     * @return return true/false
     */
	public boolean deleteTransactionAtDBX(String transactionId);
	
	/**
     * fetches transaction entry from ownaccounttransfers table for the given confirmationNumber
     * @param transactionId
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds, String createdby , String legalEntityId);
	
	/**
	 * executes the own account fund transaction after approval
	 * @param transactionId
	 * @param featureActionId 
	 * @param request
	 */
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request);

	/**
	 * fetches ownaccounttransfers based on List of BBRequestDTOs and the returned value contains the merged information
	 * @param requests
	 * @param dcr
	 * @return List<ApprovalRequestDTO>
	 */
	public List<ApprovalRequestDTO> fetchOwnAccountFundTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);

	/**
	 * fetches the transactions based on filter
	 * @param filter
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchOwnAccountFundTransactionsForApprovalInfo(String filter, DataControllerRequest dcr);
	
	/**
     * Invokes line of business service for Internal transfer to edit at the Core with pending(INAU) status
     * @param {@link OwnAccountFundTransferDTO}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO editPendingTransaction(OwnAccountFundTransferDTO input, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount transfer to cancel with approval
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO cancelTransactionWithApproval(String referenceId,  String transactionId,  DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount transfer to approve cancellation
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount transfer to reject cancellation
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO rejectCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ownaccount transfer to withdraw cancellation
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link OwnAccountFundTransferDTO}
	 */
	public OwnAccountFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request);
	
	
	/**
     * Invokes line of business service for ownaccount transfer transfer to delete with approval
     * @param {@link transactionId}
     * @param transactionType
	 * @return return OwnAccountFundTransferDTO
     */
	public OwnAccountFundTransferDTO deleteTransactionWithApproval(String confirmationNumber, String transactionType, String frequencyType, String transactionId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount transfer transfer to approve deletion
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ownaccount transfer transfer to reject deletion
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link OwnAccountFundTransferDTO}
     */
	public OwnAccountFundTransferDTO rejectDeletion(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ownaccount transfer to withdraw deletion
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link OwnAccountFundTransferDTO}
	 */
	public OwnAccountFundTransferDTO withdrawDeletion(String referenceId, DataControllerRequest request);

	/**
	 * cancels the own account fund transaction after approval
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
	 * @return {@link OwnAccountFundTransferDTO}
	 */
	public OwnAccountFundTransferDTO updateStatusUsingTransactionId(String transactionId, String status,
			String confirmationNumber);

}
