package com.temenos.infinity.api.holdings.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface DisputeTransactionResource extends Resource {

	public Result crearteDisputeCoreTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result crearteDisputeBillPayTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result crearteDisputePayAPersonTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result crearteDisputeCardTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
			
	public Result getDisputeTransactionRequests(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
 
	public Result createDisputeTransaction(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
