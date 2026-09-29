package com.temenos.dbx.datamigrationservices.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MigrateApprovalMatrixResource extends Resource {

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
    public Result createOrUpdateApprovalRule(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;
}
