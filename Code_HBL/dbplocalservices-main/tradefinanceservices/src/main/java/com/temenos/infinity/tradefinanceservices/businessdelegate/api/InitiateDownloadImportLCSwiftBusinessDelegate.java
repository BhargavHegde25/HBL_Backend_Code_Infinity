/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;

import java.io.IOException;

public interface InitiateDownloadImportLCSwiftBusinessDelegate extends BusinessDelegate {
    byte[] generateImportLCSwiftPdf(String srmsRequestId, DataControllerRequest request) throws IOException;
}
