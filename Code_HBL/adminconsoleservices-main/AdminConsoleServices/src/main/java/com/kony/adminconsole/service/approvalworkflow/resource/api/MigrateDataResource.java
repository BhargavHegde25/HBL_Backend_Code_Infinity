package com.kony.adminconsole.service.approvalworkflow.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MigrateDataResource extends Resource{
    public Result migrateData(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response) throws Exception;

}