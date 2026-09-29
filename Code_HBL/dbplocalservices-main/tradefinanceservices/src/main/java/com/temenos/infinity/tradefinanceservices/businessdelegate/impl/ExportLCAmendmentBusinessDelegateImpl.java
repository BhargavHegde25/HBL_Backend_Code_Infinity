/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import java.util.List;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ExportLCAmendmentBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ExportLCAmendmentBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;

public class ExportLCAmendmentBusinessDelegateImpl implements ExportLCAmendmentBusinessDelegate, ExcelBusinessDelegate {

	@Override
	public List<ExportLCAmendmentsDTO> getExportAmendments(DataControllerRequest request) {

		ExportLCAmendmentBackendDelegate drawingsBackend = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ExportLCAmendmentBackendDelegate.class);
		return drawingsBackend.getExportAmendments(request);
	}

	@Override
	public ExportLCAmendmentsDTO amendExportLetterOfCredits(ExportLCAmendmentsDTO letterOfCredits,
			DataControllerRequest request) {
		ExportLCAmendmentBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ExportLCAmendmentBackendDelegate.class);
		return orderBackendDelegate.amendExportLCcreate(letterOfCredits, request);
	}

	@Override
	public ExportLCAmendmentsDTO updateExportLCAmendment(ExportLCAmendmentsDTO amendmentData,
			DataControllerRequest request) {
		ExportLCAmendmentBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ExportLCAmendmentBackendDelegate.class);
		return requestBackend.updateExportLCAmendment(amendmentData, request);
	}

	@Override
	public ExportLCAmendmentsDTO getExportLCAmendmentById(String amendmentSRMSRequestId,
			DataControllerRequest request) {
		ExportLCAmendmentBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ExportLCAmendmentBackendDelegate.class);
		return requestBackend.getExportLCAmendmentById(amendmentSRMSRequestId, request);
	}

	@Override
	public List<ExportLCAmendmentsDTO> getList(DataControllerRequest request) throws ApplicationException {
		ExportLCAmendmentBackendDelegate drawingsBackend = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ExportLCAmendmentBackendDelegate.class);
		return drawingsBackend.getExportAmendments(request);
	}
}
