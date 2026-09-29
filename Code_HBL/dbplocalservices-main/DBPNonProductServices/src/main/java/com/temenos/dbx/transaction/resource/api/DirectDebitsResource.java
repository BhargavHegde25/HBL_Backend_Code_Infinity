package com.temenos.dbx.transaction.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author sribarani.vasthan
 */
public interface DirectDebitsResource extends Resource {
	public Result getDirectDebits(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result cancelDirectDebit(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
