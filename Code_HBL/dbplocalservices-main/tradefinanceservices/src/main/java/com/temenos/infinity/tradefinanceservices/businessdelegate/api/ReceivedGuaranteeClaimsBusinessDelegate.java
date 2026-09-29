/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteeClaimsDTO;

import java.util.HashMap;
import java.util.List;

public interface ReceivedGuaranteeClaimsBusinessDelegate extends BusinessDelegate {

    ReceivedGuaranteeClaimsDTO createClaim(ReceivedGuaranteeClaimsDTO ReceivedGuaranteeClaimsDTO, HashMap<String, Object> inputParams, DataControllerRequest request);
    List<ReceivedGuaranteeClaimsDTO> getClaims(DataControllerRequest request);
    ReceivedGuaranteeClaimsDTO getClaimsById(String claimsSRMSId, DataControllerRequest request);
    ReceivedGuaranteeClaimsDTO updateGuaranteeClaims(ReceivedGuaranteeClaimsDTO inputClaimsDTO, boolean isMergeRequired,
                                                     ReceivedGuaranteeClaimsDTO initiatedClaimsDTO, DataControllerRequest request);

    byte[] generateReceivedGuaranteeClaim(ReceivedGuaranteeClaimsDTO claimsDTO, DataControllerRequest request);
}
