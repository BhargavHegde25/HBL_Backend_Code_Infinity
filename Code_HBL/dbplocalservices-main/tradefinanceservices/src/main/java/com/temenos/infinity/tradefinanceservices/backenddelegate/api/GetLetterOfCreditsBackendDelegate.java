package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;

/**
 *
 */
public interface GetLetterOfCreditsBackendDelegate  extends BackendDelegate {

    /**
     * method to get the Blocked Funds return result Object
     * 
     * @param request
     */
    List<LetterOfCreditsDTO> getLetterOfCreditsFromSRMS(LetterOfCreditsDTO letterOfCreditsDTO, DataControllerRequest request)
            throws ApplicationException;
}
