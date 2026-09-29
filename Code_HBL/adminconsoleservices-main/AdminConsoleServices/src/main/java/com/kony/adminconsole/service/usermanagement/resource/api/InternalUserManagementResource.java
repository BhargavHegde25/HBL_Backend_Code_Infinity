package com.kony.adminconsole.service.usermanagement.resource.api;
import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface InternalUserManagementResource extends Resource {

    public Result createInternalUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;

    public Result editInternalUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;

    public Result getInternalUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
    
    public Result downloadUsersList(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
    
    public Result updateUserStatus(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
    
    public Result getUserProfile(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
    
    public Result manageUserCompositeActions(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
    
    public Result getUserList(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
}
