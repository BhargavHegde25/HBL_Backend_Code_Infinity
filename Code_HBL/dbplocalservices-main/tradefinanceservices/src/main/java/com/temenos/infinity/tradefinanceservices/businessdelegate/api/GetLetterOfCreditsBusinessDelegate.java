package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;

/**
 */
public interface GetLetterOfCreditsBusinessDelegate extends BusinessDelegate {

    /**
     * method to getLetterOfCredits
     * 
     * @param request
     */
    List<LetterOfCreditsDTO> getLetterOfCredits(LetterOfCreditsDTO letterOfCreditsDTO, DataControllerRequest request)
            throws ApplicationException;      
}