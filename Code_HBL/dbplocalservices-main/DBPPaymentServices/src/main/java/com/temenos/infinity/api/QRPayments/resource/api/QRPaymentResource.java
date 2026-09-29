package com.temenos.infinity.api.QRPayments.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface QRPaymentResource extends Resource {
	
	Result createQRPayment(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
}
