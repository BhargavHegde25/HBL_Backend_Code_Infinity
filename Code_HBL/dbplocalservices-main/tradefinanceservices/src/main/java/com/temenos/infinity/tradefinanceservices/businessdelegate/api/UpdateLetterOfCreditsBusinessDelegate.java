package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;

public interface UpdateLetterOfCreditsBusinessDelegate extends BusinessDelegate {

    LetterOfCreditsDTO updateLetterOfCredits(LetterOfCreditsDTO letterOfCreditsDTO, DataControllerRequest request)
            throws ApplicationException;

}
