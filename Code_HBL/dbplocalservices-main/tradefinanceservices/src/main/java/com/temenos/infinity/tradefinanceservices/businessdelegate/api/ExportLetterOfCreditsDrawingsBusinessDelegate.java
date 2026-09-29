/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCDrawingsDTO;

import java.util.List;

public interface ExportLetterOfCreditsDrawingsBusinessDelegate extends BusinessDelegate {
    ExportLCDrawingsDTO createExportDrawing(ExportLCDrawingsDTO inputArray, DataControllerRequest request);

    ExportLCDrawingsDTO updateExportLetterOfCreditDrawing(ExportLCDrawingsDTO exportPayloadDTO, DataControllerRequest request);

    List<ExportLCDrawingsDTO> getExportLetterOfCreditDrawings(DataControllerRequest request);

    ExportLCDrawingsDTO getExportLetterOfCreditDrawingById(DataControllerRequest request, String drawingSRMSRequestId);

    byte[] generateExportDrawingPdf(ExportLCDrawingsDTO drawingDetails, DataControllerRequest request);

}