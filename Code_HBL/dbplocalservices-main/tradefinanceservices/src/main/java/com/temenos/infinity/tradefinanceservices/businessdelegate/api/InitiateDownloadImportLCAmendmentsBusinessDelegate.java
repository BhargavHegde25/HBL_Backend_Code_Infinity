/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface InitiateDownloadImportLCAmendmentsBusinessDelegate  extends BusinessDelegate {

	public byte[] generateExportDrawingPdf(String srmsRequestId, DataControllerRequest request);

}
