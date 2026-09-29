/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsAmendmentDTO;

public interface GetAmendmentsLetterOfCreditsBackendDelegate extends BackendDelegate {

    List<LetterOfCreditsAmendmentDTO> getamendLetterOfCreditsFromSRMS(DataControllerRequest request) throws ApplicationException;

    LetterOfCreditsAmendmentDTO getAmendmentsById(String amendmentReference, DataControllerRequest request);

}