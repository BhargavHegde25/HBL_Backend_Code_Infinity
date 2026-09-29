package com.temenos.dbx.product.limitsandpermissions.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface LimitsAndPermissionsResource extends Resource {
	
	/**
     *  This method updates the limits of service definition and its referenced contracts, customers
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to update an existing service definition limits
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains acknowledgement for updated service definition
     */

    public Result UpdateServiceDefinitionLimits(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);
    
    /**
     *  This method updates the limits of service definition and its referenced contracts, customers
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to update an existing service definition limits
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains acknowledgement for updated service definition
     */

    public Result UpdateCustomerRoleLimitsAndPermissions(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);

}
