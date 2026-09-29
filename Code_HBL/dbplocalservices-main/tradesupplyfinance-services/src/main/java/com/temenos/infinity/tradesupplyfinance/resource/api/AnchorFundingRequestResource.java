/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;

/**
 * @author k.meiyazhagan
 */
public interface AnchorFundingRequestResource extends Resource {

    Result saveAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request);
    
    Result submitAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request);
    
    Result getAllFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request);
    
    Result cancelAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request);
    
    Result saveAnchorFundingRequestMock(AnchorFundingRequestDTO inputDto, DataControllerRequest request);
    
}
