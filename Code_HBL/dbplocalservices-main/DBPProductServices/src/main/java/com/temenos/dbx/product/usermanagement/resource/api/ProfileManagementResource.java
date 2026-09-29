package com.temenos.dbx.product.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.DBXResult;

public interface ProfileManagementResource extends Resource {

	public Result getUserDetailsToAdmin(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result getCustomerForUserResponse(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result getUserDetailsConcurrent(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	public Result createCustomer(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result updateProfile(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	public Result searchCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	public Result getBasicInformation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	public Result get(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result getByUserName(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result checkifUserEnrolled(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    public Object sendCustomerUnlockEmail(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    public Result getAddressTypes(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    
    public Object createRetailContract(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

    public Result getUserResponseForAlerts(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);
}
