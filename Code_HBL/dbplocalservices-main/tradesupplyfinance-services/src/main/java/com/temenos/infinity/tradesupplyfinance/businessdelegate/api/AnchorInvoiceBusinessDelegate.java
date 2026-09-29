/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public interface AnchorInvoiceBusinessDelegate extends BusinessDelegate {
    AnchorInvoiceDTO createAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request);

    AnchorInvoiceDTO getAnchorInvoiceById(String invoiceReference, DataControllerRequest request);

    AnchorInvoiceDTO updateAnchorInvoice(AnchorInvoiceDTO inputDto, DataControllerRequest request);

    List<AnchorInvoiceDTO> getAllAnchorInvoices(DataControllerRequest request);
}