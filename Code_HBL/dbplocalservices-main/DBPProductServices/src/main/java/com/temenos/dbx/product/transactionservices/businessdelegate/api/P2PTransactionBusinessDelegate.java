package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionDTO;

/**
 *  Handles all the operations on P2P transaction
 *  @author kh1755
 *	extends {@link BusinessDelegate}
 */
public interface P2PTransactionBusinessDelegate extends BusinessDelegate {

	/**
     * Creates a transaction entry into p2p transfers table with appropriate status
     * @param {@link P2PTransactionDTO}
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO createTransactionAtDBX(P2PTransactionDTO p2pTransactionDTO);
	
	/**
	 * Updtaes a transaction entry into p2p transfers table with appropriate status
	 * @param p2pTransactionDTO
	 * @return
	 */
	public  P2PTransactionDTO updateTransactionAtDBX(P2PTransactionDTO p2pTransactionDTO);
	
	/**
	 *  Updates the status and confirmation number for a given transactionId in the table
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return
	 */
	public P2PTransactionDTO updateStatus(String transactionId, String status, String confirmationNumber);
	
	/**
	 * Updates the requestId for the given transactionId
	 * @param transactionId
	 * @param requestId
	 * @return true is successful else false
	 */
	public boolean updateRequestId(String transactionId, String requestId);
	
	/**
     * fetches transaction entry from p2ptransfers table for the given transactionId
     * @param requestParameters
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO fetchTranscationEntry(String transactionId);
	
	/**
     * Deletes a transaction entry from p2p transfers table
     * @param transactionId
     * @return return true/false
     */
	public boolean deleteTransactionAtDBX(String transactionId);
	
	/**
     * fetches transaction entry from p2ptransfers table for the given confirmationNumber
     * @param transactionId
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds, String createdby, String legalEntityId);
	
	/**
	 * executes the p2p transaction after approval
	 * @param transactionId
	 * @param featureActionId 
	 * @param request
	 */
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request);
	
	/**
	 * Fetches the P2PTransactions based on List of BBRequestDTOs and the returned value contains the merged information
	 * @param requests
	 * @param dcr
	 * @return List<ApprovalRequestDTO>
	 */
	public List<ApprovalRequestDTO> fetchP2PTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);
	
	/**
     * Invokes line of business service for p2p transfer to create at the Core with pending(INAU) status
     * @param {@link P2PTransactionDTO}
     * @param dataControllerRequest
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO createPendingTransaction(P2PTransactionDTO p2ptransferdto, DataControllerRequest request);
	
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
	public P2PTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for p2p to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link P2PTransactionDTO}
	 */
	public P2PTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for p2p to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link P2PTransactionDTO}
	 */
	public P2PTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);

	/**
	 * fetches the transactions based on filter
	 * @param filter
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchP2PTransactionsForApprovalInfo(String filter, DataControllerRequest dcr);
	
	/**
     * Invokes line of business service for p2p transfer to edit at the Core with pending(INAU) status
     * @param {@link P2PTransactionDTO}
     * @return {@link P2PTransactionDTO}
     */
	public P2PTransactionDTO editPendingTransaction(P2PTransactionDTO input, DataControllerRequest request);

	/**
	 * Method to update status in dbx infinity tables using transactionId
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return {@link P2PTransactionDTO}
	 */
	public P2PTransactionDTO updateStatusUsingTransactionId(String transactionId, String status, String confirmationNumber);

		

}
