/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsAmendmentDTO;

public interface GetAmendmentsLetterOfCreditsBusinessDelegate extends BusinessDelegate {

    List<LetterOfCreditsAmendmentDTO> getAmendLetterOfCredits(DataControllerRequest request) throws ApplicationException;

    LetterOfCreditsAmendmentDTO getAmendmentsById(String amendmentReference, DataControllerRequest request);
}