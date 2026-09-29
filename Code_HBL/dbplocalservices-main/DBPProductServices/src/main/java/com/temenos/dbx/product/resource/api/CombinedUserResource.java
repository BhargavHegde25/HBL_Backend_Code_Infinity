package com.temenos.dbx.product.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CombinedUserResource extends Resource{

	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param dcRequest
	 * @param dcResponse
	 * @return retailUserPermissions and BusinessUserPermissions
	 * @throws ApplicationException 
	 */
	public Result getCombinedUserPermissions(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
}
