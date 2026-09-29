/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.businessdelegate.impl;

import java.util.List;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.AnchorFundingRequestBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.AnchorFundingRequestBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;

/**
 * @author k.meiyazhagan
 */
public class AnchorFundingRequestBusinessDelegateImpl implements AnchorFundingRequestBusinessDelegate {

    @Override
    public AnchorFundingRequestDTO createAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request) {
    	AnchorFundingRequestBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorFundingRequestBackendDelegate.class);
        return requestBackend.createAnchorFundingRequest(inputDto, request);
    }

    @Override
    public AnchorFundingRequestDTO getAnchorFundingRequestById(String fundingRequestId, DataControllerRequest request) {
    	AnchorFundingRequestBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorFundingRequestBackendDelegate.class);
        return requestBackend.getAnchorFundingRequestById(fundingRequestId, request);
    }

    @Override
    public AnchorFundingRequestDTO updateAnchorFundingRequest(AnchorFundingRequestDTO inputDto, DataControllerRequest request) {
    	AnchorFundingRequestBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorFundingRequestBackendDelegate.class);
        return requestBackend.updateAnchorFundingRequest(inputDto, request);
    }
    
    @Override
    public List<AnchorFundingRequestDTO> getAllFundingRequest(DataControllerRequest request) {
    	AnchorFundingRequestBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorFundingRequestBackendDelegate.class);
        return requestBackend.getAllFundingRequest(request);
    }

    
}
