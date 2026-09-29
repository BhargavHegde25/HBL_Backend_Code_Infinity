package com.temenos.infinity.api.accountsweeps.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;

import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author naveen.yerra
 */
public interface AccountSweepsResource extends Resource {
    Result getSweepByAccountId(HashMap<String, Object> inputParams, DataControllerRequest controllerRequest);
    Result createSweep(String methodID, Object[] inputArray, DataControllerRequest request,
                       DataControllerResponse response);

    Result getAccountSweeps(FilterDTO filterDTO, DataControllerRequest request);
    Result editSweep(Map<String, Object> inputParams, DataControllerRequest request);
    Result deleteSweep(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws IOException;
}
