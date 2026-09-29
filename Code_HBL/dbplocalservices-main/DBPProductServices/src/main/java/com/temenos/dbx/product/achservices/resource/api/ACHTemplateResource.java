package com.temenos.dbx.product.achservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ACHTemplateResource extends Resource{

	/**
	 *@author KH2626
	 *@version 1.0
	 *@param methodId contains the operation id
	 *@param inputArray contains the input parameter to create template
	 *@param request contains request handler
	 *@param response contains the response handler
	 *@return Result object contains newly created template id
	 */
	public Result createACHTemplate(String methodID, Object[] inputArray, DataControllerRequest request, 
									DataControllerResponse response);

	/**
	 * Method to execute Template for given template_id
	 * @author KH2624
	 * @param methodID has service name
	 * @param inputArray has array of input param objects
	 * @param request data controller request
	 * @param response date controller response
	 * @return  result object that has transaction_id of the executed template
	 */
	public Result executeTemplate(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);
	
	/**
	 * method to fetch template types.
	 *@author kh2304
	 *@version 1.0
	 *@param inputArray contains the input parameter to get template types
	 *@param dcRequest contains request handler
	 *@return Result object with Template Details
	 *
	 */
	public Result getACHTemplateType(Object[] inputArray, DataControllerRequest dcRequest);
	
	/**
	 *@author KH2626
	 *@version 1.0
	 *@param methodId contains the operation id
	 *@param inputArray contains the input parameter to fetch all templates
	 *@param request contains request handler
	 *@param response contains the response handler
	 *@return Result object contains details of all templates
	 */
	public Result fetchAllACHTemplates(String methodID, Object[] inputArray, DataControllerRequest request, 
									   DataControllerResponse response);
	/**
	 *This method will delete the ACHTemplate based on the template id sent as parameter.
	 *@author KH9439
	 *@version 1.0
	 *@param methodId contains the operation id
	 *@param inputArray contains the input parameter to delete template
	 *@param request contains request handler
	 *@param response contains the response handler
	 *@return Result object contains details of deleted template
	 */
	public Result deleteACHTemplate(String methodID, Object[] inputArray, DataControllerRequest request, 
									   DataControllerResponse response);
	/**
	 *This method will fetch the ACHTemplate based on the template id sent as parameter.
	 *@author KH9439
	 *@version 1.0
	 *@param methodId contains the operation id
	 *@param inputArray contains the input parameter to fetch template by ID.
	 *@param request contains request handler
	 *@param response contains the response handler
	 *@return Result object contains details of fetched template.
	 */
	public Result fetchACHTemplateById(String methodID, Object[] inputArray, DataControllerRequest request, 
									   DataControllerResponse response);
	
	/**
	 *This method will update the template based on request input data
	 *@author sharath.prabhala
	 *@version 1.0
	 *@param methodId contains the operation id
	 *@param inputArray contains the input parameter to update template
	 *@param request contains request handler
	 *@param response contains the response handler
	 *@return Result object contains details related to update template operation.
	 */
	public Result editACHTemplate(String methodID, Object[] inputArray, DataControllerRequest request, 
									   DataControllerResponse response);
}