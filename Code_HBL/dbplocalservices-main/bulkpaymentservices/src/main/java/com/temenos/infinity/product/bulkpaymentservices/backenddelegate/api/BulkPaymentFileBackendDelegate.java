package com.temenos.infinity.product.bulkpaymentservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentFileDTO;

public interface BulkPaymentFileBackendDelegate extends BackendDelegate{
	/**
	 * method to execute Bulk Payment File At Backend(Calls BulkPayments Vendor Mock Service)
	 * @param BulkPaymentFileDTO bulkPaymentFileDTO - contains file details
	 * @return BulkPaymentFileDTO 
	 */
	public BulkPaymentFileDTO uploadBulkPaymentFile(BulkPaymentFileDTO bulkPaymentfileDTO, DataControllerRequest dcr);
	
	/**
	 * method to fetch Bulk Payment Sample Files
	 * @param DataControllerRequest
	 * @return List of BulkPaymentFileDTO contains all sample files details
	 */
	public List<BulkPaymentFileDTO> fetchBulkPaymentSampleFiles(DataControllerRequest dcr);
	
	/**
	 * method to fetch Bulk Payment Uploaded Files
	 * @param DataControllerRequest
	 * @return List of BulkPaymentFileDTO contains all uploaded files details
	 */
	public List<BulkPaymentFileDTO> fetchBulkPaymentUploadedFiles(String fromDate, String toDate,String userSignOnName ,DataControllerRequest dcr);
	
}
