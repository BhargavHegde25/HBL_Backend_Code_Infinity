package com.temenos.dbx.product.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface OrganisationEmployeesResource extends Resource {
    public Result checkIfOrgUserExists(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

}
