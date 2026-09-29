/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface TradeFinanceDocumentsBackendDelegate extends BackendDelegate {

    Result uploadDocumentDMS(String referenceId, String customerId, String uploadedFileName,
                             String fileContents, DataControllerRequest request);

    Result fetchDocumentDMS(String referenceId, DataControllerRequest request);

    Result deleteDocumentDMS(String referenceId, DataControllerRequest request);

    Result uploadDocumentDBXDB(String referenceId, String customerId, String uploadedFileName,
                               String fileExtension, String fileContents, DataControllerRequest request);

    Result fetchDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request);

    Result deleteDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request);

}
