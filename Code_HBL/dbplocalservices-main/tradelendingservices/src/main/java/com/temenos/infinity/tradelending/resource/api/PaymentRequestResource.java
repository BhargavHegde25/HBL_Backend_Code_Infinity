/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradelending.dto.PaymentRequestDTO;

public interface PaymentRequestResource extends Resource {
    Result submitPaymentRequest(PaymentRequestDTO inputDto, DataControllerRequest request);

    PaymentRequestDTO getPaymentRequestById(PaymentRequestDTO inputDto, DataControllerRequest request);
}