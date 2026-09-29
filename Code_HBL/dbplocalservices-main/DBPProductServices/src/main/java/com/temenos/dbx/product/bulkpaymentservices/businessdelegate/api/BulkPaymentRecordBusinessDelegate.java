package com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;

public interface BulkPaymentRecordBusinessDelegate extends BusinessDelegate{
	
	/**
	 * method to fetch Bulk Payment Record At DBX
	 * @param recordId  
	 * @return BulkPaymentFileDTO
	 */
	public List<BulkPaymentRecordDTO> fetchBulkPaymentRecords(Set<String> recordIds);
	
	/**
	 * method to create Bulk Payment Record At DBX
	 * @param BulkPaymentFileDTO  
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentRecordDTO createBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO);
	
	/**
	 * method to update Bulk Payment Record At DBX
	 * @param bulkPaymentRecordDTO
	 * @return
	 */
	public BulkPaymentRecordDTO editBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO);
	
	/**
	 * method to delete bulk payment record at DBX
	 * @param bulkPaymentRecordId
	 * @return
	 */
	public boolean deleteBulkPaymentRecord(String bulkPaymentRecordId);
	
	/**
	 * method to update Bulk Payment Record At DBX
	 * @param BulkPaymentFileDTO  
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentRecordDTO updateBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO);
	
	/**
	 * method to update request id at DBX.
	 * @param recordId
	 * @param requestId
	 * @return
	 */
	public BulkPaymentRecordDTO updateRequestId(String recordId, String requestId);
	
	/**
	 * method to update status at DBX
	 * @param transactionId
	 * @param status
	 * @param confirmationNumber
	 * @return
	 */
	public BulkPaymentRecordDTO updateStatus(String recordId, String status, String paymentId, String rejectionreason);
	
	/**
	 * method to fetch Bulk Payment Record At DBX
	 * @param recordId  
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentRecordDTO fetchBulkPaymentRecordDetailsById(String recordId);
	
	/**
	 * method to fetch Bulk Payment Record At DBX by using backendId
	 * @param confirmationNumber
	 * @return
	 */
	public BulkPaymentRecordDTO fetchBulkPaymentRecordBybackendId(String confirmationNumber);
	
	/**
	 * method to approve Bulk Payment Records
	 *
	 * @param long   requestId
	 * @param String comments
	 * @param String customerId
	 * @param String companyId
	 * @param dcr
	 * @return BBRequestDTO
	 */
	public Object approveBulkPaymentRecord(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr);
	
	/**
	 * executes the given transaction after approval
	 * @param transactionId
	 */
	public void executeRecordAfterApproval(String recordId, DataControllerRequest dcrequest, DataControllerResponse dcresponse, Result result);
	
	/**
	 * method to fetch Ongoing Bulk Payment Records
	 * @param fromAccounts Set<String>
	 * @param DataControllerRequest request
	 * @return List of BulkpaymentRecordDTO
	 */
	public List<BulkPaymentRecordDTO> fetchOnGoingPaymentsfromBackend(Set<String> fromAccounts, String batchMode, String requiredDate, String currentDate, DataControllerRequest request);

	/**
	 * method to fetch Bulk Payment Record History
	 * @param fromAccounts Set<String>
	 * @param DataControllerRequest request
	 * @return List of BulkpaymentRecordDTO
	 */
	public List<BulkPaymentRecordDTO> fetchBulkPaymentHistoryfromBackend(Set<String> fromAccounts, String fromDate, String toDate, DataControllerRequest request);
	

	/**
	 * method to fetch Bulk Payment Record 
	 * @param fromAccounts Set<String>
	 * @param DataControllerRequest request
	 * @return List of BulkpaymentRecordDTO
	 */
	public List<BulkPaymentRecordDTO> fetchRecordsFromBackend(Set<String> backendrecordIdList, DataControllerRequest dcr);
	
	/**
	 * method to fetch Bulk Payment Record by requestId
	 * @param requestId String
	 * @param DataControllerRequest request
	 * @return BulkpaymentRecordDTO
	 */
	public BulkPaymentRecordDTO fetchBulkPaymentRecordByRequestId(String requestId, DataControllerRequest dcr);

	/**
	 * ADP-7058 - consolidated method for fetching all the records with approval info (invoked by ApprovalQueueBusinessDelegate.fetchAdditionalBackendData)
	 * @param bbRequests
	 * @param dcr
	 * @return List<BulkPaymentRecordDTO>
	 * @author sourav.raychaudhuri
	 */
	public List<BulkPaymentRecordDTO> fetchAllRecordsFromBackendWithApprovalInfo(List<BBRequestDTO> bbRequests, DataControllerRequest dcr);

}
