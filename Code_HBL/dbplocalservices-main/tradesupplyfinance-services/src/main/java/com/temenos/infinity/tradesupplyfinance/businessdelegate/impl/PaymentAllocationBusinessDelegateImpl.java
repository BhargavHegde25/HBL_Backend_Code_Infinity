/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.PaymentAllocationBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.PaymentAllocationBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.PaymentAllocationDTO;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public class PaymentAllocationBusinessDelegateImpl implements PaymentAllocationBusinessDelegate {
    @Override
    public PaymentAllocationDTO createPaymentAllocation(PaymentAllocationDTO inputDto, DataControllerRequest request) {
        PaymentAllocationBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(PaymentAllocationBackendDelegate.class);
        return requestBackend.createPaymentAllocation(inputDto, request);
    }

    @Override
    public List<PaymentAllocationDTO> getPaymentAllocations(DataControllerRequest request) {
        PaymentAllocationBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(PaymentAllocationBackendDelegate.class);
        return requestBackend.getPaymentAllocations(request);
    }

    @Override
    public PaymentAllocationDTO getPaymentAllocationById(String paymentAllocationId, DataControllerRequest request) {
        PaymentAllocationBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(PaymentAllocationBackendDelegate.class);
        return requestBackend.getPaymentAllocationById(paymentAllocationId, request);
    }

    @Override
    public PaymentAllocationDTO updatePaymentAllocation(PaymentAllocationDTO inputDto, DataControllerRequest request) {
        PaymentAllocationBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(PaymentAllocationBackendDelegate.class);
        return requestBackend.updatePaymentAllocation(inputDto, request);
    }
}