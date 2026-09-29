/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.dto.PaymentRequestDTO;

public interface PaymentRequestBackendDelegate extends BackendDelegate {

    PaymentRequestDTO createPaymentRequest(PaymentRequestDTO inputDto, DataControllerRequest request);

    PaymentRequestDTO getPaymentRequestById(String paymentRequestId, DataControllerRequest request);
}