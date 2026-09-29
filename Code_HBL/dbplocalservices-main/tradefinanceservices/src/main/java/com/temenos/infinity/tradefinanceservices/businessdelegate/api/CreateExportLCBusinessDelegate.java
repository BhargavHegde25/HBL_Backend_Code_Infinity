/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;

public interface CreateExportLCBusinessDelegate extends BusinessDelegate {

	ExportLOCDTO createExportLetterOfCredit(ExportLOCDTO createPayloadDTO, DataControllerRequest request);
	ExportLOCDTO updateExportLetterOfCredit(ExportLOCDTO createPayloadDTO, DataControllerRequest request);

}
