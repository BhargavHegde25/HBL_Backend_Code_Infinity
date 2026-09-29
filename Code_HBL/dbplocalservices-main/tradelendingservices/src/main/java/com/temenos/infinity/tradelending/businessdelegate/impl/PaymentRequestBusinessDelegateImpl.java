/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.backenddelegate.api.PaymentRequestBackendDelegate;
import com.temenos.infinity.tradelending.businessdelegate.api.PaymentRequestBusinessDelegate;
import com.temenos.infinity.tradelending.dto.PaymentRequestDTO;

public class PaymentRequestBusinessDelegateImpl implements PaymentRequestBusinessDelegate {

    @Override
    public PaymentRequestDTO createPaymentRequest(PaymentRequestDTO inputDto, DataControllerRequest request) {
        PaymentRequestBackendDelegate paymentReqBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(PaymentRequestBackendDelegate.class);
        return paymentReqBackendDelegate.createPaymentRequest(inputDto, request);
    }

    @Override
    public PaymentRequestDTO getPaymentRequestById(String paymentRequestId, DataControllerRequest request) {
        PaymentRequestBackendDelegate paymentReqBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(PaymentRequestBackendDelegate.class);
        return paymentReqBackendDelegate.getPaymentRequestById(paymentRequestId, request);
    }

}