/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.IssuedGuaranteeClaimsDTO;

import java.util.List;

public interface IssuedGuaranteeClaimsBackendDelegate extends BackendDelegate {
    IssuedGuaranteeClaimsDTO createClaim(IssuedGuaranteeClaimsDTO guaranteeClaimsDTO, DataControllerRequest request);

    IssuedGuaranteeClaimsDTO getClaimById(String claimsSRMSId, DataControllerRequest request);

    List<IssuedGuaranteeClaimsDTO> getClaims(DataControllerRequest request);
    IssuedGuaranteeClaimsDTO updateClaim(IssuedGuaranteeClaimsDTO guaranteeClaimsDTO, IssuedGuaranteeClaimsDTO previousGuaranteeClaimsDTO, boolean isMergeRequired,  DataControllerRequest request);
}
