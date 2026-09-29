package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionDTO;

/**
 * Handles all the operations on Wire transaction
 * @author kh1755
 * extends {@link BusinessDelegate}
 *
 */
public interface DomesticWireTransactionBusinessDelegate extends BusinessDelegate {

	/**
     * Creates a transaction entry into wire transfers table with appropriate status
     * @param {@link WireTransactionDTO}
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO createTransactionAtDBX(WireTransactionDTO wireTransactionDTO);
	
	/**
	 *  Updates the status and confirmation number for a given transcationId in the table
	 * @param transcationId
	 * @param status
	 * @param confirmationNumber
	 * @return
	 */
	public WireTransactionDTO updateStatus(String transcationId, String status, String confirmationNumber);
	
	/**
	 * Updates the requestId for the given transactionId
	 * @param transcationId
	 * @param requestId
	 * @return true is successful else false
	 */
	public boolean updateRequestId(String transcationId, String requestId);
	
	/**
	 * Updates the created time stamp for the given transactionId
	 * @param transcationId
	 * @param createdts
	 * @return true is successful else false
	 */
	public boolean updateCreatedts(String transcationId, String createdts);
	
	/**
     * fetches transaction entry from wiretransfers table for the given transcationId 
     * and if the given customerId has access to that transcationId
     * @param transcationId
     * @param customerId
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO fetchTranscationById(String transcationId, String customerId);
	
	/**
	 * fetches transaction entry from wiretransfers table for the given confirmationNumber 
	 * @param confirmationNumber
	 * @return
	 */
	public WireTransactionDTO fetchExecutedTranscationEntry(String confirmationNumber);
	
	/**
     * Deletes a transaction entry from billpay transfers table
     * @param transactionId
     * @return return true/false
     */
	public boolean deleteTransactionAtDBX(String transactionId);
	
	/**
	 * executes the wire transaction after approval
	 * @param transactionId
	 * @param request
	 */
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request);

	/**
     * executes the audit logging engine after wire transaction
     * @param bulkWireTransactionJSON
     * @param transactionStatus
     * @param totalAmount
     * @param requestId
     * @param referenceId
     * @param requestPayload
     * @param serviceName
     */
	public void auditloggingforWireTransfers(DataControllerRequest request,DataControllerResponse response,Result result,Object[] inputArray,JSONObject bulkWireTransactionJSON,TransactionStatusEnum transactionStatus, Double totalAmount,String requestId, String referenceId,JsonObject requestPayload, String serviceName);
	
	/**
	 * fetch wire transactions based on List of BBRequestDTOs and the returned value contains the merged information
	 * @param requests
	 * @param dcr
	 * @return List<ApprovalRequestDTO>
	 */
	public List<ApprovalRequestDTO> fetchWireTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);

	/**
     * Invokes line of business service for domestic wire transfer to approve at the Core
     * @param referenceId
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency);
	
	/**
     * Invokes line of business service for domestic wire transfer to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for domestic wire to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link WireTransactionDTO}
	 */
	public WireTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for domestic wire to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link WireTransactionDTO}
	 */
	public WireTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);
	
	/**
     * Invokes line of business service for domestic wire transfer to create at the Core with pending(INAU) status
     * @param {@link WireTransactionDTO}
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO createPendingTransaction(WireTransactionDTO wireTransactiondto, DataControllerRequest request);

	/**
	 * fetches the transactions based on filter
	 * @param filter
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchWireTransactionsForApprovalInfo(String filter, DataControllerRequest dcr);

	/**
	 * Method to update status in dbx infinity tables using transactionId
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return {@link WireTransactionDTO}
	 */
	public WireTransactionDTO updateStatusUsingTransactionId(String transactionId, String status, String confirmationNumber);
	
}
