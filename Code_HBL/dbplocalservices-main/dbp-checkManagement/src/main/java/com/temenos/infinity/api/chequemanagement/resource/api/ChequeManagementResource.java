package com.temenos.infinity.api.chequemanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface ChequeManagementResource extends Resource{
	Result rejectChequeBook(DataControllerRequest request);
	
	Result withdrawCheque(DataControllerRequest request);
	
	Result fetchChequeDetails(DataControllerRequest request);
}
