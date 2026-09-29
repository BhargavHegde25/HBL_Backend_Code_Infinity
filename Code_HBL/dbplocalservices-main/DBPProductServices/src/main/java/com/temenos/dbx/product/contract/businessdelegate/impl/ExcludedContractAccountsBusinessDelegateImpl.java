package com.temenos.dbx.product.contract.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.contract.backenddelegate.api.ExcludedContractAccountsBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ExcludedContractAccountsBusinessDelegate;
import com.temenos.dbx.product.dto.ExcludedContractAccountDTO;

public class ExcludedContractAccountsBusinessDelegateImpl implements ExcludedContractAccountsBusinessDelegate {

    @Override
    public ExcludedContractAccountDTO createExcludedContractAccount(ExcludedContractAccountDTO dto,
            Map<String, Object> headersMap) throws ApplicationException {
        ExcludedContractAccountsBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ExcludedContractAccountsBackendDelegate.class);
        return backendDelegate.createExcludedContractAccount(dto, headersMap);
    }

    @Override
    public ExcludedContractAccountDTO getExcludedContractAccount(ExcludedContractAccountDTO dto,
            Map<String, Object> headersMap) throws ApplicationException {
        ExcludedContractAccountsBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ExcludedContractAccountsBackendDelegate.class);
        return backendDelegate.getExcludedContractAccount(dto, headersMap);
    }

}
