package com.temenos.dbx.product.payeeservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface BulkPaymentsPayeeResource extends Resource {
	
	/**
	 *  method to getBeneficiaryNameFromAccountId
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Beneficiary Details
	 *  
	 */
	public Result getBeneficiaryNameFromAccountId(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);
	
	/**
	 *  method to Validate IBAN and Get Bank Details
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Bank Details
	 *  
	 */
	public Result validateIBANandGetBankDetails(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);
	
	/**
	 *  method to Get BankDetails From BIC
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Bank Details
	 *  
	 */
	public Result getBankDetailsFromBIC(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);
	
	/**
	 *  method to Get All BICs and BankDetails
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return Result Object with Bank Details
	 *  
	 */
	public Result getAllBICsAndBankDetails(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);
	

}
