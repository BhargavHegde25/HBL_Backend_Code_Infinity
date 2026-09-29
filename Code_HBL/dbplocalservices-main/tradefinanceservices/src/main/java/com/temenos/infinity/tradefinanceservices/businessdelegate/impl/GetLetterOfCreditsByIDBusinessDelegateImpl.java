package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetLetterOfCreditsByIdBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetLetterOfCreditsByIDBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;

public class GetLetterOfCreditsByIDBusinessDelegateImpl implements GetLetterOfCreditsByIDBusinessDelegate {

    @Override
    public LetterOfCreditsDTO getImportLCById(String importLcId, DataControllerRequest request) throws ApplicationException {
        GetLetterOfCreditsByIdBackendDelegate getSRMSID_Business = DBPAPIAbstractFactoryImpl.getBackendDelegate(GetLetterOfCreditsByIdBackendDelegate.class);
        return getSRMSID_Business.getImportLCById(importLcId, request);
    }

}