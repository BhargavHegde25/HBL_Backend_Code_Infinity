/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.dto.PaymentAllocationDTO;
import org.json.JSONObject;


/**
 * @author k.meiyazhagan
 */
public interface PaymentAllocationResource extends Resource {
    PaymentAllocationDTO createPaymentAllocation(PaymentAllocationDTO inputDto, DataControllerRequest request);

    Result getPaymentAllocations(DataControllerRequest request);

    Result requestPaymentAllocationDocuments(PaymentAllocationDTO inputDto, DataControllerRequest request);

    Result submitPaymentAllocationDocuments(PaymentAllocationDTO inputDto, DataControllerRequest request);
}
