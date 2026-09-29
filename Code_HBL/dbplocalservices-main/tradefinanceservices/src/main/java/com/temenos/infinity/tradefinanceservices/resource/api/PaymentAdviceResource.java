/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;

public interface PaymentAdviceResource extends Resource {

	Result createPaymentAdvice(PaymentAdviceDTO paymentAdvice, DataControllerRequest request)
			throws ApplicationException;

	Result getPaymentAdvice(DataControllerRequest request);

}
