/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author k.meiyazhagan
 */
public interface DocumentsBackendDelegate extends BackendDelegate {

    Result uploadDocumentDBXDB(String referenceId, String customerId, String uploadedFileName,
                               String fileExtension, String fileContents, DataControllerRequest request);

    Result fetchDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request);

    Result deleteDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request);

}
