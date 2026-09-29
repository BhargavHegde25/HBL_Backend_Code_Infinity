package com.temenos.dbx.product.payeeservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ExternalPayeeResource extends Resource {

    /**
     * fetches all external payees/accounts
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    public Result fetchAllMyPayees(String methodID, Object[] inputArray, DataControllerRequest request,
                               DataControllerResponse response);

    /**
     * calls respective delete payee method based on isSameBankAccount and isInternationalAccount parameters
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    public Result deletePayee(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

    /**
     * calls respective edit payee method based on isSameBankAccount and isInternationalAccount parameters
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    public Result editPayee(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

    /**
     * calls respective create payee method based on isSameBankAccount and isInternationalAccount parameters
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     */
    public Result createPayee(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
    
}
