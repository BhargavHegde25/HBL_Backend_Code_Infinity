package com.temenos.dbx.eum.product.contract.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CoreCustomerResource extends Resource {

    /**
     * @author sowmya.vandanapu
     * @version 1.0
     * @since 2021.01
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return result of searched customers
     * @throws ApplicationException
     */
    public Result searchCoreCustomers(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * @author sowmya.vandanapu
     * @version 1.0
     * @since 2021.01
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return result of customer details and relative customers
     * @throws ApplicationException
     */
    public Result getCoreRelativeCustomers(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * @author sowmya.vandanapu
     * @version 1.0
     * @since 2021.01
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return result of customer accounts
     * @throws ApplicationException
     */
    public Result getCoreCustomerAccounts(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
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
    public Result checkCoreRelativeCustomerForCompany(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    public Result getCompanyAccounts(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException;

}
