package com.temenos.auth.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface AuthUserManagementResource extends Resource {
	
	public Result getCustomerActiveLegalEntities(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse responseInstance) 
			throws ApplicationException;
	
	public Result getAllLegalEntities(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception;
	
	public Result getCustomerFeatureAndPermissions(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception;
	
	public Result updateTNCTimeStamp(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception;
}
