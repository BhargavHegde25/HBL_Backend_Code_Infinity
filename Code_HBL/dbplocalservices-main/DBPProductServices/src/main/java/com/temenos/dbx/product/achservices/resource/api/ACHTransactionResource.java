package com.temenos.dbx.product.achservices.resource.api;

import org.json.JSONObject;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ACHTransactionResource extends Resource {
	/**
	 *@author KH2317
	 *@version 1.0
	 *@param inputArray contains the input parameter to create ACH transaction
	 *@param request contains request handler
	 *@param response contains response handler
	 ***/	
	Result createACHTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

	/**
	 * @author KH2626
	 * @version 1.0
	 * @param methodID contains id related to the invoked service
	 * @param inputArray contains request params
	 * @param request sdk request object
	 * @param response sdk response object
	 * @return list of ach transactions
	 * @throws Exception
	 */
	public Result fetchAllACHTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

	/**
	 * @author KH2626
	 * @version 1.0
	 * @param methodID contains id related to the invoked service
	 * @param inputArray contains request params
	 * @param request sdk request object
	 * @param response sdk response object
	 * @return details of ach transaction with specific id
	 * @throws Exception
	 */
	public Result fetchACHTransactionById(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	/**
	 * @author KH9450
	 * @version 1.0
	 * @param transactionID contains id related to the achtransaction we need the details of
	 * @return details of ach transaction along with records and subrecords with specific id
	 */
	public JSONObject fetchTranscationEntryWithRecordsAndSubRecords (String transactionId);
}
