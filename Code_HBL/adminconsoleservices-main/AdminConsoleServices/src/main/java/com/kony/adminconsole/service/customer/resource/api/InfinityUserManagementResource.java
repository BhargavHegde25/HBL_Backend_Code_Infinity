package com.kony.adminconsole.service.customer.resource.api;

import com.dbp.core.api.Resource;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author srirama.sadineni
 *
 */
public interface InfinityUserManagementResource extends Resource {

    /**
     * 
     * @param methodId
     * @param inputArray
     * @param request
     * @param response
     * @return Result containing the customers with which the logged in user associated
     * @throws ApplicationException
     */
    public Result getAssociatedCustomers(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

    public Result getAllEligibleRelationalCustomers(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    public Result createInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    public Result editInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    public Result getInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result getCoreCustomerRoleFeatureActionLimits(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result getCoreCustomerProductRolesFeatureActionLimits(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result getRelativeCoreCustomerContractDetails(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result getCoreCustomerContractDetails(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result getInfinityUserContractDetails(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result getInfinityUserAccounts(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result getInfinityUserFeatureActions(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result getInfinityUserLimits(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
    
    public Result getInfinityUserAccountsForCorecustomer(String methodId, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);

	public Result getInfinityUserServicedefsRoles(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);   

}
