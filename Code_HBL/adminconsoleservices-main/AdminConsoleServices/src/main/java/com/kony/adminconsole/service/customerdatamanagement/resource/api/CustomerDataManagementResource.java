package com.kony.adminconsole.service.customerdatamanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CustomerDataManagementResource extends Resource{
	public Result getSDPReport(String methodId, Object[] inputArray, DataControllerRequest request,
             DataControllerResponse response) throws Exception;

    Result triggerErasure(DataControllerRequest dataControllerRequest);

    public Result updateCustomerErasureStatus(String methodId, Object[] inputArray, DataControllerRequest request,
             DataControllerResponse response) throws Exception;

    public Result applicationPurge(String methodId, Object[] inputArray, DataControllerRequest request,
                                    DataControllerResponse response) throws Exception;
}
