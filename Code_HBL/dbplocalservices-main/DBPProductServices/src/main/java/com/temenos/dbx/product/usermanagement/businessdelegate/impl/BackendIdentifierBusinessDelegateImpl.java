package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;

public class BackendIdentifierBusinessDelegateImpl implements BackendIdentifierBusinessDelegate {

    @Override
    public DBXResult get(BackendIdentifierDTO backendIdentifierDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        BackendIdentifiersBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class);
        return backendDelegate.get(backendIdentifierDTO, headerMap);
    }

    @Override
    public DBXResult getList(BackendIdentifierDTO backendIdentifierDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        BackendIdentifiersBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class);
        return backendDelegate.getList(backendIdentifierDTO, headerMap);
    }

    @Override
    public List<BackendIdentifierDTO> getBackendIdentifierList(List<BackendIdentifierDTO> dtoList,
            Map<String, Object> headerMap) throws ApplicationException {
        BackendIdentifiersBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class);
        return backendDelegate.getBackendIdentifierList(dtoList, headerMap);
    }

    @Override
    public BackendIdentifierDTO createBackendIdentifier(BackendIdentifierDTO backendIdentifierDTO,
            Map<String, Object> headerMap) throws ApplicationException {
        BackendIdentifiersBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class);
        return backendDelegate.createBackendIdentifier(backendIdentifierDTO, headerMap);
    }
}
