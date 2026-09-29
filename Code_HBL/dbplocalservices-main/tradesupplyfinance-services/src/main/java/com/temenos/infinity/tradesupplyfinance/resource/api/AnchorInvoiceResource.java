/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;

/**
 * @author k.meiyazhagan
 */
public interface AnchorInvoiceResource extends Resource {
    Result saveAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request);

    Result deleteAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request);

    Result getAllAnchorInvoices(AnchorInvoiceDTO inputDto, DataControllerRequest request);

    Result submitAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request);
    
    Result rejectAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request);
    
    Result approveAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request);

    Result createAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request);
}