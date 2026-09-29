package com.temenos.dbx.eum.product.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface FeedBackStatusResource extends Resource {

    public Result updateStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

	public Object createStatus(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse);
	
	public Object getStatus(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse);

}
