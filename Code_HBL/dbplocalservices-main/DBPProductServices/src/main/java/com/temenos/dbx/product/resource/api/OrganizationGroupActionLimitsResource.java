package com.temenos.dbx.product.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2627
 * @version 1.0 Interface for OrganizationGroupActionLimitsResource extends {@link Resource}
 *
 */

public interface OrganizationGroupActionLimitsResource extends Resource {

    /**
     * method to fetch Approval matrix records
     * 
     * @param methodID
     *            operationName
     * @param inputArray
     *            input parameters
     * @param request
     *            DataControllerRequest
     * @param response
     *            DataControllerResponse
     * @return Result containing all the organization and group action limits
     * 
     */
    public Result getOrganizationGroupActionLimits(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);
}
