/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface InitiateDownloadExportLCAmendmentsBusinessDelegate extends BusinessDelegate {
    byte[] generateExportLCAmendmentsPdf(String srmsRequestId, DataControllerRequest request);
}
