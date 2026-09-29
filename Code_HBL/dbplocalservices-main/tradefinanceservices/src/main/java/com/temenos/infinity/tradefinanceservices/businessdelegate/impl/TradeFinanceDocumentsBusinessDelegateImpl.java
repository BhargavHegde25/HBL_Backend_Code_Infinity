/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.TradeFinanceDocumentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.TradeFinanceDocumentsBusinessDelegate;

public class TradeFinanceDocumentsBusinessDelegateImpl implements TradeFinanceDocumentsBusinessDelegate {

    @Override
    public Result uploadDocumentDMS(String referenceId, String customerId, String uploadedFileName, String fileContents, DataControllerRequest request) {
        TradeFinanceDocumentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TradeFinanceDocumentsBackendDelegate.class);
        return orderBackendDelegate.uploadDocumentDMS(referenceId, customerId, uploadedFileName, fileContents, request);
    }

    @Override
    public Result fetchDocumentDMS(String referenceId, DataControllerRequest request) {
        TradeFinanceDocumentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TradeFinanceDocumentsBackendDelegate.class);
        return orderBackendDelegate.fetchDocumentDMS(referenceId, request);
    }

    @Override
    public Result deleteDocumentDMS(String referenceId, DataControllerRequest request) {
        TradeFinanceDocumentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TradeFinanceDocumentsBackendDelegate.class);
        return orderBackendDelegate.deleteDocumentDMS(referenceId, request);
    }

    @Override
    public Result uploadDocumentDBXDB(String referenceId, String customerId, String uploadedFileName, String fileExtension, String fileContents, DataControllerRequest request) {
        TradeFinanceDocumentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TradeFinanceDocumentsBackendDelegate.class);
        return orderBackendDelegate.uploadDocumentDBXDB(referenceId, customerId, uploadedFileName, fileExtension, fileContents, request);
    }

    @Override
    public Result fetchDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request) {
        TradeFinanceDocumentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TradeFinanceDocumentsBackendDelegate.class);
        return orderBackendDelegate.fetchDocumentDBXDB(customerId, referenceId, request);
    }

    @Override
    public Result deleteDocumentDBXDB(String customerId, String referenceId, DataControllerRequest request) {
        TradeFinanceDocumentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TradeFinanceDocumentsBackendDelegate.class);
        return orderBackendDelegate.deleteDocumentDBXDB(customerId, referenceId, request);
    }

}
