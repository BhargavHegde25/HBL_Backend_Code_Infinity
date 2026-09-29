package com.temenos.dbx.eum.product.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CustomerAddressResource extends Resource {

    public Result getCustomerAddress(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

	public Object getCustomerAddressForUserResponse(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse);

}
