/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author k.meiyazhagan
 */
public interface DocumentsBusinessDelegate extends BusinessDelegate {

    Result uploadDocumentDBXDB(String referenceId, String customerId, String uploadedFileName,
                               String fileExtension, String fileContents, DataControllerRequest request);

    Result fetchDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request);

    Result deleteDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request);

}
