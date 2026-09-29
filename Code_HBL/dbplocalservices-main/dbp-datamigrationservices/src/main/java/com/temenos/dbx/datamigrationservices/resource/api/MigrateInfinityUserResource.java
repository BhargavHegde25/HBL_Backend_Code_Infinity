package com.temenos.dbx.datamigrationservices.resource.api;

import org.json.JSONException;

import com.dbp.core.api.Resource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public interface MigrateInfinityUserResource extends Resource {
	public Object createUser(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception;
	
	public Object linkUserToContract(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception;

	public Object createVirtualUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Object createSignatoryGroup(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Object createPayee(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException;

	public Object editInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)
			throws JsonMappingException, JsonProcessingException, JSONException, ApplicationException;
}
