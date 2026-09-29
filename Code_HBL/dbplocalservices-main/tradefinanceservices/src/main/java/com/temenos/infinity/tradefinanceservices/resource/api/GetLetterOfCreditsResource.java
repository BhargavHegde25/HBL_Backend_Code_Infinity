package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;


/**
 *
 *
 */
public interface GetLetterOfCreditsResource extends Resource {

    Result getLetterOfCredits(Object[] inputArray,LetterOfCreditsDTO letterOfCreditsDTO, DataControllerRequest request);

} 