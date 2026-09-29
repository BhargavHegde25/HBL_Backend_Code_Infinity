package com.kony.adminconsole.service.termandcondition.resource.api;

import com.dbp.core.api.Resource;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface TnCResource extends Resource {

	public Result createTermsAndConditionsVersion(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;
	
	public Result editTermsAndConditions(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result deleteTermsAndConditionsVersion(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getAllTermsAndConditions(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getTermsAndConditions(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getRequiredTermsAndConditions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
