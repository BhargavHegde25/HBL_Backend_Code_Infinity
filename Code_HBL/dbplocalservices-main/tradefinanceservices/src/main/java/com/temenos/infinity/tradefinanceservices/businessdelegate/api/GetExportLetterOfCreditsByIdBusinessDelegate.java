/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;

public interface GetExportLetterOfCreditsByIdBusinessDelegate extends BusinessDelegate {

	ExportLOCDTO getExportLetterOfCreditById(String exportLcId, DataControllerRequest request);
}
