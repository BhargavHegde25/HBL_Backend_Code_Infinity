package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;

public interface UpdateLetterOfCreditsResource extends Resource{
	Result updateLetterOfCredits(LetterOfCreditsDTO letterOfCredit, DataControllerRequest request);

	Result updateImportLCByBank(LetterOfCreditsDTO inputDto, DataControllerRequest request);
	
}
