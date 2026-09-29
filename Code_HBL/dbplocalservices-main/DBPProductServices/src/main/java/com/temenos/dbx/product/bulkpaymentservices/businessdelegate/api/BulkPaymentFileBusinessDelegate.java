package com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api;

import java.io.File;
import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;

public interface BulkPaymentFileBusinessDelegate extends BusinessDelegate{
	
	/**
	 * method to create Bulk Payment File At DBX
	 * @param BulkPaymentFileDTO  
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentFileDTO uploadBulkPaymentFileAtDBX(BulkPaymentFileDTO bulkPaymentfileDTO);
	
	/**
	 * method to parse Bulk Payments File
	 * @param File  
	 * @return BulkPaymentFileDTO 
	 * @throws ApplicationException 
	 */
	public BulkPaymentFileDTO parseFile(File file) throws ApplicationException;
	
	/**
	 * method to fetch Bulk Payment Header Information  
	 * @param File  
	 * @return BulkPaymentRecordDTO 
	 * @throws ApplicationException 
	 */
	public BulkPaymentRecordDTO fetchBulkPaymentRecord(File file) throws ApplicationException;

	/**
	 * method to fetch Bulk Payment Files Information  
	 * @param CustomerId and ComapnyId  
	 * @return List of BulkPaymentFilwDTO 
	 * @throws ApplicationException 
	 */
	public List<BulkPaymentFileDTO> fetchBulkPaymentFiles(String customerId, List<String> companyIds);
	
	/**
	 * method to fetch Bulk Payment Files Information  
	 * @param DataControllerRequest
	 * @return List of BulkPaymentFileDTO 
	 */
	public List<BulkPaymentFileDTO> fetchBulkPaymentUploadedFilesfromBackend(String fromDate, String toDate, DataControllerRequest request);
	
	/**
	 * method to fetch current date from server  
	 * @param DataControllerRequest
	 * @return date
	 */
	public String getCurrentDateForUpload(DataControllerRequest request);
	
	/**
	 * method to generate Bulk payment Pdf file 
	 * @param recordId
	 * @param customerId 
	 * @param DataControllerRequest
	 * @return Pdf file in byte array
	 */
	public byte[] getRecordPDFAsBytes(String recordId, String requestId, String customerId, DataControllerRequest request);	
}
