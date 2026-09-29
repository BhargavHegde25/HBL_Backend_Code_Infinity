/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.CounterPartyInvoiceBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.CounterPartyInvoiceBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.CounterPartyInvoiceDTO;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public class CounterPartyInvoiceBusinessDelegateImpl implements CounterPartyInvoiceBusinessDelegate {
    @Override
    public CounterPartyInvoiceDTO createCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request) {
        CounterPartyInvoiceBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(CounterPartyInvoiceBackendDelegate.class);
        return requestBackend.createCounterPartyInvoice(inputDto, request);
    }

    @Override
    public CounterPartyInvoiceDTO getCounterPartyInvoiceById(String invoiceReference, DataControllerRequest request) {
        CounterPartyInvoiceBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(CounterPartyInvoiceBackendDelegate.class);
        return requestBackend.getCounterPartyInvoiceById(invoiceReference, request);
    }

    @Override
    public CounterPartyInvoiceDTO updateCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request) {
        CounterPartyInvoiceBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(CounterPartyInvoiceBackendDelegate.class);
        return requestBackend.updateCounterPartyInvoice(inputDto, request);
    }

    @Override
    public List<CounterPartyInvoiceDTO> getAllCounterPartyInvoices(DataControllerRequest request) {
        CounterPartyInvoiceBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(CounterPartyInvoiceBackendDelegate.class);
        return requestBackend.getAllCounterPartyInvoices(request);
    }
}