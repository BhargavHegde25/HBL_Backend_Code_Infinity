/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCAmendmentsDTO;

public interface ExportLCAmendmentBackendDelegate extends BackendDelegate {
    List<ExportLCAmendmentsDTO> getExportAmendments(DataControllerRequest request);

    ExportLCAmendmentsDTO amendExportLCcreate(ExportLCAmendmentsDTO letterOfCredits, DataControllerRequest request);

    ExportLCAmendmentsDTO updateExportLCAmendment(ExportLCAmendmentsDTO amendmentData, DataControllerRequest request);

    ExportLCAmendmentsDTO getExportLCAmendmentById(String amendmentSRMSRequestId, DataControllerRequest request);

}
