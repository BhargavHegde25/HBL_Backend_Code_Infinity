/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteeClaimsDTO;

import java.util.HashMap;
import java.util.List;

public interface ReceivedGuaranteeClaimsBackendDelegate extends BackendDelegate {

    ReceivedGuaranteeClaimsDTO createClaim(ReceivedGuaranteeClaimsDTO ReceivedGuaranteeClaimsDTO, HashMap<String, Object> inputParams,  DataControllerRequest request);
    List<ReceivedGuaranteeClaimsDTO> getClaims(DataControllerRequest request);
    ReceivedGuaranteeClaimsDTO getClaimsById(String claimsSRMSId, DataControllerRequest request);
    ReceivedGuaranteeClaimsDTO updateGuaranteeClaims(ReceivedGuaranteeClaimsDTO inputClaimsDTO, boolean isMergeRequired,
                                                     ReceivedGuaranteeClaimsDTO initiatedClaimsDTO, DataControllerRequest request);
}
