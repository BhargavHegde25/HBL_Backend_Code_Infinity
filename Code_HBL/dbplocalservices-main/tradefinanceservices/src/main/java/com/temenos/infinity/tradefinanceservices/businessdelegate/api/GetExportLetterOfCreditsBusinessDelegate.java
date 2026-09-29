/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;

public interface GetExportLetterOfCreditsBusinessDelegate extends BusinessDelegate{
	public List<ExportLOCDTO> getExportLetterOfCredits(DataControllerRequest request);
}
