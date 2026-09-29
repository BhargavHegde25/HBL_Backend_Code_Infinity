/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.dto.CounterPartyInvoiceDTO;

/**
 * @author k.meiyazhagan
 */
public interface CounterPartyInvoiceResource extends Resource {
    Result saveCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request);

    Result deleteCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request);

    Result getAllCounterPartyInvoices(CounterPartyInvoiceDTO inputDto, DataControllerRequest request);

    Result submitCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request);

    Result createCounterPartyInvoice(CounterPartyInvoiceDTO inputDto, DataControllerRequest request);
}