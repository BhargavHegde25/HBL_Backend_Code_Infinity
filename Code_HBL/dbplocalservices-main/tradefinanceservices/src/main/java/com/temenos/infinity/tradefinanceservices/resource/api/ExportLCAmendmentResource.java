/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCAmendmentsDTO;

public interface ExportLCAmendmentResource extends Resource {

    Object getExportAmendments(FilterDTO filterDto, DataControllerRequest request, boolean filter);

    Result amendExportLetterOfCredits(ExportLCAmendmentsDTO letterOfCredit, DataControllerRequest request);

    Result updateExportLCAmendment(DataControllerRequest request);

    Result getExportLCAmendmentById(DataControllerRequest request);
    
    Result updateExportLCAmendmentByBank(ExportLCAmendmentsDTO letterOfCredit,DataControllerRequest request);
}
