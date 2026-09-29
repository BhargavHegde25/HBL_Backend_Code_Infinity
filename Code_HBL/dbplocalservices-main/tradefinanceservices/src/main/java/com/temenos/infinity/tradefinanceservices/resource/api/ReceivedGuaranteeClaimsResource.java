/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteeClaimsDTO;

import java.util.HashMap;

public interface ReceivedGuaranteeClaimsResource extends Resource {

    Result createClaim(String methodId, HashMap<String, Object> inputParams, DataControllerRequest request);

    Result getClaims(HashMap<String, Object> inputParams, DataControllerRequest request);

    Result getClaimsById(HashMap<String, Object> inputParams, DataControllerRequest request);

    Result generateReceivedGuaranteeClaim(DataControllerRequest request);

    Object updateClaimByBank(ReceivedGuaranteeClaimsDTO inputParams, DataControllerRequest dataControllerRequest);
}
