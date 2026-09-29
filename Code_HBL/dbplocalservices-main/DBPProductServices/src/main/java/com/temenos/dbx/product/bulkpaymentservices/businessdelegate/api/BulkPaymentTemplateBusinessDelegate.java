package com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentTemplateDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentTemplatePODTO;

public interface BulkPaymentTemplateBusinessDelegate extends BusinessDelegate{

	/**
	 * method to create Bulk Payment Template
	 * @param BulkPaymentTemplateDTO  
	 * @return BulkPaymentTemplateDTO
	 */
	BulkPaymentTemplateDTO createTemplate(BulkPaymentTemplateDTO bulkPaymentTemplateDTO);
	
	/**
	 * method to delete Bulk Payment Template
	 * @param BulkPaymentTemplate Id  
	 * @return boolean
	 */
	boolean deleteTemplate(String templateId);
	
	/**
	 * method to fetch Bulk Payment Template Information
	 * @param CustomerId and ComapnyId  
	 * @return List of BulkPaymentTemplateDTO 
	 */ 	 
	public List<BulkPaymentTemplateDTO> fetchTemplates(String customerId, List<String> companyIds);

	/**
	 * method to fetch Bulk Payment Template Information
	 * @param TemplateId 
	 * @return List of BulkPaymentTemplateDTO 
	 */ 
	public BulkPaymentTemplateDTO fetchTemplateById(String templateId);
	
	/**
	 * method to generate Bulk Payment Request File
	 * @param requestId 
	 * @return CSV file containing bulkpayement Request and Request POs associated with the request ID 
	 */ 
	public String generateCSV(String requestId);
	
	/**
	 * method to fetch Bulk Payment Template POs by templateId
	 * @param TemplateId 
	 * @return List of BulkPaymentTemplatePODTO 
	 */
	public List<BulkPaymentTemplatePODTO> fetchTemplatePOsByTemplateId(String templateId);
	
	/**
	 * method to create Bulk request
	 * @param BulkPaymentTemplateDTO and List of BulkPaymentTemplatePODTO
	 * @return BulkPaymentTemplateDTO
	 */
	public BulkPaymentTemplateDTO createTemplateRequest(BulkPaymentTemplateDTO templateDTO, List<BulkPaymentTemplatePODTO> paymentOrders);
	
	/**
	 * method to delete Bulk Payment Request
	 * @param payment request Id  
	 * @return boolean
	 */
	public boolean deleteTemplateRequest(String paymentrequestId);
	
	/**
	 * method to create Bulk Payment File for template
	 * @param BulkPaymentTemplateDTO , content, request  
	 * @return BulkPaymentFileDTO
	 */
	public BulkPaymentFileDTO uploadTemplateRequestAsFile(BulkPaymentTemplateDTO template, String content, DataControllerRequest dcr);
}
