/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.dto.PaymentRequestDTO;

public interface PaymentRequestBusinessDelegate extends BusinessDelegate {

    PaymentRequestDTO createPaymentRequest(PaymentRequestDTO inputDto, DataControllerRequest request);

    PaymentRequestDTO getPaymentRequestById(String paymentRequestId, DataControllerRequest request);
}
