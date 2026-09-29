package com.temenos.dbx.product.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 * Business Resource Interface
 * 
 */
public interface BusinessTypeResource extends Resource{
	
	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param dcRequest
	 * @param dcResponse
	 * @return Result containing all business type
	 * @throws ApplicationException 
	 */

	public Result getBusinessTypes(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
	
	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param dcRequest
	 * @param dcResponse
	 * @return Roles associated with the Business type
	 * @throws ApplicationException 
	 *
	 */
	public Result getBusinessTypeRoles(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
}
