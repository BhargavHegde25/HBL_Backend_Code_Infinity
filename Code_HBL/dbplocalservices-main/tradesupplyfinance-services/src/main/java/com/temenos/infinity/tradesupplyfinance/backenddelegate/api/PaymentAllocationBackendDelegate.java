/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.dto.PaymentAllocationDTO;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public interface PaymentAllocationBackendDelegate extends BackendDelegate {
    PaymentAllocationDTO createPaymentAllocation(PaymentAllocationDTO inputDto, DataControllerRequest request);

    List<PaymentAllocationDTO> getPaymentAllocations(DataControllerRequest request);

    PaymentAllocationDTO getPaymentAllocationById(String paymentAllocationId, DataControllerRequest request);

    PaymentAllocationDTO updatePaymentAllocation(PaymentAllocationDTO inputDto, DataControllerRequest request);
}