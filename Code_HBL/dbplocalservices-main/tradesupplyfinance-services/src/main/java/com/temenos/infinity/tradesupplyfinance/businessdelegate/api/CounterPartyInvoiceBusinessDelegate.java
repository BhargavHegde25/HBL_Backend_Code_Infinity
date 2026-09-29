/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.dto.CounterPartyInvoiceDTO;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public interface CounterPartyInvoiceBusinessDelegate extends BusinessDelegate {
    CounterPartyInvoiceDTO createCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request);

    CounterPartyInvoiceDTO getCounterPartyInvoiceById(String invoiceReference, DataControllerRequest request);

    CounterPartyInvoiceDTO updateCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request);

    List<CounterPartyInvoiceDTO> getAllCounterPartyInvoices(DataControllerRequest request);
}