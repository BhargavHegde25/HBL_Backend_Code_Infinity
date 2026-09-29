package com.temenos.dbx.product.transactionservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface BulkWiresResource extends Resource {

	/**
	 * (INFO)Based on whether the user type is "Retail" or "Small Business",
	 * (INFO)the bulk wire files are queried from table based on "user_name" or
	 * "organisation_id" of the user respectively Create query parameters from input
	 * and calls business delegate method to fetch bulk wires
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2347
	 */
	Result getBulkWiresForUser(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * uploading a bulk wire templates file of the supported format with valid
	 * recipients
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2144
	 */
	Result uploadBWTemplateFile(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance);

	/**
	 * Create BulkWireTemplate
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	Result createBulkWireTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * Update BulkWireTemplate
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	Result updateBulkWireTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	 /**
	 * (INFO)the bulk wire template line items are queried from table based on "bulkWireTemplateID" respectively
	 * Fetches the file line items by calling business delegate method to fetch wire template line items
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2347
	 */
	Result getBulkWireTemplateLineItems(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * Delete BulkWireTemplate
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	Result deleteBulkWireTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * Delete BulkWireTemplate Recipient
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	Result deleteBulkWireTemplateRecipient(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * Get Unselected Payees For BulkWire Template
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 */
	Result getUnselectedPayeesForBWTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
}
