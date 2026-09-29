package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;

/**
 * Handles all the operations on Bill pay transaction
 * @author kh1755
 * extends {@link BusinessDelegate}
 */
public interface BillPayTransactionBusinessDelegate extends BusinessDelegate {
	
	/**
     * Creates a transaction entry into billpay transfers table with appropriate status
     * @param {@link BillPayTransactionDTO}
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO createTransactionAtDBX(BillPayTransactionDTO billpayTransactionDTO);
	
	/**
	 * Updates a transaction entry into billpay transfers table with appropriate status
	 * @param billpayTransactionDTO
	 * @return
	 */
	public  BillPayTransactionDTO updateTransactionAtDBX(BillPayTransactionDTO billpayTransactionDTO);
	
	/**
     * Invokes Orchestration Service for Bulk Bill pay
     * @param requestParameters
     * @return {@link List<BillPayTransactionDTO>}
     */
	public List<BillPayTransactionDTO> createBulkBillPay(Map<String, Object> requestParameters);

	/**
	 *  Updates the status and confirmation number for a given transactionId in the table
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return
	 */
	public BillPayTransactionDTO updateStatus(String transactionId, String status, String confirmationNumber);
	
	/**
	 * Updates the requestId for the given transactionId
	 * @param transactionId
	 * @param requestId
	 * @return true is successful else false
	 */
	public boolean updateRequestId(String transactionId, String requestId);
	
	/**
     * fetches transaction entry from billpaytransfers table for the given transactionId
     * @param transactionId
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO fetchTranscationEntry(String transactionId);
	
	/**
     * fetches transaction entry from billpaytransfers table for the given confirmationNumber
     * @param transactionId
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds, String createdby, String legalEntityId);
	
	/**
     * Deletes a transaction entry from billpay transfers table
     * @param transactionId
     * @return return true/false
     */
	public boolean deleteTransactionAtDBX(String transactionId);
	
	/**
	 * executes the billpay transaction after approval
	 * @param transactionId
	 * @param featureActionId 
	 * @param request
	 */
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request);
	
	/**
	 * fetches the billpay transactions based on List of BBRequestDTOs and the returned value contains the merged information
	 * @param requests
	 * @param dcr
	 * @return
	 */
	public List<ApprovalRequestDTO> fetchBillPayTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);
	
	 /**
     * Invokes line of business service for billpay transfer to create at the Core with pending(INAU) status
     * @param {@link BillPayTransactionDTO}
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO createPendingTransaction(BillPayTransactionDTO billpaytransferdto, DataControllerRequest request);
	
	/**
     * Invokes line of business service for billpay transfer to approve at the Core
     * @param referenceId
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency);
	
	/**
     * Invokes line of business service for billpay transfer to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for billpay to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link BillPayTransactionDTO}
	 */
	public BillPayTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for billpay to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link BillPayTransactionDTO}
	 */
	public BillPayTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);

	/**
	 * fetches the transactions based on filter
	 * @param filter
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchBillPayTransactionsForApprovalInfo(String filter, DataControllerRequest dcr);
	
	/**
     * Invokes line of business service for bill pay transfer to edit at the Core with pending(INAU) status
     * @param {@link BillPayTransactionDTO}
     * @return {@link BillPayTransactionDTO}
     */
	public BillPayTransactionDTO editPendingTransaction(BillPayTransactionDTO input, DataControllerRequest request);

	/**
	 * Method to update status in dbx infinity tables using transactionId
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return {@link BillPayTransactionDTO}
	 */
	public BillPayTransactionDTO updateStatusUsingTransactionId(String transactionId, String status,
			String confirmationNumber);
		
	
	
}
