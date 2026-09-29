package com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;

public interface BulkPaymentRecordBackendDelegate extends BackendDelegate{
	
	/**
	 * method to fetch bulk Payment record from mock table.
	 * @param dcr
	 * @param recordId
	 * @return BulkPaymentRecordDTO
	 */
	public BulkPaymentRecordDTO fetchBulkPaymentRecordDetailsById(String recordId, DataControllerRequest dcr);
	
	/**
	 * method to cancel Bulk Payment Record
	 * @param recordId, comments
	 * @param filterDTO, recordId, DataControllerRequest dcr 
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentRecordDTO cancelBulkPaymentRecord(String bulkPaymentRecordId, String comments, String cancellationreason, String cancellationReasonId, String statusCode, DataControllerRequest dcr);
	
	/**
	 * method to create Bulk Payment Record
	 * @param BulkPaymentFileDTO  
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentRecordDTO createBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO);
	
	/**
	 * method to delete Bulk Payment Record
	 * @param bulkPaymentRecordId  
	 * @return boolean
	 */
	public boolean deleteBulkPaymentRecord(String bulkPaymentRecordId);
	
	/**
	 * method to update Bulk Payment Record
	 * @param BulkPaymentFileDTO  
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentRecordDTO updateBulkPaymentRecord(BulkPaymentRecordDTO bulkPaymentRecordDTO);

	/**
	 * method to get onGoing Bulk Payment Record
	 * @param fromAccounts Set<String>
	 * @param   String CompanyId and customerId
	 * @return List of BulkPaymentFileDTO
	 */
	public List<BulkPaymentRecordDTO> fetchOnGoingPayments(Set<String> fromAccounts, String batchMode, String fromDate, String toDate, DataControllerRequest request);
	
    /**
     * Method to initiate bulk payment
     * @param bulkPaymentRecordDTO
     * @param dcr
     * @return BulkPaymentRecordDTO
     */
	public BulkPaymentRecordDTO initiateBulkPayment(BulkPaymentRecordDTO bulkPaymentRecordDTO, DataControllerRequest dcr);

	/**
	 * method to get Bulk Payment History
	 * @param fromAccounts Set<String>
	 * @return List of BulkPaymentRecordDTO
	 */
	public List<BulkPaymentRecordDTO> fetchBulkPaymentHistory(Set<String> fromAccounts, String fromDate, String toDate, DataControllerRequest dcr);

	 /**
     * Method to update bulk payment
     * @param recordId, fromAccount, description and dcr
     * @return BulkPaymentRecordDTO
     */
	public BulkPaymentRecordDTO updateBulkPaymentRecord(String recordId, String fromAccount, String description, DataControllerRequest request);

	 /**
     * Method to fetch bulk payment records by Id
     * @param List of recordIds and dcr
     * @return List of BulkPaymentRecordDTO
     */
	public List<BulkPaymentRecordDTO> fetchRecords(Set<String> recordIdList, DataControllerRequest request);
	
	/**
	 * method to update Bulk Payment Record Status
	 * @param recordId, status and dcr  
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentRecordDTO updateBulkPaymentRecordStatus(String recordId, String status, DataControllerRequest request);
	
	/**
	 * method to Reject Bulk Payment Record 
	 * @param recordId and dcr  
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentRecordDTO rejectBulkPaymentRecord(String recordId, String comments, String rejectionreason, DataControllerRequest request);
}
