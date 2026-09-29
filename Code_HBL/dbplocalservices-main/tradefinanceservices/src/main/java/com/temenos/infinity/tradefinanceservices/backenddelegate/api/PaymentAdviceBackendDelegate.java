/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;

import java.util.List;

public interface PaymentAdviceBackendDelegate extends BackendDelegate {

    PaymentAdviceDTO createPaymentAdvice(PaymentAdviceDTO paymentAdvice, DataControllerRequest request);

    List<PaymentAdviceDTO> getPaymentAdvice(DataControllerRequest request);

}
