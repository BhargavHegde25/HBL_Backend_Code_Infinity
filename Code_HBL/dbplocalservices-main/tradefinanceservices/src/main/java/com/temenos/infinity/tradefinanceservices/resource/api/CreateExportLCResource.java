/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;

public interface CreateExportLCResource extends Resource {

	Result createExportLetterOfCredit(ExportLOCDTO createPayloadDTO, DataControllerRequest request);	
	Result updateExportLCByBank(ExportLOCDTO createPayloadDTO, DataControllerRequest request);
	Result beneficiaryConsent(ExportLOCDTO createPayloadDTO, DataControllerRequest request);
}
