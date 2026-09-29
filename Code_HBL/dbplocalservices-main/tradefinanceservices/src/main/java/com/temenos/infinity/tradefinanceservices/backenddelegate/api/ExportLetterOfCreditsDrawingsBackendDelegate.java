/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCDrawingsDTO;

import java.util.List;

public interface ExportLetterOfCreditsDrawingsBackendDelegate extends BackendDelegate {
    ExportLCDrawingsDTO createExportDrawing(ExportLCDrawingsDTO inputArray, DataControllerRequest request);

    ExportLCDrawingsDTO updateExportLetterOfCreditDrawing(ExportLCDrawingsDTO exportPayloadDTO,
                                                          DataControllerRequest request);

    ExportLCDrawingsDTO getExportLetterOfCreditDrawingById(DataControllerRequest request,
                                                           String drawingSRMSRequestId);

    List<ExportLCDrawingsDTO> getExportLetterOfCreditDrawings(DataControllerRequest request);

}
