/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

import java.io.IOException;
import java.util.HashMap;

public interface IssuedGuaranteeClaimsResource extends Resource {
    Result createClaim(String methodId, HashMap<String, Object> inputParams, DataControllerRequest request) throws IOException;
    Result updateClaim(String methodId, HashMap<String, Object> inputParams,
                             DataControllerRequest request) throws IOException;
    Result updateClaimByBank(String methodId, HashMap<String, Object> inputParams,
                             DataControllerRequest request) throws IOException;
	Result generateIssuedGuaranteeClaim(DataControllerRequest request);

    Result getClaims(HashMap<String, Object> inputParams, DataControllerRequest dataControllerRequest);

    Result getClaimById(HashMap<String, Object> inputParams, DataControllerRequest dataControllerRequest);
}
