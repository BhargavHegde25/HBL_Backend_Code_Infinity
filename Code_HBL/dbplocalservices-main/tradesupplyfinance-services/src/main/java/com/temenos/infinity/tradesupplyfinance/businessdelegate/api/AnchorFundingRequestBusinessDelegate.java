/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;

/**
 * @author k.meiyazhagan
 */
public interface AnchorFundingRequestBusinessDelegate extends BusinessDelegate {
    AnchorFundingRequestDTO createAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request);

    AnchorFundingRequestDTO getAnchorFundingRequestById(String fundingRequestId, DataControllerRequest request);

    AnchorFundingRequestDTO updateAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request);
    
    List<AnchorFundingRequestDTO> getAllFundingRequest(DataControllerRequest request);

}
