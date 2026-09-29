package com.temenos.dbx.product.achservices.businessdelegate.api;

import java.util.List;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionRecordDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionSubRecordDTO;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;

public interface ACHTransactionBusinessDelegate extends BusinessDelegate {
	
	
	/**
	 *@author KH2317
	 *@version 1.0
	 *@param achTransactionDTO contains data to create transaction
	 **/
	ACHTransactionDTO createTransactionAtDBX(ACHTransactionDTO achTransactionDTO);

	/**
	 *@author KH2317
	 *@version 1.0
	 *@param transactionId
	 *@param status
	 *@param confirmationNumber
	 **/	
	ACHTransactionDTO updateStatus(String transactionId, String status, String confirmationNumber);

	/**
	 *@author KH2317
	 *@version 1.0
	 *@param achTransactionRecordDTO contains data to create transaction records
	 **/
	ACHTransactionRecordDTO createTransactionRecord(ACHTransactionRecordDTO achTransactionRecordDTO);	

	/**
	 *@author KH2317
	 *@version 1.0
	 *@param achTransactionSubRecordDTO contains data to create transaction sub records
	 **/
	ACHTransactionSubRecordDTO createTransactionSubRecord(ACHTransactionSubRecordDTO achTransactionSubRecordDTO);

	/**
	 *@author KH2317
	 *@version 1.0
	 *@param achTransaction_id id of transaction to be updated
	 **@param requestId field to be updated in transaction corresponding to given id
	 **/
	boolean updateRequestId(String achTransaction_id, String requestId);

	/**
	 *@author KH2317
	 *@version 1.0
	 *@param achTransaction_id id of transaction to be updated
	 **@param confirmationNumber field to be updated in transaction corresponding to given id
	 **/
	boolean updateConfirmationNumber(long achTransaction_id, String confirmationNumber);

	/**
	 *@author KH2317
	 *@version 1.0
	 *@param queryData which contains data to create records and sub-records
	 **/	
	Result createTransactionRecordAndSubRecords(List<ACHTransactionRecordDTO> queryData);

	/**
	 * This method returns list of ACH Transaction Records for given Transaction_id
	 * @author KH2624
	 * @param transaction_id has transaction_id
	 * @return ACH Transaction Records
	 */
    public List<ACHTransactionRecordDTO> fetchTransactionRecords(String transaction_id);

	/**
	 * THis method returns list of ACH transaction subrecords for given transactionRecord_id
	 * @param transactionRecord_id has transactionRecord_id
	 * @return list of ACH transaction subrecords
	 */
	public List<ACHTransactionSubRecordDTO> fetchTransactionSubRecords(String transactionRecord_id);
	
	/**
	 * @author KH2626
	 * @version 1.0
	 * @param filters
	 * @return list of ach transactions
	 */
	public List<ACHTransactionDTO> getACHTransactions(FilterDTO filters, String customerId, Object transactionId, DataControllerRequest dcr);
	
	/**
	 * executes the given transaction after approval
	 * @param transactionId
	 * @param featureActionId 
	 */
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest dcr);
	
	/**
	 * fetches the transaction from DBX tables
	 * @param transactionId
	 * @return ACHTransactionDTO
	 */
	public ACHTransactionDTO fetchTranscationEntry(String transactionId);
	
	/**
     * Deletes a transaction entry from achtransaction table
     * @param transactionId
     * @return return true/false
     */
	public boolean deleteTransactionAtDBX(String transactionId);
	
	/**
	 * @author KH9450
	 * fetches the transaction along with its records and subrecords
	 * @param transactionId
	 * @return JSONObject
	 */
	public JSONObject fetchTranscationEntryWithRecordsAndSubRecords(String transactionId);

	/**
	 * fetches the ACH Transactions based on List of BBRequestDTOs and the returned value contains the merged information
	 * @param requests
	 * @param dcr
	 * @return List<ApprovalRequestDTO>
	 */
	public List<ApprovalRequestDTO> fetchACHTransactionsWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);
	/**
     * Invokes line of business service for Internal transfer to create at the Core with pending(INAU) status
     * @param {@link ACHTransactionDTO}
     * @return {@link ACHTransactionDTO}
     */
	public ACHTransactionDTO createPendingTransaction(ACHTransactionDTO ACHTransactionDTO, DataControllerRequest request);
	
	/**
     * Invokes line of business service for Internal transfer to approve at the Core
     * @param referenceId
     * @return {@link ACHTransactionDTO}
     */
	public ACHTransactionDTO approveTransaction(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for Internal transfer to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link ACHTransactionDTO}
     */
	public ACHTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ach transaction to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link ACHTransactionDTO}
	 */
	public ACHTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ach transaction to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link ACHTransactionDTO}
	 */
	public ACHTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);

	/**
	 * fetches transaction entry from achtransaction table for the given confirmationNumber
	 * @param confirmationNumber
	 * @param companyIds
	 * @param createdby
	 * @return {@link ACHTransactionDTO}
	 */
	public ACHTransactionDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds,
			String createdby);

	public List<ApprovalRequestDTO> fetchACHTransactionsForApprovalInfo(String filter, DataControllerRequest dcr);

	/**
	 * Method to update status in dbx infinity tables using transactionId
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return {@link ACHTransactionDTO}
	 */
	public ACHTransactionDTO updateStatusUsingTransactionId(String transactionId, String status, String confirmationNumber);
	
}
