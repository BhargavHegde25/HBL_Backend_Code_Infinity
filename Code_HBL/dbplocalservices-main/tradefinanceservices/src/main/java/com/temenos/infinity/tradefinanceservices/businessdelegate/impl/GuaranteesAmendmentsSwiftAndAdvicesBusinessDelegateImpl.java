/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GuaranteesAmendmentsSwiftAndAdvicesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;

import java.util.List;

public class GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegateImpl implements GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate {
    public SwiftsAndAdvisesDTO createSwiftsAndAdvises(SwiftsAndAdvisesDTO SwiftsAndAdvises,
                                                      DataControllerRequest request) throws ApplicationException, com.temenos.infinity.api.commons.exception.ApplicationException {
        GuaranteesAmendmentsSwiftAndAdvicesBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteesAmendmentsSwiftAndAdvicesBackendDelegate.class);
        SwiftsAndAdvises = orderBackendDelegate.createSwiftsAndAdvises(SwiftsAndAdvises, request);
        return SwiftsAndAdvises;
    }

    @Override
    public List<SwiftsAndAdvisesDTO> getGuaranteeSwiftAdvices(DataControllerRequest request) {
        GuaranteesAmendmentsSwiftAndAdvicesBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteesAmendmentsSwiftAndAdvicesBackendDelegate.class);
        List<SwiftsAndAdvisesDTO> SwiftsAndAdvises = orderBackendDelegate.getGuaranteeSwiftAdvices(request);
        return SwiftsAndAdvises;
    }
}
