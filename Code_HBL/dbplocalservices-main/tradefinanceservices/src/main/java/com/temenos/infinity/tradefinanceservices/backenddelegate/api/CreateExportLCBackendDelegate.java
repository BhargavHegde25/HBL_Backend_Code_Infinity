/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;

public interface CreateExportLCBackendDelegate extends BackendDelegate {

	ExportLOCDTO createExportLetterOfCredit(ExportLOCDTO createPayloadDTO, DataControllerRequest request);
	public ExportLOCDTO updateExportLetterOfCredit(ExportLOCDTO createPayloadDTO, DataControllerRequest request);

}
