package com.hbl.adminconsoleextn.api;

import org.json.JSONArray;

import com.kony.adminconsole.service.servicedefinition.resource.api.ServiceDefinitionResource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ServiceDefinitionResourceExtn extends ServiceDefinitionResource{
	public Result editServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
	 public boolean validateActions(String roleTypeId, JSONArray actionlimits, String legalEntityId);

}
