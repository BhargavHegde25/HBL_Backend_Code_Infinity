/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.DocumentsBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.DocumentsBusinessDelegate;

/**
 * @author k.meiyazhagan
 */
public class DocumentsBusinessDelegateImpl implements DocumentsBusinessDelegate {

    @Override
    public Result uploadDocumentDBXDB(String referenceId, String customerId, String uploadedFileName, String fileExtension, String fileContents, DataControllerRequest request) {
        DocumentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(DocumentsBackendDelegate.class);
        return orderBackendDelegate.uploadDocumentDBXDB(referenceId, customerId, uploadedFileName, fileExtension, fileContents, request);
    }

    @Override
    public Result fetchDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request) {
        DocumentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(DocumentsBackendDelegate.class);
        return orderBackendDelegate.fetchDocumentDBXDB(customerId, referenceId, request);
    }

    @Override
    public Result deleteDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request) {
        DocumentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(DocumentsBackendDelegate.class);
        return orderBackendDelegate.deleteDocumentDBXDB(customerId, referenceId, request);
    }

}
