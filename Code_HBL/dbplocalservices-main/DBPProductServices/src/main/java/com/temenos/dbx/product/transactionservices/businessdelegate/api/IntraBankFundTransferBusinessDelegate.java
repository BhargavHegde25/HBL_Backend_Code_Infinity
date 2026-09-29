package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;

/**
 *  Handles all the operations on IntraBank fund transaction
 *  @author kh1755
 *	extends {@link BusinessDelegate}
 */
public interface IntraBankFundTransferBusinessDelegate extends BusinessDelegate {

	/**
     * Creates a transaction entry into intrabank transfers table with appropriate status
     * @param {@link IntraBankFundTransferDTO}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO createTransactionAtDBX(IntraBankFundTransferDTO IntraBankFundTransferDTO);
	
	/**
	 * Updtaes a transaction entry into intrabank transfers table with appropriate status
	 * @param intrabankfundtransferdto
	 * @return
	 */
	public  IntraBankFundTransferDTO updateTransactionAtDBX(IntraBankFundTransferDTO intrabankfundtransferdto);
	
	/**
	 *  Updates the status and confirmation number for a given transactionId in the table
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return
	 */
	public IntraBankFundTransferDTO updateStatus(String transactionId, String status, String confirmationNumber);
	
	/**
	 * Updates the requestId for the given transactionId
	 * @param transactionId
	 * @param requestId
	 * @return true is successful else false
	 */
	public boolean updateRequestId(String transactionId, String requestId);
	
	/**
     * fetches transaction entry from intrabankfundtransfers table for the given transactionId
     * @param requestParameters
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO fetchTranscationEntry(String transactionId);
	
	/**
     * Deletes a transaction entry from intrabank transfers table
     * @param transactionId
     * @return return true/false
     */
	public boolean deleteTransactionAtDBX(String transactionId);
	
	/**
     * fetches transaction entry from intrabankfundtransfers table for the given confirmationNumber
     * @param transactionId
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds, String createdby , String legalEntityId);
	
	/**
	 * executes the intra bank fund transaction after approval
	 * @param transactionId
	 * @param request
	 */
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request);
	
	/**
	 * Fetches the intra bank transactions based on List of BBRequestDTOs and the returned value contains the merged information
	 * @param requests
	 * @param dcr
	 * @return List<ApprovalRequestDTO>
	 */
	public List<ApprovalRequestDTO> fetchIntraBankTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);
	
	/**
	 * Invokes line of business service for Internal transfer to cancel occurrence at the backend
	 * @param {@link IntraBankFundTransferBackendDTO} 
	 * @param dataControllerRequest
	 * @return {@link IntraBankFundTransferDTO} 
	 */
	public IntraBankFundTransferDTO createPendingTransaction( IntraBankFundTransferDTO input , DataControllerRequest dataControllerRequest);

	/**
     * Invokes line of business service for Internal transfer to approve at the Core
     * @param referenceId
     * @param dataControllerRequest
     * @return {@link IntraBankFundTransferDTO}     
     */
	public IntraBankFundTransferDTO approveTransaction(String referenceId, DataControllerRequest dcr, String frequency);
	
	/**
	 * Invokes line of business service for Internal transfer to rejectTransaction at the backend
	 * @param referenceId
     * @param transactionType
     * @param dataControllerRequest
     * @return {@link IntraBankFundTransferDTO} 
	 */
	public IntraBankFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest dcr);
	
	/**
	 * Invokes line of business service for Internal transfer to withdrawTransaction at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param dataControllerRequest
	 * @return {@link IntraBankFundTransferDTO} 
	 */
	public IntraBankFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dcr);
	
	/**
	 * Invokes line of business service for Internal transfer to fetchTransactionById at the backend
	 * @param referenceId
	 * @param dataControllerRequest
	 * @return {@link IntraBankFundTransferDTO} 
	 */
	public IntraBankFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest dcr);

	/**
	 * fetches the transactions based on filter
	 * @param filter
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchIntraBankTransactionsForApprovalInfo(String filter, DataControllerRequest dcr);
	
	/**
     * Invokes line of business service for intra bank transfer to edit at the Core with pending(INAU) status
     * @param {@link IntraBankFundTransferDTO}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO editPendingTransaction(IntraBankFundTransferDTO input, DataControllerRequest request);
	
	/**
     * Invokes line of business service for intrabank transfer to cancel with approval
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO cancelTransactionWithApproval(String referenceId,  String transactionId,  DataControllerRequest request);
	
	/**
     * Invokes line of business service for intrabank transfer to approve cancellation
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for intrabank transfer to reject cancellation
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO rejectCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for intrabank transfer to withdraw cancellation
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link IntraBankFundTransferDTO}
	 */
	public IntraBankFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request);
	
	
	/**
     * Invokes line of business service for intrabank transfer transfer to delete with approval
     * @param {@link transactionId}
     * @param {@link transactionType}
     * @param {@link request}
	 * @return return IntraBankFundTransferDTO
     */
	public IntraBankFundTransferDTO deleteTransactionWithApproval(String confirmationNumber, String transactionType, String frequencyType, String transactionId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for intrabank transfer transfer to approve deletion
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request);
	
	/**
     * Invokes line of business service for intrabank transfer transfer to reject deletion
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO rejectDeletion(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for intrabank transfer to withdraw deletion
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link IntraBankFundTransferDTO}
	 */
	public IntraBankFundTransferDTO withdrawDeletion(String referenceId, DataControllerRequest request);
	
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
	 * @return {@link IntraBankFundTransferDTO}
	 */
	public IntraBankFundTransferDTO updateStatusUsingTransactionId(String transactionId, String status,
			String confirmationNumber);
}
