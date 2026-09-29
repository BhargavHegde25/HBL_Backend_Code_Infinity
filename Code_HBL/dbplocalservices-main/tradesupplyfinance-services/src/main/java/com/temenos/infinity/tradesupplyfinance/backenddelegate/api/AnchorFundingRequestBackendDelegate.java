/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;

/**
 * @author k.meiyazhagan
 */
public interface AnchorFundingRequestBackendDelegate extends BackendDelegate {
    AnchorFundingRequestDTO createAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request);

    AnchorFundingRequestDTO getAnchorFundingRequestById(String fundingRequestId, DataControllerRequest request);

    AnchorFundingRequestDTO updateAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request);
    
    List<AnchorFundingRequestDTO> getAllFundingRequest(DataControllerRequest request);
}
