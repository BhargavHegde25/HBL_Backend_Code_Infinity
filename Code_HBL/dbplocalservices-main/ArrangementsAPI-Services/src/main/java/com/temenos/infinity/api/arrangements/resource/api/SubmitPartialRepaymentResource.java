package com.temenos.infinity.api.arrangements.resource.api;

import java.util.HashMap;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.dto.PartialRepaymentDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface SubmitPartialRepaymentResource extends Resource {

	Result SubmitPartialRepayment(PartialRepaymentDTO partialRepayment, DataControllerRequest request,
			HashMap<String, Object> headerMap) throws ApplicationException;
	
}
