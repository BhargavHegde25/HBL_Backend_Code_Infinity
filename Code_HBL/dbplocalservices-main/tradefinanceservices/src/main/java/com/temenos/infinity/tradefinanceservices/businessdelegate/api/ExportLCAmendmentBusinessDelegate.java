/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCAmendmentsDTO;

public interface ExportLCAmendmentBusinessDelegate extends BusinessDelegate {
	List<ExportLCAmendmentsDTO> getExportAmendments(DataControllerRequest request);

	ExportLCAmendmentsDTO amendExportLetterOfCredits(ExportLCAmendmentsDTO letterOfCredit,
			DataControllerRequest request);

	ExportLCAmendmentsDTO updateExportLCAmendment(ExportLCAmendmentsDTO amendmentData, DataControllerRequest request);

	ExportLCAmendmentsDTO getExportLCAmendmentById(String amendmentSRMSRequestId, DataControllerRequest request);

}
