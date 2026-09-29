package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCDrawingsDTO;

public interface ExportLetterOfCreditsDrawingsResource extends Resource {

    public Result createExportDrawing(ExportLCDrawingsDTO inputArray, DataControllerRequest request);

    public Result updateExportLCDrawing(ExportLCDrawingsDTO updatePayloadDTO, DataControllerRequest request);

    public Result getExportLetterOfCreditDrawingById(DataControllerRequest request);

    public Result getExportLetterOfCreditDrawings(Object[] inputArray, DataControllerRequest request);

    public Result generateExportDrawingPdf(DataControllerRequest request);

    public Result deleteExportLetterOfCreditDrawing(DataControllerRequest request);

    public Result updateDrawingByBank(ExportLCDrawingsDTO updatePayloadDTO, DataControllerRequest request);
}
