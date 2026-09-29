package com.temenos.dbx.eum.product.usermanagement.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface BackendIdentifierBusinessDelegate extends BusinessDelegate {

    public DBXResult get(BackendIdentifierDTO backendIdentifierDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    public DBXResult getList(BackendIdentifierDTO backendIdentifierDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    /**
     * 
     * @param dtoList
     * @param headerMap
     * @return dto list
     * @throws ApplicationException
     */
    public List<BackendIdentifierDTO> getBackendIdentifierList(List<BackendIdentifierDTO> dtoList,
            Map<String, Object> headerMap) throws ApplicationException;

    public BackendIdentifierDTO createBackendIdentifier(BackendIdentifierDTO backendIdentifierDTO,
            Map<String, Object> headerMap) throws ApplicationException;
    
    public String getBackendType(BackendIdentifierDTO backendIdentifierDTO,Map<String, Object> headerMap)
            throws ApplicationException;

}
