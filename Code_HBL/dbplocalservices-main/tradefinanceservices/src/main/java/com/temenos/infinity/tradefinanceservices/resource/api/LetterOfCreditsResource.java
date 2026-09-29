package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface LetterOfCreditsResource extends Resource{
	Result rejectLetterOfCredit(DataControllerRequest request);
	
	Result withdrawLetterOfCredit(DataControllerRequest request);
	Result fetchLetterOfCreditDetails(DataControllerRequest request);
	
}