/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.OutwardCollectionsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.OutwardCollectionsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public class OutwardCollectionsBusinessDelegateImpl implements OutwardCollectionsBusinessDelegate, ExcelBusinessDelegate {

    private final OutwardCollectionsBackendDelegate requestBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(OutwardCollectionsBackendDelegate.class);

    @Override
    public OutwardCollectionsDTO createCollection(OutwardCollectionsDTO inputDto, DataControllerRequest request) {
        return requestBackendDelegate.createCollection(inputDto, request);
    }

    @Override
    public List<OutwardCollectionsDTO> getCollections(DataControllerRequest request) {
        return requestBackendDelegate.getCollections(request);
    }

    @Override
    public OutwardCollectionsDTO getCollectionById(String collectionReference, DataControllerRequest request) {
        return requestBackendDelegate.getCollectionById(collectionReference, request);
    }

    @Override
    public OutwardCollectionsDTO updateCollection(OutwardCollectionsDTO collectionDto, DataControllerRequest request) {
        return requestBackendDelegate.updateCollection(collectionDto, request);
    }

    @Override
    public List<OutwardCollectionsDTO> getList(DataControllerRequest request) throws ApplicationException {
        return requestBackendDelegate.getCollections(request);
    }
}
