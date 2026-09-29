package com.temenos.dbx.party.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CustomerResource extends Resource {

	public Result save(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result createPartyClassification(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse, boolean isUpdate);
	
	public Result createPartyRelationship(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result update(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
    public Result getReferenceByID(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);
    
    public Result createIdentifier(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

	public Result get(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result createEmployments(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse, boolean b) throws HttpCallException;
	
	public Result updateIdentifier(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);
    
}
