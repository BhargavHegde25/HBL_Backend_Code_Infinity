package com.kony.adminconsole.service.contract.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ContractResource extends Resource {

    /**
     * This method gets the contract details
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to fetch existing contract
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the contract details
     */
    public Result getContractDetails(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * This method creates a contract
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to create a contract
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the contract details
     */
    public Result createContract(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;

    /**
     * This method gets all the contract action limits
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to fetch existing contract action limits
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains all the contract details
     */
    public Result getContractFeatureActionLimits(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    /**
     * This method edits a contract
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to edit a contract
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the contract details
     */
    public Result editContract(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;

    /**
     * This method updates status of a contract
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to search a contract
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the contract details
     */
    public Result updateContractStatus(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
    
    /**
     * This method searches a contract
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to search a contract
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the contract details
     */
    public Result getListOfContractsByStatus(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;
    
    /**
     * This method searches a contract
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to search a contract
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the contract details
     */
    public Result searchContract(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;

    /**
     * This method gets the core customer accounts
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to search a contract
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the contract details
     */
    public Result getCoreCustomerAccounts(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;

    /**
     * This method gets the core customer relative customers
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to search related customers to a core customer
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the contract details
     */
    public Result getCoreRelativeCustomers(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;

    /**
     * This method gets the core customers
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to search core customers
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the customer details
     */
    public Result searchCoreCustomers(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception;

    /**
     * This method gets the contract infinity users
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter contract id
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the infinity users associated to contract
     */
    public Result getContractInfinityUsers(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception;
    
    /**
     * This method gets the contract related accounts
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter contract id
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the infinity users associated to contract
     */
    public Result getContractAccounts(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception;   

    /**
     * This method gets the core customer details
     * 
     * @author
     * @version 1.0
     * @param methodId
     *            contains the operation id
     * @param inputArray
     *            contains the input parameter to search core customers
     * @param request
     *            contains request handler
     * @param response
     *            contains the response handler
     * @return Result object contains the core customer details
     */
    public Result getCoreCustomerDetails(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception;

}
