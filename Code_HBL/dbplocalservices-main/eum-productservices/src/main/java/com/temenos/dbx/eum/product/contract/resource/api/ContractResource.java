package com.temenos.dbx.eum.product.contract.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ContractResource extends Resource {

    /**
     * @since 20201.01
     * @version 1.0
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return
     * @throws ApplicationException
     */
    public Result createContract(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * @since 20201.01
     * @version 1.0
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return fetches the searched contracts
     * @throws ApplicationException
     */
    public Result searchContracts(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
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
    public Result getContractDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
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
    public Result editContract(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return Contract feature action limits
     * @throws ApplicationException
     */
    public Result getContractFeatureActionLimits(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return Infinity users of the contract
     * @throws ApplicationException
     */
    public Result getContractInfinityUsers(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return fetches the list of contracts based on status id
     * @throws ApplicationException
     */
    public Result getListOfContractsByStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return status of the contract status update
     * @throws ApplicationException
     */
    public Result updateContractStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
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
    public Result getCoreCustomerGroupFeatureActionLimits(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return contract details based on core customer id
     * @throws ApplicationException
     */
    public Result getCoreCustomerBasedContractDetails(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return contract details based on relative core customers for input core customer id
     * @throws ApplicationException
     */
    public Result getRelativeCoreCustomerBasedContractDetails(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return Fetches the contract accounts
     * @throws ApplicationException
     */
    public Result getContractAccounts(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    public boolean validateEnrollContractPayload(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException;
    
    public Result getCoreCustomerProductRolesFeatureActionLimits(String methodId, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response)throws ApplicationException;

    public Result getProductLevelPermissions(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

    public Result getServiceDefinitionProductPermissions(String methodId, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response)throws ApplicationException;

}
