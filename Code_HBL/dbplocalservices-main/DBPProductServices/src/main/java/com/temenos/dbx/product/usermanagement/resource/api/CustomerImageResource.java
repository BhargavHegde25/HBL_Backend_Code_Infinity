package com.temenos.dbx.product.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2627
 * @version 1.0 Interface for CustomerImageResource extends {@link Resource}
 *
 */

public interface CustomerImageResource extends Resource {

	public Result getCustomerImage(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result deleteCustomerImage(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result updateCustomerImage(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
}
