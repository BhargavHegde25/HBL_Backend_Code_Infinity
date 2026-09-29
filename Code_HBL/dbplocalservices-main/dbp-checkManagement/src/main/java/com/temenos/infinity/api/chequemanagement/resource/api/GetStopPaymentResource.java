package com.temenos.infinity.api.chequemanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface GetStopPaymentResource extends Resource {
    Result getStopPaymentRequests(StopPayment stopPaymentInput, DataControllerRequest request);
}
