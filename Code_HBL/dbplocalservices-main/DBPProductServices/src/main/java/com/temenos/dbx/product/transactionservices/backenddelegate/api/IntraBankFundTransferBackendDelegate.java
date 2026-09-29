package com.temenos.dbx.product.transactionservices.backenddelegate.api;


import java.util.List;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;

/**
 * Handles all backend operations on International Fund transaction
 * @author KH2624
 * extends {@link BackendDelegate}
 */

public interface IntraBankFundTransferBackendDelegate extends BackendDelegate {
	/**
	 * Invokes line of business service for intrabank transfer to create at the Core
	 * @param {@link IntraBankFundTransferBackendDTO}
	 * @param headerParams
	 * @return {@link IntraBankFundTransferDTO}
	 */
	public IntraBankFundTransferDTO createTransactionWithoutApproval(IntraBankFundTransferBackendDTO intrabankfundtransferbackenddto, DataControllerRequest request);

	/**
     * Invokes line of business service for intrabank to edit at the backend
     * @param {@link IntraBankFundTransferBackendDTO}
     * @param headerParams
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO editTransactionWithoutApproval(IntraBankFundTransferBackendDTO intrabankTransactionBackendDTO, DataControllerRequest request);

	/**
	 * Invokes line of business service for Internal transfer to validate transaction at the backend
	 * @param {@link IntraBankFundTransferDTO} 
	 * @param dataControllerRequest
	 * @return {@link IntraBankFundTransferDTO}
	 */
	public IntraBankFundTransferDTO validateTransaction( IntraBankFundTransferBackendDTO input , DataControllerRequest dataControllerRequest);
	
	/**
	 * Invokes line of business service for Internal transfer to cancel occurrence at the backend
	 * @param {@link IntraBankFundTransferBackendDTO} 
	 * @param dataControllerRequest
	 * @return {@link IntraBankFundTransferDTO} 
	 */
	public IntraBankFundTransferDTO createPendingTransaction( IntraBankFundTransferBackendDTO input , DataControllerRequest dataControllerRequest);

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
	public IntraBankFundTransferDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest dcr, String frequency);
	
	/**
	 * Invokes line of business service for Internal transfer to withdrawTransaction at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param dataControllerRequest
	 * @return {@link IntraBankFundTransferDTO} 
	 */
	public IntraBankFundTransferDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dcr, String frequency);
	
	/**
	 * Invokes line of business service for Internal transfer to fetchTransactionById at the backend
	 * @param referenceId
	 * @param dataControllerRequest
	 * @return {@link IntraBankFundTransferDTO} 
	 */
	public IntraBankFundTransferDTO fetchTransactionById(String referenceId, DataControllerRequest dcr);
	
	/**
	 * fetches the transactions based on input set of ids
	 * @param billPayTransIds
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> intraBankTransIds,
			DataControllerRequest dcr);
	
	/**
	 * updates status of transaction from LIVE to Pending
	 * @param IntraBankFundTransferBackendDTO
	 * @param dcr
	 * @return IntraBankFundTransferDTO
	 */
	public IntraBankFundTransferDTO editTransactionWithApproval(IntraBankFundTransferBackendDTO input, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for intrabank transfer to cancel without approval at the backend
	 * @param {@link referenceId}
	 * @param {@link request}
	 * @return {@link intrabankFundTransferDTO}
	 */	
	public IntraBankFundTransferDTO cancelTransactionWithoutApproval(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for intrabank transfer to cancel at the Core with approval
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO cancelTransactionWithApproval(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for intrabank transfer to approve cancellation at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO approveCancellation(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for intrabank transfer to reject cancellation at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO rejectCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for intrabank transfer to withdraw cancellation at the backend
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link IntraBankFundTransferDTO}
	 */
	public IntraBankFundTransferDTO withdrawCancellation(String referenceId, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for intrabank transfer to cancel without delete at the backend
	 * @param frequencyType 
	 * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
	 * @return {@link IntraBankFundTransferDTO}
	 */	
	public IntraBankFundTransferDTO deleteTransactionWithoutApproval(String transactionId, String transactionType,	String frequencyType, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for intrabank transfer transfer to delete at the Core with approval
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO deleteTransactionWithApproval(String referenceId, String transactionType, String frequencyType, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for intrabank transfer transfer to approve deletion at the Core
     * @param {@link refrenceId}
     * @param {@link transactionType}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO approveDeletion(String referenceId, String transactionType, String frequencyType, DataControllerRequest request);
	
	/**
     * Invokes line of business service for intrabank transfer transfer to reject deletion at the Core
     * @param {@link refrenceId}
     * @param {@link request}
     * @return {@link IntraBankFundTransferDTO}
     */
	public IntraBankFundTransferDTO rejectDeletion(String referenceId, String frequencyType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for intrabank transfer to withdraw deletion at the backend
	 * @param {@link refrenceId}
     * @param {@link request}
	 * @return {@link IntraBankFundTransferDTO}
	 */
	public IntraBankFundTransferDTO withdrawDeletion(String referenceId, String frequencyType, DataControllerRequest request);
}