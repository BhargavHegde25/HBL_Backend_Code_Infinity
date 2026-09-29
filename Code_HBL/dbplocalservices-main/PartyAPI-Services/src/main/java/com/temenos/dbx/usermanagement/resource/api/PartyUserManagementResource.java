package com.temenos.dbx.usermanagement.resource.api;


import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface PartyUserManagementResource extends Resource {

	public Result partyGet(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result partyUpdate(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result partyCreate(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result GetPartyData(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);



}
