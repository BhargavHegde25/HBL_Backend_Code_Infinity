package com.temenos.dbx.product.approvalservices.businessdelegate.api;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBActedRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.RequestHistoryDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;

/**
 * 
 * @author KH2174
 * @version 1.0
 * Interface for ApprovalRuleResource extends {@link BusinessDelegate}
 *
 */
public interface ApprovalQueueBusinessDelegate extends BusinessDelegate {

	/**
	 * Adds the given transaction to approval queue (bbrequest table)
	 * @param BBRequestDTO
	 * @return requestId of the entry
	 */
	public String addTransactionToApprovalQueue(BBRequestDTO bbrequestDTO);
	
	/**
	 * Updates the given transaction in approval queue (bbrequest table)
	 * @param bbrequestDTO
	 * @return
	 */
	public String updateTransactionInApprovalQueue(BBRequestDTO bbrequestDTO);
	
	/**
	 * Deletes the given transaction from approval queue (bbrequest table)
	 * @param requestId
	 * @return true/false
	 */
	public boolean deleteTransactionFromApprovalQueue(String requestId);

	/**
	 * @author KH2626 
	 * @version 1.0
	 * @param requestId is used to identify all the actions performed on a transaction
	 * @return list of actions
	 **/
	public List<RequestHistoryDTO> fetchRequestHistory(String requestId, String customerId);

	/**
	 * method to update receivedstatus counter by 1
	 *
	 * @param String requestId
	 * @param int    counter
	 * @param dcr
	 * @return BBRequestDTO
	 */
	public BBRequestDTO updateBBRequestCounter(String requestId, int counter, DataControllerRequest dcr);

	/**
	 * method to update status of record
	 *
	 * @param long   requestId
	 * @param String status
	 * @param dcr
	 * @return BBRequestDTO
	 */
	public BBRequestDTO updateBBRequestStatus(String requestId, String status, DataControllerRequest dcr);

	/**
	 * method to log into bbActedRequest
	 * 
	 * @param long 
	 *            requestId
	 * @param String 
	 *            companyId
	 * @param String
	 *            status
	 * @param String
	 *           comments
	 * @param String
	 *            createdby
	 * @param String
	 *            actions
	 * @return Boolean
	 */
	public boolean logActedRequest(String requestId,String companyId, String status,String comments,String createdby,String actions);

	public boolean logActedRequest(String requestId,String companyId, String status,String comments,String createdby,String actions, String groupName);
	
	/**
	 * fetches the request history of all the requests acted by the given user.
	 * @param customerId
	 * @param requestIds
	 * @return
	 */
	public List<BBActedRequestDTO> fetchRequestHistory(String customerId, Set<String> requestIds);
	
	/**
	 * method to approve ACH Transaction
	 *
	 * @param long   requestId
	 * @param String comments
	 * @param String customerId
	 * @param String companyId
	 * @param dcr
	 * @return Object
	 */
	public Object approveACHTransaction(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr);

	/**
	 * method to approve ACH File
	 *
	 * @param long   requestId
	 * @param String comments
	 * @param String customerId
	 * @param String companyId
	 * @param dcr
	 * @return Object
	 */
	public Object approveACHFile(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr);

	/**
	 * method to approve General Transaction
	 *
	 * @param long   requestId
	 * @param String comments
	 * @param String customerId
	 * @param String companyId
	 * @param dcr
	 * @return Object
	 */
	public Object approveGeneralTransaction(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr);

	/**
	 * method to call authorizationCheckForRejectAndWithdrawl proc
	 * 
	 * @param long requestId
	 * @param String companyId
	 * @param String featureactionlist
	 * 
	 * @return BBRequestDTO
	 */
	BBRequestDTO authorizationCheckForRejectAndWithdrawl(String requestId, String companyId, String featureactionlist);

	/**
	 * Method to check If User Already Approved the transaction
	 * @param requestId
	 * @param customerId
	 * @return Boolean
	 */
	Boolean checkIfUserAlreadyApproved(String requestId, String customerId);

	/**
	 * Method to fetch request status
	 * @param requestId
	 * String Status
	 */
	String getRequestStatus(String requestId);

	/**
	 * All the Pending transaction will be auto rejected, when the approver of that is no longer valid
	 * If the transaction cannot meet the required approval requirement, then it will be auto rejected
	 * @param customerId
	 * @return
	 */
	public boolean autoRejectInvalidPendingTransactionsWhenApproverPermissionIsRevoked(String customerId);
	
	/**
	 * Method to call fetchRequest_proc 
	 * @param requestId
	 * @param customerId
	 * @return JSONArray of requestDetails
	 */
	public int callFetchRequestProc(String requestId, String customerId, String comments, String companyId);

	/**
	 * Method to call fetch_approvalqueue_proc 
	 * @param customerId, transactionId, requestId and featureActionList
	 * @return list of requestDTOs
	 */
	public List<BBRequestDTO> fetchRequests(String customerId, String transactionId, String requestId,
			String featureActionlist);

	/**
	 * Method to call fetch_approvers_proc 
	 * @param requestId
	 * @return JSONArray of Approvers
	 */
	public JSONArray fetchApprovers(String requestId, String customerId);

	/**
	 * Method to get BBRequest 
	 * @author sribarani.vasthan
	 * @param requestId String Status
	 * @return BBRequestDTO 
	 */
	public BBRequestDTO getBbRequest(String requestId); //Added as part of ADP-2810

	/**
	 * method to update transactionId
	 * @param String requestId
	 * @param String transactionId
	 * @return boolean - true if update is successful otherwise false
	 */	
	public boolean updateBBRequestTransactionId(String requestId, String transactionId);

	/**
	 * method to approve a transaction
	 * @param String requestId
	 * @param String comments
	 * @param String featureActionId
	 * @param DataControllerRequest request
	 * @return BBRequestDTO
	 */
	public BBRequestDTO approve(String requestId, String comments, String featureActionId, DataControllerRequest request);

	/**
	 * method to fetch the approval status of a transaction
	 * @param TransactionStatusDTO transactionstatus
	 * @param DataControllerRequest Request
	 * @return TransactionStatusDTO
	 */
	public TransactionStatusDTO validateForApprovals(TransactionStatusDTO transactionstatus,DataControllerRequest Request);
	
	/**
	 * method to update a transactionId/backendId for a given requestId
	 * @param String requestId
	 * @param String transactionId
	 * @param String isSelfApproved
	 * @param String featureActionId
	 * @param DataControllerRequest request
	 * @return TransactionStatusDTO
	 */
	public TransactionStatusDTO updateBackendIdInApprovalQueue(String requestId, String transactionId,  boolean isSelfApproved,  String featureActionId, DataControllerRequest request);
	
	/**
	 * method to approve cheque book request
	 *
	 * @param String requestId
	 * @param String customerId
	 * @param String comments
	 * @param String companyId
	 * @param dcr
	 * @return Object
	 */
	public Object approveChequeBookrequest(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr);
	
	/**
	 * method to execute Chequebook request After Approval.
	 * @param featureActionId
	 * @param dcr
	 */
	public void executeChequeBookRequestAfterApproval(String requestId,String transactionId, String featureActionId, DataControllerRequest dcr);

	/**
	 * method to withdraw the checkbook requested for a given requestId
	 * @param requestId
	 * @param comments
	 * @param request
	 * @return Boolean
	 */
	public Boolean withdrawRequest(String requestId, String comments, DataControllerRequest request);
	 /*
	  * * method to reject checkbook for a given requestId
	 * @param String requestId
	 * @param String comments
	 * @param DataControllerRequest request
	 * @return Boolean isRejected
	 */
	public Boolean rejectChequeBookRequest(String requestId, String comments, DataControllerRequest request);
	
	
	/**
	 * method to fetch ChequeBook Details for requests
	 * @param requests
	 * @param dcr
	 * @return List of ApprovalRequestDTO
	 */
	public List<ApprovalRequestDTO> fetchChequeBookInfo(List<BBRequestDTO> requests, DataControllerRequest request);
	
	/**
	 * 
	 * @param transactionId
	 * @param request
	 * @return
	 */
	public ApprovalRequestDTO fetchChequeBookDetails(String transactionId, DataControllerRequest request);
	
	/**
	 * Increments received approvals in requestapprovalmatrix table
	 * @param requestId
	 * @param approvalMatrixId
	 * @return boolean true - if successful otherwise false
	 */
	public boolean incrementReceivedApprovalsInRequestMatrix(String requestId, String approvalMatrixId);

	/**
	 * Increments received approvals in requestapprovalmatrix table
	 * @param requestApprovalMatrixId - comma seperated requestApprovalMatrixIds
	 * @return boolean true - if successful otherwise false
	 */
	public boolean updateRequestApprovalMatrix(String requestApprovalMatrixId);
	
	/**
	 * Method to call getRequestApprovers_proc 
	 * @param requestId
	 * @return JSONArray of Approver Ids
	 */
	public JSONArray fetchRequestApproverIds(String requestId);
 
	/**
	 * method to approve letter of credit
	 *
	 * @param String requestId
	 * @param String customerId
	 * @param String comments
	 * @param String companyId
	 * @param dcr
	 * @return Object
	 */
	public Object approveLetterOfCredit(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr);
	
	/**
	 * method to execute letter of credit request After Approval.
	 * @param featureActionId
	 * @param dcr
	 */
	public void executeLetterOfCreditAfterApproval(String requestId,String transactionId, String featureActionId, DataControllerRequest dcr);
	
	 /*
	 * method to reject letter of credit for a given requestId
	 * @param String requestId
	 * @param String comments
	 * @param DataControllerRequest request
	 * @return Boolean isRejected
	 */
	public Boolean rejectLetterOfCredit(String requestId, String comments, DataControllerRequest request);
	

	/**
	 * method to withdraw the letter of credit requested for a given requestId
	 * @param requestId
	 * @param comments
	 * @param request
	 * @return Boolean
	 */
	public Boolean withdrawLetterOfCredit(String requestId, String comments, DataControllerRequest request);
	
	public List<ApprovalRequestDTO> fetchLetterOfCreditDetails(List<BBRequestDTO> requests,
			DataControllerRequest dcr);

	/**
	 * @description - ADP-7058 - OVERLOAD - update the additional meta column for bbrequest
	 * @param requestId - the request Id string
	 * @param dcr
	 * @return true/false - if the update is a success or a failure
	 * @author - sourav.raychaudhuri
	 */
	public boolean updateAdditionalMetaForApprovalRequest(String requestId, DataControllerRequest dcr);

	/**
	 * @description - ADP-7058 - update the additional meta column for bbrequest
	 * @param bbRequest - the request Id string
	 * @param dcr
	 * @return true/false - if the update is a success or a failure
	 * @author - sourav.raychaudhuri
	 */
	public boolean updateAdditionalMetaForApprovalRequest(BBRequestDTO bbRequest, DataControllerRequest dcr);

	/**
	 * @desciption - ADP-7058 - OVERLOADED - fetch the additional t24 meta data to be stored in the bbrequest table
	 * @param requestId - bbrequest id
	 * @param dcr
	 * @return ApprovalRequestDTO - the t24 meta data consisting of the additional backend info
	 * @author - sourav.raychaudhuri
	 */
	public boolean updateAdditionalMetaForApprovalRequestInDB(ApprovalRequestDTO approvalRequestDTO);

	public ApprovalRequestDTO fetchAdditionalBackendData(String requestId, DataControllerRequest dcr);

	/**
	 * @desciption - ADP-7058 - fetch the additional t24 meta data to be stored in the bbrequest table
	 * @param bbRequest - bbrequest object
	 * @param dcr
	 * @return ApprovalRequestDTO - the t24 meta data consisting of the additional backend info
	 * @author - sourav.raychaudhuri
	 */
	public ApprovalRequestDTO fetchAdditionalBackendData(BBRequestDTO bbRequest, DataControllerRequest dcr);

	/**
	 * Method to call fetch_pending_approvalsqueue_proc 
	 * @param customerId, transactionId, requestId and featureActionList
	 * @return list of requestDTOs
	 */
	List<BBRequestDTO> fetchPendingRequests(String customerId, String transactionId, String requestId,
			String featureActionlist);

	Object approveNewActionTypes(String requestId, String customerId, String comments, String companyId,
			DataControllerRequest dcr);
}
