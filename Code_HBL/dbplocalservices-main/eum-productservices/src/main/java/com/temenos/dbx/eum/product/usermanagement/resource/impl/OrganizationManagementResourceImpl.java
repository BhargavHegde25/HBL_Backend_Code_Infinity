package com.temenos.dbx.eum.product.usermanagement.resource.impl;

import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.resource.api.OrganizationUserManagementResource;

public class OrganizationManagementResourceImpl implements OrganizationUserManagementResource {

    @Override
    public Result checkIfMemberExists(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        return new Result();
    }

}
