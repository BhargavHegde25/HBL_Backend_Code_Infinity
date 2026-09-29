package com.temenos.dbx.product.achservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * This Class takes in the request payload from JavaService(Integration Layer)
 * and massages the data as per required by Business Delegate contracts to fetch
 * ACH File Records
 *
 */
public interface ACHFileRecordResource extends Resource {
	
	/**
	 * This method takes user input and fetches ACHFileRecords
	 * @param inputArray  contains the input parameter to fetch ach file records - achFileId should be present
	 * @param methodID
	 * @param dcRequest
	 * @param dataControllerResponse
	 * @return Result object containing ACH File Records
	 */
	public Result fetchACHFileRecords(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dataControllerResponse);

}
