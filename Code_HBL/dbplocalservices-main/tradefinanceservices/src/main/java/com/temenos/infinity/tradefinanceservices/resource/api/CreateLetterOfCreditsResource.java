package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsAmendmentDTO;

/**
 * The Interface CreateLetterOfCreditsResource.
 */
public interface CreateLetterOfCreditsResource extends Resource {

    Result createLetterOfCredits(LetterOfCreditsDTO letterOfCredit, DataControllerRequest request);

    Result executeLetterOfCreditsRequest(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response);

    Result amendLetterOfCredits(LetterOfCreditsAmendmentDTO letterOfCredit, DataControllerRequest request);

    Result updateImportLCAmendmentByBank(LetterOfCreditsAmendmentDTO amendmentDto, DataControllerRequest request);

}