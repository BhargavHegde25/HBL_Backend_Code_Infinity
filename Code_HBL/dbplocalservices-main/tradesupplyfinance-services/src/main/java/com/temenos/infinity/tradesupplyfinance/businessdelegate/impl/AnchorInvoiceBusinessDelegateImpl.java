/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.AnchorInvoiceBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.AnchorInvoiceBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public class AnchorInvoiceBusinessDelegateImpl implements AnchorInvoiceBusinessDelegate {
    @Override
    public AnchorInvoiceDTO createAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        AnchorInvoiceBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorInvoiceBackendDelegate.class);
        return requestBackend.createAnchorInvoice(inputDto, request);
    }

    @Override
    public AnchorInvoiceDTO getAnchorInvoiceById(String invoiceReference, DataControllerRequest request) {
        AnchorInvoiceBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorInvoiceBackendDelegate.class);
        return requestBackend.getAnchorInvoiceById(invoiceReference, request);
    }

    @Override
    public AnchorInvoiceDTO updateAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request) {
        AnchorInvoiceBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorInvoiceBackendDelegate.class);
        return requestBackend.updateAnchorInvoice(inputDto, request);
    }

    @Override
    public List<AnchorInvoiceDTO> getAllAnchorInvoices(DataControllerRequest request) {
        AnchorInvoiceBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorInvoiceBackendDelegate.class);
        return requestBackend.getAllAnchorInvoices(request);
    }
}