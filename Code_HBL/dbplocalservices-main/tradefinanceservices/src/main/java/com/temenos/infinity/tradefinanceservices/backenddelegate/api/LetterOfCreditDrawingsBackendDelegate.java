/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;

import java.util.List;

public interface LetterOfCreditDrawingsBackendDelegate extends BackendDelegate {
    DrawingsDTO getImportDrawingDetailsById(DataControllerRequest request, String srmsRequestOrderId)
            throws ApplicationException, com.temenos.infinity.api.commons.exception.ApplicationException;

    List<DrawingsDTO> getImportDrawingsFromSRMS(DrawingsDTO drawingsDTO, DataControllerRequest request)
			throws ApplicationException, com.temenos.infinity.api.commons.exception.ApplicationException;

    DrawingsDTO updateDrawingsOrder(DrawingsDTO drawings, DataControllerRequest request) throws ApplicationException, com.temenos.infinity.api.commons.exception.ApplicationException;

    DrawingsDTO createDrawingsOrder(DrawingsDTO drawings, DataControllerRequest request) throws ApplicationException, com.temenos.infinity.api.commons.exception.ApplicationException;


}
