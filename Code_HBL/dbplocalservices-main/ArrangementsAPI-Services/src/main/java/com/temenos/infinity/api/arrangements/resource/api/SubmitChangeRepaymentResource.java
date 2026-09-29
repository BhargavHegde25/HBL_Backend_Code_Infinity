package com.temenos.infinity.api.arrangements.resource.api;

import java.util.HashMap;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.dto.MortgageRepaymentDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface SubmitChangeRepaymentResource extends Resource {

	Result SubmitChangeRepaymentDay(MortgageRepaymentDTO mortgageAccount, DataControllerRequest request,
			HashMap<String, Object> headerMap) throws ApplicationException;
	Result SubmitChangeRepaymentAccount(MortgageRepaymentDTO changeRepaymentAccount, DataControllerRequest request,
			HashMap<String, Object> headerMap) throws ApplicationException;
}
