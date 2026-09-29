package com.temenos.infinity.api.chequemanagement.resource.api;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;

public interface CreateStopPaymentResource extends Resource {

    Result createStopPayment(StopPayment stopPayment, DataControllerRequest request);
 
}
