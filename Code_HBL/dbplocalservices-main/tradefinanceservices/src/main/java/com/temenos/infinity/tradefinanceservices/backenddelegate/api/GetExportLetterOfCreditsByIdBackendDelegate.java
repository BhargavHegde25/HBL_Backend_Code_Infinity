/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;

public interface GetExportLetterOfCreditsByIdBackendDelegate extends BackendDelegate {
	ExportLOCDTO getExportLetterOfCreditById(String exportLcId, DataControllerRequest request);

}
