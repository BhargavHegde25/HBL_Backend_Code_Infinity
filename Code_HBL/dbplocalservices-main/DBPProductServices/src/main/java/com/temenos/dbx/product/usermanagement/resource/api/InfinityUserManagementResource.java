package com.temenos.dbx.product.usermanagement.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

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
    public Result getAssociatedCustomers(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodId
     * @param inputArray
     * @param request
     * @param response
     * @return Result of relative customers
     * @throws ApplicationException
     */
    public Result getAllEligibleRelationalCustomers(String methodId, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    public Object createInfinityUser(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Object editInfinityUser(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Object getInfinityUser(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    /**
     * 
     * @param methodId
     * @param inputArray
     * @param request
     * @param response
     * @return Feature and Limits for the core customer id
     * @throws ApplicationException
     */
    public Result getCoreCustomerFeatureActionLimits(String methodId, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodId
     * @param inputArray
     * @param request
     * @param response
     * @return
     * @throws ApplicationException
     */
    public Result getCoreCustomerInformation(String methodId, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodId
     * @param inputArray
     * @param request
     * @param response
     * @return contract and core customer information for the which the logged in user has access
     * @throws ApplicationException
     */
    public Result getInfinityUserContractCustomerDetails(String methodId, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return customRoleId
     */
    public Result createCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return customRoleId
     */
    public Result editCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return customRoleId
     */
    public Result verifyCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return customRoleId
     */
    public Result getCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return actions associated to the CIF for the logged in user
     * @throws ApplicationException
     */
    public Result getInfinityUserContractCoreCustomerActions(String methodID, Object[] inputArray,
            DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    public Result getCustomRoleByCompanyID(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    public Result getCompanyLevelCustomRoles(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    public Result getAssociatedContractUsers(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return fetches the contract details for the infinity user id
     * @throws ApplicationException
     */
    public Result getInfinityUserBasedContractDetails(String methodID, Object[] inputArray,
            DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return
     * @throws ApplicationException
     */
    public void createAUserAndAssignTOGivenContract(String methodID, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) throws ApplicationException;

    /**
     * 
     * @param methodId
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    public Object createInfinityUserWithContract(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return infinity user accounts*@throws ApplicationException
     **/

    public Result getInfinityUserAccountsForAdmin(String methodID, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return fetches the infinity user features and actions
     * @throws ApplicationException
     */
    public Result getInfinityUserFeatureActions(String methodID, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return fetches the infinity user limits
     * @throws ApplicationException
     */
    public Result getInfinityUserLimits(String methodID, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return generation of activationCode and username status
     * @throws ApplicationException
     */
    public Result generateInfinityUserActivationCodeAndUsername(String methodID, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    public Object createRetailContract(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

    /**
     * 
     * @param methodId
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    Object editInfinityUserWithContract(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodId
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    public Object applyCustomRole(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    public Result UpdateInfinityUserStatus(String methodID, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) throws ApplicationException;

    /**
     * Auto syncing the new opened acounts
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return
     * @throws ApplicationException
     */
    public Result processOpenedNewAccounts(String methodID, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    public Result assignInfinityUserToPrimaryRetailContract(String methodID, Object[] inputArray,
            DataControllerRequest request,
            DataControllerResponse response);

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return
     * @throws ApplicationException
     */
    public Result getUserApprovalPermissions(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return
     * @throws ApplicationException
     */
    public Result enrollRetailUserOperation(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    public Result createBusinessContract(String methodID, Object[] inputArray,
            DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;
}
