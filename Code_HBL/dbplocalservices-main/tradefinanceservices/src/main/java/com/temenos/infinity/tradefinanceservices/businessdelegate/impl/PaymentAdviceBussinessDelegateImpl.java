/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.PaymentAdviceBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.PaymentAdviceBussinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;

import java.util.List;

public class PaymentAdviceBussinessDelegateImpl implements PaymentAdviceBussinessDelegate {

    @Override
    public PaymentAdviceDTO createPaymentAdvice(PaymentAdviceDTO paymentAdvice, DataControllerRequest request) {
        PaymentAdviceBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(PaymentAdviceBackendDelegate.class);
        return orderBackendDelegate.createPaymentAdvice(paymentAdvice, request);
    }

    @Override
    public List<PaymentAdviceDTO> getPaymentAdvice(DataControllerRequest request) {
        PaymentAdviceBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(PaymentAdviceBackendDelegate.class);
        return orderBackendDelegate.getPaymentAdvice(request);
    }

}
