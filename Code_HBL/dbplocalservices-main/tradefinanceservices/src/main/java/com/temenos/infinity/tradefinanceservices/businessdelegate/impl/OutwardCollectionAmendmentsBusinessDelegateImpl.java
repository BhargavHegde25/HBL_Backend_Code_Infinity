/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.OutwardCollectionAmendmentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.OutwardCollectionAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public class OutwardCollectionAmendmentsBusinessDelegateImpl implements OutwardCollectionAmendmentsBusinessDelegate, ExcelBusinessDelegate {

    private final OutwardCollectionAmendmentsBackendDelegate requestBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(OutwardCollectionAmendmentsBackendDelegate.class);


    @Override
    public OutwardCollectionAmendmentsDTO createAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) {
        return requestBackendDelegate.createAmendment(inputDto, request);
    }

    @Override
    public List<OutwardCollectionAmendmentsDTO> getAmendments(DataControllerRequest request) {
        return requestBackendDelegate.getAmendments(request);
    }

    @Override
    public OutwardCollectionAmendmentsDTO getAmendmentById(String amendmentReference, DataControllerRequest request) {
        return requestBackendDelegate.getAmendmentById(amendmentReference, request);
    }

    @Override
    public OutwardCollectionAmendmentsDTO updateAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) {
        return requestBackendDelegate.updateAmendment(inputDto, request);
    }

    @Override
    public List<OutwardCollectionAmendmentsDTO> getList(DataControllerRequest request) throws ApplicationException {
        return requestBackendDelegate.getAmendments(request);
    }
}
