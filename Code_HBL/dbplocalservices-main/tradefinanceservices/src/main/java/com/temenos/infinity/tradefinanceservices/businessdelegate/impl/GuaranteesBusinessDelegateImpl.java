/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GuaranteesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;

import java.util.List;

public class GuaranteesBusinessDelegateImpl implements GuaranteesBusinessDelegate, ExcelBusinessDelegate {

    public GuaranteesDTO createGuarantees(GuaranteesDTO guaranteesDto, DataControllerRequest request) {

        GuaranteesBackendDelegate guaranteesBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteesBackendDelegate.class);
        return guaranteesBackend.createGuarantees(guaranteesDto, request);
    }

    public GuaranteesDTO updateGuarantees(GuaranteesDTO guaranteesDto, DataControllerRequest request) {

        GuaranteesBackendDelegate guaranteesBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteesBackendDelegate.class);
        return guaranteesBackend.updateGuarantees(guaranteesDto, request);
    }

    @Override
    public GuaranteesDTO getGuaranteesById(String srmsId, DataControllerRequest request) {
        GuaranteesBackendDelegate guaranteesBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteesBackendDelegate.class);
        return guaranteesBackend.getGuaranteesById(srmsId, request);
    }

    @Override
    public List<GuaranteesDTO> getGuranteesLC(GuaranteesDTO guaranteesDTO, DataControllerRequest request) {
        GuaranteesBackendDelegate guaranteesBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteesBackendDelegate.class);
        return guaranteesBackend.getGuranteesLC(guaranteesDTO, request);
    }

    @Override
    public List<GuaranteesDTO> getList(DataControllerRequest request) throws ApplicationException {
        GuaranteesBackendDelegate guaranteesBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteesBackendDelegate.class);
        return guaranteesBackend.getGuranteesLC(null, request);
    }
}
