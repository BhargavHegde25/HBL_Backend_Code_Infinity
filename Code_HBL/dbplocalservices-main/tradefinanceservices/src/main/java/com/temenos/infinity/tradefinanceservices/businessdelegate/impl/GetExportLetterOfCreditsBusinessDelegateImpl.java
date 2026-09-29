/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetExportLetterOfCreditsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetExportLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;

import java.util.List;

public class GetExportLetterOfCreditsBusinessDelegateImpl implements GetExportLetterOfCreditsBusinessDelegate, ExcelBusinessDelegate {
	GetExportLetterOfCreditsBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
			.getBackendDelegate(GetExportLetterOfCreditsBackendDelegate.class);
	public List<ExportLOCDTO> getExportLetterOfCredits(DataControllerRequest request) {

		return backendDelegate.getExportLetterOfCredits(request);
	}

	public List<ExportLOCDTO> getList(DataControllerRequest request) {

		return backendDelegate.getExportLetterOfCredits(request);
	}
}
