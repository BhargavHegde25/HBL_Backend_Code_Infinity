package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;

public interface GetLetterOfCreditsByIDBusinessDelegate extends BusinessDelegate {
    LetterOfCreditsDTO getImportLCById(String importLcId, DataControllerRequest request) throws ApplicationException;
}