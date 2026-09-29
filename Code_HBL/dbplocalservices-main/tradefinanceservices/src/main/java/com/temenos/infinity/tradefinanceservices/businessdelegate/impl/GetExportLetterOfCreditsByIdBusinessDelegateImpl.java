/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetExportLetterOfCreditsByIdBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetExportLetterOfCreditsByIdBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;

public class GetExportLetterOfCreditsByIdBusinessDelegateImpl implements GetExportLetterOfCreditsByIdBusinessDelegate {

	@Override
	public ExportLOCDTO getExportLetterOfCreditById(String exportLcId, DataControllerRequest request) {
		GetExportLetterOfCreditsByIdBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(GetExportLetterOfCreditsByIdBackendDelegate.class);
		return backendDelegate.getExportLetterOfCreditById(exportLcId, request);
	}

}
