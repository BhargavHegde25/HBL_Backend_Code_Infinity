/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.dto.IssuedGuaranteeClaimsDTO;

import java.util.List;

public interface IssuedGuaranteeClaimsBusinessDelegate extends BusinessDelegate {
    IssuedGuaranteeClaimsDTO createClaim(IssuedGuaranteeClaimsDTO guaranteeClaimsDTO, DataControllerRequest request);
    IssuedGuaranteeClaimsDTO getClaimById(String claimsSRMSId, DataControllerRequest request);
    List<IssuedGuaranteeClaimsDTO> getClaims(DataControllerRequest request);
    IssuedGuaranteeClaimsDTO updateClaim(IssuedGuaranteeClaimsDTO guaranteeClaimsDTO, IssuedGuaranteeClaimsDTO previousGuaranteeClaimsDTO, boolean isMergeRequired,  DataControllerRequest request);
	byte[] generateIssuedGuaranteeClaim(IssuedGuaranteeClaimsDTO claimsDTO, GuaranteesDTO guaranteeDTO, DataControllerRequest request);
}
