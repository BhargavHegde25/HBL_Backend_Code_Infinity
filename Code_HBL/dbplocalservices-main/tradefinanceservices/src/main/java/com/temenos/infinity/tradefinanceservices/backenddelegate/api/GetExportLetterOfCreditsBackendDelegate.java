/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;

public interface GetExportLetterOfCreditsBackendDelegate extends BackendDelegate {
	public List<ExportLOCDTO> getExportLetterOfCredits(DataControllerRequest request);
}
