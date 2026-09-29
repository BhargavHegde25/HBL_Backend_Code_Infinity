package com.temenos.dbx.product.achservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * This Class takes in the request payload from JavaService(Integration Layer)
 * and massages the data as per required by Business Delegate contracts to fetch
 * ACH File Sub Records
 *
 */
public interface ACHFileSubRecordResource extends Resource{
	
	/**
	 * This method takes user input and fetches ACHFile SubRecords
	 * @param inputArray contains the input parameter to fetch ach file sub records - achFileRecordId should be present
	 * @param dcRequest
	 * @param dcResponse
	 * @return Result object containing ACH File Subrecords
	 */
	public Result fetchACHFileSubrecords(String methodId, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse);
}
