package com.temenos.dbx.usermanagement.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface PartyIdentifierBackendDelegate extends BackendDelegate {

    public DBXResult update(CustomerDTO customerDTO, Map<String, Object> headerMap);
    
    public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap);

	public DBXResult create(CustomerDTO customerDTO, Map<String, Object> headerMap);
    
}
